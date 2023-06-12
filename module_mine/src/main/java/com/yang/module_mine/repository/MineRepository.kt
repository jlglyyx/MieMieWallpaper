package com.yang.module_mine.repository

import com.yang.lib_common.base.repository.BaseRepository
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.remote.di.response.MListResult
import com.yang.lib_common.remote.di.response.MResult
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.module_mine.api.MineApi
import com.yang.lib_common.data.FansData
import com.yang.module_mine.data.VipPackageData
import com.yang.module_mine.data.WalletDetailData
import com.yang.module_mine.data.WeChatInfoData
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
class MineRepository @Inject constructor(private val mineApi: MineApi) :BaseRepository(){


    suspend fun getA():String{
        return withContext(Dispatchers.IO) {
            mineApi.getA()
        }
    }

    suspend fun getUserInfo(params:MutableMap<String,Any?>): MResult<UserInfoData> {
        return withContextIO {
            mineApi.getUserInfo(params)
        }
    }
    suspend fun loginOut(): MResult<String> {
        return withContextIO {
            mineApi.loginOut()
        }
    }

    suspend fun getWallpaper(params:MutableMap<String,Any?>): MResult<MListResult<WallpaperData>> {
        return withContextIO {
            mineApi.getWallpaper(params)
        }
    }
    suspend fun queryCollectionWallpaper(params:MutableMap<String,Any?>): MResult<MListResult<WallpaperData>> {
        return withContextIO {
            mineApi.queryCollectionWallpaper(params)
        }
    }


    suspend fun uploadFile(filePaths: MutableMap<String, RequestBody>): MResult<MutableList<String>> {
        return withContext(Dispatchers.IO) {
            mineApi.uploadFile(filePaths)
        }
    }


    suspend fun updateUserInfo(userInfoData: UserInfoData): MResult<UserInfoData> {
        return withContextIO{
            mineApi.updateUserInfo(userInfoData)
        }
    }
    suspend fun sign(id: String): MResult<UserInfoData> {
        return withContextIO{
            mineApi.sign(id)
        }
    }
    suspend fun openVip(params:MutableMap<String,Any?>): MResult<Any> {
        return withContextIO{
            mineApi.openVip(params)
        }
    }


    suspend fun getWeChatToken(params:MutableMap<String,Any?>): WeChatInfoData {

        return withContext(Dispatchers.IO) {
            mineApi.getWeChatToken(params)
        }

    }
    suspend fun refreshWeChatToken(params:MutableMap<String,Any?>): WeChatInfoData {
        return withContext(Dispatchers.IO) {
            mineApi.refreshWeChatToken(params)
        }
    }

    suspend fun getDynamicList(params:MutableMap<String,Any?>): MResult<MListResult<WallpaperDynamicData>> {
        return withContextIO {
            mineApi.getDynamicList(params)
        }
    }

    suspend fun getDynamicDetail(params:MutableMap<String,Any?>): MResult<WallpaperDynamicData> {
        return withContextIO {
            mineApi.getDynamicDetail(params)
        }
    }

    suspend fun getMineFollowList(params:MutableMap<String,Any?>): MResult<MListResult<FansData>> {
        return withContextIO {
            mineApi.getMineFollowList(params)
        }
    }
    suspend fun addFollow(params:MutableMap<String,Any?>): MResult<FansData> {
        return withContextIO {
            mineApi.addFollow(params)
        }
    }
    suspend fun getVipPackage(): MResult<MutableList<VipPackageData>> {
        return withContextIO {
            mineApi.getVipPackage()
        }
    }

    suspend fun getWalletDetail(params:MutableMap<String,Any?>): MResult<MListResult<WalletDetailData>> {
        return withContextIO {
            mineApi.getWalletDetail(params)
        }
    }
}