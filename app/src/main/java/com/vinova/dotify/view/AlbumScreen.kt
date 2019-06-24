package com.vinova.dotify.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.vinova.dotify.R
import com.vinova.dotify.adapter.MusicAdapter
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.BaseConst
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_album_screen.*

class AlbumScreen : AppCompatActivity() {

    private lateinit var album : MusicCollection
    private var listMusic : MutableList<Music> = arrayListOf()
    lateinit var musicAdapter : MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_screen)

        img_album_back.setOnClickListener {
            super.onBackPressed()
        }

        getBackAlbum()
        initView()

    }

    private fun getBackAlbum(){
        album = intent.extras?.get(BaseConst.passMusicCollection) as MusicCollection
        listMusic = album.listMusic?.values?.toList() as MutableList<Music>
    }

    private fun initView(){
        Glide
            .with(this)
            .load(album.photoURL)
            .thumbnail(0.001f)
            .apply(bitmapTransform(BlurTransformation(18, 3)))
            .into(album_background_imd)
        Glide
            .with(this)
            .load(album.photoURL)
            .centerCrop()
            .into(photo_album_img)

        album_name.text = album.name

        list_music_container.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        musicAdapter = MusicAdapter(applicationContext, listMusic){
                music : Music -> itemClicked(music)
        }

        list_music_container.adapter = musicAdapter
        musicAdapter.notifyDataSetChanged()

//        Glide.with(this)
//            .load(cover)
//            .thumbnail(0.5f)
//            .error(R.drawable.ic_launcher_background)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(photo_album_img)
    }

    private fun itemClicked(music: Music) {
        var musicIntent = Intent(this, PlayScreen::class.java)

        musicIntent.putExtra(BaseConst.passmusicobject, music)

        startActivity(musicIntent)
    }
}
