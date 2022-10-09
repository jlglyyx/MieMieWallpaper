package com.yang.module_task.dialog

import android.content.Context
import android.view.LayoutInflater
import com.lxj.xpopup.core.FullScreenDialog
import com.lxj.xpopup.impl.FullScreenPopupView
import com.yang.lib_common.databinding.DialogImageViewpagerBinding
import com.yang.lib_common.util.clicks
import com.yang.module_task.R
import com.yang.module_task.databinding.DialogTaskDetailBinding

/**
 * @ClassName: TaskDetailDialog
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/1 14:05
 */
class TaskDetailDialog(context: Context) : FullScreenPopupView(context) {


    private val mBinding by lazy {
        DialogTaskDetailBinding.bind(contentView)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_task_detail
    }


    override fun onCreate() {
        super.onCreate()

        mBinding.tvClose.clicks().subscribe {
            dismiss()
        }
    }
}