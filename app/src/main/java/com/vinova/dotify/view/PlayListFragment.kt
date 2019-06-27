package com.vinova.dotify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinova.dotify.R
import com.vinova.dotify.adapter.YourPlaylistAdapter
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.viewmodel.YourMusicViewModel
import kotlinx.android.synthetic.main.playlist_fragment.*

class PlayListFragment : Fragment(){

    private lateinit var playlistAdapter: YourPlaylistAdapter
    private lateinit var mYourMusicViewViewModel: YourMusicViewModel
    private var listPlaylist: MutableList<MusicCollection> = arrayListOf()

    companion object{
        fun newInstance() : Fragment{
            return PlayListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mYourMusicViewViewModel = ViewModelProviders.of(this).get(YourMusicViewModel::class.java)
        mYourMusicViewViewModel.getListPlaylist(BaseConst.curUId)
            ?.observe(this, Observer<MutableList<MusicCollection>?> {
                run {
                    nothing_text.visibility = View.INVISIBLE
                    if (it != null) {
                        listPlaylist.clear()
                        listPlaylist.addAll(it)
                        playlistAdapter.notifyDataSetChanged()
                    }
                }
            })

        return inflater.inflate(R.layout.playlist_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        playlist_RecycleView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        playlistAdapter = YourPlaylistAdapter( context!! , listPlaylist) {
                musicCollection : MusicCollection -> itemClicked(musicCollection)
        }
        playlist_RecycleView.adapter = playlistAdapter
        playlistAdapter.notifyDataSetChanged()
    }

    private fun itemClicked(musicCollection: MusicCollection) {
        gotoArtistScreen(musicCollection, "ARTIST")
    }

    private fun gotoArtistScreen(collection: MusicCollection, type: String) {

        val destFragment = DetailCollectionFragment()
        val bundle = Bundle()
        bundle.putSerializable(BaseConst.passMusicCollection, collection)
        bundle.putString("Type", type)
        destFragment.arguments = bundle
        fragmentManager!!.beginTransaction()
            .addToBackStack("xyz")
            .hide(this@PlayListFragment)
            .add(R.id.feature_container, destFragment)
            .commit()
    }
}