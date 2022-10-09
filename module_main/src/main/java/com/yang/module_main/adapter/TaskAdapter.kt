package com.yang.module_main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.lib_common.widget.AccountImageListView
import com.yang.module_main.R
import com.yang.module_main.data.TaskData

/**
 * @ClassName: TaskAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/26 13:59
 */
class TaskAdapter(data: MutableList<TaskData>?) : BaseQuickAdapter<TaskData, BaseViewHolder>(data) {

    val imageList = mutableListOf<String>("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnimg.ws.126.net%2F%3Furl%3Dhttp%253A%252F%252Fdingyue.ws.126.net%252F2021%252F0704%252F202dd245j00qvok1c00f6c000n000n0c.jpg%26thumbnail%3D650x2147483647%26quality%3D80%26type%3Djpg&refer=http%3A%2F%2Fnimg.ws.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661511623&t=9b6b572d941c9e4ead992151ef04d380"
    ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201710%2F22%2F20171022120808_VZMjz.thumb.700_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661511623&t=a0409035bc60c51ea02888a8e73e8df7",
    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202107%2F27%2F20210727210749_01031.thumb.1000_0.png&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661511623&t=5c6d401e04421aef7acaebde735d93f6",
    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202107%2F27%2F20210727210749_01031.thumb.1000_0.png&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661511623&t=5c6d401e04421aef7acaebde735d93f6",
    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202108%2F22%2F20210822213626_4dcb2.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661511623&t=32e09f7ed4b20ca35a20463a2c000f99")
    init {
        mLayoutResId = R.layout.item_task
    }

    override fun convert(helper: BaseViewHolder, item: TaskData) {

        helper.getView<AccountImageListView>(R.id.accountImageListView).pathList = imageList
    }


}