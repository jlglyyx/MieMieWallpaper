package com.yang.module_mine.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.getVersionName
import com.yang.lib_common.util.showShort
import com.yang.module_mine.databinding.ActAboutBinding

/**
 * @ClassName: AboutActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/4 13:36
 */
@Route(path = AppConstant.RoutePath.MINE_ABOUT_ACTIVITY)
class AboutActivity : BaseActivity<ActAboutBinding>() {

    override fun initViewBinding(): ActAboutBinding {
        return bind(ActAboutBinding::inflate)
    }

    override fun initData() {

    }

    override fun initView() {
        mViewBinding.apply {
            icvUpdate.rightContent = "v${getVersionName(this@AboutActivity)}"
            icvUpdate.redPointVisible = true
            icvUpdate.clicks().subscribe {

                showShort("已是最新版本")
            }

            icvFunctionIntroduce.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_WEB_ACTIVITY).withString(
                    AppConstant.Constant.TITLE,
                    "功能介绍"
                ).withString(
                    AppConstant.Constant.URL,
                    AppConstant.ClientInfo.BASE_WEB_URL + "/pages/about/functionIntroduce"
                ).navigation()
            }
            icvServiceProtocol.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_WEB_ACTIVITY).withString(
                    AppConstant.Constant.TITLE,
                    "服务协议"
                ).withString(
                    AppConstant.Constant.URL,
                    AppConstant.ClientInfo.BASE_WEB_URL + "/pages/about/serviceProtocol"
                ).navigation()
            }
            icvPrivacyPolicy.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_WEB_ACTIVITY).withString(
                    AppConstant.Constant.TITLE,
                    "隐私政策"
                ).withString(
                    AppConstant.Constant.URL,
                    AppConstant.ClientInfo.BASE_WEB_URL + "/pages/about/privacyPolicy"
                ).navigation()
            }
        }


    }

    override fun initViewModel() {

    }
}