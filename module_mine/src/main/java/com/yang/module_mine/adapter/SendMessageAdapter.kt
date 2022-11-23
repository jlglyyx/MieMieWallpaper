package com.yang.module_mine.adapter

import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.GridNinePictureView
import io.rong.imlib.model.Message
import io.rong.message.TextMessage

/**
 * @ClassName: SendMessageAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/16 16:04
 */
class SendMessageAdapter : BaseQuickAdapter<Message, BaseViewHolder>(R.layout.item_send_message) {
    override fun convert(helper: BaseViewHolder, item: Message) {

        Log.i("sssssssssssssss", "convert: ${item.senderUserId}")
        val isMine = TextUtils.equals(item.senderUserId, "111111")
        helper.setGone(R.id.cl_message, !isMine)
            .setGone(R.id.cl_message_right, isMine)
            .setGone(R.id.tv_time,item.receivedTime != 0L)
            .setText(R.id.tv_time, formatDate_YYYY_MMM_DD_HHMMSS.format(item.receivedTime))
        when (item.content) {
            is TextMessage -> {
                val content = (item.content as TextMessage).content
                helper.setText(R.id.tv_message, content)
                helper.setText(R.id.tv_right_message, content)
            }
        }
//        helper.setGone(R.id.tv_time, helper.bindingAdapterPosition == 0)
//        val gridNinePictureView = helper.getView<GridNinePictureView>(R.id.gridNinePictureView)
//        gridNinePictureView.data = item.wallList.map {
//            getRealUrl(it.imageUrl).toString()
//        } as MutableList<String>
//        gridNinePictureView.imageCallback = object : GridNinePictureView.ImageCallback{
//            override fun imageClickListener(position: Int) {
//                buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
//                    .withOptionsCompat(
//                        ActivityOptionsCompat.makeCustomAnimation(
//                            mContext,
//                            R.anim.fade_in,
//                            R.anim.fade_out
//                        )
//                    )
//                    .withString(AppConstant.Constant.DATA, item.wallList.toJson())
//                    .withInt(AppConstant.Constant.INDEX, position)
//                    .withBoolean(AppConstant.Constant.TO_LOAD, false)
//                    .navigation()
//            }
//
//        }
//
//        val imageView = helper.getView<ImageView>(R.id.iv_image)
//        imageView.loadCircle(mContext, getRealUrl(item.userImage).toString())
//        helper.setText(R.id.tv_name,item.userName)
//            .setText(R.id.tv_attention,if (item.isAttention) "取消关注" else "+关注")
//            .setText(R.id.tv_text,item.dynamicContent)
//            .setText(R.id.tv_fabulous_num, formatNumUnit(item.dynamicLikeNum))
//            .setText(R.id.tv_comment_num, formatNumUnit(item.dynamicCommentNum))
//            .setText(R.id.tv_forward_num, formatNumUnit(item.dynamicForwardNum))
//            .addOnClickListener(R.id.iv_image)
//            .addOnClickListener(R.id.tv_attention)
//            .addOnClickListener(R.id.tv_content)
//            .addOnClickListener(R.id.ll_fabulous)
//            .addOnClickListener(R.id.ll_forward)
    }
}