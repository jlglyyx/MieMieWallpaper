package com.yang.module_square.api

import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.*
import com.yang.lib_common.remote.di.response.MListResult
import com.yang.lib_common.remote.di.response.MResult
import com.yang.lib_common.room.entity.UserInfoData
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @ClassName: MineApi
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:16
 */
interface SquareApi {

    @GET("/")
    suspend fun getA():String

    @POST("user/login")
    suspend fun login(@Body params:MutableMap<String,Any>):MResult<UserInfoData>

    @POST("api/user/query/userInfo")
    suspend fun getUserInfo(@Query(AppConstant.Constant.ID) id:String):MResult<UserInfoData>

    @POST("user/register")
    suspend fun register(@Query(AppConstant.Constant.ID) id:String):MResult<UserInfoData>

//    @POST("api/user/insert/task")
//    suspend fun insertTask(@Body taskData: TaskData): MResult<String>
//
//    @POST("api/user/query/task")
//    suspend fun getTaskList(@Body params:MutableMap<String,Any>):MResult<MutableList<TaskData>>

    @Multipart
    @POST("/uploadFile")
    suspend fun uploadFile(@PartMap file: MutableMap<String, RequestBody>): MResult<MutableList<String>>

    @Multipart
    @POST("/uploadFile")
    suspend fun uploadFileAndParam(@Body file: MutableList<RequestBody>): MResult<MutableList<String>>


    @GET("main/queryWallType")
    suspend fun getWallType():MResult<MutableList<WallpaperTypeData>>

    @GET("main/queryTabs")
    suspend fun getTabs(@Query(AppConstant.Constant.WALL_TYPE) wallType:Int):MResult<MutableList<WallpaperTabData>>

    @POST("main/queryAllByTab")
    suspend fun getWallpaper(@Body params:MutableMap<String,Any?>):MResult<MListResult<WallpaperData>>



    @POST("api/dynamic/getDynamicList")
    suspend fun getDynamicList(@Body params:MutableMap<String,Any?>):MResult<MListResult<WallpaperDynamicData>>

    @POST("api/dynamic/getDynamicDetail")
    suspend fun getDynamicDetail(@Body params:MutableMap<String,Any?>):MResult<WallpaperDynamicData>

    @POST("api/dynamic/getDynamicCommentList")
    suspend fun getDynamicCommentList(@Body params:MutableMap<String,Any?>):MResult<MutableList<WallpaperDynamicCommentData>>

    @POST("api/dynamic/insertDynamicComment")
    suspend fun insertDynamicComment(@Body params:MutableMap<String,Any?>):MResult<WallpaperDynamicCommentData>



}