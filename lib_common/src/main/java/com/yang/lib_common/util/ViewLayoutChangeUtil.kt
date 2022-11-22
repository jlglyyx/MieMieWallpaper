package com.yang.lib_common.util

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup

/**
 * @ClassName: ViewLayoutChangeUtil
 * @Description:
 * @Author: yxy
 * @Date: 2022/6/8 15:02
 */
class ViewLayoutChangeUtil {

    private var isFirst = true
    var currentHeight = 0

    lateinit var layoutParams:ViewGroup.LayoutParams

    fun add(rootView: View,onChange:(open:Boolean) -> Unit ={}){
        val rect = Rect()
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            if (!isFirst){
                rootView.getWindowVisibleDisplayFrame(rect)
                if (currentHeight != rect.bottom){
                    layoutParams.height = rect.bottom
                    rootView.requestLayout()
                    onChange(currentHeight > rect.bottom)
                    currentHeight = rect.bottom
                }
            }
        }
        layoutParams = rootView.layoutParams
        isFirst = false
    }

}