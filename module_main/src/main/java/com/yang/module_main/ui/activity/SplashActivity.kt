package com.yang.module_main.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.mob.MobSDK
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.PushAgent
import com.yang.lib_common.R
import com.yang.lib_common.app.BaseApplication
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.dialog.PermissionDialog
import com.yang.lib_common.dialog.PrivacyAgreementDialog
import com.yang.lib_common.helper.PushHelper
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.getMMKVValue
import com.yang.lib_common.util.isNextDay
import com.yang.lib_common.util.setMMKVValue
import com.yang.module_main.databinding.ActSplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * @ClassName: SplashActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/26 11:26
 */
@Route(path = AppConstant.RoutePath.SPLASH_ACTIVITY)
class SplashActivity : BaseActivity<ActSplashBinding>() {

    private var basePopupView: BasePopupView? = null

    private var basePrivacyPopupView: BasePopupView? = null

    private var basePermissionPopupView: BasePopupView? = null

    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var isGrantAll = true
            it.forEach { item ->
                isGrantAll = isGrantAll and item.value
            }
            if (isGrantAll) {
//                createAppId(getAppId(path = obbDir.absolutePath))

                //AdManager.instance.splashAd(this, mViewBinding.container) {
                buildARouter(AppConstant.RoutePath.MAIN_ACTIVITY)
                    .withOptionsCompat(
                        ActivityOptionsCompat.makeCustomAnimation(
                            this,
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                    )
                    .navigation(this, object : NavigationCallback {
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
                //}
            } else {
                initDialog()
            }
        }

    override fun initViewBinding(): ActSplashBinding {
        return bind(ActSplashBinding::inflate)
    }

    override fun initData() {
    }

    override fun initView() {

        isNextDay()
    }

    private fun initPermission() {

        registerForActivityResult.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        )
    }

    private fun initDialog() {
        if (null == basePopupView) {
            basePopupView = XPopup.Builder(this)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asConfirm(
                    "提示", "不授与权限，将无法下和上传载图片哦~", "", "去设置",
                    {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.fromParts("package", packageName, null)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }, null, true
                ).show()
        } else {
            if (!basePopupView!!.isShow) {
                basePopupView!!.show()
            }
        }
    }

    private fun initPermissionDialog() {
        if (null == basePermissionPopupView) {
            basePermissionPopupView = XPopup.Builder(this)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(PermissionDialog(this).apply {
                    onCancel = {
                        dismiss()
                        finish()
                    }
                    onConfirm = {
                        dismiss()
                        setMMKVValue(AppConstant.Constant.PRIVACY_AGREEMENT, true)
                        initPermission()
                    }
                })
                .show()
        } else {
            if (!basePermissionPopupView!!.isShow) {
                basePermissionPopupView!!.show()
            }
        }
    }

    private fun initPrivacyDialog() {
        if (null == basePrivacyPopupView) {
            basePrivacyPopupView = XPopup.Builder(this)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(PrivacyAgreementDialog(this).apply {
                    onCancel = {
                        dismiss()
                        finish()
                    }
                    onConfirm = {
                        dismiss()
                        setMMKVValue(AppConstant.Constant.PRIVACY_AGREEMENT, true)
                        MobSDK.submitPolicyGrantResult(true)
                        initUM()
                        initPermission()
                        //initPermissionDialog()
                    }
                }).show()
        } else {
            if (!basePrivacyPopupView!!.isShow) {
                basePrivacyPopupView!!.show()
            }
        }
    }

    override fun initViewModel() {

    }


    override fun onStart() {
        super.onStart()
//        setMMKVValue(AppConstant.Constant.PRIVACY_AGREEMENT,false)
        val privacyAgreement = getMMKVValue(AppConstant.Constant.PRIVACY_AGREEMENT, false)
        if (privacyAgreement) {
            initPermission()
        } else {
            initPrivacyDialog()
        }
    }


    private fun initUM() {
        lifecycleScope.launch(Dispatchers.IO) {
            PushHelper.init(BaseApplication.baseApplication.applicationContext)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel()
    }
}