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

    var normalTint = Color.parseColor("#999999")

    var clickTint = Color.RED

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
    }

    private fun setImageTint(tint:Boolean){
        if (tint){
            DrawableCompat.setTintList(drawable, ColorStateList.valueOf(clickTint))
        }else{
            DrawableCompat.setTintList(drawable, ColorStateList.valueOf(normalTint))
        }
    }
}