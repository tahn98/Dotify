package com.vinova.dotify.model

import java.io.Serializable

class Feed:Serializable {
    private var albums: MutableMap<String,Album>?=null
}