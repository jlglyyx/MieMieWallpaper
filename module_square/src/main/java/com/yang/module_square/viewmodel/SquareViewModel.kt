package com.yang.module_square.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.huawei.hms.ads.id
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.*
import com.yang.lib_common.data.UserInfoHold.userId
import com.yang.lib_common.util.*
import com.yang.module_square.repository.SquareRepository
import javax.inject.Inject

/**
 * @ClassName: MineViewModel
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:13
 */
class SquareViewModel @Inject constructor(
    application: Application,
    private val squareRepository: SquareRepository
) : BaseViewModel(application) {

    var pageNum = 1

    var wallType = 0

    var keyword = ""

    val mWallpaperDynamicData = MutableLiveData<MutableList<WallpaperDynamicData>>()

    val mWallpaperDynamicDetailData = MutableLiveData<WallpaperDynamicData>()

    val mWallpaperDynamicCommentData = MutableLiveData<MutableList<WallpaperDynamicCommentData>>()

    val mWallpaperDynamicCommentDetailData = MutableLiveData<WallpaperDynamicCommentData>()




    /**
     * 关键字查询动态
     */
    fun getDynamicList(userId: String = "") {
        launch({
            if (!TextUtils.isEmpty(userId)) {
                params[AppConstant.Constant.USER_ID] = userId
            }
            if (!TextUtils.isEmpty(keyword)) {
                params[AppConstant.Constant.KEYWORD] = keyword
            }
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            squareRepository.getDynamicList(params)
        }, {
            mWallpaperDynamicData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }
    /**
     * 查询详情
     */
    fun getDynamicDetail(id: String) {
        val params = mutableMapOf<String,Any?>()
        launch({
            params[AppConstant.Constant.ID] = id
            squareRepository.getDynamicDetail(params)
        }, {
            mWallpaperDynamicDetailData.postValue(it.data)
        }, {

        })
    }
    /**
     * 查询详情评论
     */
    fun getDynamicCommentList(id: String) {
        val params = mutableMapOf<String,Any?>()
        launch({
            params[AppConstant.Constant.RESOURCE_ID] = id
            squareRepository.getDynamicCommentList(params)
        }, {
            mWallpaperDynamicCommentData.postValue(it.data)
        }, {

        })
    }
    /**
     * 查询详情评论
     */
    fun insertDynamicComment(userId: String?,parentId: String?,resourceId:String,replyUserId:String?,content:String) {
        launch({
            params[AppConstant.Constant.USER_ID] = userId
            params[AppConstant.Constant.PARENT_ID] = parentId
            params[AppConstant.Constant.RESOURCE_ID] = resourceId
            params[AppConstant.Constant.REPLY_USER_ID] = replyUserId
            params[AppConstant.Constant.CONTENT] = content
            squareRepository.insertDynamicComment(params)
        }, {
            mWallpaperDynamicCommentDetailData.postValue(it.data)
        }, {

        })
    }


}