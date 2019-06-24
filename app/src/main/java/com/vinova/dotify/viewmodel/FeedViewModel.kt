package com.vinova.dotify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.repository.FeedRepository

class FeedViewModel:ViewModel() {
    private var latest: LiveData<MusicCollection>? = null
    private var suggested: LiveData<List<MusicCollection>>?=null
    private var albums: LiveData<List<MusicCollection>>?=null
    private var genres: LiveData<List<MusicCollection>>?=null
    private var repository: FeedRepository = FeedRepository()
    init {
        if(latest==null) latest=repository.getLatest()
        if(suggested==null) suggested=repository.getSuggested()
        if(albums==null) albums=repository.getAlbum()
        if(genres==null) genres=repository.getGenre()
    }
    fun getLatest(): LiveData<MusicCollection>?
    {
        return latest
    }
    fun getSuggested(): LiveData<List<MusicCollection>>?
    {
        return suggested
    }
    fun getAlbum(): LiveData<List<MusicCollection>>?
    {
        return albums
    }
    fun getGenre(): LiveData<List<MusicCollection>>?
    {
        return genres
    }
}