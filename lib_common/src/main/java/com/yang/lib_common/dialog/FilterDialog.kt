package com.yang.lib_common.dialog

import android.content.Context
import com.lxj.xpopup.impl.PartShadowPopupView
import com.yang.lib_common.R
import com.yang.lib_common.databinding.DialogFilterBinding

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
    var showBlock : (mViewBinding:DialogFilterBinding) -> Unit = {}
    var dismissBlock : (mViewBinding:DialogFilterBinding) -> Unit = {}

    override fun onCreate() {
        super.onCreate()
        block(mViewBinding)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_filter
    }

    override fun onShow() {
        super.onShow()
        showBlock(mViewBinding)
    }

    override fun onDismiss() {
        super.onDismiss()
        dismissBlock(mViewBinding)
    }


}