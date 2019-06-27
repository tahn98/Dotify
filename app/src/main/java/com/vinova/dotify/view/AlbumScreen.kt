package com.vinova.dotify.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vinova.dotify.R
import com.vinova.dotify.adapter.MusicAdapter
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_album_screen.*
import java.io.Serializable


class AlbumScreen : AppCompatActivity() {

    private lateinit var album : MusicCollection
    private var listMusic : MutableList<Music> = arrayListOf()
    private lateinit var musicAdapter : MusicAdapter
    private var mViewModel: UserViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_screen)
        mViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        img_album_back.setOnClickListener {
            super.onBackPressed()
        }

        getBackAlbum()
        initView()

        play_button.setOnClickListener {
            playListClick()
        }

    }

    private fun getBackAlbum(){

        album = intent.extras?.get(BaseConst.passMusicCollection) as MusicCollection
        listMusic = album.listMusic?.values?.toList() as MutableList<Music>
    }

    private fun initView(){
//        Glide
//            .with(this)
//            .load(album.photoURL)
//            .thumbnail(0.001f)
//            .apply(bitmapTransform(BlurTransformation(18, 3)))
//            .into(album_background_imd)

        Glide
            .with(this)
            .load(album.photoURL)
            .centerCrop()
            .into(photo_album_img)
        var collectionType = intent.extras?.getString("Type")

        type.text=collectionType
        album_name.text = album.name
        var action=false

        mViewModel?.isLike("HkWQty0QRTh9eEaBdCngJQuU1uf2",album,collectionType!!)?.observe(this, Observer<Boolean> {data->
            run {
                action = if(data) {
                    like_button.setImageResource(R.drawable.ic_heart_song_btn_selected)
                    false
                } else {
                    like_button.setImageResource(R.drawable.ic_heart_song_btn)
                    true
                }
            }
        })
        like_button.setOnClickListener{
            action = if(action) {
                like_button.setImageResource(R.drawable.ic_heart_song_btn_selected)
                false
            } else {
                like_button.setImageResource(R.drawable.ic_heart_song_btn)
                true
            }
            mViewModel?.likeCollection("HkWQty0QRTh9eEaBdCngJQuU1uf2",album,collectionType!!,action)

        }
        share_button.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_SUBJECT, album.name)
            share.putExtra(Intent.EXTRA_TEXT, album.listMusic!!.values.toList()[0].musicURL)
            startActivity(Intent.createChooser(share, "Share link"))
        }
        list_music_container.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        musicAdapter = MusicAdapter(applicationContext, listMusic){
                music : Music, pos : Int -> itemClicked(music, pos)
        }

        list_music_container.adapter = musicAdapter
        musicAdapter.notifyDataSetChanged()
    }

    private fun itemClicked(music: Music, pos: Int?) {
        var musicIntent = Intent(this, PlayScreen::class.java)
        musicIntent.putExtra(BaseConst.passlistmusicobject, listMusic as Serializable)
        musicIntent.putExtra(BaseConst.passmusicobject, pos!! - 1)
        startActivity(musicIntent)
    }

    private fun playListClick(){
        var intent = Intent(this, PlayScreen::class.java)
        intent.putExtra(BaseConst.passlistmusicobject, listMusic as Serializable)
        startActivity(intent)
    }
}
