package com.yang.module_main.repository

import com.yang.lib_common.base.repository.BaseRepository
import com.yang.lib_common.remote.di.response.MListResult
import com.yang.lib_common.remote.di.response.MResult
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.module_main.api.MainApi
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.data.WallpaperTabData
import com.yang.module_main.data.SearchFindData
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
class MainRepository @Inject constructor(private val mainApi: MainApi) :BaseRepository(){


    suspend fun getA():String{
        return withContext(Dispatchers.IO) {
            mainApi.getA()
        }
    }
    suspend fun login(params:MutableMap<String,Any>): MResult<UserInfoData> {
        return withContextIO {
            mainApi.login(params)
        }
    }
    suspend fun getUserInfo(id:String): MResult<UserInfoData> {
        return withContextIO {
            mainApi.getUserInfo(id)
        }
    }


    suspend fun getTabs(wallType:Int): MResult<MutableList<WallpaperTabData>> {
        return withContextIO {
            mainApi.getTabs(wallType)
        }
    }
    suspend fun getWallpaper(params:MutableMap<String,Any?>): MResult<MListResult<WallpaperData>> {
        return withContextIO {
            mainApi.getWallpaper(params)
        }
    }
    suspend fun getSearchFind(params:MutableMap<String,Any?>): MResult<MListResult<SearchFindData>> {
        return withContextIO {
            mainApi.getSearchFind(params)
        }
    }

//    suspend fun insertTask(taskData: TaskData): MResult<String> {
//        return withContextIO {
//            mainApi.insertTask(taskData)
//        }
//    }
//
//    suspend fun getTaskList(params:MutableMap<String,Any>): MResult<MutableList<TaskData>> {
//        return withContextIO {
//            mainApi.getTaskList(params)
//        }
//    }

    suspend fun uploadFile(filePaths: MutableMap<String, RequestBody>): MResult<MutableList<String>> {
        return withContextIO {
            mainApi.uploadFile(filePaths)
        }
    }

    suspend fun uploadFileAndParam(filePaths: MutableList<RequestBody>): MResult<MutableList<String>> {
        return withContextIO {
            mainApi.uploadFileAndParam(filePaths)
        }
    }
}