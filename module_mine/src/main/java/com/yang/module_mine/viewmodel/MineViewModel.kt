package com.yang.module_mine.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bytedance.sdk.openadsdk.*
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.room.entity.MineGoodsDetailData
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.*
import com.yang.lib_common.R
import com.yang.module_mine.repository.MineRepository
import okhttp3.MediaType
import okhttp3.RequestBody
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
                true,
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
                true,
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
            requestFail()
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






















    var mUserInfoData = MutableLiveData<UserInfoData>()

    var pictureListLiveData = MutableLiveData<MutableList<String>>()


    var mTTRewardMineTaskAd = MutableLiveData<TTRewardVideoAd>()




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
            mineRepository.changePassword(password)
        }, {
            requestSuccess()
        }, messages = arrayOf(getString(R.string.string_change_password_ing), getString(R.string.string_change_password_success), getString(R.string.string_change_password_fail)))
    }

    fun changeUserInfo(userInfoData: UserInfoData) {
        launch({
            mineRepository.changeUserInfo(userInfoData)
        }, {
            mUserInfoData.postValue(it.data)
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