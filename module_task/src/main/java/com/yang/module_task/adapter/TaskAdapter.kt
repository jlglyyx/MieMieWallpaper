package com.yang.module_task.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.yang.lib_common.widget.AccountImageListView
import com.yang.module_task.data.TaskProgressData
import com.yang.module_task.R

/**
 * @ClassName: TaskAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/26 13:59
 */
class TaskAdapter(data: MutableList<TaskProgressData>?) : BaseMultiItemQuickAdapter<TaskProgressData, BaseViewHolder>(data) {


    init {
        addItemType(0,R.layout.item_task_parent)
        addItemType(1,R.layout.item_task_seller)
        addItemType(2,R.layout.item_task_buyer)
    }

    override fun convert(helper: BaseViewHolder, item: TaskProgressData) {

        when(item.itemType){
            0 ->{
                helper.itemView.setOnClickListener {
                    val pos = helper.absoluteAdapterPosition
                    if (item.isExpanded){
                        collapse(pos)
                    }else{
                        expand(pos)
                    }
                }
            }
            1 ->{
                val sivImg = helper.getView<ShapeableImageView>(R.id.siv_head)
                Glide.with(sivImg)
                    .load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F99689cbe5898812b3b1340545c08847a430d047f.jpg&refer=http%3A%2F%2Fi0.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661329266&t=3bd482aedc9184e4a91f252914b2291f")
                    .error(com.yang.lib_common.R.drawable.iv_image_error)
                    .placeholder(com.yang.lib_common.R.drawable.iv_image_placeholder)
                    .into(sivImg)
                if (!item.progressList.isNullOrEmpty()){
                    initRecyclerView(helper,item)
                }
            }
            else ->{
                if (!item.progressList.isNullOrEmpty()){
                    initRecyclerView(helper,item)
                }
            }

        }
    }

    private fun initRecyclerView(helper: BaseViewHolder,item: TaskProgressData){
        val taskProgressAdapter = TaskProgressAdapter(item.progressList)
        val recyclerView = helper.getView<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false)
        recyclerView.adapter = taskProgressAdapter
        recyclerView.setOnTouchListener { v, event ->
            helper.itemView.onTouchEvent(event)
            return@setOnTouchListener true
        }
    }


}