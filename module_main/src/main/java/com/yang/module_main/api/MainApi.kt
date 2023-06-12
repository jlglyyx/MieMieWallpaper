package com.yang.module_main.api

import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.FansData
import com.yang.lib_common.remote.di.response.MListResult
import com.yang.lib_common.remote.di.response.MResult
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.data.WallpaperTabData
import com.yang.lib_common.data.WallpaperTypeData
import com.yang.module_main.data.SearchFindData
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @ClassName: MainApi
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:16
 */
interface MainApi {

    @GET("/")
    suspend fun getA():String

    @POST("api/user/login")
    suspend fun login(@Body params:MutableMap<String,Any>):MResult<UserInfoData>

    @POST("api/user/getUserInfo")
    suspend fun getUserInfo(): MResult<UserInfoData>

    @POST("user/register")
    suspend fun register(@Query(AppConstant.Constant.ID) id:String):MResult<UserInfoData>

//    @POST("api/user/insert/task")
//    suspend fun insertTask(@Body taskData: TaskData): MResult<String>
//
//    @POST("api/user/query/task")
//    suspend fun getTaskList(@Body params:MutableMap<String,Any>):MResult<MutableList<TaskData>>

    @Multipart
    @POST("/api/file/uploadFile")
    suspend fun uploadFile(@PartMap file: MutableMap<String, RequestBody>): MResult<MutableList<String>>

    @Multipart
    @POST("/api/dynamic/publishDynamic")
    suspend fun publishDynamic(@PartMap file: MutableMap<String, RequestBody>,@Part("userId") userId:String,@Part("content") content:String,@Part("sendLocation") sendLocation:String): MResult<MutableList<String>>


    @GET("main/queryWallType")
    suspend fun getWallType():MResult<MutableList<WallpaperTypeData>>

    @GET("api/wallpaper/queryTabs")
    suspend fun getTabs(@Query(AppConstant.Constant.WALL_TYPE) wallType:Int):MResult<MutableList<WallpaperTabData>>

    @POST("api/wallpaper/queryWallpaper")
    suspend fun getWallpaper(@Body params:MutableMap<String,Any?>):MResult<MListResult<WallpaperData>>

    @POST("api/wallpaper/queryWallpaperDetail")
    suspend fun queryWallpaperDetail(@Body params:MutableMap<String,Any?>):MResult<WallpaperData>

    @POST("api/user/searchUser")
    suspend fun searchUser(@Body params:MutableMap<String,Any?>):MResult<MListResult<UserInfoData>>

    @POST("api/wallpaper/querySearchFind")
    suspend fun getSearchFind(@Body params:MutableMap<String,Any?>):MResult<MListResult<SearchFindData>>


    @POST("api/main/insertDeviceToken")
    suspend fun insertDeviceToken(@Body params:MutableMap<String,Any?>):MResult<MListResult<String>>

    @POST("api/follow/addFollow")
    suspend fun addFollow(@Body params:MutableMap<String,Any?>):MResult<FansData>

    @POST("api/wallpaper/addOrCancelCollect")
    suspend fun addOrCancelCollect(@Body params:MutableMap<String,Any?>):MResult<WallpaperData>

}