package com.yang.lib_common.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.math.max


/**
 * @ClassName: FlowLayout
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/20 9:46
 */
class FlowLayout : ViewGroup {

    private val HEIGHT_MEASURESPEC = 0

    private val WIDTH_MEASURESPEC = 1


    private val space = 20


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        var resultWidth = 0
        var resultHeight = 0
        var maxItemHeight = 0
        if (childCount > 0) {
            for (i in 0 until childCount) {
                val childAt: View = getChildAt(i)
                val childWidth = childAt.measuredWidth
                val childHeight = childAt.measuredHeight
                if (resultWidth + childWidth > measuredWidth) {
                    resultWidth = 0
                    resultHeight += maxItemHeight + space
                    maxItemHeight = 0
                }
                childAt.tag = Rect(
                    resultWidth,
                    resultHeight,
                    resultWidth + childWidth,
                    resultHeight + childHeight
                )
                resultWidth += childWidth + space
                maxItemHeight = max(maxItemHeight, childHeight)
            }
        }
        setMeasuredDimension(widthMeasureSpec, resultHeight+maxItemHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val childAt: View = getChildAt(i)
            val rect = childAt.tag as Rect
            childAt.layout(rect.left, rect.top, rect.right , rect.bottom)
        }
    }


    fun <T> addChildView(
        resource: Int,
        mutableList: MutableList<T>,
        block: (view: View, position: Int, item: T) -> Unit
    ) {
        mutableList.forEachIndexed { index, t ->
            val inflate = LayoutInflater.from(context).inflate(resource, this, false)
            this.addView(inflate)
            block(inflate, index, mutableList[index])
        }
    }

}