package com.vinova.dotify.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.model.User
import com.vinova.dotify.repository.UserRepository

class UserViewModel :ViewModel() {
    private var check: MutableLiveData<Boolean>? = null
    private var user: MutableLiveData<User>? = null
    private var userModel: UserRepository = UserRepository()
    fun checkUser(UID:String): MutableLiveData<Boolean>? {
        check=userModel.checkUser(UID)
        return this.check
    }
    fun postUser(user:User){
        userModel.postUser(user)
    }
    fun createUser(email: String, password:String): MutableLiveData<String>? {
        return userModel.createUser(email,password)
    }

    fun logInUser(email: String, password:String): MutableLiveData<String>? {
        return userModel.logInUser(email,password)
    }

    fun logInUser(credential: AuthCredential): MutableLiveData<String>? {
        return userModel.logInUser(credential)
    }

    fun getUser(UID:String): MutableLiveData<User>? {
        user=userModel.getUserInfo(UID)
        return user
    }

    fun isLike(UID:String,collection:MusicCollection,type:String): MutableLiveData<Boolean> {
        return userModel.isLike(UID,collection,type)
    }

    fun isLike(UID:String,collection: Music): MutableLiveData<Boolean> {
        return userModel.isLike(UID,collection)
    }
    fun likeCollection(UID:String,collection:MusicCollection,type:String,action:Boolean){
        userModel.likeCollection(UID,collection,type,action)
    }

    fun likeMusic(UID:String,music:Music,action:Boolean){
        userModel.likeMusic(UID,music,action)
    }

    fun updateAvatar(UID:String,uri: Uri?):MutableLiveData<Boolean>
    {
        return userModel.updateAvatar(UID,uri)
    }
}