package com.yang.module_mine.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.module_mine.databinding.ActTaskHistoryBinding

/**
 * @ClassName: TaskHistoryActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/4 16:58
 */
@Route(path = AppConstant.RoutePath.MINE_TASK_HISTORY_ACTIVITY)
class TaskHistoryActivity:BaseActivity<ActTaskHistoryBinding>() {
    override fun initViewBinding(): ActTaskHistoryBinding {
        return bind(ActTaskHistoryBinding::inflate)
    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun initViewModel() {

    }
}