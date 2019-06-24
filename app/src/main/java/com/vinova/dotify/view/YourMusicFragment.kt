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

        fragmentManager?.beginTransaction()?.replace(R.id.tab_container, ArtistFragment.newInstance(), null)
            ?.commit()
        artist_btn.setImageResource(R.drawable.ic_artists_btn_selected)

        artist_btn.setOnClickListener {
            resetSelectedImage()
            artist_btn.setImageResource(R.drawable.ic_artists_btn_selected)
            fragmentManager?.beginTransaction()?.replace(R.id.tab_container, ArtistFragment.newInstance(), null)
                ?.commit()
        }

        albums_btn.setOnClickListener {
            resetSelectedImage()
            albums_btn.setImageResource(R.drawable.ic_album_btn_selected)
            fragmentManager?.beginTransaction()?.replace(R.id.tab_container, AlbumFragment.newInstance(), null)
                ?.commit()
        }

        songs_btn.setOnClickListener {
            resetSelectedImage()
            songs_btn.setImageResource(R.drawable.ic_songs_btn_selected)
            fragmentManager?.beginTransaction()?.replace(R.id.tab_container, SongsFragment.newInstance(), null)
                ?.commit()
        }
    }

    private fun resetSelectedImage(){
        artist_btn.setImageResource(R.drawable.ic_artists_btn)
        albums_btn.setImageResource(R.drawable.ic_album_btn)
        songs_btn.setImageResource(R.drawable.ic_songs_btn)
        playlists_btn.setImageResource(R.drawable.ic_playlists_btn)
    }
}