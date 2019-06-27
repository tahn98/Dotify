package com.vinova.dotify.utils

import com.vinova.dotify.model.Music

class BaseConst{
    companion object{
        val passMusicCollection = "PASSLISTMUSICALBUM"
        val passmusicobject = "MUSICOBJECT"
        val passlistmusicobject = "passlistmusicobject"
        val passartist = "passartist"
        var curUId=""
        var musicStorage:MutableCollection<Music> = ArrayList<Music>()
    }
}