package com.vinova.dotify.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaopiz.kprogresshud.KProgressHUD
import com.vinova.dotify.R
import com.vinova.dotify.adapter.MusicCollectionAdapter
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.activity_view_more.*
import com.vinova.dotify.utils.ItemOffsetDecoration
import com.vinova.dotify.utils.OnClickListener


class ViewMore : AppCompatActivity() {
    private var mViewModel: FeedViewModel? = null
    private var adapter:MusicCollectionAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.vinova.dotify.R.layout.activity_view_more)
        val loading= KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Loading data ...")
            .setCancellable(false)
            .setAnimationSpeed(1)
            .setDimAmount(0.2f)
        loading.show()
        val type = intent.extras?.getString("Type")
        toolbar.setNavigationOnClickListener { onBackPressed() }
        typeCollection.text=type
        mViewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        when(type)
        {
            "Suggested Playlists"->{
                adapter= MusicCollectionAdapter(this,1)
                rcv_Collection.adapter=adapter
                rcv_Collection.layoutManager=GridLayoutManager(this,2,RecyclerView.VERTICAL,false)
                rcv_Collection.setPadding(70,15,65,10)
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
                adapter= MusicCollectionAdapter(this,3)
                rcv_Collection.adapter=adapter
                rcv_Collection.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                rcv_Collection.setPadding(5,10,5,10)
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
                adapter= MusicCollectionAdapter(this,2)
                rcv_Collection.adapter=adapter
                rcv_Collection.layoutManager=GridLayoutManager(this,2,RecyclerView.VERTICAL,false)
                rcv_Collection.setPadding(40,10,20,10)
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


    }
    private fun moveToDetail(collection: MusicCollection,type:String) {
        var albumIntent = Intent(this, AlbumScreen::class.java)
        albumIntent.putExtra(BaseConst.passMusicCollection, collection)
        albumIntent.putExtra("Type", type)
        startActivity(albumIntent)
    }
}
