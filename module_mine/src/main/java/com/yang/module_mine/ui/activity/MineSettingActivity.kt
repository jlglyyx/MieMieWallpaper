package com.yang.module_mine.ui.activity

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.getDefaultMMKV
import com.yang.module_mine.databinding.ActMineSettingsBinding
import com.yang.module_mine.viewmodel.MineViewModel

/**
 * @ClassName: SettingActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/26 9:41
 */
@Route(path = AppConstant.RoutePath.MINE_SETTING_ACTIVITY)
class MineSettingActivity : BaseActivity<ActMineSettingsBinding>() {

    @InjectViewModel
    lateinit var mineViewModel: MineViewModel

    override fun initViewBinding(): ActMineSettingsBinding {
        return bind(ActMineSettingsBinding::inflate)
    }

    override fun initData() {
    }

    override fun initView() {
        val loginStatus = getDefaultMMKV().getInt(AppConstant.Constant.LOGIN_STATUS, -1)
        if (loginStatus == AppConstant.Constant.LOGIN_SUCCESS){
            mViewBinding.tvLoginOut.visibility = View.VISIBLE
        }
        mViewBinding.tvLoginOut.clicks().subscribe {
            mineViewModel.loginOut()
        }
        mViewBinding.icvChangePhone.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_CHANGE_PHONE_ACTIVITY).navigation()
        }
        mViewBinding.icvChangePassword.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_CHANGE_PASSWORD_ACTIVITY).navigation()
        }
        mViewBinding.icvLoginOutAccount.clicks().subscribe {
            mineViewModel.loginOut()
        }
    }

    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mineViewModel.uC
    }
}