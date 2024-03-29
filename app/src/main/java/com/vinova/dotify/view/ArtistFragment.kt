package com.vinova.dotify.view

import android.content.Intent
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
import com.vinova.dotify.adapter.YourArtistAdapter
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.viewmodel.YourMusicViewModel
import kotlinx.android.synthetic.main.artist_fragment.*

class ArtistFragment : Fragment() {

    private lateinit var artistAdapter: YourArtistAdapter
    private lateinit var mYourMusicViewViewModel: YourMusicViewModel
    private var listArtist: MutableList<MusicCollection> = arrayListOf()

    companion object {
        fun newInstance(): ArtistFragment {
            return ArtistFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mYourMusicViewViewModel = ViewModelProviders.of(this).get(YourMusicViewModel::class.java)
        mYourMusicViewViewModel.getListArtist(BaseConst.curUId)
            ?.observe(this, Observer<MutableList<MusicCollection>?> {
                run {
                    if (it != null) {
                        Log.d("abc", it.toString())
                        listArtist.clear()
                        listArtist.addAll(it)
                        artistAdapter.notifyDataSetChanged()
                        if(listArtist.isEmpty()){
                            nothing_text.text = "You don't have any music collection"
                        }
                    }
                }

            })

        return inflater.inflate(R.layout.artist_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        artist_RecycleView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        artistAdapter = YourArtistAdapter( context!! , listArtist) { musicCollection : MusicCollection -> itemClicked(musicCollection)}
        artist_RecycleView.adapter = artistAdapter
        artistAdapter.notifyDataSetChanged()
    }

    private fun itemClicked(musicCollection: MusicCollection) {
        gotoArtistScreen(musicCollection, "ARTIST")
    }

    private fun gotoArtistScreen(artist: MusicCollection, type: String) {

        val destFragment = DetailCollectionFragment()
        val bundle = Bundle()
        bundle.putSerializable(BaseConst.passMusicCollection, artist)
        bundle.putString("Type", type)
        destFragment.arguments = bundle
        fragmentManager!!.beginTransaction()
            .addToBackStack("xyz")
            .hide(this@ArtistFragment)
            .add(R.id.feature_container, destFragment)
            .commit()
    }
}