package com.yang.module_task.data

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @ClassName: TaskData
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/26 14:45
 */
class TaskProgressData(var mItemType:Int,var mLevel:Int): AbstractExpandableItem<TaskProgressData>(),MultiItemEntity {

    var progressList:MutableList<TaskProgressItemData>? = mutableListOf<TaskProgressItemData>().apply {
        add(TaskProgressItemData("开始任务",true))
        add(TaskProgressItemData("提交任务凭证",false))
        add(TaskProgressItemData("提交支付凭证",false))
        add(TaskProgressItemData("提交评价凭证",false))
    }

    override fun getLevel(): Int {
        return mLevel
    }

    override fun getItemType(): Int {
        return mItemType
    }
    data class TaskProgressItemData(val progress:String,val complete:Boolean){

    }

}
