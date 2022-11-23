package com.yang.lib_common.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import java.lang.Integer.min

/**
 * @ClassName: CircleImageView
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/23 15:46
 */
class CircleImageView:AppCompatImageView {

    private val mPaint = Paint()

    private var radius = 0f

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val diameter = min(w,h)
        radius = (diameter/2).toFloat()
    }


    override fun onDraw(canvas: Canvas) {
//        mPaint.color = Color.RED
//        canvas.drawCircle(width/2f, height/2f,radius,mPaint)
        val saveLayer = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        canvas.drawCircle(width/2f, height/2f,radius,mPaint)
        //canvas.drawRoundRect(0f,0f,width.toFloat(),height.toFloat(),20f,20f,mPaint)
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(drawable.toBitmap(min(width,height),min(width,height)),width/2f-min(width,height)/2f,height/2f-min(width,height)/2f,mPaint)
//        super.onDraw(canvas)
        mPaint.xfermode = null
        canvas.restoreToCount(saveLayer)
    }
}