package com.vinova.dotify.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaopiz.kprogresshud.KProgressHUD
import com.vinova.dotify.R
import com.vinova.dotify.adapter.CustomPagerAdapter
import com.vinova.dotify.adapter.MusicCollectionAdapter
import com.vinova.dotify.databinding.BrowseScreenBinding
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.utils.OnClickListener
import com.vinova.dotify.utils.OnViewPagerItemClickListener
import com.vinova.dotify.viewmodel.FeedViewModel


class BrowseFragment : Fragment() {
    private var mViewModel: FeedViewModel? = null
    private var playlistAdapter: MusicCollectionAdapter? = null
    private var genreAdapter: MusicCollectionAdapter? = null
    private var albumAdapter: MusicCollectionAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        val loading = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Loading data ...")
            .setCancellable(false)
            .setAnimationSpeed(1)
            .setDimAmount(0.7f)
        loading.show()
        playlistAdapter = MusicCollectionAdapter(context!!, 1)
        playlistAdapter?.setOnClick(object : OnClickListener {
            override fun onClick(collection: MusicCollection) {
                moveToDetail(collection, "PLAYLIST")
            }

        })
        genreAdapter = MusicCollectionAdapter(context!!, 2)
        genreAdapter?.setOnClick(object : OnClickListener {
            override fun onClick(collection: MusicCollection) {
                moveToDetail(collection, "PLAYLIST")
            }

        })
        albumAdapter = MusicCollectionAdapter(context!!, 3)
        albumAdapter?.setOnClick(object : OnClickListener {
            override fun onClick(collection: MusicCollection) {
                moveToDetail(collection, "ALBUM")
            }

        })
        //Binding view with xml
        val binding = DataBindingUtil.inflate<BrowseScreenBinding>(inflater, R.layout.browse_screen, container, false)
        binding.pageIndicatorView.setViewPager(binding.viewPager)
        mViewModel?.getLatest()?.observe(this, Observer<MusicCollection> { data ->
            run {
                if (data != null) {
                    val adapter=CustomPagerAdapter(context!!, data.listMusic?.values!!.toList())
                    adapter.setOnClickListener(object:OnViewPagerItemClickListener{
                        override fun onClick(song: Music) {
                            (activity as MainScreen).play(song)
                        }

                    })
                    binding.viewPager.adapter=adapter
                }
            }
        })
        binding.rcvPlaylist.adapter = playlistAdapter
        binding.rcvPlaylist.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        mViewModel?.getSuggested()?.observe(this, Observer<List<MusicCollection>> { data ->
            run {
                if (data != null) {
                    playlistAdapter?.addAll(data)
                }
            }
        })
        binding.rcvAlbum.adapter = albumAdapter
        binding.rcvAlbum.layoutManager = LinearLayoutManager(context!!)
        mViewModel?.getAlbum()?.observe(this, Observer<List<MusicCollection>> { data ->
            run {
                if (data != null) {
                    albumAdapter?.addAll(data)
                }
            }
        })
        binding.rcvGenre.adapter = genreAdapter
        binding.rcvGenre.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        mViewModel?.getGenre()?.observe(this, Observer<List<MusicCollection>> { data ->
            run {
                if (data != null) {
                    genreAdapter?.addAll(data)
                    loading.dismiss()
                }
            }
        })
        binding.suggestedSection.setOnClickListener {
            moveToViewMore("Suggested Playlists")
        }
        binding.albumSection.setOnClickListener {
            moveToViewMore("Featured Albums")
        }
        binding.genreSection.setOnClickListener {
            moveToViewMore("Genres & Mood")
        }
        return binding.root
    }

    private fun moveToViewMore(type: String) {
        val destFragment = ViewMoreFragment()
        val bundle = Bundle()
        bundle.putString("Type", type)
        destFragment.arguments = bundle
        fragmentManager!!.beginTransaction()
            .addToBackStack("xyz")
            .hide(this@BrowseFragment)
            .add(R.id.feature_container, destFragment)
            .commit()
    }

    private fun moveToDetail(collection: MusicCollection, type: String) {

        val destFragment = DetailCollectionFragment()
        val bundle = Bundle()
        bundle.putSerializable(BaseConst.passMusicCollection, collection)
        bundle.putString("Type", type)
        destFragment.arguments = bundle
        fragmentManager!!.beginTransaction()
            .addToBackStack("xyz")
            .hide(this@BrowseFragment)
            .add(R.id.feature_container, destFragment)
            .commit()
    }
}