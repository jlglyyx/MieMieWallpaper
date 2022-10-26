package com.yang.module_main.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.yang.lib_common.R
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.helper.AdManager
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.buildARouterLogin
import com.yang.lib_common.util.createAppId
import com.yang.lib_common.util.getAppId
import com.yang.module_main.databinding.ActSplashBinding

/**
 * @ClassName: SplashActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/26 11:26
 */
@Route(path = AppConstant.RoutePath.SPLASH_ACTIVITY)
class SplashActivity:BaseActivity<ActSplashBinding>() {

    private var basePopupView: BasePopupView? = null

   private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var isGrantAll = true
            it.forEach { item ->
                isGrantAll = isGrantAll and item.value
            }
            if (isGrantAll){
                createAppId(getAppId(path = obbDir.absolutePath))

                AdManager.instance.splashAd(this,mViewBinding.container){
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
            }else{
                initDialog()
            }
        }

    override fun initViewBinding(): ActSplashBinding {
        return bind(ActSplashBinding::inflate)
    }

    override fun initData() {

    }

    override fun initView() {


    }

    private fun initPermission(){

        registerForActivityResult.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_PHONE_STATE ))
    }

    private fun initDialog(){
        if (null == basePopupView){
            basePopupView = XPopup.Builder(this).dismissOnTouchOutside(false).asConfirm("提示","不授权文件存储权限，将无法下载图片哦~","","去设置",
                {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", packageName, null)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                },null,true).show()
        }else{
            if (!basePopupView!!.isShow){
                basePopupView!!.show()
            }
        }
    }

    override fun initViewModel() {

    }





    override fun onStart() {
        super.onStart()
        initPermission()
    }
}