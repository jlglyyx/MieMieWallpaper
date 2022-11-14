package com.yang.module_main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @ClassName: UploadWallpaperAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/14 9:43
 */
class UploadWallpaperAdapter(layoutResId: Int, data: MutableList<String>?) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: String) {
    }
}