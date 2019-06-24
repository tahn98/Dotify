package com.vinova.dotify.model

import java.io.Serializable

data class MusicCollection(
   var backdrop : String ="",
   var photoURL : String = "",
   var artist : String = "",
   var itemCount : Int = 0,
   var id : String? = null,
   var name : String = "",
   var listMusic : MutableMap<String, Music>? = mutableMapOf()
) : Serializable
