package com.vinova.dotify.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinova.dotify.R
import com.vinova.dotify.adapter.YourAlbumAdapter
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.viewmodel.YourMusicViewModel
import kotlinx.android.synthetic.main.album_fragment.*

class AlbumFragment : Fragment(){

    private lateinit var albumAdapter : YourAlbumAdapter
    private lateinit var mYourMusicViewViewModel : YourMusicViewModel
    private var listAlbum : MutableList<MusicCollection> = arrayListOf()

    companion object{
        fun newInstance() : AlbumFragment{
            return AlbumFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mYourMusicViewViewModel = ViewModelProviders.of(this).get(YourMusicViewModel::class.java)
        mYourMusicViewViewModel.getAlbums(BaseConst.curUId)
            ?.observe(this, Observer <MutableList<MusicCollection>?>{
                run{
                    if(it != null){
                        Log.d("abc", it.toString())
                        listAlbum.clear()
                        listAlbum.addAll(it)
                        albumAdapter.notifyDataSetChanged()
                        if(listAlbum.isEmpty()){
                            nothing_text.text = "You don't have any music collection"
                        }
                    }
                }
            })
        return inflater.inflate(R.layout.album_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        album_recycleView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        albumAdapter = YourAlbumAdapter( context!! , listAlbum) { musicCollection : MusicCollection -> itemClicked(musicCollection)}
        album_recycleView.adapter = albumAdapter
        albumAdapter.notifyDataSetChanged()

    }

    private fun itemClicked(album: MusicCollection){
        gotoAlbumScreen(album, "ALBUM")
    }

    private fun gotoAlbumScreen(collection: MusicCollection, type: String) {

        val destFragment = DetailCollectionFragment()
        val bundle = Bundle()
        bundle.putSerializable(BaseConst.passMusicCollection, collection)
        bundle.putString("Type", type)
        destFragment.arguments = bundle
        fragmentManager!!.beginTransaction()
            .addToBackStack("xyz")
            .hide(this@AlbumFragment)
            .add(R.id.feature_container, destFragment)
            .commit()
    }
}