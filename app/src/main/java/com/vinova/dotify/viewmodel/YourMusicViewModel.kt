package com.vinova.dotify.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.repository.YourMusicRepository

class YourMusicViewModel : ViewModel(){
    private var yourMusicModel = YourMusicRepository()

    fun getAlbulm(uid : String) : MutableLiveData<MutableList<MusicCollection>>?{
        return yourMusicModel?.getAlbulm(uid)
    }

    fun getListMusic(uid: String): MutableLiveData<MutableList<Music>>?{
        return yourMusicModel?.getYourMusic(uid)
    }
}