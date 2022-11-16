package com.yang.lib_common.adapter

import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.GridNinePictureView

/**
 * @ClassName: WallpaperDynamicAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/16 16:04
 */
class WallpaperDynamicAdapter: BaseQuickAdapter<WallpaperDynamicData, BaseViewHolder>(R.layout.item_dynamic) {
    override fun convert(helper: BaseViewHolder, item: WallpaperDynamicData) {
        helper.setGone(R.id.view_top, helper.bindingAdapterPosition == 0)
        val gridNinePictureView = helper.getView<GridNinePictureView>(R.id.gridNinePictureView)
        gridNinePictureView.data = item.wallList.map {
            getRealUrl(it.imageUrl).toString()
        } as MutableList<String>
        gridNinePictureView.imageCallback = object : GridNinePictureView.ImageCallback{
            override fun imageClickListener(position: Int) {
                buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
                    .withOptionsCompat(
                        ActivityOptionsCompat.makeCustomAnimation(
                            mContext,
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                    )
                    .withString(AppConstant.Constant.DATA, item.wallList.toJson())
                    .withInt(AppConstant.Constant.INDEX, position)
                    .withBoolean(AppConstant.Constant.TO_LOAD, false)
                    .navigation()
            }

        }

        val imageView = helper.getView<ImageView>(R.id.iv_image)
        imageView.loadCircle(mContext, getRealUrl(item.userImage).toString())
        helper.setText(R.id.tv_name,item.userName)
            .setText(R.id.tv_attention,if (item.isAttention) "取消关注" else "+关注")
            .setText(R.id.tv_text,item.dynamicContent)
            .setText(R.id.tv_fabulous_num, formatNumUnit(item.dynamicLikeNum))
            .setText(R.id.tv_comment_num, formatNumUnit(item.dynamicCommentNum))
            .setText(R.id.tv_forward_num, formatNumUnit(item.dynamicForwardNum))
            .addOnClickListener(R.id.iv_image)
            .addOnClickListener(R.id.tv_attention)
            .addOnClickListener(R.id.tv_content)
            .addOnClickListener(R.id.ll_fabulous)
            .addOnClickListener(R.id.ll_forward)
    }
}