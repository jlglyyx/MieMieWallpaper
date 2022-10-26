package com.yang.lib_common.helper

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import com.yang.lib_common.util.getScreenPx
import io.dcloud.ads.core.entry.SplashConfig
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
                finish()
            }

            override fun onClose() {
                Log.i(TAG, "onClose: ")
                finish()
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


}