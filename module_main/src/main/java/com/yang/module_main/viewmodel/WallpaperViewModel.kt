package com.yang.module_main.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.yang.lib_common.R
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.FansData
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.showShort
import com.yang.module_main.data.SearchFindData
import com.yang.module_main.repository.MainRepository
import okhttp3.MediaType
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

    var wallType = 0

    var pageNum = 1

    var order = 0

    var currentPosition = 0

    val mWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    val mWallpaperDetailData = MutableLiveData<WallpaperData>()

    val mSearchUserInfoData = MutableLiveData<MutableList<UserInfoData>>()

    val mSearchFindData = MutableLiveData<MutableList<SearchFindData>>()

    var pictureListLiveData = MutableLiveData<MutableList<String>>()

    val mFansDetailData = MutableLiveData<FansData>()



    /**
     * 关键字查询 收藏查询 tab查询
     */
    fun getWallpaper(tabId: String?, keyword: String = "", userId: String = "") {
        launch({
            if (!TextUtils.isEmpty(userId)) {
                params[AppConstant.Constant.USER_ID] = userId
            } else {
                params[AppConstant.Constant.ORDER] = order
                params[AppConstant.Constant.TAB_ID] = tabId
            }
            params[AppConstant.Constant.KEYWORD] = keyword
            params[AppConstant.Constant.WALL_TYPE] = wallType
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.CURRENT_USER_ID] = UserInfoHold.userId
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            mainRepository.getWallpaper(params)
        }, {
            mWallpaperData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }


    /**
     * 查询单个壁纸详情
     */
    fun queryWallpaperDetail(id: String?) {
        launch({
            params[AppConstant.Constant.USER_ID] = UserInfoHold.userId
            params[AppConstant.Constant.ID] = id
            params[AppConstant.Constant.CURRENT_USER_ID] = UserInfoHold.userId
            mainRepository.queryWallpaperDetail(params)
        }, {
            mWallpaperDetailData.postValue(it.data)
        }, errorDialog = false, showDialog = false)
    }


    /**
     * 关键字搜索壁纸
     */
    fun getWallpaper() {
        launch({
            params[AppConstant.Constant.WALL_TYPE] = wallType
            params[AppConstant.Constant.KEYWORD] = keyword
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            params[AppConstant.Constant.CURRENT_USER_ID] = UserInfoHold.userId
            mainRepository.getWallpaper(params)
        }, {
            mWallpaperData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }






    /**
     * 关键字搜索用户
     */
    fun searchUser() {
        launch({
            params[AppConstant.Constant.KEYWORD] = keyword
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            mainRepository.searchUser(params)
        }, {
            mSearchUserInfoData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }

    fun getSearchFind() {
        launch({
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


    fun uploadFile(filePaths: MutableList<String>) {
        launch({
            val mutableMapOf = mutableMapOf<String, RequestBody>()
            filePaths.forEach {
                val file = File(it)
                val requestBody = RequestBody.create(MediaType.parse(AppConstant.ClientInfo.CONTENT_TYPE), file)
                val encode = URLEncoder.encode("${System.currentTimeMillis()}_${file.name}", AppConstant.ClientInfo.UTF_8)
                mutableMapOf["file\";filename=\"$encode"] = requestBody
            }
            mainRepository.uploadFile(mutableMapOf)
        }, {
            pictureListLiveData.postValue(it.data)
        }, messages = arrayOf(getString(R.string.string_add_wallpaper_loading), getString(R.string.string_add_wallpaper_success)))
    }
    fun publishDynamic(filePaths: MutableList<String>,userId:String,content:String = "",sendLocation:String = "") {
        launch({
            val mutableMapOf = mutableMapOf<String, RequestBody>()
            filePaths.forEach {
                val file = File(it)
                val requestBody = RequestBody.create(MediaType.parse(AppConstant.ClientInfo.CONTENT_TYPE), file)
                val encode = URLEncoder.encode("${System.currentTimeMillis()}_${file.name}", AppConstant.ClientInfo.UTF_8)
                mutableMapOf["file\";filename=\"$encode"] = requestBody
            }
            mainRepository.publishDynamic(mutableMapOf,userId,content,sendLocation)
        }, {
            pictureListLiveData.postValue(it.data)
        }, messages = arrayOf(getString(R.string.string_uploading), getString(R.string.string_insert_success)))
    }

    /**
     * 查询粉丝关注列表
     */
    fun addFollow(userId: String?, followUserId:String?,wallpaperId:String?,isFollow:Boolean) {
        if (TextUtils.isEmpty(userId)) {
            showShort("用户不存在")
        }
        launch({
            params[AppConstant.Constant.USER_ID] = userId
            params[AppConstant.Constant.FOLLOW_USER_ID] = followUserId
            params[AppConstant.Constant.IS_FOLLOW] = isFollow
            mainRepository.addFollow(params)
        }, {
            mFansDetailData.postValue(it.data)
            queryWallpaperDetail(wallpaperId)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }

    /**
     * 查询粉丝关注列表
     */
    fun addOrCancelCollect(collectionId:String?,collectionUserId: String?,isDeleteFlag:Boolean, collectionType:Int = 0) {
        launch({
            params[AppConstant.Constant.USER_ID] = UserInfoHold.userId
            params[AppConstant.Constant.COLLECTION_ID] = collectionId
            params[AppConstant.Constant.COLLECTION_USER_ID] = collectionUserId
            params[AppConstant.Constant.COLLECTION_TYPE] = collectionType
            params[AppConstant.Constant.IS_DELETE_FLAG] = isDeleteFlag
            mainRepository.addOrCancelCollect(params)
        }, {
            mWallpaperDetailData.postValue(it.data)
        }, showDialog = false)
    }

}


