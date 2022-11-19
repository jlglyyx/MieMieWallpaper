package com.yang.lib_common.dialog

import android.content.Context
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.impl.FullScreenPopupView
import com.yang.lib_common.R
import com.yang.lib_common.databinding.DialogPermissionBinding
import com.yang.lib_common.databinding.DialogPrivacyAgreementBinding
import com.yang.lib_common.util.clicks

/**
 * @ClassName: PrivacyAgreementDialog
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/18 9:36
 */
class PermissionDialog(context: Context) : FullScreenPopupView(context) {

    private val mBinding by lazy {
        DialogPermissionBinding.bind(contentView)
    }

    var onConfirm : () -> Unit = {}
    var onCancel : () -> Unit = {}


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_permission
    }


    override fun onCreate() {
        super.onCreate()

        mBinding.tvCancel.clicks().subscribe {
            onCancel()
        }
        mBinding.tvConfirm.clicks().subscribe {
            onConfirm()
        }
    }

    override fun doDismissAnimation() {
//        super.doDismissAnimation()
    }

    override fun onDismiss() {
        super.onDismiss()
    }
}