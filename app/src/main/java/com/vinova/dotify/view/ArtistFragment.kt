package com.vinova.dotify.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vinova.dotify.R
import com.vinova.dotify.adapter.YourArtistAdapter
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.viewmodel.YourMusicViewModel
import kotlinx.android.synthetic.main.artist_fragment.*

class ArtistFragment : Fragment(){

    private lateinit var artistAdapter : YourArtistAdapter
    private lateinit var mYourMusicViewViewModel : YourMusicViewModel
    private var listArtist : MutableList<MusicCollection> = arrayListOf()

    companion object{
        fun newInstance() : ArtistFragment{
            return ArtistFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.artist_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}