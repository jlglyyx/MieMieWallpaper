package com.yang.module_mine.api

import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.remote.di.response.MListResult
import com.yang.lib_common.remote.di.response.MResult
import com.yang.lib_common.room.entity.MineGoodsDetailData
import com.yang.lib_common.room.entity.UserInfoData
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

    @POST("api/user/query/userInfo")
    suspend fun getUserInfo(@Query(AppConstant.Constant.ID) id:String): MResult<UserInfoData>

    @POST("api/user/loginOut")
    suspend fun loginOut(): MResult<String>

    @POST("main/queryAllByTab")
    suspend fun getWallpaper(@Body params:MutableMap<String,Any?>):MResult<MListResult<WallpaperData>>



    @Multipart
    @POST("/uploadFile")
    suspend fun uploadFile(@PartMap file: MutableMap<String, RequestBody>): MResult<MutableList<String>>

    @POST("user/changePassword")
    suspend fun changePassword(@Query("password") password:String): MResult<String>

    @POST("user/changeUserInfo")
    suspend fun changeUserInfo(@Body userInfoData: UserInfoData): MResult<UserInfoData>


    @POST
    suspend fun getWeChatToken(@QueryMap params:MutableMap<String,Any?>,@Url url:String = "https://api.weixin.qq.com/sns/oauth2/access_token"): WeChatInfoData

    @POST
    suspend fun refreshWeChatToken(@QueryMap params:MutableMap<String,Any?>,@Url url:String = "https://api.weixin.qq.com/sns/oauth2/refresh_token"): WeChatInfoData







}