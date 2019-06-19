package com.vinova.dotify.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

class BindingAdapter{
    companion object {
        @BindingAdapter("app:errorText")
        fun errorText(view: TextInputLayout, error:String)
        {
            view.error = error
        }
    }
}