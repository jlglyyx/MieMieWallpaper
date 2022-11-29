package com.yang.module_mine.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bytedance.sdk.openadsdk.*
import com.czhj.sdk.common.utils.Md5Util

import com.huawei.hms.ads.id
import com.umeng.analytics.MobclickAgent
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.*
import com.yang.lib_common.R
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

    val mWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    val mCollectionWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    val mDownWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

//    var mUserInfoData = MutableLiveData<UserInfoData>()

    var pictureListLiveData = MutableLiveData<MutableList<String>>()


    var mTTRewardMineTaskAd = MutableLiveData<TTRewardVideoAd>()


    var mWeChatInfoData = MutableLiveData<WeChatInfoData>()

    var body = MutableLiveData<String>()


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
    fun getWallpaper(userId: String,keyword: String = "") {
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
        }, errorDialog = false)
    }


    fun getDownWallpaper() {
        val filePath = getFilePath(getSaveAlbumPath(null, getApplication()))
        val data = filePath.map {
            val fromJson = "{}".fromJson<WallpaperData>()
            fromJson.wallUrl = it
            fromJson
        } as MutableList<WallpaperData>
        mDownWallpaperData.postValue(data)
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
            mineRepository.uploadFile(mutableMapOf)
        }, {
            pictureListLiveData.postValue(it.data)
        }, messages = arrayOf(getString(R.string.string_uploading), getString(R.string.string_insert_success)))
    }

    fun changePassword(password: String) {
        launch({
            val userInfoData = "{}".fromJson<UserInfoData>()
            userInfoData.userPassword = Md5Util.md5(password)
            mineRepository.updateUserInfo(userInfoData)
        }, {
            requestSuccess()
        }, messages = arrayOf(getString(R.string.string_change_password_ing), getString(R.string.string_change_password_success), getString(R.string.string_change_password_fail)))
    }

    fun updateUserInfo(userInfoData: UserInfoData) {
        launch({
            mineRepository.updateUserInfo(userInfoData)
        }, {
            mUserInfoData.postValue(it.data)
        }, messages = arrayOf(getString(R.string.string_requesting)))
    }
    fun sign(id: String) {
        launch({
            mineRepository.sign(id)
        }, {
            showShort("签到成功")
            getUserInfo(id)
        })
    }

    fun alipay() {
        launch({
            mineRepository.alipay()
        }, {
            val jsonObject = JSONObject(it.data.toJson())
            body.postValue(jsonObject.getString("body"))
            showShort("签到成功")
        })
    }





    fun getWeChatToken(code:String) {
        params[AppConstant.WeChatConstant.APP_ID] = AppConstant.WeChatConstant.WECHAT_PAY_ID
        params[AppConstant.WeChatConstant.SECRET] = AppConstant.WeChatConstant.SECRET_ID
        params[AppConstant.WeChatConstant.CODE] = code
        params[AppConstant.WeChatConstant.GRANT_TYPE] = AppConstant.WeChatConstant.AUTHORIZATION_CODE
        launch({
            mineRepository.getWeChatToken(params)
        }, {
            Log.i("TAG", "getWeChatToken: "+it.toJson())
            setMMKVValue(AppConstant.WeChatConstant.WECHAT_TOKEN,it.toString())
            mWeChatInfoData.postValue(it)
        }, messages = arrayOf(getString(R.string.string_requesting)))
    }

    fun refreshWeChatToken(refreshToken:String) {
        params[AppConstant.WeChatConstant.APP_ID] = AppConstant.WeChatConstant.WECHAT_PAY_ID
        params[AppConstant.WeChatConstant.REFRESH_TOKEN] = refreshToken
        params[AppConstant.WeChatConstant.GRANT_TYPE] = AppConstant.WeChatConstant.REFRESH_TOKEN
        launch({
            mineRepository.refreshWeChatToken(params)
        }, {
            setMMKVValue(AppConstant.WeChatConstant.WECHAT_TOKEN,it.toString())
            mWeChatInfoData.postValue(it)
        }, messages = arrayOf(getString(R.string.string_requesting)))
    }










    fun loadMineTaskAd(){
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