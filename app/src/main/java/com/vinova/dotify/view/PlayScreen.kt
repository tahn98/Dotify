package com.vinova.dotify.view

import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.widget.SeekBar
import android.widget.Toast
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
    private var next : Boolean = true
    private var position : Int = 0
    private var repeat : Boolean = false
    private var random : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_screen)
//
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
//        seekbar_music.progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
//        seekbar_music.thumb.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
//        seekbar_music.progressDrawable.colorFilter = BlendModeColorFilter(Color.RED, BlendMode.SRC_IN)
//        seekbar_music.thumb.colorFilter = BlendModeColorFilter(Color.RED, BlendMode.SRC_IN)

        repeat_btn.setOnClickListener {
            repeatEvent()
        }

        shuffle_btn.setOnClickListener {
            shuffleEvent()
        }

        forward_btn.setOnClickListener {
            forwardListener()
        }

        rewind_btn.setOnClickListener {
            rewindListener()
        }

        btn_play.setOnClickListener {
            playListener()
        }
    }

    private fun playListener() {
        if (!mediaPlayer?.isPlaying!!) {
            btn_play.setImageResource(R.drawable.pause_btn)
            mediaPlayer?.start()

        } else {
            btn_play.setImageResource(R.drawable.play_btn)
            mediaPlayer?.pause()
        }
    }

    private fun rewindListener() {
        if (listMusic!!.size > 0) {
            if (mediaPlayer?.isPlaying!! && mediaPlayer != null) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
            if (position < listMusic!!.size - 1 && position > 0) {
                position--
                if (repeat) {
                    position++
                }
                if (random) {
                    position = (0 until listMusic!!.size).random()
                }
            }
            if (position == 0) {
                position = listMusic!!.size - 1
            }
            initMediaPlayer(position)
        }
    }

    private fun forwardListener() {
        if (listMusic!!.size > 0) {
            if (mediaPlayer?.isPlaying!! && mediaPlayer != null) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
            if (position < listMusic!!.size) {
                position++
                if (repeat) {
                    position--
                }
                if (random) {
                    position = (0 until listMusic!!.size).random()
                }
            }
            if (position > listMusic!!.size - 1) {
                position = 0
            }
            initMediaPlayer(position)
        }
    }

    private fun shuffleEvent() {
        if (!random) {
            if (repeat) {
                repeat = false
                shuffle_btn.setImageResource(R.drawable.ic_shuffle_btn_selected)
                repeat_btn.setImageResource(R.drawable.ic_repeat_btn)
            }
            shuffle_btn.setImageResource(R.drawable.ic_shuffle_btn_selected)
            random = true
            Toast.makeText(this, "Shuffle On", Toast.LENGTH_SHORT).show()
        } else {
            shuffle_btn.setImageResource(R.drawable.ic_shuffle_btn)
            random = false
            Toast.makeText(this, "Shuffle Off", Toast.LENGTH_SHORT).show()
        }
    }

    private fun repeatEvent() {
        if (!repeat) {
            if (random) {
                random = false
                repeat_btn.setImageResource(R.drawable.ic_repeat_btn_selected)
                shuffle_btn.setImageResource(R.drawable.ic_shuffle_btn)
            }
            repeat_btn.setImageResource(R.drawable.ic_repeat_btn_selected)
            repeat = true
            Toast.makeText(this, "Repeat On", Toast.LENGTH_SHORT).show()
        } else {
            repeat_btn.setImageResource(R.drawable.ic_repeat_btn)
            repeat = false
            Toast.makeText(this, "Repeat Off", Toast.LENGTH_SHORT).show()
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

    private fun updateTime(){
        val handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run() {
                if (mediaPlayer != null) {
                    seekbar_music.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 400)
                    mediaPlayer?.setOnCompletionListener {
                        next = true
                        Thread.sleep(1000)
                    }
                }
            }
        },400)

        val handlerNext = Handler()
        handlerNext.postDelayed(object : Runnable{
            override fun run() {
                if (next){
                    initMediaPlayer(position)
                    next = false
                    handlerNext.removeCallbacks(this)
                }else{
                    handler.postDelayed(this, 1000)
                }
            }

        },1000)
    }
}
