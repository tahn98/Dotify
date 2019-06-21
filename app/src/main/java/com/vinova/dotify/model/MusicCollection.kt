package com.vinova.dotify.model

data class MusicCollection(
   var photoURL : String = "",
   var artist : String = "",
   var itemCount : Int = 0,
   var id : String? = null,
   var name : String = "",
   var listMusic : MutableMap<String, Music>? = mutableMapOf()
)