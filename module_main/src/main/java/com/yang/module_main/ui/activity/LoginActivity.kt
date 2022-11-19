package com.yang.module_main.ui.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.bytedance.msdk.adapter.util.UIUtils.setOnClickListener
import com.jakewharton.rxbinding4.view.clicks
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.*
import com.yang.module_main.databinding.ActLoginBinding
import com.yang.module_main.viewmodel.MainViewModel
import kotlinx.coroutines.*


/**
 * @ClassName: LoginActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/22 14:58
 */
@Route(path = AppConstant.RoutePath.LOGIN_ACTIVITY)
class LoginActivity : BaseActivity<ActLoginBinding>() {

    @InjectViewModel
    lateinit var mainViewModel: MainViewModel


    override fun initViewBinding(): ActLoginBinding {
        return bind(ActLoginBinding::inflate)
    }

    override fun initData() {
    }

    override fun initView() {
        lifecycle.addObserver(mViewBinding.imageScrollView)
        mViewBinding.imageScrollView.imageUrl = "${AppConstant.ClientInfo.IMAGE_MODULE}image/login.png"
        mViewBinding.tvLoginType.setOnClickListener {
            if (mainViewModel.loginType == 0) {
                mViewBinding.clTime.visibility = View.GONE
                mViewBinding.etPassword.visibility = View.VISIBLE
                mainViewModel.loginType = 1
                mainViewModel.verificationText = ""
                mViewBinding.etPassword.setText("")
                mViewBinding.tvLoginType.text = getString(R.string.string_login_verification_code)
            } else {
                mViewBinding.clTime.visibility = View.VISIBLE
                mViewBinding.etPassword.visibility = View.GONE
                mainViewModel.loginType = 0
                mainViewModel.passwordText = ""
                mViewBinding.etVerificationCode.setText("")
                mViewBinding.tvLoginType.text = getString(R.string.string_login_account_password)
            }
        }

        mViewBinding.tvTime.clicks().subscribe {
            mViewBinding.tvTime.isEnabled = false
            initTimer()
        }

        mViewBinding.btToLogin.clicks().subscribe {
            hideSoftInput(this, mViewBinding.btToLogin)
            mainViewModel.login()
        }
        mViewBinding.tvOtherToLogin.clicks().subscribe {
//            //设置授权登录的平台
//            val plat = ShareSDK.getPlatform(QQ.NAME)
//            //授权回调监听，监听oncomplete，onerror，oncancel三种状态
//            plat.platformActionListener = object : PlatformActionListener{
//                override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
//                }
//
//                override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
//                }
//
//                override fun onCancel(p0: Platform?, p1: Int) {
//                }
//
//            }
//            //抖音登录适配安卓9.0
//            //ShareSDK.setActivity(MainActivity.this);
//            plat.showUser(null)
//            //plat.authorize();

        }

        mViewBinding.apply {
            cbServiceAgreement.setOnCheckedChangeListener { buttonView, isChecked ->
                mainViewModel.checkStatus = isChecked
            }

            tvServiceAgreement.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_WEB_ACTIVITY).withString(
                    AppConstant.Constant.TITLE,
                    "服务协议"
                ).withString(
                    AppConstant.Constant.URL,
                    AppConstant.ClientInfo.SERVICE_AGREEMENT
                ).navigation()
            }
            tvPrivacyAgreement.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_WEB_ACTIVITY).withString(
                    AppConstant.Constant.TITLE,
                    "隐私政策"
                ).withString(
                    AppConstant.Constant.URL,
                    AppConstant.ClientInfo.PRIVACY_AGREEMENT
                ).navigation()
            }



            tvLostPassword.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_CHANGE_PASSWORD_ACTIVITY).navigation()
            }

            ivClose.clicks().subscribe {
                finish()
            }
            etPhone.setRadius(6f)
            etPassword.setRadius(6f)
            etVerificationCode.setRadius(6f)
        }







        initTextChangedListener()
    }

    private fun initTimer() {
        lifecycleScope.launch {
            val await = async {
                repeat(60) {
                    mViewBinding.tvTime.text = "${60 - it}秒后获取"
                    delay(1000)
                }
                true
            }
            if (await.await()) {
                mViewBinding.tvTime.text = "获取验证码"
                mViewBinding.tvTime.isEnabled = true
            }

        }

    }

    private fun initTextChangedListener() {
        mViewBinding.etPhone.getEdit().addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mainViewModel.phoneText = s.toString()
                if (mainViewModel.loginType == 0) {
                    mViewBinding.btToLogin.isEnabled =
                        !TextUtils.isEmpty(mainViewModel.phoneText) && !TextUtils.isEmpty(
                            mainViewModel.verificationText
                        )
                } else {
                    mViewBinding.btToLogin.isEnabled =
                        !TextUtils.isEmpty(mainViewModel.phoneText) && !TextUtils.isEmpty(
                            mainViewModel.passwordText
                        )
                }
            }

            override fun afterTextChanged(s: Editable) {

            }

        })
        mViewBinding.etPassword.getEdit().addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mainViewModel.passwordText = s.toString()
                mViewBinding.btToLogin.isEnabled =
                    !TextUtils.isEmpty(mainViewModel.phoneText) && !TextUtils.isEmpty(mainViewModel.passwordText)
            }

            override fun afterTextChanged(s: Editable) {

            }

        })

        mViewBinding.etVerificationCode.getEdit().addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mainViewModel.verificationText = s.toString()
                mViewBinding.btToLogin.isEnabled =
                    !TextUtils.isEmpty(mainViewModel.phoneText) && !TextUtils.isEmpty(mainViewModel.verificationText)
            }

            override fun afterTextChanged(s: Editable) {

            }

        })
    }


    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mainViewModel.uC
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }

}