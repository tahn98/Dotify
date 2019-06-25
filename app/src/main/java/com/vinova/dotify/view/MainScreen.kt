package com.vinova.dotify.view

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vinova.dotify.R
import com.vinova.dotify.model.Music
import kotlinx.android.synthetic.main.activity_browse_screen.*
import kotlinx.android.synthetic.main.activity_browse_screen.btn_play
import kotlinx.android.synthetic.main.activity_browse_screen.forward_btn
import kotlinx.android.synthetic.main.activity_browse_screen.repeat_btn
import kotlinx.android.synthetic.main.activity_browse_screen.rewind_btn
import kotlinx.android.synthetic.main.activity_browse_screen.seekbar_music
import kotlinx.android.synthetic.main.activity_browse_screen.shuffle_btn
import kotlinx.android.synthetic.main.activity_play_screen.*


class MainScreen : AppCompatActivity() {

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
        setContentView(R.layout.activity_browse_screen)


        setupToolBar()

        btn_play.setImageResource(R.drawable.pause_btn)

        song_play.setOnClickListener {

        }

        song_forward.setOnClickListener {

        }

        song_rewind.setOnClickListener {

        }

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

        nav_view.setNavigationItemSelectedListener { p0 ->
            when(p0.itemId){
                R.id.menu_brower -> {
                    p0.isChecked = true
                    goToBrowseFragment()
                    drawer_layout.closeDrawers()
                }
                R.id.menu_yourmusic -> {
                    p0.isChecked = true
                    goToYourMusicFragment()
                    drawer_layout.closeDrawers()
                }
            }
            true
        }

        goToBrowseFragment()

        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {

            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                if(newState== SlidingUpPanelLayout.PanelState.EXPANDED)
                {
                    bottom_sheet.visibility=View.INVISIBLE
                    container.visibility= View.VISIBLE
                }
                if(newState== SlidingUpPanelLayout.PanelState.COLLAPSED)
                {
                    bottom_sheet.visibility=View.VISIBLE
                    container.visibility= View.INVISIBLE
                }
            }

        })
    }

    private fun setupToolBar(){
        setSupportActionBar(main_toolbar)
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.nav_ic)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToYourMusicFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.feature_container, YourMusicFragment())
            .commit()
    }

    private fun goToBrowseFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.feature_container, BrowseFragment())
            .commit()
    }

    fun hideToolbar() {
       supportActionBar?.hide()
    }
    fun showToolbar() {
        supportActionBar?.show()
    }

    private fun initMediaPlayer(position : Int){
        if(PlayScreen.mediaPlayer == null){
            PlayScreen.mediaPlayer = MediaPlayer()
        }
        else{
            PlayScreen.mediaPlayer?.reset()
        }


        PlayScreen.mediaPlayer?.setDataSource(listMusic?.get(position)?.musicURL)
        PlayScreen.mediaPlayer?.prepare()

        seekbar_music.max = PlayScreen.mediaPlayer?.duration!!
        updateTime()

        PlayScreen.mediaPlayer?.start()

        seekbar_music.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                PlayScreen.mediaPlayer?.seekTo(p0!!.progress)
            }

        })
    }

    fun play(position: Int, listMusic : MutableList<Music>){
        this.listMusic = listMusic
        this.position = position
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        initMediaPlayer(position!!)
    }

    private fun updateTime(){
        val handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run() {
                if (PlayScreen.mediaPlayer != null) {
                    seekbar_music.progress = PlayScreen.mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 400)
                    PlayScreen.mediaPlayer?.setOnCompletionListener {
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

    private fun playListener() {
        if (!PlayScreen.mediaPlayer?.isPlaying!!) {
            btn_play.setImageResource(R.drawable.pause_btn)
            PlayScreen.mediaPlayer?.start()

        } else {
            btn_play.setImageResource(R.drawable.play_btn)
            PlayScreen.mediaPlayer?.pause()
        }
    }

    private fun rewindListener() {
        if (listMusic!!.size > 0) {
            if (PlayScreen.mediaPlayer?.isPlaying!! && PlayScreen.mediaPlayer != null) {
                PlayScreen.mediaPlayer!!.stop()
                PlayScreen.mediaPlayer!!.release()
                PlayScreen.mediaPlayer = null
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
        if (listMusic!!.size > 1) {
            if (PlayScreen.mediaPlayer?.isPlaying!! && PlayScreen.mediaPlayer != null) {
                PlayScreen.mediaPlayer!!.stop()
                PlayScreen.mediaPlayer!!.release()
                PlayScreen.mediaPlayer = null
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
}
