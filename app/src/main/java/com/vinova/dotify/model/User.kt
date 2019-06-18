package com.vinova.dotify.model

import androidx.databinding.ObservableField

class User {
    var email: ObservableField<String> = ObservableField()
    var username: ObservableField<String> = ObservableField()
    var gender: ObservableField<String> = ObservableField()
    var birthdate: ObservableField<String> = ObservableField()
    var profile_photo: ObservableField<String> = ObservableField()
}