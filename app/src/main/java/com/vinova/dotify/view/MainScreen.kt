package com.vinova.dotify.view

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.vinova.dotify.R
import kotlinx.android.synthetic.main.activity_browse_screen.*
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vinova.dotify.model.Music
import com.vinova.dotify.utils.BaseConst
import kotlinx.android.synthetic.main.activity_browse_screen.seekbar_music
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

        listMusic = intent.extras?.get(BaseConst.passlistmusicobject) as MutableList<Music>?

        position = 0
        position = if (intent.extras?.get(BaseConst.passmusicobject) as Int? is Int){
            intent.extras?.get(BaseConst.passmusicobject) as Int
        }else{
            0
        }

        initMediaPlayer(position!!)



        setupToolBar()

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

//        seekbar_music.max = PlayScreen.mediaPlayer?.duration!!
//        updateTime()

        PlayScreen.mediaPlayer?.start()

//        seekbar_music.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) {
//
//            }
//
//            override fun onStopTrackingTouch(p0: SeekBar?) {
//                PlayScreen.mediaPlayer?.seekTo(p0!!.progress)
//            }
//
//        })
    }

}
