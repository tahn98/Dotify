package com.vinova.dotify.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.model.User

class UserRepository {

    fun checkUser(UID: String): MutableLiveData<Boolean> {
        val res = MutableLiveData<Boolean>()
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        val ref = mDatabase.child("users").orderByKey().equalTo(UID)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                res.value = !dataSnapshot.exists()
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
        return res
    }

    fun postUser(user: User) {
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        mDatabase.child("users").child(user.uid).setValue(user)
    }

    fun createUser(email: String, password: String): MutableLiveData<String>? {
        val res = MutableLiveData<String>()
        val mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                res.value = task.result?.user!!.uid

            } else {
                res.value = "-"
            }
        }
        return res
    }

    fun logInUser(email: String, password: String): MutableLiveData<String>? {
        val res = MutableLiveData<String>()
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                res.value = task.result?.user?.uid
            } else {
                res.value = "-1"
            }
        }
        return res
    }

    fun logInUser(credential: AuthCredential): MutableLiveData<String>? {
        val res = MutableLiveData<String>()
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                res.value = task.result?.user?.uid
            } else {
                res.value = "-1"
            }
        }
        return res
    }

    fun getUserInfo(UID: String): MutableLiveData<User>? {
        val res = MutableLiveData<User>()
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        val ref = mDatabase.child("users").orderByKey().equalTo(UID)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    res.value = dataSnapshot.getValue(User::class.java)
                } else {
                    res.value?.uid = "-1"
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
        return res
    }

    fun isLike(UID: String, collection: MusicCollection, type: String): MutableLiveData<Boolean> {
        val res = MutableLiveData<Boolean>()
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        var ref = mDatabase.child("users").child(UID)
        ref = when (type) {
            "ALBUM" -> ref.child("listAlbums")
            "PLAYLIST" -> ref.child("listPlaylists")
            else -> ref.child("listPlaylists")
        }
        var query = ref.orderByKey().equalTo(collection.id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                res.value = dataSnapshot.exists()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return res
    }

    fun likeCollection(UID: String, collection: MusicCollection, type: String, action: Boolean) {
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        var ref = mDatabase.child("users").child(UID)
        ref = when (type) {
            "ALBUM" -> ref.child("listAlbums")
            "PLAYLIST" -> ref.child("listPlaylists")
            else -> ref.child("listPlaylists")
        }
        if (action) {
            ref.child(collection.id!!).removeValue()
        } else {
            ref.child(collection.id!!).setValue(collection)
        }

    }

    fun likeMusic(UID: String, music: Music, action: Boolean) {
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        var ref = mDatabase.child("users").child(UID).child("listMusics")
        if (action) {
            ref.child(music.id!!).removeValue()
        } else {
            ref.child(music.id!!).setValue(music)
        }

    }

    fun isLike(UID: String, music: Music): MutableLiveData<Boolean> {
        val res = MutableLiveData<Boolean>()
        val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
        var ref = mDatabase.child("users").child(UID)

        var query = ref.child("listMusics").orderByKey().equalTo(music.id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                res.value = dataSnapshot.exists()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return res
    }
}