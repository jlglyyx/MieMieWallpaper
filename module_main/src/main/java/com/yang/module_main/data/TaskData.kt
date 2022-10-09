package com.yang.module_main.data

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yang.lib_common.constant.AppConstant

/**
 * @ClassName: TaskData
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/26 14:45
 */
class TaskData(val id:String?=""){

    var userId: String? = null
    var userImage: String? = null

    var userName: String? = null
    var userVipLevel: Int? = 0
    var userCredit: Int? = 0

    var imageUrls: String? = null

    var content: String? = null
    var createTime: String? = null
    var location: String? = null
    var browseNumber: String? = null
    var updateTime: String? = null
    var extraInfo: String? = null

    var taskType: String? = null
    var taskStatus: String? = null
    var taskTitle: String? = null
    var taskContent: String? = null
    var taskShop: String? = null
    var taskLink: String? = null
    var taskKeyword: String? = null
    var taskNumber: Int = 0
    var taskNumbering: Int = 0
    var taskNumbered: Int = 0
    var taskPrice: String? = null
    var taskCommission: String? = null
    var taskPayUser: Int = 0

    var taskPersonList:MutableList<TaskPersonData> = mutableListOf()




}
data class TaskPersonData(val id: String?,val imageUrls:String?,val progress:Int = 0)