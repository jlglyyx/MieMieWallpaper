package com.yang.module_mine.adapter

import android.graphics.Paint
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjq.shape.layout.ShapeLinearLayout
import com.yang.module_mine.R
import com.yang.module_mine.data.PayTypeData
import com.yang.module_mine.data.VipPackageData

/**
 * @ClassName: VipPackageAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/16 13:47
 */
class VipPackageAdapter :
    BaseQuickAdapter<VipPackageData, BaseViewHolder>(R.layout.item_vip_package) {

    override fun convert(helper: BaseViewHolder, item: VipPackageData) {

        val tvOriginalPrice = helper.getView<TextView>(R.id.tv_original_price)
        tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        helper.setText(R.id.stv_desc, item.desc)
            .setText(R.id.tv_time, item.timeDesc)
            .setText(R.id.tv_price, item.price)
            .setText(R.id.tv_original_price,"原价"+ item.originalPrice)
        val shapeLinearLayout = helper.itemView as ShapeLinearLayout
        shapeLinearLayout.isEnabled = !item.isSelect

    }


    fun getSelectItem(): VipPackageData?{
        return data.findLast {
            it.isSelect
        }
    }
}