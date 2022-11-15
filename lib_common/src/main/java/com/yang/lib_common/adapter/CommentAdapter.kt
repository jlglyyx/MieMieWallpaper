package com.yang.lib_common.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.CommentData
import com.yang.lib_common.util.loadCircle

/**
 * @Author Administrator
 * @ClassName CommentAdapter
 * @Description
 * @Date 2021/11/25 11:12
 */
class CommentAdapter(data: MutableList<CommentData>?) :
    BaseMultiItemQuickAdapter<CommentData, BaseViewHolder>(data) {
    private var childSize = 0
    init {
        addItemType(AppConstant.Constant.PARENT_COMMENT_TYPE, R.layout.item_comment)
        addItemType(AppConstant.Constant.CHILD_COMMENT_TYPE, R.layout.item_child_comment)
        addItemType(
            AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE,
            R.layout.item_child_reply_comment
        )
    }

    override fun convert(helper: BaseViewHolder, item: CommentData) {

        helper.setText(R.id.tv_comment, item.comment)
        val sivImg = helper.getView<ImageView>(R.id.siv_img)
        sivImg.loadCircle(mContext,"https://img1.baidu.com/it/u=1834859148,419625166&fm=26&fmt=auto&gp=0.jpg",R.drawable.iv_attr,R.drawable.iv_attr)



        when (item.mLevel) {
            AppConstant.Constant.PARENT_COMMENT_TYPE -> {
                helper.setGone(R.id.tv_open_comment, !item.isExpanded)
                helper.setGone(
                    R.id.tv_open_comment,
                    !(null == item.subItems || item.subItems.size == 0 || item.isExpanded)
                )
                childSize = 0
                getSubItemsSize(item)
                helper.setText(R.id.tv_open_comment, "-----展开${childSize}条评论-----")
                helper.itemView.setOnClickListener {
                    if (item.isExpanded) {
                        collapse(helper.layoutPosition,false,true)
                    } else {
                        expand(helper.layoutPosition,false,true)
                    }
                }
            }
            AppConstant.Constant.CHILD_COMMENT_TYPE -> {
                when (item.mItemType) {
                    AppConstant.Constant.CHILD_COMMENT_TYPE -> {

                    }
                    AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE -> {
                        helper.addOnClickListener(R.id.siv_reply_img)
                        val sivReplyImg = helper.getView<ImageView>(R.id.siv_reply_img)
                        sivReplyImg.loadCircle(mContext,"https://img1.baidu.com/it/u=1834859148,419625166&fm=26&fmt=auto&gp=0.jpg",R.drawable.iv_attr,R.drawable.iv_attr)
                    }
                }
            }
        }

        helper.addOnClickListener(R.id.siv_img)
            .addOnClickListener(R.id.tv_reply)
    }

    private fun getSubItemsSize(item: CommentData){
        if (item.hasSubItem()){
            childSize += item.subItems.size
            item.subItems.forEach {
                if (it.hasSubItem()){
                    getSubItemsSize(it)
                }
            }
        }
    }
}