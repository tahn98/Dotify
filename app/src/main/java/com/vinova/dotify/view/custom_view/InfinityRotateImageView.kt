package com.vinova.dotify.view.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.vinova.dotify.R


class InfinityRotateImageView : ImageView {
    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {

    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // Set and scale the image
        setImageResource(R.drawable.circle_logo)

        // Start the animation
        startAnimation()
    }

    /**
     * Starts the rotate animation.
     */
    private fun startAnimation() {
        clearAnimation()

        val rotate = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.duration = 1000
        rotate.repeatCount = Animation.INFINITE
        startAnimation(rotate)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE) {
            startAnimation()
        } else {
            clearAnimation()
        }
    }

}