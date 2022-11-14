package com.yang.lib_common.adapter

import androidx.core.content.ContentProviderCompat.requireContext
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjq.shape.view.ShapeTextView
import com.yang.lib_common.R

/**
 * @ClassName: TabMoreAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/14 15:28
 */
class TabMoreAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_filter_tab) {
    var currentPosition = 0
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.tv_title, item)
        val tvTitle = helper.getView<ShapeTextView>(R.id.tv_title)
        if (helper.absoluteAdapterPosition == currentPosition){
            tvTitle.shapeDrawableBuilder.setSolidColor(mContext.getColor(
                R.color.appColor)).intoBackground()
            tvTitle.setTextColor(mContext.getColor(
                R.color.white))
        }else{
            tvTitle.shapeDrawableBuilder.setSolidColor(mContext.getColor(
                R.color.white)).intoBackground()
            tvTitle.setTextColor(mContext.getColor(
                R.color.textColor_666666))
        }
    }

}