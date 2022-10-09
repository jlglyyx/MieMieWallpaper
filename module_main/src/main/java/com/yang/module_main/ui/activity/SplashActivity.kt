package com.yang.module_main.ui.activity

import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.yang.lib_common.R
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.buildARouterLogin
import com.yang.module_main.databinding.ActSplashBinding

/**
 * @ClassName: SplashActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/26 11:26
 */
@Route(path = AppConstant.RoutePath.SPLASH_ACTIVITY)
class SplashActivity:BaseActivity<ActSplashBinding>() {

    override fun initViewBinding(): ActSplashBinding {
        return bind(ActSplashBinding::inflate)
    }

    override fun initData() {

    }

    override fun initView() {
        buildARouter(AppConstant.RoutePath.MAIN_ACTIVITY)
            .withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out))
            .navigation(this,object :NavigationCallback{
                override fun onFound(postcard: Postcard?) {
                }

                override fun onLost(postcard: Postcard?) {
                }

                override fun onArrival(postcard: Postcard?) {
                    finish()
                }

                override fun onInterrupt(postcard: Postcard?) {
                }

            })
    }

    override fun initViewModel() {

    }
}