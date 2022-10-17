package com.yang.module_main.ui.dialog

import android.content.Context
import android.view.ViewGroup
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.impl.PartShadowPopupView
import com.yang.lib_common.util.getScreenPx
import com.yang.module_main.R
import com.yang.module_main.databinding.DialogFilterBinding

/**
 * @ClassName: FilterDialog
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/17 13:56
 */
class FilterDialog(context: Context) : PartShadowPopupView(context) {

    private val mViewBinding by lazy {
        DialogFilterBinding.bind(attachPopupContainer.getChildAt(0))
    }

    var block : (mViewBinding:DialogFilterBinding) -> Unit = {}

    override fun onCreate() {
        super.onCreate()
        block(mViewBinding)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_filter
    }


}