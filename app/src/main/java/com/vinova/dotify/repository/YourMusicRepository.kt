package com.vinova.dotify.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection

class YourMusicRepository{
    fun getAlbums(UID : String) : MutableLiveData<MutableList<MusicCollection>>? {
        var listAlbums : MutableLiveData<MutableList<MusicCollection>> = MutableLiveData()
        listAlbums.value = null
        var mData : DatabaseReference = FirebaseDatabase.getInstance().reference

        mData.child("users")
            .child(UID)
            .child("listAlbums")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var listTemp : MutableList<MusicCollection> = arrayListOf()
                    listTemp.clear()
                    for (data in p0.children){
                        val albumModel : MusicCollection = data.getValue(MusicCollection::class.java)!!
                        listTemp.add(albumModel)
                    }
                    listAlbums?.value = listTemp
                }
            })
        return listAlbums
    }

    fun getYourMusic(UID : String) : MutableLiveData<MutableList<Music>>? {
        var listMusics : MutableLiveData<MutableList<Music>> = MutableLiveData()
        var mData : DatabaseReference = FirebaseDatabase.getInstance().reference

        mData.child("users")
            .child(UID)
            .child("listMusics")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var listTemp : MutableList<Music> = arrayListOf()
                    listTemp.clear()
                    for (data in p0.children){
                        val musicModel : Music = data.getValue(Music::class.java)!!
                        listTemp.add(musicModel)
                    }
                    listMusics?.value = listTemp
                }
            })
        return listMusics
    }

    fun getYourArtist(UID : String) : MutableLiveData<MutableList<MusicCollection>>? {
        var listArtist : MutableLiveData<MutableList<MusicCollection>> = MutableLiveData()
        listArtist.value = null
        var mData : DatabaseReference = FirebaseDatabase.getInstance().reference

        mData.child("users")
            .child(UID)
            .child("listArtists")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var listTemp : MutableList<MusicCollection> = arrayListOf()
                    listTemp.clear()
                    for (data in p0.children){
                        val albumModel : MusicCollection = data.getValue(MusicCollection::class.java)!!
                        listTemp.add(albumModel)
                    }
                    listArtist?.value = listTemp
                }
            })
        return listArtist
    }
}