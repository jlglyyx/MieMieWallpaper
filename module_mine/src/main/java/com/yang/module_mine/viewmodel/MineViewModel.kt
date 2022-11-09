package com.yang.module_mine.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.*
import com.yang.module_mine.repository.MineRepository
import javax.inject.Inject

/**
 * @ClassName: MineViewModel
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:13
 */
class MineViewModel @Inject constructor(
    application: Application,
    private val mineRepository: MineRepository
) : BaseViewModel(application) {

    val userInfoData = MutableLiveData<UserInfoData>()

    var keyword = ""

    var pageNum = 1

    val mWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    val mCollectionWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    val mDownWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    fun getA() {
        launch({
            mineRepository.getA()
        }, {
            showShort(it)
        })
    }


    fun getUserInfo(id: String) {
        if (TextUtils.isEmpty(id)) {
            cancelRefresh()
            return
        }
        launch({
            mineRepository.getUserInfo(id)
        }, {
            val userInfoData1 = UserInfoData(
                "0",
                "sahk",
                null,
                0,
                10,
                null,
                null,
                "0",
                "",
                "",
                0,
                10,
                20,
                "",
                "",
                "",
                "",
                "",
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F99689cbe5898812b3b1340545c08847a430d047f.jpg&refer=http%3A%2F%2Fi0.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661329266&t=3bd482aedc9184e4a91f252914b2291f",
                0,
                0,
                true,
                0,
                "",
                10,
                true,
                "",
                "",
                "",
                ""
            )
            userInfoData.postValue(userInfoData1)
        }, {
            val userInfoData1 = UserInfoData(
                "0",
                "sahk",
                "游客———",
                0,
                10,
                null,
                "你以为你是她的唯一，你以为...",
                "133*****124",
                "",
                "",
                0,
                10,
                20,
                "",
                "",
                "",
                "",
                "",
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F99689cbe5898812b3b1340545c08847a430d047f.jpg&refer=http%3A%2F%2Fi0.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661329266&t=3bd482aedc9184e4a91f252914b2291f",
                0,
                0,
                true,
                0,
                "",
                10,
                true,
                "",
                "",
                "",
                ""
            )
            userInfoData.postValue(userInfoData1)
            cancelRefresh()
        }, errorDialog = false)
    }

    fun loginOut() {
        launch({
            mineRepository.loginOut()
        }, {
            clearAllMMKV()
            LiveDataBus.instance.with(AppConstant.Constant.LOGIN_STATUS)
                .postValue(AppConstant.Constant.LOGIN_FAIL)
            finishActivity()
        }, {
            clearAllMMKV()
            LiveDataBus.instance.with(AppConstant.Constant.LOGIN_STATUS)
                .postValue(AppConstant.Constant.LOGIN_FAIL)
            finishActivity()
        }, messages = arrayOf("请求中", "退出登陆成功"))
    }





    /**
     * 获取收藏壁纸
     */
    fun getWallpaper() {
        launch({
            params[AppConstant.Constant.USER_ID] = UserInfoHold.userId
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = 3
            mineRepository.getWallpaper(params)
        }, {
            mCollectionWallpaperData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }

    fun getDownWallpaper() {
        val filePath = getFilePath(getSaveAlbumPath(null, getApplication()))
        val data = filePath.map {
            val fromJson = "{}".fromJson<WallpaperData>()
            fromJson.imageUrl = it
            fromJson
        } as MutableList<WallpaperData>
        mDownWallpaperData.postValue(data)
    }
}