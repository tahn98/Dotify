package com.vinova.dotify.utils

import com.vinova.dotify.model.Music

interface OnViewPagerItemClickListener {
    fun onClick(collection: Music)
}