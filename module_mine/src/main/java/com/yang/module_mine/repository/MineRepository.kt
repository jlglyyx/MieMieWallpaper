package com.yang.module_mine.repository

import com.yang.lib_common.base.repository.BaseRepository
import com.yang.lib_common.remote.di.response.MResult
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.module_mine.api.MineApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @ClassName: Repository
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/14 11:36
 */
class MineRepository @Inject constructor(private val mainApi: MineApi) :BaseRepository(){


    suspend fun getA():String{
        return withContext(Dispatchers.IO) {
            mainApi.getA()
        }
    }

    suspend fun getUserInfo(id:String): MResult<UserInfoData> {
        return withContextIO {
            mainApi.getUserInfo(id)
        }
    }
    suspend fun loginOut(): MResult<String> {
        return withContextIO {
            mainApi.loginOut()
        }
    }
}