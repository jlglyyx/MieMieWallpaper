package com.yang.module_mine.api

import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.remote.di.response.MListResult
import com.yang.lib_common.remote.di.response.MResult
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.data.FansData
import com.yang.module_mine.data.VipPackageData
import com.yang.module_mine.data.WalletDetailData
import com.yang.module_mine.data.WeChatInfoData
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @ClassName: MineApi
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:16
 */
interface MineApi {

    @GET("/")
    suspend fun getA():String

    @POST("api/user/getUserInfo")
    suspend fun getUserInfo(@Body params:MutableMap<String,Any?>): MResult<UserInfoData>

    @POST("api/user/loginOut")
    suspend fun loginOut(): MResult<String>

    @POST("api/wallpaper/queryWallpaper")
    suspend fun getWallpaper(@Body params:MutableMap<String,Any?>):MResult<MListResult<WallpaperData>>

    @POST("api/wallpaper/queryCollectionWallpaper")
    suspend fun queryCollectionWallpaper(@Body params:MutableMap<String,Any?>):MResult<MListResult<WallpaperData>>



    @Multipart
    @POST("api/file/uploadFile")
    suspend fun uploadFile(@PartMap file: MutableMap<String, RequestBody>): MResult<MutableList<String>>



    @POST("api/user/updateUserInfo")
    suspend fun updateUserInfo(@Body userInfoData: UserInfoData): MResult<UserInfoData>

    @POST("api/user/sign")
    suspend fun sign(@Body id: String): MResult<UserInfoData>


    @POST("api/pay/openVip")
    suspend fun openVip(@Body params:MutableMap<String,Any?>): MResult<Any>


    @POST
    suspend fun getWeChatToken(@QueryMap params:MutableMap<String,Any?>,@Url url:String = "https://api.weixin.qq.com/sns/oauth2/access_token"): WeChatInfoData

    @POST
    suspend fun refreshWeChatToken(@QueryMap params:MutableMap<String,Any?>,@Url url:String = "https://api.weixin.qq.com/sns/oauth2/refresh_token"): WeChatInfoData



    @POST("api/dynamic/getDynamicList")
    suspend fun getDynamicList(@Body params:MutableMap<String,Any?>):MResult<MListResult<WallpaperDynamicData>>



    @POST("api/dynamic/getDynamicDetail")
    suspend fun getDynamicDetail(@Body params:MutableMap<String,Any?>):MResult<WallpaperDynamicData>

    @POST("api/follow/getMineFollowList")
    suspend fun getMineFollowList(@Body params:MutableMap<String,Any?>):MResult<MListResult<FansData>>

    @POST("api/follow/addFollow")
    suspend fun addFollow(@Body params:MutableMap<String,Any?>):MResult<FansData>



    @POST("api/pay/getVipPackage")
    suspend fun getVipPackage():MResult<MutableList<VipPackageData>>

    @POST("api/pay/getWalletDetail")
    suspend fun getWalletDetail(@Body params:MutableMap<String,Any?>):MResult<MListResult<WalletDetailData>>




}