package com.vinova.dotify.model

data class Music(
    var id : String? = null,
    var artist : String? = null,
    var musicURL : String = "",
    var name : String = "",
    var photoURL : String = ""
)