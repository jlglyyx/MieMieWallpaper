package com.yang.lib_common.helper

import android.content.Context
import android.util.Log
import anet.channel.util.Utils
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.PushAgent
import com.umeng.message.api.UPushRegisterCallback
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant


/**
 * @ClassName: PushHelper
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/19 22:27
 */
class PushHelper {

    companion object{
        private const val TAG = "PushHelper"

        fun preInit(context: Context){
            UMConfigure.preInit(context,
                AppConstant.UMConstant.UM_APP_ID,
                AppConstant.UMConstant.UM_APP_CHANNEL)
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        }

        fun init(context: Context){
            UMConfigure.init(
                context,
                AppConstant.UMConstant.UM_APP_ID,
                AppConstant.UMConstant.UM_APP_CHANNEL,
                UMConfigure.DEVICE_TYPE_PHONE,
                AppConstant.UMConstant.UM_APP_MESSAGE_SECRET
            )

            PushAgent.getInstance(context).register(object : UPushRegisterCallback {
                override fun onSuccess(deviceToken: String?) {
                    //注册成功后返回deviceToken，deviceToken是推送消息的唯一标志
//                    Log.i(TAG, "注册成功 deviceToken:" + deviceToken)
                    LiveDataBus.instance.with(AppConstant.UMConstant.DEVICE_TOKEN).postValue(deviceToken)
                }

                override fun onFailure(errCode: String?, errDesc: String?) {
                    Log.e(TAG, "注册失败 " + "code:" + errCode + ", desc:" + errDesc)
                }

            })
        }
    }
}