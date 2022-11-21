package com.yang.module_mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.module_mine.R

/**
 * @ClassName: WalletDetailFilterAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/14 15:28
 */
class WalletDetailFilterAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_wallet_detail_filter) {

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.tv_title, item)
    }

}