package com.vinova.dotify.view.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView

class RoundRectCornerImageView : ImageView {
    private val radius = 360.0f
    private var path: Path? = null
    private var rect: RectF? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        path = Path()

    }

    override fun onDraw(canvas: Canvas) {
        rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
        path!!.addRoundRect(rect!!, radius, radius, Path.Direction.CW)
        canvas.clipPath(path!!)
        super.onDraw(canvas)
    }
}
