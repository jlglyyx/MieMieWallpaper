package com.yang.module_mine.api

import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.remote.di.response.MListResult
import com.yang.lib_common.remote.di.response.MResult
import com.yang.lib_common.room.entity.UserInfoData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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
}