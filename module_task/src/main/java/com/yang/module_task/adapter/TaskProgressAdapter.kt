package com.yang.module_task.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.yang.lib_common.widget.AccountImageListView
import com.yang.module_task.data.TaskProgressData
import com.yang.module_task.R

/**
 * @ClassName: TaskProgressAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/26 13:59
 */
class TaskProgressAdapter(data: MutableList<TaskProgressData.TaskProgressItemData>?) :
    BaseQuickAdapter<TaskProgressData.TaskProgressItemData, BaseViewHolder>(data) {

    init {
        mLayoutResId = R.layout.item_task_progress
    }

    override fun convert(helper: BaseViewHolder, item: TaskProgressData.TaskProgressItemData) {
        helper.setVisible(R.id.view_left_line, helper.absoluteAdapterPosition != 0)
            .setVisible(R.id.view_right_line, helper.absoluteAdapterPosition != data.size - 1)
            .setText(R.id.tv_progress,item.progress)
            .setBackgroundRes(R.id.view_circle,if (item.complete) R.drawable.shape_circle_32cd32_bg else R.drawable.shape_circle_808080_bg)
            .setBackgroundColor(R.id.view_left_line,if (item.complete) mContext.resources.getColor(R.color.limegreen) else mContext.resources.getColor(R.color.grey))
            .setBackgroundColor(R.id.view_right_line,if (item.complete) mContext.resources.getColor(R.color.limegreen) else mContext.resources.getColor(R.color.grey))
    }


}