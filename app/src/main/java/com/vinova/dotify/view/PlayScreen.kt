package com.vinova.dotify.view

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.SeekBar
import android.widget.Toast
import com.vinova.dotify.R
import com.vinova.dotify.model.Music
import com.vinova.dotify.utils.BaseConst
import kotlinx.android.synthetic.main.activity_play_screen.*

class PlayScreen : AppCompatActivity() {
    private lateinit var music : Music
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_screen)

        if (android.os.Build.VERSION.SDK_INT > 9) {
            var policy: StrictMode.ThreadPolicy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)
        }

        music = intent.extras?.get(BaseConst.passmusicobject) as Music

        class PlayMusic : AsyncTask<String, Unit, String>() {
            override fun doInBackground(vararg p0: String?): String {
                return p0[0]!!
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)

                mediaPlayer = MediaPlayer()
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mediaPlayer.setOnCompletionListener {
                    MediaPlayer.OnCompletionListener { p0 ->
                        p0?.stop()
                        p0?.reset()
                    }
                }
                mediaPlayer.setDataSource(result)
                mediaPlayer.prepare()
                mediaPlayer.start()

                TimeSong()
            }

            private fun TimeSong() {

            }
        }
    }
}
