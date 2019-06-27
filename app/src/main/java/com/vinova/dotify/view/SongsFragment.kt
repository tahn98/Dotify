package com.vinova.dotify.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinova.dotify.R
import com.vinova.dotify.adapter.YourSongAdapter
import com.vinova.dotify.model.Music
import com.vinova.dotify.viewmodel.YourMusicViewModel
import kotlinx.android.synthetic.main.songs_fragment.*

class SongsFragment : Fragment(){
    private lateinit var mYourMusicViewViewModel : YourMusicViewModel
    private var listMusic : MutableList<Music> = arrayListOf()
    private lateinit var songAdapter : YourSongAdapter

    companion object{
        fun newInstance() : SongsFragment{
            return SongsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        mYourMusicViewViewModel = ViewModelProviders.of(this).get(YourMusicViewModel::class.java)
        mYourMusicViewViewModel.getListMusic("HkWQty0QRTh9eEaBdCngJQuU1uf2")
            ?.observe(this, Observer <MutableList<Music>>{
                run{
                    if(it != null){
                        listMusic.clear()
                        listMusic.addAll(it)
                        songAdapter.notifyDataSetChanged()
                        Log.d("it", it.toString())
                        if(listMusic.isEmpty()){
                            nothing_text_song.text = "You don't have any music collection"
                        }
                    }
                }
            })
        return inflater.inflate(R.layout.songs_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        songs_recycleView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        songAdapter = YourSongAdapter( context!! ,listMusic) { music : Music -> itemClicked(music)}
        songs_recycleView.adapter = songAdapter

        songAdapter.notifyDataSetChanged()
    }

    private fun itemClicked(music: Music) {
        (activity as MainScreen).play(music)
    }
}