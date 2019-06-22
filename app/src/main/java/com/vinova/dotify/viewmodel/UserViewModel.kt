package com.vinova.dotify.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
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
}