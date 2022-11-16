package com.yang.module_mine.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.module_mine.R
import com.yang.module_mine.data.VipRightsData

/**
 * @ClassName: VipRightsAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/16 13:47
 */
class VipRightsAdapter : BaseQuickAdapter<VipRightsData, BaseViewHolder>(R.layout.item_vip_rights) {

    override fun convert(helper: BaseViewHolder, item: VipRightsData) {
        helper.setImageResource(R.id.iv_icon,item.icon)
            .setText(R.id.tv_name,item.name)
    }

}