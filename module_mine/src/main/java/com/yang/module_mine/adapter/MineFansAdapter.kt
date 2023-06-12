package com.yang.module_mine.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.lib_common.util.formatNumUnit
import com.yang.lib_common.util.loadCircle
import com.yang.module_mine.R
import com.yang.lib_common.data.FansData

/**
 * @ClassName: MineFansAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/12/8 10:01
 */
class MineFansAdapter : BaseQuickAdapter<FansData, BaseViewHolder>(R.layout.item_fans) {

    override fun convert(helper: BaseViewHolder, item: FansData) {
        val imageView = helper.getView<ImageView>(R.id.iv_image)
        imageView.loadCircle(mContext, item.userAttr)
        helper.setText(R.id.tv_desc, "粉丝：${if (item.userFan == null) "0" else item.userFan?.formatNumUnit()} 关注：${if (item.userAttention == null) "0" else item.userAttention?.formatNumUnit()}")
            .setText(R.id.tv_name, item.userName)
            .setText(R.id.tv_attention, if (item.isFollowAll) "互相关注" else "已关注")
            .addOnClickListener(R.id.tv_attention)
    }
}