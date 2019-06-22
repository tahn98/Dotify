package com.vinova.dotify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vinova.dotify.R
import kotlinx.android.synthetic.main.your_music.*

class YourMusicFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.your_music, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        albums_btn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.tab_container, AlbumFragment.newInstance(), null)
                ?.commit()
        }

        songs_btn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.tab_container, SongsFragment.newInstance(), null)
                ?.commit()
        }
    }
}