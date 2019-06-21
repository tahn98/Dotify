package com.vinova.dotify.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vinova.dotify.R
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.viewmodel.YourMusicViewModel
import kotlinx.android.synthetic.main.your_music.*

class YourMusicFragment : Fragment() {

//    private lateinit var mYourMusicViewViewModel : YourMusicViewModel
//    private var listMusic : MutableList<Music> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

//        mYourMusicViewViewModel = ViewModelProviders.of(this).get(YourMusicViewModel::class.java)
//        mYourMusicViewViewModel.getListMusic("HkWQty0QRTh9eEaBdCngJQuU1uf2")
//            ?.observe(this, Observer <MutableList<Music>>{
//                run{
//                    if(it != null){
//                        listMusic.addAll(it)
//                        Log.d("hihi", it[0].name)
//                    }
//                }
//            })
        return inflater.inflate(R.layout.your_music, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        albums_btn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.tab_container, AlbumFragment.newInstance(), null)
                ?.commit()
        }

        songs_btn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.tab_container, SongsFragment.newInstance(), null)
                ?.commit()
        }
    }
}