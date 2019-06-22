package com.vinova.dotify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinova.dotify.R
import com.vinova.dotify.adapter.CustomPagerAdapter
import com.vinova.dotify.adapter.SuggestedPlaylistAdapter
import com.vinova.dotify.databinding.BrowseScreenBinding
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.viewmodel.FeedViewModel


class BrowseFragment : Fragment(){
    private var mViewModel: FeedViewModel? = null
    private var myAdapter: SuggestedPlaylistAdapter?=null
    private var mLayoutManager: LinearLayoutManager? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        myAdapter= SuggestedPlaylistAdapter(context!!)
        mLayoutManager = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL, false)
        //Binding view with xml
        val binding = DataBindingUtil.inflate<BrowseScreenBinding>(inflater, R.layout.browse_screen, container, false)
        binding.pageIndicatorView.setViewPager(binding.viewPager)
        mViewModel?.getLatest()?.observe(this, Observer<MusicCollection>{ data->run{
            if(data!=null)
            {
                binding.viewPager.adapter= CustomPagerAdapter(context!!,data.listMusic?.values!!.toList())
            }
        }})
        binding.rcvPlaylist.adapter= myAdapter
        binding.rcvPlaylist.layoutManager= mLayoutManager
        mViewModel?.getSuggested()?.observe(this, Observer<List<MusicCollection>>{ data->run{
            if(data!=null)
            {
                myAdapter?.addAll(data)
            }
        }})
        return binding.root
    }
}