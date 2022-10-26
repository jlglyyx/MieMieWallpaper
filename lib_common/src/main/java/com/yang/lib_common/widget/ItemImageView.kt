package com.yang.lib_common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yang.lib_common.R
import com.yang.lib_common.databinding.ViewItemImageBinding

/**
 * @ClassName: ItemImageView
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/13 10:37
 */
class ItemImageView:LinearLayout {
    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        if (attrs != null) {
            init(context,attrs)
        }
    }
    lateinit var mViewItemImageBinding:ViewItemImageBinding


    private fun init(context:Context,attrs: AttributeSet){
        mViewItemImageBinding = ViewItemImageBinding.inflate(LayoutInflater.from(context),this,true)

        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ItemImageView)
        val itemTitle = obtainStyledAttributes.getString(R.styleable.ItemImageView_itemTitle)

        val itemTipImgSrc =
            obtainStyledAttributes.getResourceId(R.styleable.ItemImageView_itemTipImgSrc, 0)

        val itemImgSrc =
            obtainStyledAttributes.getResourceId(R.styleable.ItemImageView_itemImgSrc, 0)

        mViewItemImageBinding.tvTv.text = itemTitle

        if (itemTipImgSrc != 0){
            mViewItemImageBinding.ivTip.setImageResource(itemTipImgSrc)
        }

        if (itemImgSrc != 0){
            mViewItemImageBinding.ivImage.setImageResource(itemImgSrc)
        }

        obtainStyledAttributes.recycle()
    }
}