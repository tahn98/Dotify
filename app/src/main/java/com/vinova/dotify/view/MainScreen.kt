package com.vinova.dotify.view

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kaopiz.kprogresshud.KProgressHUD
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vinova.dotify.R
import com.vinova.dotify.adapter.ListMusicViewPager
import com.vinova.dotify.adapter.MusicAdapter
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.User
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_browse_screen.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainScreen : AppCompatActivity() {


    companion object {
        var mediaPlayer: MediaPlayer? = null
        lateinit var searchResultAdapter: MusicAdapter
    }
    var searchView: SearchView?=null
    private var listMusic: MutableCollection<Music> = ArrayList()
    private lateinit var viewPager: ListMusicViewPager
    private var next: Boolean = false
    private var position: Int = 0
    private var repeat: Boolean = false
    private var random: Boolean = false
    private var mViewModel: UserViewModel? = null
    private var action = false
    private var panelState = false
    private var mDatabase: DatabaseReference? = null
    private var mImageUri: String? = null
    var user: User? = null
    private val GALLERY_REQUEST = 1
    private val CAMERA_REQUEST_CODE = 1
    private var mStorage: StorageReference? = null
    private lateinit var avatar: ImageView
    private lateinit var listCurrentFragment: ListCurrentFragment
    private lateinit var diskFragment: DiskFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_screen)
        mViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val extras = intent.extras
        if (extras != null) {
            user = intent.getSerializableExtra("curUser") as User
            BaseConst.curUId = user?.uid!!
        }
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val header = navigationView.getHeaderView(0)
        val username = header.findViewById<TextView>(R.id.username)
        username.text = user?.username
        avatar = header.findViewById<ImageView>(R.id.avatar)
        Glide.with(this).load(user?.profile_photo).thumbnail(0.0001f).into(avatar)
        mStorage = FirebaseStorage.getInstance().reference
        mDatabase = FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid)
        header.findViewById<TextView>(R.id.change_avatar).setOnClickListener { dispatchTakePictureIntent() }
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

        btn_dismiss.setOnClickListener {
            onBackPressed()
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
        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {

            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                if (newState == SlidingUpPanelLayout.PanelState.DRAGGING && !panelState) {
                    visibleExpandedContainer()
                }
                if (newState == SlidingUpPanelLayout.PanelState.DRAGGING && panelState) {
                    visibleBottomContainer()
                }
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    if (container.visibility == View.INVISIBLE) {
                        visibleExpandedContainer()
                    }
                    panelState = true
                }
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    if (bottom_sheet.visibility == View.INVISIBLE) {
                        visibleBottomContainer()
                    }
                    panelState = false
                }
            }

        })
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        button_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        goToBrowseFragment()
    }

    private fun visibleExpandedContainer() {
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

    private fun visibleBottomContainer() {
        container.visibility = View.INVISIBLE
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


    override fun onCreateOptionsMenu(menu: Menu):Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        // Getting search action from action bar and setting up search view
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView?.setOnQueryTextFocusChangeListener { _ , hasFocus ->
            if (hasFocus) {
                supportFragmentManager.beginTransaction().replace(R.id.feature_container, SearchResultFragment()).addToBackStack(null)
                    .commit()
            } else {
                onBackPressed()
            }
        }
        // Setup searchView
        //setupSearchView(searchItem)

        return true;
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


    @SuppressLint("SimpleDateFormat")
    private fun initMediaPlayer(position: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }

        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)

        btn_play.setImageResource(R.drawable.pause_btn)

        try {
            mediaPlayer?.setDataSource(listMusic.elementAt(position).musicURL)
            mediaPlayer?.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mediaPlayer?.setOnPreparedListener { it ->
            seekbar_music.max = mediaPlayer?.duration!!
            val simpleDateFormat = SimpleDateFormat("mm:ss")
            max_time.text = simpleDateFormat.format(mediaPlayer?.duration)
            updateTime()
            it.start()
        }
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


    fun play(song: Music, current : Boolean) {
        song_play.setImageResource(R.drawable.pause_btn)
        this.listMusic.add(song)
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
        mViewModel?.isLike(BaseConst.curUId, listMusic.elementAt(position))
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
            @SuppressLint("SimpleDateFormat")
            override fun run() {
                if (mediaPlayer != null) {
                    seekbar_music.progress = mediaPlayer!!.currentPosition
                    val simpleDateFormat = SimpleDateFormat("mm:ss")
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
        if (checkSlidingUpPanel()) collapseSlidingPanel()
        else {
            if (supportFragmentManager.backStackEntryCount == 1) this.showToolbar()
            super.onBackPressed()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            //  mImageUri = data?.extras?.get("uri") as Uri
            val uri = Uri.fromFile(File(mImageUri!!))
            val loading = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Updating avatar ...")
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(5f)
            loading.show()
            mViewModel?.updateAvatar(user?.uid!!, uri)?.observe(this, Observer<Boolean> { data ->
                run {
                    if (data != null && data) {
                        mViewModel?.getUser(user?.uid!!)?.observe(this, Observer<User> { data ->
                            run {
                                if (data != null) {
                                    Glide.with(this).load(data.profile_photo).thumbnail(0.0001f).into(avatar)
                                }
                            }
                        })
                        loading.dismiss()
                    } else {
                        loading.dismiss()
                        AlertDialog.Builder(this@MainScreen)
                            .setTitle("Thông báo")
                            .setMessage("Đã xảy ra lỗi trong quá trình upload file. Vui lòng thử lại sau")
                            .setPositiveButton(android.R.string.ok, null)
                            .show()
                    }
                }
            })
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mImageUri = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 1)
                }
            }
        }
    }


}
