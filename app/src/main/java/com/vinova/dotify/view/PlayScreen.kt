package com.vinova.dotify.view

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.vinova.dotify.R
import com.vinova.dotify.model.Music
import com.vinova.dotify.utils.BaseConst
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_play_screen.*

class PlayScreen : AppCompatActivity() {
    companion object{
        var mediaPlayer : MediaPlayer? = null
    }

    private var listMusic : MutableList<Music>? = null
    private lateinit var realtimeSeekBar: Thread

    private var position : Int = 0
    private var next : Boolean = false
    private var repeat : Boolean = false
    private var random : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_screen)

//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            val policy: StrictMode.ThreadPolicy =
//                StrictMode.ThreadPolicy.Builder().permitAll().build()
//
//            StrictMode.setThreadPolicy(policy)
//        }

        listMusic = intent.extras?.get(BaseConst.passlistmusicobject) as MutableList<Music>?

        position = 0
        position = if (intent.extras?.get(BaseConst.passmusicobject) as Int? is Int){
            intent.extras?.get(BaseConst.passmusicobject) as Int
        }else{
            0
        }

        initMediaPlayer(position!!)

        btn_play.setImageResource(R.drawable.pause_btn)

        repeat_btn.setOnClickListener {
            if (!repeat) {
                if (random) {
                    random = false
                    repeat_btn.setImageResource(R.drawable.ic_repeat_btn_selected)
                    shuffle_btn.setImageResource(R.drawable.ic_shuffle_btn)
                }
                repeat_btn.setImageResource(R.drawable.ic_repeat_btn_selected)
                repeat = true
            }else{
                repeat_btn.setImageResource(R.drawable.ic_repeat_btn)
                repeat = false
            }
        }

        shuffle_btn.setOnClickListener {
            if (!random) {
                if (repeat) {
                    repeat = false
                    shuffle_btn.setImageResource(R.drawable.ic_shuffle_btn_selected)
                    repeat_btn.setImageResource(R.drawable.ic_repeat_btn)
                }
                shuffle_btn.setImageResource(R.drawable.ic_shuffle_btn_selected)
                random = true
            }else{
                shuffle_btn.setImageResource(R.drawable.ic_shuffle_btn)
                random = false
            }
        }

        forward_btn.setOnClickListener {
            if (listMusic?.size!! > 0 && position < listMusic!!.size - 1){
                if(mediaPlayer?.isPlaying!! && mediaPlayer != null){
                    position++
                    mediaPlayer!!.stop()
                    mediaPlayer!!.release()
                    mediaPlayer = null
                    initMediaPlayer(position)
                }
            }
        }

        btn_play.setOnClickListener {
            if (!mediaPlayer?.isPlaying!!) {
                btn_play.setImageResource(R.drawable.pause_btn)
                mediaPlayer?.start()

            } else {
                btn_play.setImageResource(R.drawable.play_btn)
                mediaPlayer?.pause()
            }
        }
    }

    private fun setView(positon : Int){
        music_play_name.text = listMusic?.get(positon)?.name!!
        music_artist_name.text = listMusic?.get(positon)?.artist!!

        Glide.with(applicationContext)
            .load(listMusic!![positon].photoURL)
            .thumbnail(0.001f)
            .apply(bitmapTransform(BlurTransformation(18, 3)))
            .into(cards_brands)
    }

    private fun initMediaPlayer(position : Int){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer()
        }
        else{
            mediaPlayer?.reset()
        }

        setView(position)

//        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
//        mediaPlayer?.setOnCompletionListener {
//            MediaPlayer.OnCompletionListener { p0 ->
//                p0?.stop()
//                p0?.reset()
//            }
//        }

        mediaPlayer?.setDataSource(listMusic?.get(position)?.musicURL)
        mediaPlayer?.prepare()

        seekbar_music.max = mediaPlayer?.duration!!
        updateTime()

        mediaPlayer?.start()

        seekbar_music.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mediaPlayer?.seekTo(p0!!.progress)
            }

        })
    }


    fun updateTime(){
        var handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run() {
                if (mediaPlayer != null) {
                    seekbar_music.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 400)
                }
            }
        },400)
    }
}
