package com.yang.lib_common.adapter

import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lxj.xpopup.XPopup
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.dialog.ImageViewPagerDialog
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

        item.resourceUrls?.fromJson<MutableList<String>>()?.let {
            gridNinePictureView.data = it
        }

        gridNinePictureView.imageCallback = object : GridNinePictureView.ImageCallback{
            override fun imageClickListener(position: Int) {
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
                XPopup.Builder(mContext).asCustom(ImageViewPagerDialog(mContext,item.resourceUrls?.fromJson<MutableList<String>>()!!.map {
                        getRealUrl(it)
                    } as MutableList<String>,position)).show()
            }

        }

        val imageView = helper.getView<ImageView>(R.id.iv_image)
        imageView.loadCircle(mContext, getRealUrl(item.userAttr).toString())
        helper.setText(R.id.tv_name,item.userName)
            .setText(R.id.tv_attention,if (item.isAttention) "取消关注" else "+关注")
            .setText(R.id.tv_text,item.content)
            .setText(R.id.tv_time,item.createTime)
            .setText(R.id.tv_fabulous_num, if (item.likeNum == null) "0" else item.likeNum?.formatNumUnit())
            .setText(R.id.tv_comment_num, if (item.commentNum == null) "0" else item.commentNum?.formatNumUnit())
            .setText(R.id.tv_forward_num, if (item.forwardNum == null) "0" else item.forwardNum?.formatNumUnit())
            .setText(R.id.tv_degree, "浏览"+if (item.seeNum == null) "0" else item.seeNum?.formatNumUnit()+"次")
            .setText(R.id.tv_location, item.sendLocation)
            .addOnClickListener(R.id.tv_name)
            .addOnClickListener(R.id.iv_image)
            .addOnClickListener(R.id.tv_attention)
            .addOnClickListener(R.id.tv_content)
            .addOnClickListener(R.id.ll_fabulous)
            .addOnClickListener(R.id.ll_forward)
    }
}