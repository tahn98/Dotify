package com.vinova.dotify.view

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vinova.dotify.R
import com.vinova.dotify.adapter.ListMusicViewPager
import com.vinova.dotify.model.Music
import com.vinova.dotify.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_browse_screen.*
import kotlinx.android.synthetic.main.activity_browse_screen.btn_play
import kotlinx.android.synthetic.main.activity_browse_screen.cards_brands
import kotlinx.android.synthetic.main.activity_browse_screen.container
import kotlinx.android.synthetic.main.activity_browse_screen.favorite_btn
import kotlinx.android.synthetic.main.activity_browse_screen.forward_btn
import kotlinx.android.synthetic.main.activity_browse_screen.music_artist_name
import kotlinx.android.synthetic.main.activity_browse_screen.music_play_name
import kotlinx.android.synthetic.main.activity_browse_screen.repeat_btn
import kotlinx.android.synthetic.main.activity_browse_screen.rewind_btn
import kotlinx.android.synthetic.main.activity_browse_screen.seekbar_music
import kotlinx.android.synthetic.main.activity_browse_screen.shuffle_btn
import kotlinx.android.synthetic.main.demo.*
import java.io.IOException
import java.text.SimpleDateFormat


class MainScreen : AppCompatActivity() {

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    private var listMusic: MutableCollection<Music> = ArrayList()
    private lateinit var viewPager : ListMusicViewPager
    private var next: Boolean = false
    private var position: Int = 0
    private var repeat: Boolean = false
    private var random: Boolean = false
    private var mViewModel: UserViewModel? = null
    private var action = false
    private var panelState= false
    private lateinit var listCurrentFragment : ListCurrentFragment
    private lateinit var diskFragment: DiskFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_screen)
        mViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        setupToolBar()

        diskFragment = DiskFragment()
        listCurrentFragment = ListCurrentFragment()


        viewPager = ListMusicViewPager(supportFragmentManager)
        viewPager.addFragment(diskFragment)
        viewPager.addFragment(listCurrentFragment)
        cards_brands.adapter = viewPager

        btn_play.setImageResource(R.drawable.pause_btn)
        song_play.setImageResource(R.drawable.pause_btn)

        song_play.setOnClickListener {
            if (!mediaPlayer?.isPlaying!!) {
                song_play.setImageResource(R.drawable.pause_btn)
                btn_play.setImageResource(R.drawable.pause_btn)
                mediaPlayer?.start()

            } else {
                song_play.setImageResource(R.drawable.play_btn)
                btn_play.setImageResource(R.drawable.play_btn)
                mediaPlayer?.pause()
            }
        }

        song_forward.setOnClickListener {
            forwardListener()
        }

        song_rewind.setOnClickListener {
            rewindListener()
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
            when (p0.itemId) {
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
                if (newState == SlidingUpPanelLayout.PanelState.DRAGGING && !panelState) {
                    val fadeInAnimation = AnimationUtils.loadAnimation(
                        this@MainScreen, R.anim.fade_in
                    )
                    fadeInAnimation.setAnimationListener(object : AnimationListener {

                        override fun onAnimationStart(animation: Animation) {
                            container.visibility = View.VISIBLE
                        }
                        override fun onAnimationRepeat(animation: Animation) {
                        }
                        override fun onAnimationEnd(animation: Animation) {
                        }
                    })
                    container.startAnimation(fadeInAnimation)
                    bottom_sheet.visibility = View.INVISIBLE
                }
                if (newState == SlidingUpPanelLayout.PanelState.DRAGGING && panelState) {
                    container.visibility=View.INVISIBLE
                    bottom_sheet.visibility = View.VISIBLE
                    val fadeInAnimation = AnimationUtils.loadAnimation(
                        this@MainScreen, R.anim.fade_in
                    )
                    fadeInAnimation.setAnimationListener(object : AnimationListener {

                        override fun onAnimationStart(animation: Animation) {
                            bottom_sheet.visibility = View.VISIBLE
                        }
                        override fun onAnimationRepeat(animation: Animation) {
                        }
                        override fun onAnimationEnd(animation: Animation) {
                        }
                    })
                    bottom_sheet.startAnimation(fadeInAnimation)
                }
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    panelState=true
                }
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    panelState=false
                }
            }

        })
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

    private fun setupToolBar() {
        setSupportActionBar(main_toolbar)
        val actionBar = supportActionBar
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

    private fun goToYourMusicFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.feature_container, YourMusicFragment())
            .commit()
    }

    private fun goToBrowseFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.feature_container, BrowseFragment())
            .commit()
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }

    private fun showToolbar() {
        supportActionBar?.show()
    }


    private fun initMediaPlayer(position: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }

        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)

        btn_play.setImageResource(R.drawable.pause_btn)

        try{
            mediaPlayer?.setDataSource(listMusic.elementAt(position).musicURL)
            mediaPlayer?.prepareAsync()
        }
        catch (e : IOException){
            e.printStackTrace()
        }

        mediaPlayer?.setOnPreparedListener {
            it ->
            seekbar_music.max = mediaPlayer?.duration!!
            var simpleDateFormat = SimpleDateFormat("mm:ss")
            max_time.text = simpleDateFormat.format(mediaPlayer?.duration)
            updateTime()
            it.start()
        }

//        seekbar_music.max = mediaPlayer?.duration!!
//        updateTime()

//        mediaPlayer?.start()

        seekbar_music.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mediaPlayer?.seekTo(p0!!.progress)
            }

        })
    }

    fun play(position: Int, listMusic: MutableList<Music>) {
        song_play.setImageResource(R.drawable.pause_btn)
        diskFragment.setDiskImage(listMusic[position])
        listCurrentFragment.add(listMusic)
        cards_brands.adapter?.notifyDataSetChanged()

        setPlayerView(listMusic, position)
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        this.listMusic = listMusic
        this.position = position
        this.listCurrentFragment.add(listMusic)
        initMediaPlayer(position)
    }

    fun play(song: Music) {
        song_play.setImageResource(R.drawable.pause_btn)
        this.listMusic.add(song)
        listCurrentFragment.add(song)
        diskFragment.setDiskImage(song)
        cards_brands.adapter?.notifyDataSetChanged()

        this.position = listMusic.size - 1
        setPlayerView(listMusic, position)
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        initMediaPlayer(position)
    }

    private fun setPlayerView(
        listMusic: MutableCollection<Music>,
        position: Int
    ) {
        mViewModel?.isLike("HkWQty0QRTh9eEaBdCngJQuU1uf2", listMusic.elementAt(position))
            ?.observe(this, Observer<Boolean> { data ->
                run {
                    action = if (data) {
                        favorite_btn.setImageResource(R.drawable.ic_favorite_song_btn_selected)
                        false
                    } else {
                        favorite_btn.setImageResource(R.drawable.favorite_song_btn)
                        true
                    }
                }
            })
        favorite_btn.setOnClickListener {
            action = if (action) {
                favorite_btn.setImageResource(R.drawable.ic_favorite_song_btn_selected)
                false
            } else {
                favorite_btn.setImageResource(R.drawable.favorite_song_btn)
                true
            }
            mViewModel?.likeMusic("HkWQty0QRTh9eEaBdCngJQuU1uf2", listMusic.elementAt(position), action)

        }
        Glide
            .with(this)
            .load(listMusic.elementAt(position).posterURL)
            .thumbnail(0.001f)
            .into(song_img)

        song_name.text = listMusic.elementAt(position).name
        song_artist.text = listMusic.elementAt(position).artist
        music_artist_name.text = listMusic.elementAt(position).name
        music_play_name.text = listMusic.elementAt(position).artist
    }

    private fun updateTime() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    seekbar_music.progress = mediaPlayer!!.currentPosition
                    var simpleDateFormat = SimpleDateFormat("mm:ss")
                    time_run.text = simpleDateFormat.format(mediaPlayer?.currentPosition)
                    handler.postDelayed(this, 700)
                    mediaPlayer?.setOnCompletionListener {
                        next = true
                        Thread.sleep(200)
                    }
                }
            }
        }, 700)

        val handlerNext = Handler()
        handlerNext.postDelayed(object : Runnable {
            override fun run() {
                if (next) {
                    initMediaPlayer(position)
                    next = false
                    handlerNext.removeCallbacks(this)
                } else {
                    handlerNext.postDelayed(this, 1000)
                }
            }

        }, 1000)
    }

    private fun playListener() {
        if (!mediaPlayer?.isPlaying!!) {
            btn_play.setImageResource(R.drawable.pause_btn)
            song_play.setImageResource(R.drawable.pause_btn)
            mediaPlayer?.start()
        } else {
            btn_play.setImageResource(R.drawable.play_btn)
            song_play.setImageResource(R.drawable.play_btn)
            mediaPlayer?.pause()
        }
    }

    private fun rewindListener() {
        btn_play.setImageResource(R.drawable.pause_btn)
        if (listMusic.isNotEmpty()) {
            if (mediaPlayer?.isPlaying!! && mediaPlayer != null) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
            if (position < listMusic.size - 1 && position > 0) {
                position--
                if (repeat) {
                    position++
                }
                if (random) {
                    position = (0 until listMusic.size).random()
                }
            }
            if (position == 0) {
                position = listMusic.size - 1
            }
            initMediaPlayer(position)
            setPlayerView(listMusic, position)
        }
    }

    private fun forwardListener() {
        btn_play.setImageResource(R.drawable.pause_btn)
        if (listMusic.size > 1) {
            if (mediaPlayer?.isPlaying!! && mediaPlayer != null) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
            if (position < listMusic.size) {
                position++
                if (repeat) {
                    position--
                }
                if (random) {
                    position = (0 until listMusic.size).random()
                }
            }
            if (position > listMusic.size - 1) {
                position = 0
            }
            initMediaPlayer(position)
            setPlayerView(listMusic, position)
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

    private fun collapseSlidingPanel() {
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    private fun checkSlidingUpPanel(): Boolean {
        return sliding_layout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1) this.showToolbar()
        if (checkSlidingUpPanel()) collapseSlidingPanel()
        else super.onBackPressed()
    }
}
