package com.yang.lib_common.helper

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import com.yang.lib_common.util.getScreenPx
import com.yang.lib_common.util.showShort
import com.yang.lib_common.util.userIsVip
import io.dcloud.ads.core.entry.DCloudAdSlot
import io.dcloud.ads.core.entry.SplashConfig
import io.dcloud.ads.core.v2.reward.DCRewardAd
import io.dcloud.ads.core.v2.reward.DCRewardAdListener
import io.dcloud.ads.core.v2.reward.DCRewardAdLoadListener
import io.dcloud.ads.core.v2.splash.DCSplashAd
import io.dcloud.ads.core.v2.splash.DCSplashAdListener
import io.dcloud.ads.core.v2.splash.DCSplashAdLoadListener
import org.json.JSONArray
import org.json.JSONObject


/**
 * @ClassName: AdManager
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/11 14:53
 */
class AdManager {

    companion object{
        private const val TAG = "AdManager"
        val instance by lazy (LazyThreadSafetyMode.SYNCHRONIZED){
            AdManager()
        }
    }



    fun splashAd(context: Activity,container:RelativeLayout,finish:() -> Unit){
        var isClose = false
        val splashAd = DCSplashAd(context)
        // 宽高必传，默认为屏幕宽高

        val config: SplashConfig = SplashConfig.Builder()
            .width(getScreenPx(context)[0])
            .height(getScreenPx(context)[1] / 5 * 4) // 高度必须与父视图一致，否则部分广告显示不全
            .build()
        splashAd.setSplashAdListener(object : DCSplashAdListener {
            override fun onShow() {}
            override fun onClick() {}
            override fun onVideoPlayEnd() {
                Log.i(TAG, "onVideoPlayEnd: ")
                finish()
            }

            override fun onSkip() {
                Log.i(TAG, "onSkip: ")
                if (!isClose){
                    finish()
                    isClose = true
                }
            }

            override fun onClose() {

                Log.i(TAG, "onClose: ")
                if (!isClose){
                    finish()
                    isClose = true
                }
            }

            override fun onShowError(i: Int, s: String) {
                Log.i(TAG, "onShowError: ")
                finish()
            }
        })

        splashAd.load(config, object : DCSplashAdLoadListener {


            override fun onError(p0: Int, p1: String?, p2: JSONArray?) {
                finish()
                Log.i(TAG, "onError: $p0   $p1  ${p2.toString()}")
            }

            override fun onSplashAdLoad() {
                splashAd.showIn(container)
            }

            override fun redBag(p0: View?, p1: FrameLayout.LayoutParams?) {
                Log.i(TAG, "redBag: ")
            }

            override fun pushAd(p0: JSONObject?) {
                Log.i(TAG, "pushAd: ")
            }
        })
    }



    fun showReward(context: Activity,finish:() -> Unit) {
        if (userIsVip()){
            finish()
            return
        }

        var isSuccess = false
        val rewardAd = DCRewardAd(context)
        rewardAd.setRewardAdListener(object : DCRewardAdListener {
            override fun onReward(jsonObject: JSONObject) {
                isSuccess = true
            }
            override fun onShow() {}
            override fun onClick() {}
            override fun onVideoPlayEnd() {}
            override fun onSkip() {}
            override fun onClose() {
                if (isSuccess){
                    finish()
                }
            }
            override fun onShowError(i: Int, s: String) {
                finish()
            }
        })
        val slot = DCloudAdSlot.Builder().adpid("1377300887").build()
        rewardAd.load(slot, object : DCRewardAdLoadListener {
            override fun onRewardAdLoad() {
                rewardAd.show(context)
            }

            override fun onError(i: Int, s: String?, p2: JSONArray?) {
                Log.e("打印日志", i.toString() + s+" "+p2)
                showShort(s.toString())
            }
        })
    }

}