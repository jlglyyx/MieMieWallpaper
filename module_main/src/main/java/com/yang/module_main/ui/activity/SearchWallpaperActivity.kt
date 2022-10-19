package com.yang.module_main.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.showShort
import com.yang.lib_common.widget.CommonSearchToolBar
import com.yang.module_main.databinding.ActSearchWallpaperBinding

/**
 * @ClassName: SearchWallpaperActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/19 17:13
 */
@Route(path = AppConstant.RoutePath.SEARCH_WALLPAPER_ACTIVITY)
class SearchWallpaperActivity:BaseActivity<ActSearchWallpaperBinding>() {
    override fun initViewBinding(): ActSearchWallpaperBinding {
        return bind(ActSearchWallpaperBinding::inflate)
    }

    override fun initData() {
    }

    override fun initView() {
        mViewBinding.commonToolBar.canEdit(true)
        mViewBinding.commonToolBar.etSearch.hint = "请输入搜索内容"
        mViewBinding.commonToolBar.searchListener = object :CommonSearchToolBar.OnSearchListener{
            override fun onSearch(text: String) {
                showShort(text)
            }

        }
    }

    override fun initViewModel() {
    }
}