package com.vinova.dotify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vinova.dotify.R

class ArtistFragment : Fragment(){

    companion object{
        fun newInstance() : ArtistFragment{
            return ArtistFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.artist_fragment, container, false)
    }
}