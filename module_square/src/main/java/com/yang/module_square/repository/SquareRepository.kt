package com.yang.module_square.repository

import com.yang.lib_common.base.repository.BaseRepository
import com.yang.lib_common.data.*
import com.yang.lib_common.remote.di.response.MListResult
import com.yang.lib_common.remote.di.response.MResult
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.module_square.api.SquareApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * @ClassName: Repository
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/14 11:36
 */
class SquareRepository @Inject constructor(private val squareApi: SquareApi) :BaseRepository(){


    suspend fun getA():String{
        return withContext(Dispatchers.IO) {
            squareApi.getA()
        }
    }
    suspend fun login(params:MutableMap<String,Any>): MResult<UserInfoData> {
        return withContextIO {
            squareApi.login(params)
        }
    }
    suspend fun getUserInfo(id:String): MResult<UserInfoData> {
        return withContextIO {
            squareApi.getUserInfo(id)
        }
    }


    suspend fun getWallType(): MResult<MutableList<WallpaperTypeData>> {
        return withContextIO {
            squareApi.getWallType()
        }
    }
    suspend fun getTabs(wallType:Int): MResult<MutableList<WallpaperTabData>> {
        return withContextIO {
            squareApi.getTabs(wallType)
        }
    }
    suspend fun getWallpaper(params:MutableMap<String,Any?>): MResult<MListResult<WallpaperData>> {
        return withContextIO {
            squareApi.getWallpaper(params)
        }
    }


//    suspend fun insertTask(taskData: TaskData): MResult<String> {
//        return withContextIO {
//            squareApi.insertTask(taskData)
//        }
//    }
//
//    suspend fun getTaskList(params:MutableMap<String,Any>): MResult<MutableList<TaskData>> {
//        return withContextIO {
//            squareApi.getTaskList(params)
//        }
//    }

    suspend fun uploadFile(filePaths: MutableMap<String, RequestBody>): MResult<MutableList<String>> {
        return withContextIO {
            squareApi.uploadFile(filePaths)
        }
    }

    suspend fun uploadFileAndParam(filePaths: MutableList<RequestBody>): MResult<MutableList<String>> {
        return withContextIO {
            squareApi.uploadFileAndParam(filePaths)
        }
    }

    suspend fun getDynamicList(params:MutableMap<String,Any?>): MResult<MListResult<WallpaperDynamicData>> {
        return withContextIO {
            squareApi.getDynamicList(params)
        }
    }

    suspend fun getDynamicDetail(params:MutableMap<String,Any?>): MResult<WallpaperDynamicData> {
        return withContextIO {
            squareApi.getDynamicDetail(params)
        }
    }
    suspend fun getDynamicCommentList(params:MutableMap<String,Any?>): MResult<MutableList<WallpaperDynamicCommentData>> {
        return withContextIO {
            squareApi.getDynamicCommentList(params)
        }
    }
    suspend fun insertDynamicComment(params:MutableMap<String,Any?>): MResult<WallpaperDynamicCommentData> {
        return withContextIO {
            squareApi.insertDynamicComment(params)
        }
    }
}