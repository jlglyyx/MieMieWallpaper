package com.yang.module_mine.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.lib_common.widget.ImageTintView
import com.yang.module_mine.R
import com.yang.module_mine.data.PayTypeData
import com.yang.module_mine.data.WalletDetailData

/**
 * @ClassName: WalletDetailAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/16 13:47
 */
class WalletDetailAdapter : BaseQuickAdapter<WalletDetailData, BaseViewHolder>(R.layout.item_wallet_detail) {

    override fun convert(helper: BaseViewHolder, item: WalletDetailData) {

        helper.setText(R.id.tv_title,item.title)
        .setText(R.id.tv_name,item.payTypeDesc)
        .setText(R.id.tv_time,item.createTime)
        .setText(R.id.tv_price,(if (item.revenueOrExpenditure == 0) "-" else "+") + item.price)

    }

}