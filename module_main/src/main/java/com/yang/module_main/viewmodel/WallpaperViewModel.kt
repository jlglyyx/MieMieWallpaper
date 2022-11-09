package com.yang.module_main.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperData
import com.yang.module_main.data.SearchFindData
import com.yang.module_main.repository.MainRepository
import javax.inject.Inject

/**
 * @ClassName: WallpaperViewModel
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:13
 */
class WallpaperViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : BaseViewModel(application) {

    var keyword = ""

    var wallType = 0

    var pageNum = 1

    val mWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    val mSearchFindData = MutableLiveData<MutableList<SearchFindData>>()

    /**
     * 关键字搜索壁纸
     */
    fun getWallpaper() {
        launch({
            params[AppConstant.Constant.WALL_TYPE] = wallType
            params[AppConstant.Constant.KEYWORD] = keyword
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            mainRepository.getWallpaper(params)
        }, {
            mWallpaperData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }

    fun getSearchFind() {
        launch({
            params[AppConstant.Constant.KEYWORD] = keyword
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            mainRepository.getSearchFind(params)
        }, {
            mSearchFindData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }
}


