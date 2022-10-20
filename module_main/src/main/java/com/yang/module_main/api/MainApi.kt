package com.yang.module_main.api

import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.LoginData
import com.yang.lib_common.remote.di.response.MListResult
import com.yang.lib_common.remote.di.response.MResult
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.module_main.data.WallpaperData
import com.yang.module_main.data.WallpaperTabData
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

    // TODO:  替换 @Body
    @POST("user/login")
    suspend fun login(@QueryMap params:MutableMap<String,Any>):MResult<LoginData>

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


    @GET("main/queryTabs")
    suspend fun getTabs(@Query(AppConstant.Constant.WALL_TYPE) wallType:Int):MResult<MutableList<WallpaperTabData>>

    @POST("main/queryAllByTab")
    suspend fun getWallpaper(@Body params:MutableMap<String,Any?>):MResult<MListResult<WallpaperData>>

}