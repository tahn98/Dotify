package com.vinova.dotify.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.vinova.dotify.R
import com.vinova.dotify.model.Music
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.disk_fragment.*

class DiskFragment : Fragment(){

    lateinit var mView : View
    lateinit var circleImageView : CircleImageView
    lateinit var objectAnimator : ObjectAnimator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mView = inflater.inflate(R.layout.disk_fragment, container, false)
        circleImageView = mView.findViewById(R.id.disk)
        objectAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0f, 360f)
        objectAnimator.duration = 10000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.RESTART
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.start()

        return mView
    }

    fun setDiskImage(music : Music){
        Glide
            .with(context!!)
            .load(music.posterURL)
            .centerCrop()
            .thumbnail(0.5f)
            .error(R.drawable.disk_place_holder)
            .into(disk)
    }

//    fun rotate(){
//        if(objectAnimator.isPaused){
//            objectAnimator.resume()
//        }else{
//            objectAnimator.start()
//        }
//    }
//
//    fun stopRotate(){
//        objectAnimator.pause()
//    }

}