package com.vinova.dotify.model
import java.io.Serializable

class Music : Serializable {
    var id:String?=null
    var artist:String?=null
    var listenCounter:Int?=0
    var musicURL:String=""
    var name:String=""
    var backdropURL:String?=null
    var posterURL:String?=null
    var description:String?=null
}

