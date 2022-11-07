package com.yang.module_mine.ui.activity

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.showShort
import com.yang.lib_common.widget.CommonSearchToolBar
import com.yang.lib_common.widget.CommonToolBar
import com.yang.module_mine.databinding.ActMineChangePasswordBinding
import com.yang.module_mine.viewmodel.MineViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @ClassName: MineChangePasswordActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/26 10:27
 */
@Route(path = AppConstant.RoutePath.MINE_CHANGE_PASSWORD_ACTIVITY)
class MineChangePasswordActivity : BaseActivity<ActMineChangePasswordBinding>() {

    @InjectViewModel
    lateinit var mineViewModel: MineViewModel

    override fun initViewBinding(): ActMineChangePasswordBinding {
        return bind(ActMineChangePasswordBinding::inflate)
    }

    override fun initData() {
    }

    override fun initView() {
        mViewBinding.apply {
            tvTime.clicks().subscribe {
                tvTime.isEnabled = false
                initTimer()
            }

            btConfirm.setOnClickListener {
                checkForm()
            }

        }
    }

    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mineViewModel.uC
    }


    private fun initTimer(){
        lifecycleScope.launch {
            val await = async  {
                repeat(60) {
                    mViewBinding.tvTime.text = "${60 - it}秒后获取"
                    delay(1000)
                }
                true
            }.await()
            if (await){
                mViewBinding.tvTime.text = "获取验证码"
                mViewBinding.tvTime.isEnabled = true
            }

        }
    }

    private fun checkForm(){

        val phone = mViewBinding.etPhone.text.toString()

        val verificationCode = mViewBinding.etVerificationCode.text.toString()

        val newPassword = mViewBinding.etNewPassword.text.toString()

        if (phone.isEmpty()){
            showShort("请输入手机号")
            return
        }
        if (verificationCode.isEmpty()){
            showShort("请输入验证码")
            return
        }

        if (newPassword.isEmpty()){
            showShort("请输入新密码")
            return
        }
        finish()
    }
}