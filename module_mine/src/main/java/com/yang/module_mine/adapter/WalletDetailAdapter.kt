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

//        val ivPayType = helper.getView<ImageTintView>(R.id.iv_pay_type)
//
//        helper.setText(R.id.tv_pay_type,if (TextUtils.equals(item.id , "2")) "${item.desc} (${item.balance}元)" else item.desc)
//            .setImageResource(R.id.iv_pay_type,item.imgResourceId)
//        ivPayType.tintClick = item.isSelect
//        if (item.isSelect){
//            helper.setTextColor(R.id.tv_pay_type,mContext.getColor(com.yang.lib_common.R.color.appColor))
//        }else{
//            helper.setTextColor(R.id.tv_pay_type,mContext.getColor(com.yang.lib_common.R.color.textColor_999999))
//        }
    }

}