package com.vinova.dotify.model

import java.io.Serializable

data class Music(
    var id : String? = null,
    var artist : String? = null,
    var musicURL : String = "",
    var name : String = "",
    var photoURL : String = ""
) : Serializable