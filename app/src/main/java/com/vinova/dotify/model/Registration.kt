package com.vinova.dotify.model

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.databinding.ObservableField

class Registration() {
    var email: ObservableField<String> = ObservableField()
    var username: ObservableField<String> = ObservableField()
    var password: ObservableField<String> = ObservableField()
    var gender: ObservableField<String> = ObservableField()
    var birthdate: ObservableField<String> = ObservableField()
    var isFull: ObservableField<Boolean> = ObservableField(false)
    init {
        email.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
        username.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
        password.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
        gender.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
        birthdate.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
    }

    fun checkEmpty() {
        if (email.get().isNullOrEmpty()

        ) {
            isFull.set(false)
        }
        isFull.set(true)
    }
}