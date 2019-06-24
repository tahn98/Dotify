package com.vinova.dotify.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.vinova.dotify.R
import com.vinova.dotify.model.Music
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.utils.HeadsetPlugReceiver
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
    private var headsetPlugReceiver :HeadsetPlugReceiver?=  null
    private var updateUIReceiver: BroadcastReceiver? = null
    private var state:String?="unknow"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_screen)
        headsetPlugReceiver= HeadsetPlugReceiver()
        var intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.HEADSET_PLUG")
        registerReceiver(headsetPlugReceiver, intentFilter)
        registerReceiver(updateUIReceiver, IntentFilter("broadCastName"))
        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy: StrictMode.ThreadPolicy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)
        }

        listMusic = intent.extras?.get(BaseConst.passlistmusicobject) as MutableList<Music>?

        position = 0
//        if (intent.extras?.get(BaseConst.passmusicobject) as Int is Int){
//            position = intent.extras?.get(BaseConst.passmusicobject) as Int
//        }else{
//            position = 0
//        }

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
    override fun onDestroy() {
        super.onDestroy()
        if (headsetPlugReceiver != null) {
            unregisterReceiver(headsetPlugReceiver)
            headsetPlugReceiver = null
        }
    }

    override fun onPause() {
        super.onPause()
        this.unregisterReceiver(this.updateUIReceiver)
    }
    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(
            "android.intent.action.MAIN"
        )

        updateUIReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                //extract our message from intent
                state = intent.getStringExtra("state")
                if(state=="true")
                {
                    val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

                    Glide.with(context)
                        .load(R.drawable.headphone)
                        .transition(withCrossFade(factory))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(type_speaker)
                }
                else{
                    val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

                    Glide.with(context)
                        .load(R.drawable.internal_speaker)
                        .transition(withCrossFade(factory))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(type_speaker)
                }

            }
        }
        //registering our receiver
        this.registerReceiver(updateUIReceiver, intentFilter)
    }

    private fun initMediaPlayer(position : Int){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer()
        }
        else{
            mediaPlayer?.reset()
        }

        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.setOnCompletionListener {
            MediaPlayer.OnCompletionListener { p0 ->
                p0?.stop()
                p0?.reset()
            }
        }

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
