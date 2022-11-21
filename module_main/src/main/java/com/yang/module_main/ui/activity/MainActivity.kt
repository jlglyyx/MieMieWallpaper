package com.yang.module_main.ui.activity

import android.Manifest
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mob.MobSDK
import com.tbruyelle.rxpermissions3.RxPermissions
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.PushAgent
import com.umeng.message.api.UPushRegisterCallback
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.*
import com.yang.lib_common.adapter.TabAndViewPagerAdapter
import com.yang.lib_common.app.BaseApplication
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.databinding.ViewCustomTabBinding
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.getMMKVValue
import com.yang.lib_common.util.px2dip
import com.yang.lib_common.util.showShort
import com.yang.module_main.databinding.ActMainBinding
import com.yang.module_main.viewmodel.MainViewModel
import io.dcloud.ads.core.entry.DCloudAdSlot
import io.dcloud.ads.core.v2.fullscreen.DCFullScreenAd
import io.dcloud.ads.core.v2.fullscreen.DCFullScreenAdListener
import io.dcloud.ads.core.v2.fullscreen.DCFullScreenAdLoadListener
import io.dcloud.ads.core.v2.interstitial.DCInterstitialAd
import io.dcloud.ads.core.v2.interstitial.DCInterstitialAdListener
import io.dcloud.ads.core.v2.interstitial.DCInterstitialAdLoadListener
import io.dcloud.ads.core.v2.reward.DCRewardAd
import io.dcloud.ads.core.v2.reward.DCRewardAdListener
import io.dcloud.ads.core.v2.reward.DCRewardAdLoadListener
import kotlinx.coroutines.*

import org.json.JSONArray
import org.json.JSONObject


@Route(path = AppConstant.RoutePath.MAIN_ACTIVITY)
class MainActivity : BaseActivity<ActMainBinding>() {

    @InjectViewModel
    lateinit var mainViewModel: MainViewModel

    private lateinit var mFragments: MutableList<Fragment>

    private var mTitles = arrayListOf("静态壁纸", "广场", "我的")

    private var mImages =
        arrayListOf(R.drawable.iv_picture, R.drawable.iv_dynamic, R.drawable.iv_mine)


    private var lastTime = 0L


    override fun initViewBinding(): ActMainBinding {
        return bind(ActMainBinding::inflate)
    }

    override fun initData() {

        initUM()
        lifecycleScope.launch {
            val async = async(Dispatchers.IO) {
                mFragments = mutableListOf<Fragment>().apply {
                    add(buildARouter(AppConstant.RoutePath.MAIN_FRAGMENT)
                        .withInt(AppConstant.Constant.WALL_TYPE, AppConstant.Constant.WALL_STATIC_TYPE)
                        .navigation() as Fragment)
                    //add(buildARouter(AppConstant.RoutePath.MAIN_FRAGMENT).withInt(AppConstant.Constant.WALL_TYPE,AppConstant.Constant.WALL_VIDEO_TYPE).navigation() as Fragment)
//            add(buildARouter(AppConstant.RoutePath.MAIN_FRAGMENT).navigation() as Fragment)
                    add(buildARouter(AppConstant.RoutePath.SQUARE_FRAGMENT).navigation() as Fragment)
                    add(buildARouter(AppConstant.RoutePath.MINE_FRAGMENT).navigation() as Fragment)
                }
                true
            }
            val await = async.await()
            withContext(Dispatchers.Main) {
                if (await) {
//                    initPermission()
                    initViewPager()
                    initTabLayout()
                }
            }
        }
    }

    override fun initView() {



    }

    private fun initViewPager() {

        val tabAndViewPagerAdapter = TabAndViewPagerAdapter(this, mFragments, mTitles)
        mViewBinding.viewPager.adapter = tabAndViewPagerAdapter
        mViewBinding.viewPager.isUserInputEnabled = false
        mViewBinding.viewPager.offscreenPageLimit = mFragments.size
    }

    private fun initTabLayout() {

        TabLayoutMediator(
            mViewBinding.tabLayout, mViewBinding.viewPager, true, false
        ) { tab, position ->

//            tab.setText(mTitles[position])
//            tab.setIcon(mImages[position])
//            if (position == 0) {
//                (tab.view.getChildAt(0) as ImageView).imageTintList =
//                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.appColor))
//            } else {
//                (tab.view.getChildAt(0) as ImageView).imageTintList =
//                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.grey))
//            }


            val tabView = ViewCustomTabBinding.inflate(LayoutInflater.from(this))

            tabView.tvTitle.text = mTitles[position]
            tabView.ivImage.setImageResource(mImages[position])
            tab.customView = tabView.root

            if (position == 0) {
                tabView.ivImage.setImageResource(mImages[position])
                tabView.tvTitle.setTextColor(getColor(R.color.appColor))
                DrawableCompat.setTintList(
                    tabView.ivImage.drawable, ColorStateList.valueOf(
                        getColor(R.color.appColor)
                    )
                )
//                (tab.view.getChildAt(0) as ImageView).imageTintList =
//                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.colorBar))
            } else {
                tabView.ivImage.setImageResource(mImages[position])
//                (tab.view.getChildAt(0) as ImageView).imageTintList =
//                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.grey))
            }
            tab.view.setOnLongClickListener { true }
        }.attach()

        mViewBinding.tabLayout.post {
            mViewBinding.viewPager.setPadding(
                0,
                0,
                0,
                mViewBinding.tabLayout.height + 10f.px2dip(this)
            )
        }

        mViewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val customView = tab.customView
                customView?.apply {
                    val tvTitle = findViewById<TextView>(R.id.tv_title)
                    val ivImage = findViewById<ImageView>(R.id.iv_image)
                    tvTitle.setTextColor(this@MainActivity.getColor(R.color.textColor_666666))
                    ivImage.setImageResource(mImages[tab.position])
                    DrawableCompat.setTintList(ivImage.drawable, null)
                }


//
//                tab.setIcon(mImages[tab.position])
//                (tab.view.getChildAt(0) as ImageView).imageTintList =
//                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.grey))
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val customView = tab.customView
                customView?.apply {
                    val tvTitle = findViewById<TextView>(R.id.tv_title)
                    val ivImage = findViewById<ImageView>(R.id.iv_image)
                    tvTitle.setTextColor(this@MainActivity.getColor(R.color.appColor))
                    ivImage.setImageResource(mImages[tab.position])
                    DrawableCompat.setTintList(
                        ivImage.drawable, ColorStateList.valueOf(
                            getColor(R.color.appColor)
                        )
                    )
                }
//                tab.setIcon(mImages[tab.position])
//                (tab.view.getChildAt(0) as ImageView).imageTintList =
//                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.appColor))

            }

        })
    }


    private fun initPermission() {
        RxPermissions(this).requestEachCombined(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
        )
            .subscribe {
                when {
                    it.granted -> {
                        //showShort("呦西，小伙子很不错")
                    }

                    it.shouldShowRequestPermissionRationale -> {

                        showShort("逼崽子把权限关了还怎么玩，赶紧打开")
                    }
                    else -> {
                        showShort("逼崽子把权限关了还怎么玩，赶紧打开")
                    }
                }
            }
    }

    override fun initViewModel() {

        InjectViewModelProxy.inject(this)

//        showReward()
//        showInterstitial()
//        showFullscreen()
    }


//    fun showAd(view: View) {
//        when (view.getId()) {
//            R.id.reward -> {
//                showReward()
//            }
//            R.id.fullscreen -> {
//                showFullscreen()
//            }
//            R.id.interstital -> {
//                showInterstitial()
//            }
//            R.id.splash -> {
//                showSplash()
//            }
//            R.id.feed -> {
//                startActivity(Intent(this, FeedActivity::class.java))
//            }
//            R.id.draw -> {
//                startActivity(Intent(this, DrawActivity::class.java))
//            }
//        }
//    }

    private fun showSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.putExtra("FROM", "MAIN")
        startActivity(intent)
    }

    var interstitialAd: DCInterstitialAd? = null

    private fun showInterstitial() {
        if (interstitialAd == null) interstitialAd = DCInterstitialAd(this)
        val slot = DCloudAdSlot.Builder().adpid("2111111113").build()
        interstitialAd!!.load(slot, object : DCInterstitialAdLoadListener {
            override fun onInterstitialAdLoad() {
                interstitialAd!!.setInterstitialAdListener(object : DCInterstitialAdListener {
                    override fun onShow() {}
                    override fun onClick() {}
                    override fun onVideoPlayEnd() {}
                    override fun onSkip() {}
                    override fun onClose() {}
                    override fun onShowError(i: Int, s: String) {
                        Log.e("打印日志", i.toString() + s)
                    }
                })
                interstitialAd!!.show(this@MainActivity)
            }

            override fun onError(i: Int, s: String?, p2: JSONArray?) {
                Log.e("打印日志", i.toString() + s + " " + p2)
            }
        })
    }

    var fullScreenAd: DCFullScreenAd? = null

    private fun showFullscreen() {
        if (fullScreenAd == null) fullScreenAd = DCFullScreenAd(this)
        fullScreenAd!!.setFullScreenAdListener(object : DCFullScreenAdListener {
            override fun onShow() {}
            override fun onClick() {}
            override fun onVideoPlayEnd() {}
            override fun onSkip() {}
            override fun onClose() {}
            override fun onShowError(i: Int, s: String) {}
        })
        val slot = DCloudAdSlot.Builder().adpid("2507000611").build()
        fullScreenAd!!.load(slot, object : DCFullScreenAdLoadListener {
            override fun onFullScreenAdLoad() {
                fullScreenAd!!.show(this@MainActivity)
            }

            override fun onError(i: Int, s: String?, p2: JSONArray?) {
                Log.e("打印日志", i.toString() + s + " " + p2)
            }
        })
    }

    var rewardAd: DCRewardAd? = null

    private fun showReward() {
        if (rewardAd == null) rewardAd = DCRewardAd(this)
        rewardAd!!.setRewardAdListener(object : DCRewardAdListener {
            override fun onReward(jsonObject: JSONObject) {}
            override fun onShow() {}
            override fun onClick() {}
            override fun onVideoPlayEnd() {}
            override fun onSkip() {}
            override fun onClose() {}
            override fun onShowError(i: Int, s: String) {}
        })
        val slot = DCloudAdSlot.Builder().adpid("2507000689").build()
        rewardAd!!.load(slot, object : DCRewardAdLoadListener {
            override fun onRewardAdLoad() {
                rewardAd!!.show(this@MainActivity)
            }

            override fun onError(i: Int, s: String?, p2: JSONArray?) {
                Log.e("打印日志", i.toString() + s + " " + p2)
            }
        })
    }


    override fun onBackPressed() {
        //super.onBackPressed()
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastTime > 1000) {
            lastTime = currentTimeMillis
            showShort(getString(R.string.string_close_application))
        } else {
            MobclickAgent.onKillProcess(this)
            moveTaskToBack(true)
        }
    }

    private fun initUM(){

        lifecycleScope.launch(Dispatchers.IO) {
            val privacyAgreement = getMMKVValue(AppConstant.Constant.PRIVACY_AGREEMENT, false)
            if (privacyAgreement){
                MobSDK.submitPolicyGrantResult(privacyAgreement)
                PushAgent.getInstance(this@MainActivity).onAppStart()
            }
        }
        LiveDataBus.instance.with(AppConstant.UMConstant.DEVICE_TOKEN).observe(this){
            mainViewModel.insertDeviceToken(it.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel()
    }
}