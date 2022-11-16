package com.yang.lib_common.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.DrawableCompat
import com.yang.lib_common.R

/**
 * @ClassName: ImageTintView
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/25 16:07
 */
class ImageTintView : AppCompatImageView {

    var tintClick = false
    set(value) {
        field = value
        setImageTint(field)
    }

    private var normalTint = Color.parseColor("#999999")

    private var clickTint = Color.RED

//    var block : (view: View) -> Unit = {}

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
//        setOnClickListener {
////            setImageTint(tintClick)
////            tintClick = !tintClick
//            block(it)
//        }
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs,R.styleable.ImageTintView)
        val normalTintColor = obtainStyledAttributes.getResourceId(R.styleable.ImageTintView_normalTint,0)
        val clickTintColor = obtainStyledAttributes.getResourceId(R.styleable.ImageTintView_clickTint,0)
        if (normalTintColor != 0){
            normalTint = getContext().getColor(normalTintColor)
        }
        if (clickTintColor != 0){
            clickTint = getContext().getColor(clickTintColor)
        }


        obtainStyledAttributes.recycle()

    }

    private fun setImageTint(tint:Boolean){
        if (tint){
            DrawableCompat.setTintList(drawable, ColorStateList.valueOf(clickTint))
        }else{
            DrawableCompat.setTintList(drawable, ColorStateList.valueOf(normalTint))
        }
    }
}