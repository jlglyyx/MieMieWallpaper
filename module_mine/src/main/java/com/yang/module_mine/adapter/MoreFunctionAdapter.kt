package com.yang.module_mine.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.lib_common.constant.AppConstant
import com.yang.module_mine.R
import com.yang.module_mine.data.MoreFunctionData

/**
 * @ClassName: MoreFunctionAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 16:46
 */
class MoreFunctionAdapter(data: MutableList<MoreFunctionData>?) : BaseQuickAdapter<MoreFunctionData, BaseViewHolder>(data) {

    init {
        mLayoutResId = R.layout.item_mine_more_function
    }

    override fun convert(helper: BaseViewHolder, item: MoreFunctionData) {
        helper.setImageResource(R.id.iv_icon,item.icon)
            .setText(R.id.tv_name,item.name)
            .setGone(R.id.tv_badge,TextUtils.equals(item.name,"消息通知"))
    }
}