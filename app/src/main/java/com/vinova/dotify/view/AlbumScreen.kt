package com.vinova.dotify.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinova.dotify.R
import com.vinova.dotify.adapter.MusicAdapter
import com.vinova.dotify.model.Music
import com.vinova.dotify.utils.BaseConst
import kotlinx.android.synthetic.main.activity_album_screen.*
import kotlinx.android.synthetic.main.songs_fragment.*

class AlbumScreen : AppCompatActivity() {

    private var listMusic : MutableList<Music> = arrayListOf()
    lateinit var musicAdapter : MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_screen)

        img_album_back.setOnClickListener {
            super.onBackPressed()
        }

        listMusic = intent.extras?.get(BaseConst.passlistmusicalbum) as MutableList<Music>

        list_music_container.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        musicAdapter = MusicAdapter(applicationContext, listMusic){
             music : Music -> itemClicked(music)
        }

        list_music_container.adapter = musicAdapter
        musicAdapter.notifyDataSetChanged()
    }

    private fun itemClicked(music: Music) {
        Log.d("abc", "abc")
    }
}
