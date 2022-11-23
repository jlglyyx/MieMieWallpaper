package com.yang.lib_common.util

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.yang.lib_common.constant.AppConstant

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

    fun add(rootView: View,requestLayout:Boolean = true,onChange:(open:Boolean,height:Int) -> Unit ={_,_ ->}){
        val rect = Rect()
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            if (!isFirst){
                rootView.getWindowVisibleDisplayFrame(rect)
                if (currentHeight != rect.bottom){
                    if (requestLayout){
                        layoutParams.height = rect.bottom
                        rootView.requestLayout()
                    }
                    val height = currentHeight - rect.bottom
                    if (height > 0){
                        setMMKVValue(AppConstant.Constant.SOFT_INPUT_HEIGHT,height)
                    }
                    onChange(currentHeight > rect.bottom, height)
                    currentHeight = rect.bottom
                }
            }
        }
        layoutParams = rootView.layoutParams
        isFirst = false
    }

}