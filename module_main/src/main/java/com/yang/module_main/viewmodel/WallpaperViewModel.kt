package com.yang.module_main.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.LoginData
import com.yang.lib_common.data.MediaInfoBean
import com.yang.lib_common.util.getDefaultMMKV
import com.yang.lib_common.util.showShort
import com.yang.lib_common.util.toJson
import com.yang.module_main.R
import com.yang.module_main.data.WallpaperData
import com.yang.module_main.data.WallpaperTabData
import com.yang.module_main.repository.MainRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLEncoder
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

    var pageNum = 1

    val mWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    fun getWallpaper() {
        launch({
            params["tabId"] = "1"
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
}


