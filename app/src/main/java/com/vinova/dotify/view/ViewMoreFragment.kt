package com.vinova.dotify.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaopiz.kprogresshud.KProgressHUD
import com.vinova.dotify.R
import com.vinova.dotify.adapter.MusicCollectionAdapter
import com.vinova.dotify.databinding.ActivityViewMoreBinding
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.utils.OnClickListener
import com.vinova.dotify.viewmodel.FeedViewModel


class ViewMoreFragment : Fragment() {
    private var mViewModel: FeedViewModel? = null
    private var adapter:MusicCollectionAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupToolBar()
        val loading= KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Loading data ...")
            .setCancellable(false)
            .setAnimationSpeed(1)
            .setDimAmount(0.2f)
        loading.show()
        val binding = DataBindingUtil.inflate<ActivityViewMoreBinding>(
            inflater,
            R.layout.activity_view_more,
            container,
            false
        )
        val bundle = this.arguments
        var type:String?=""
        if (bundle != null) {
            type = bundle.getString("Type")
            binding.typeCollection.text=type
        }
        binding.toolbar.setNavigationOnClickListener { (activity as MainScreen).onBackPressed() }
        mViewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        when(type)
        {
            "Suggested Playlists"->{
                adapter= MusicCollectionAdapter(context!!,1)
                binding.rcvCollection.adapter=adapter
                binding.rcvCollection.layoutManager= GridLayoutManager(context!!,2, RecyclerView.VERTICAL,false)
                binding.rcvCollection.setPadding(70,15,65,10)
                mViewModel?.getAllSuggested()?.observe(this, Observer<List<MusicCollection>>{ data->run{
                    if(data!=null)
                    {
                        adapter?.addAll(data)
                    }
                    loading.dismiss()
                }})
                adapter?.setOnClick(object: OnClickListener {
                    override fun onClick(collection: MusicCollection) {
                        moveToDetail(collection,"PLAYLIST")
                    }

                })
            }
            "Featured Albums"->{
                adapter= MusicCollectionAdapter(context!!,3)
                binding.rcvCollection.adapter=adapter
                binding.rcvCollection.layoutManager= LinearLayoutManager(context!!, RecyclerView.VERTICAL,false)
                binding.rcvCollection.setPadding(5,10,5,10)
                mViewModel?.getAllAlbum()?.observe(this, Observer<List<MusicCollection>>{ data->run{
                    if(data!=null)
                    {
                        adapter?.addAll(data)
                    }
                    loading.dismiss()
                }})
                adapter?.setOnClick(object: OnClickListener {
                    override fun onClick(collection: MusicCollection) {
                        moveToDetail(collection,"ALBUM")
                    }

                })
            }
            else->{
                adapter= MusicCollectionAdapter(context!!,2)
                binding.rcvCollection.adapter=adapter
                binding.rcvCollection.layoutManager= GridLayoutManager(context!!,2, RecyclerView.VERTICAL,false)
                binding.rcvCollection.setPadding(40,10,20,10)
                mViewModel?.getAllGenre()?.observe(this, Observer<List<MusicCollection>>{ data->run{
                    if(data!=null)
                    {
                        adapter?.addAll(data)
                    }
                    loading.dismiss()
                }})
                adapter?.setOnClick(object: OnClickListener {
                    override fun onClick(collection: MusicCollection) {
                        moveToDetail(collection,"PLAYLIST")
                    }

                })
            }
        }
        return binding.root
    }

    private fun moveToDetail(collection: MusicCollection,type:String) {
        val destFragment = DetailCollectionFragment()
        val bundle = Bundle()
        bundle.putSerializable(BaseConst.passMusicCollection, collection)
        bundle.putString("Type", type)
        destFragment.arguments = bundle
        fragmentManager!!.beginTransaction()
            .addToBackStack("xyz")
            .hide(this@ViewMoreFragment)
            .add(R.id.feature_container, destFragment)
            .commit()
    }
    private fun setupToolBar() {
        (activity as MainScreen).hideToolbar()
    }


}
