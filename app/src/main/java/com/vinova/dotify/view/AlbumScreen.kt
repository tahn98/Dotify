package com.vinova.dotify.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vinova.dotify.R
import com.vinova.dotify.adapter.MusicAdapter
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.BaseConst
import kotlinx.android.synthetic.main.activity_album_screen.*
import java.io.Serializable

class AlbumScreen : AppCompatActivity() {

    private lateinit var album : MusicCollection
    private var listMusic : MutableList<Music> = arrayListOf()
    private lateinit var musicAdapter : MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_screen)

        img_album_back.setOnClickListener {
            super.onBackPressed()
        }

        getBackAlbum()
        initView()

        playlist_btn.setOnClickListener {
            playListClick()
        }

    }

    private fun getBackAlbum(){
        album = intent.extras?.get(BaseConst.passMusicCollection) as MusicCollection
        listMusic = album.listMusic?.values?.toList() as MutableList<Music>
    }

    private fun initView(){
        Glide
            .with(this)
            .load(album.photoURL)
            .centerCrop()
            .into(photo_album_img)

        album_name.text = album.name

        list_music_container.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        musicAdapter = MusicAdapter(applicationContext, listMusic){
                music : Music, pos : Int -> itemClicked(music, pos)
        }

        list_music_container.adapter = musicAdapter
        musicAdapter.notifyDataSetChanged()
    }

    private fun itemClicked(music: Music, pos: Int) {
        var musicIntent = Intent(this, PlayScreen::class.java)
        musicIntent.putExtra(BaseConst.passlistmusicobject, listMusic as Serializable)
        musicIntent.putExtra(BaseConst.passmusicobject, pos - 1)
        startActivity(musicIntent)
    }

    private fun playListClick(){
        var intent = Intent(this, PlayScreen::class.java)
        intent.putExtra(BaseConst.passlistmusicobject, listMusic as Serializable)
        startActivity(intent)
    }
}
