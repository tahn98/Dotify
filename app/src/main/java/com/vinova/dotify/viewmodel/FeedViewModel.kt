package com.vinova.dotify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.repository.FeedRepository
import androidx.lifecycle.MediatorLiveData
import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import com.vinova.dotify.model.Music


class FeedViewModel:ViewModel() {
    private var latest: LiveData<MusicCollection>? = null
    private var suggested: LiveData<List<MusicCollection>>?=null
    private var albums: LiveData<List<MusicCollection>>?=null
    private var genres: LiveData<List<MusicCollection>>?=null
    private var sampleSuggested: LiveData<List<MusicCollection>>?=null
    private var sampleAlbum: LiveData<List<MusicCollection>>?=null
    private var sampleGenre: LiveData<List<MusicCollection>>?=null
    private var repository: FeedRepository = FeedRepository()
    init {
        if(latest==null) latest=repository.getLatest()
        if(suggested==null) suggested=repository.getSuggested()
        sampleSuggested=Transformations.map(suggested!!) {
            var temp=it
            temp=temp.take(4)
            temp
        }
        if(albums==null) albums=repository.getAlbum()
        sampleAlbum=Transformations.map(albums!!) {
            var temp=it
            temp=temp.take(2)
            temp
        }
        if(genres==null) genres=repository.getGenre()
        sampleGenre=Transformations.map(genres!!) {
            var temp=it
            temp=temp.take(4)
            temp
        }
    }
    fun getLatest(): LiveData<MusicCollection>?
    {
        return latest
    }
    fun getSuggested(): LiveData<List<MusicCollection>>?
    {
        return sampleSuggested
    }
    fun getAlbum(): LiveData<List<MusicCollection>>?
    {
        return sampleAlbum
    }
    fun getGenre(): LiveData<List<MusicCollection>>?
    {
        return sampleGenre
    }

    fun getAllSuggested(): LiveData<List<MusicCollection>>?
    {
        return suggested
    }
    fun getAllAlbum(): LiveData<List<MusicCollection>>?
    {
        return albums
    }
    fun getAllGenre(): LiveData<List<MusicCollection>>?
    {
        return genres
    }
}