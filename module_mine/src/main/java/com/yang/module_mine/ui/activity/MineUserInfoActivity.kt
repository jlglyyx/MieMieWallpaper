package com.yang.module_mine.ui.activity


import android.content.Intent
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.MediaInfoBean
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.getUserInfo
import com.yang.lib_common.*
import com.yang.lib_common.widget.CommonToolBar
import com.yang.module_mine.databinding.ActMineUserInfoBinding
import com.yang.module_mine.viewmodel.MineViewModel

/**
 * @Author Administrator
 * @ClassName MineUserInfoActivity
 * @Description 其他信息
 * @Date 2021/9/10 10:51
 */
@Route(path = AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY)
class MineUserInfoActivity : BaseActivity<ActMineUserInfoBinding>() {
    @InjectViewModel
    lateinit var mineViewModel: MineViewModel

    private val FILE_CODE = 100


    override fun initViewBinding(): ActMineUserInfoBinding {
        return bind(ActMineUserInfoBinding::inflate)
    }

    override fun initData() {
        val id = intent.getStringExtra(AppConstant.Constant.ID)
        val userInfo = getUserInfo()
        userInfo.let {
            Glide.with(this).load(
                it?.userImage
                    ?: "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic%2F39%2Fb7%2F53%2F39b75357f98675e2d6d5dcde1fb805a3.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1642840086&t=2a7574a5d8ecc96669ac3e050fe4fd8e"
            ).error(R.drawable.iv_image_error)
                .placeholder(R.drawable.iv_image_placeholder).into(mViewBinding.sivImg)
            if (!TextUtils.equals(id, it?.id)) {
                mViewBinding.commonToolBar.rightContentVisible = false
            }
            mViewBinding.tvName.text = it?.userName?:"修改一下昵称吧"
            mViewBinding.tvAccount.text = "账号：${it?.userAccount}"
            mViewBinding.tvVipLevel.text = "等级：vip${it?.userVipLevel?:""}"
            mViewBinding.tvInfo.text = "${it?.userSex} ${it?.userAge}岁 | ${it?.userBirthDay} "
            mViewBinding.tvDesc.text = it?.userDescribe?:"人生在世总要留点什么吧..."
        }
    }

    override fun initView() {
        mViewBinding.commonToolBar.tVRightCallBack = object : CommonToolBar.TVRightCallBack {
            override fun tvRightClickListener() {
                buildARouter(AppConstant.RoutePath.MINE_CHANGE_USER_INFO_ACTIVITY).navigation()
            }
        }
        mViewBinding.ivBg.imageUrl =
            "https://img1.baidu.com/it/u=1251916380,3661111139&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=800"
        lifecycle.addObserver(mViewBinding.ivBg)

        mViewBinding.ivBg.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.PICTURE_SELECT_ACTIVITY)
                .withInt(AppConstant.Constant.TYPE, AppConstant.Constant.NUM_ONE)
                .withInt(AppConstant.Constant.NUM, AppConstant.Constant.NUM_ONE)
                .navigation(this, FILE_CODE)
        }
    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mineViewModel.uC
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == FILE_CODE && resultCode == RESULT_OK) {
                it.getParcelableArrayListExtra<MediaInfoBean>(AppConstant.Constant.DATA)
                    ?.let { beans ->
                        if (beans.size > 0) {
                            mViewBinding.ivBg.imageUrl = beans[0].filePath.toString()
                        }
                    }
            }
        }
    }




}