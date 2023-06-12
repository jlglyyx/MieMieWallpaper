package com.yang.module_mine.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bytedance.sdk.openadsdk.*
import com.czhj.sdk.common.utils.Md5Util

import com.umeng.analytics.MobclickAgent
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.*
import com.yang.lib_common.R
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.data.FansData
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.data.UserInfoHold.userId
import com.yang.module_mine.data.VipPackageData
import com.yang.module_mine.data.WalletDetailData
import com.yang.module_mine.data.WeChatInfoData
import com.yang.module_mine.repository.MineRepository
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder
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

    val mUserInfoData = MutableLiveData<UserInfoData>()

    var keyword = ""

    var pageNum = 1

    var payType:Int? = null

    var order = 0

    val mWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    val mCollectionWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    val mDownWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

//    var mUserInfoData = MutableLiveData<UserInfoData>()

    var pictureListLiveData = MutableLiveData<MutableList<String>>()


    var mTTRewardMineTaskAd = MutableLiveData<TTRewardVideoAd>()


    var mWeChatInfoData = MutableLiveData<WeChatInfoData>()

    var mAlipayBody = MutableLiveData<String>()

    val mWallpaperDynamicData = MutableLiveData<MutableList<WallpaperDynamicData>>()

    val mWallpaperDynamicDetailData = MutableLiveData<WallpaperDynamicData>()

    val mFansData = MutableLiveData<MutableList<FansData>>()

    val mFansDetailData = MutableLiveData<FansData>()

    val mVipPackageListData = MutableLiveData<MutableList<VipPackageData>>()

    val mWalletDetailListData = MutableLiveData<MutableList<WalletDetailData>>()


    fun getA() {
        launch({
            mineRepository.getA()
        }, {
            showShort(it)
        })
    }


    fun getUserInfo(id: String?) {
        if (TextUtils.isEmpty(id)) {
            showShort("用户不存在")
            cancelRefresh()
            return
        }
        val params = mutableMapOf<String,Any?>()
        launch({
            params[AppConstant.Constant.ID] = id
            mineRepository.getUserInfo(params)
        }, {
            mUserInfoData.postValue(it.data)
            cancelRefresh()
        }, {
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
            MobclickAgent.onProfileSignOff()
            finishActivity()
        }, {
            clearAllMMKV()
            LiveDataBus.instance.with(AppConstant.Constant.LOGIN_STATUS)
                .postValue(AppConstant.Constant.LOGIN_FAIL)
            finishActivity()
        }, messages = arrayOf("请求中", "退出登陆成功"))
    }


    /**
     * 关键字查询 收藏查询
     */
    fun getWallpaper(userId: String, keyword: String = "") {
        val params = mutableMapOf<String,Any?>()
        launch({
            params[AppConstant.Constant.USER_ID] = userId
            params[AppConstant.Constant.KEYWORD] = keyword
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            mineRepository.getWallpaper(params)
        }, {
            mWallpaperData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
            requestFail()
        }, errorDialog = false)
    }
    /**
     * 关键字查询 收藏查询
     */
    fun queryCollectionWallpaper(userId: String?, keyword: String = "") {
        val params = mutableMapOf<String,Any?>()
        launch({
            params[AppConstant.Constant.USER_ID] = userId
            params[AppConstant.Constant.KEYWORD] = keyword
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.TYPE] = AppConstant.Constant.NUM_ZERO
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            mineRepository.queryCollectionWallpaper(params)
        }, {
            mCollectionWallpaperData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }


    fun getDownWallpaper() {
        val filePath = getFilePath(getSaveAlbumPath(getString(R.string.app_name), getApplication()))
        val data = filePath.map {
            val fromJson = "{}".fromJson<WallpaperData>()
            fromJson.wallUrl = it
            fromJson
        } as MutableList<WallpaperData>
        mDownWallpaperData.postValue(data)
    }


    /**
     * 关键字查询列表
     */
    fun getDynamicList(userId: String, keyword: String = "") {
        launch({
            if (!TextUtils.isEmpty(userId)) {
                params[AppConstant.Constant.USER_ID] = userId
            }
            if (!TextUtils.isEmpty(keyword)) {
                params[AppConstant.Constant.KEYWORD] = keyword
            }
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            mineRepository.getDynamicList(params)
        }, {
            mWallpaperDynamicData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
            requestFail()
        }, errorDialog = false)
    }

    /**
     * 查询详情
     */
    fun getDynamicDetail(id: String) {
        launch({
            params[AppConstant.Constant.ID] = id
            mineRepository.getDynamicDetail(params)
        }, {
            mWallpaperDynamicDetailData.postValue(it.data)
        }, {

        })
    }

    /**
     * 查询粉丝关注列表
     */
    fun getMineFollowList(userId: String?, type:Int) {
        if (TextUtils.isEmpty(userId)) {
            showShort("用户不存在")
        }
        launch({
            params[AppConstant.Constant.USER_ID] = userId
            params[AppConstant.Constant.TYPE] = type
            if (!TextUtils.isEmpty(keyword)) {
                params[AppConstant.Constant.KEYWORD] = keyword
            }
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            mineRepository.getMineFollowList(params)
        }, {
            mFansData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }
    /**
     * 查询粉丝关注列表
     */
    fun addFollow(userId: String?, followUserId:String?) {
        if (TextUtils.isEmpty(userId)) {
            showShort("用户不存在")
        }
        launch({
            params[AppConstant.Constant.USER_ID] = userId
            params[AppConstant.Constant.FOLLOW_USER_ID] = followUserId
            mineRepository.addFollow(params)
        }, {
            mFansDetailData.postValue(it.data)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }

    /**
     * 查询vip 套餐列表
     */
    fun getVipPackage() {
        launch({
            mineRepository.getVipPackage()
        }, {
            mVipPackageListData.postValue(it.data)
        }, {

        }, errorDialog = false)
    }




    /**
     * 查询钱包明细
     */
    fun getWalletDetail() {

        launch({
            params[AppConstant.Constant.USER_ID] = UserInfoHold.userId
            if (!TextUtils.isEmpty(keyword)) {
                params[AppConstant.Constant.KEYWORD] = keyword
            }
            params[AppConstant.Constant.PAY_TYPE] = payType
            params[AppConstant.Constant.ORDER] = order
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            mineRepository.getWalletDetail(params)
        }, {
            mWalletDetailListData.postValue(it.data.list)
        }, {
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }







    fun uploadFile(filePaths: MutableList<String>, showDialog: Boolean = true) {
        launch(
            {
                val mutableMapOf = mutableMapOf<String, RequestBody>()
                filePaths.forEach {
                    val file = File(it)
                    val requestBody = RequestBody.create(
                        MediaType.parse(AppConstant.ClientInfo.CONTENT_TYPE),
                        file
                    )
                    val encode = URLEncoder.encode(
                        "${System.currentTimeMillis()}_${file.name}",
                        AppConstant.ClientInfo.UTF_8
                    )
                    mutableMapOf["file\";filename=\"$encode"] = requestBody
                }
                mineRepository.uploadFile(mutableMapOf)
            },
            {
                pictureListLiveData.postValue(it.data)
            },
            messages = arrayOf(
                getString(R.string.string_uploading),
                getString(R.string.string_insert_success)
            ),
            showDialog = showDialog
        )
    }

    fun changePassword(password: String) {
        launch(
            {
                val userInfoData = "{}".fromJson<UserInfoData>()
                userInfoData.userPassword = Md5Util.md5(password)
                mineRepository.updateUserInfo(userInfoData)
            },
            {
                requestSuccess()
            },
            messages = arrayOf(
                getString(R.string.string_change_password_ing),
                getString(R.string.string_change_password_success),
                getString(R.string.string_change_password_fail)
            )
        )
    }

    fun updateUserInfo(userInfoData: UserInfoData, showDialog: Boolean = true) {
        launch({
            mineRepository.updateUserInfo(userInfoData)
        }, {
            mUserInfoData.postValue(it.data)
            updateLocalUserInfo(it.data)
            LiveDataBus.instance.with(AppConstant.Constant.REFRESH_USER_INFO)
                .postValue(AppConstant.Constant.REFRESH_USER_INFO)
        }, messages = arrayOf(getString(R.string.string_requesting)), showDialog = showDialog)
    }

    fun sign(id: String) {
        launch({
            mineRepository.sign(id)
        }, {
            showShort("签到成功")
            getUserInfo(id)
        })
    }

    fun openVip(payType:String,vipPackageId:String) {
        val params = mutableMapOf<String,Any?>()
        launch({
            params[AppConstant.Constant.USER_ID] = UserInfoHold.userId
            params[AppConstant.Constant.PAY_TYPE] = payType
            params[AppConstant.Constant.VIP_PACKAGE_ID] = vipPackageId
            mineRepository.openVip(params)
        }, {
            if (null == it.data){
                showShort("支付成功")
                getUserInfo(UserInfoHold.userId)
            }else{
                it.data.apply {
                    val jsonObject = JSONObject(this.toJson())
                    mAlipayBody.postValue(jsonObject.getString("body"))
                }
            }
        }, messages = arrayOf("支付中"))
    }


    fun getWeChatToken(code: String) {
        params[AppConstant.WeChatConstant.APP_ID] = AppConstant.WeChatConstant.WECHAT_PAY_ID
        params[AppConstant.WeChatConstant.SECRET] = AppConstant.WeChatConstant.SECRET_ID
        params[AppConstant.WeChatConstant.CODE] = code
        params[AppConstant.WeChatConstant.GRANT_TYPE] =
            AppConstant.WeChatConstant.AUTHORIZATION_CODE
        launch({
            mineRepository.getWeChatToken(params)
        }, {
            Log.i("TAG", "getWeChatToken: " + it.toJson())
            setMMKVValue(AppConstant.WeChatConstant.WECHAT_TOKEN, it.toString())
            mWeChatInfoData.postValue(it)
        }, messages = arrayOf(getString(R.string.string_requesting)))
    }

    fun refreshWeChatToken(refreshToken: String) {
        params[AppConstant.WeChatConstant.APP_ID] = AppConstant.WeChatConstant.WECHAT_PAY_ID
        params[AppConstant.WeChatConstant.REFRESH_TOKEN] = refreshToken
        params[AppConstant.WeChatConstant.GRANT_TYPE] = AppConstant.WeChatConstant.REFRESH_TOKEN
        launch({
            mineRepository.refreshWeChatToken(params)
        }, {
            setMMKVValue(AppConstant.WeChatConstant.WECHAT_TOKEN, it.toString())
            mWeChatInfoData.postValue(it)
        }, messages = arrayOf(getString(R.string.string_requesting)))
    }


    fun loadMineTaskAd() {
        val bigAdSlot = AdSlot.Builder()
            .setCodeId("947676680") //广告位id
            .setUserID("tag123")//tag_id
            .setMediaExtra("media_extra") //附加参数
            .setOrientation(TTAdConstant.VERTICAL)
            .setExpressViewAcceptedSize(0f, 0f) //期望模板广告view的size,单位dp
            .setAdLoadType(TTAdLoadType.PRELOAD) //推荐使用，用于标注此次的广告请求用途为预加载（当做缓存）还是实时加载，方便后续为开发者优化相关策略
            .build()
        mTTAdNative?.loadRewardVideoAd(bigAdSlot, object : TTAdNative.RewardVideoAdListener {
            override fun onError(code: Int, message: String?) {
                Log.i("TAG", "onError: $code  $message")
            }


            override fun onRewardVideoAdLoad(p0: TTRewardVideoAd?) {
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            override fun onRewardVideoCached() {

            }

            override fun onRewardVideoCached(ad: TTRewardVideoAd?) {
                mTTRewardMineTaskAd.postValue(ad)
            }

        })
    }

}