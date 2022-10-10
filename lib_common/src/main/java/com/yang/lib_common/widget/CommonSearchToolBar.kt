package com.yang.lib_common.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.imageview.ShapeableImageView
import com.yang.lib_common.R
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.getStatusBarHeight

class CommonSearchToolBar : ConstraintLayout {

    var imageBackCallBack: ImageBackCallBack? = null

    var imageAddCallBack: ImageAddCallBack? = null



    lateinit var ivBack: ShapeableImageView


    lateinit var ivAdd: ImageView




    interface ImageBackCallBack {
        fun imageBackClickListener()
    }

    interface ImageAddCallBack {
        fun imageAddClickListener()
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val inflate = LayoutInflater.from(context).inflate(R.layout.view_common_search_toolbar, this)
        val llToolbar = inflate.findViewById<LinearLayout>(R.id.ll_container)
        llToolbar.setPadding(0, getStatusBarHeight(context), 0, 0)
        ivBack = inflate.findViewById(R.id.iv_back)
        ivAdd = inflate.findViewById(R.id.iv_add)
        val obtainStyledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.CommonToolBar)
        val leftImgVisible =
            obtainStyledAttributes.getBoolean(R.styleable.CommonToolBar_leftImgVisible, true)
        val leftImgSrc =
            obtainStyledAttributes.getResourceId(R.styleable.CommonToolBar_leftImgSrc, 0)

        val rightImgVisible =
            obtainStyledAttributes.getBoolean(R.styleable.CommonToolBar_rightImgVisible, false)
        val rightImgSrc =
            obtainStyledAttributes.getResourceId(R.styleable.CommonToolBar_rightImgSrc, 0)
        val toolbarBg = obtainStyledAttributes.getResourceId(
            R.styleable.CommonToolBar_toolbarBg, 0
        )
        if (toolbarBg != 0) {
            llToolbar.setBackgroundResource(toolbarBg)
        }
        if (leftImgVisible) {
            ivBack.visibility = View.VISIBLE
        } else {
            ivBack.visibility = View.GONE
        }

        if (leftImgSrc != 0) {
            ivBack.setImageResource(leftImgSrc)
        }
        if (rightImgVisible) {
            ivAdd.visibility = View.VISIBLE
        } else {
            ivAdd.visibility = View.GONE
        }
        if (rightImgSrc != 0) {
            ivAdd.setImageResource(rightImgSrc)
        }


        ivBack.clicks().subscribe {
            if (null != imageBackCallBack) {
                imageBackCallBack?.imageBackClickListener()
            } else {
                (context as Activity).finish()
            }
        }

        ivAdd.clicks().subscribe {
            imageAddCallBack?.imageAddClickListener()
        }

        obtainStyledAttributes.recycle()
    }

}