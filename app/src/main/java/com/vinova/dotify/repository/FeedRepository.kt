package com.vinova.dotify.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.vinova.dotify.model.MusicCollection

class FeedRepository {

    fun getLatest(): MutableLiveData<MusicCollection> {
        val res = MutableLiveData<MusicCollection>()
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        val ref = mDatabase.child("browse").child("latest")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                res.value = dataSnapshot.getValue(MusicCollection::class.java)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
        return res
    }

    fun getSuggested(): MutableLiveData<List<MusicCollection>> {
        val res = MutableLiveData<List<MusicCollection>>()
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        val ref = mDatabase.child("browse").child("playlist")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists())
                {
                    val result:MutableCollection<MusicCollection> = ArrayList<MusicCollection>()
                    for ( postSnapshot in dataSnapshot.children) {
                        var data=postSnapshot.getValue(MusicCollection::class.java)
                        result.add(data!!)
                    }
                    res.value=result.toList()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
        return res
    }

    fun getAlbum(): MutableLiveData<List<MusicCollection>> {
        val res = MutableLiveData<List<MusicCollection>>()
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        val ref = mDatabase.child("browse").child("album")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists())
                {
                    val result:MutableCollection<MusicCollection> = ArrayList<MusicCollection>()
                    for ( postSnapshot in dataSnapshot.children) {
                        var data=postSnapshot.getValue(MusicCollection::class.java)
                        result.add(data!!)
                    }
                    res.value=result.toList()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
        return res
    }

    fun getGenre(): MutableLiveData<List<MusicCollection>> {
        val res = MutableLiveData<List<MusicCollection>>()
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        val ref = mDatabase.child("browse").child("genre")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists())
                {
                    val result:MutableCollection<MusicCollection> = ArrayList<MusicCollection>()
                    for ( postSnapshot in dataSnapshot.children) {
                        var data=postSnapshot.getValue(MusicCollection::class.java)
                        result.add(data!!)
                    }
                    res.value=result.toList()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
        return res
    }
}