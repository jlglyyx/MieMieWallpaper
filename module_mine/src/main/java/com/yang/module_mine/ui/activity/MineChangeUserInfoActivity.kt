package com.yang.module_mine.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxj.xpopup.XPopup
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.CommonToolBar
import com.yang.module_mine.R
import com.yang.module_mine.databinding.ActChangeUserInfoBinding
import com.yang.module_mine.viewmodel.MineViewModel

/**
 * @ClassName: MineChangeUserInfoActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/10 15:30
 */
@Route(path = AppConstant.RoutePath.MINE_CHANGE_USER_INFO_ACTIVITY)
class MineChangeUserInfoActivity : BaseActivity<ActChangeUserInfoBinding>() {

    @InjectViewModel
    lateinit var mineViewModel: MineViewModel


    private var imageUrl = ""

    private var selectSex = ""

    private var sexArray = arrayOf("男", "女")

    private var url: String? = null


    override fun initViewBinding(): ActChangeUserInfoBinding {
        return bind(ActChangeUserInfoBinding::inflate)
    }


    override fun initData() {
        val userInfo = UserInfoHold.userInfo

        userInfo?.apply {
            if (userImage.isNullOrEmpty()){
                mViewBinding.sivImage.loadImage(this@MineChangeUserInfoActivity, com.yang.lib_common.R.drawable.iv_attr)
            }else{
                mViewBinding.sivImage.loadCircle(this@MineChangeUserInfoActivity,userImage)
            }
            mViewBinding.etName.setText(userName)
            mViewBinding.tvSex.text = sexArray[userSex]
            mViewBinding.etDesc.setText(userDescribe)
        }


        mineViewModel.mUserInfoData.observe(this) {
            finish()
        }
        mineViewModel.pictureListLiveData.observe(this) {
            url = it[0]
        }

    }

    override fun initView() {
        mViewBinding.commonToolBar.tVRightCallBack = object : CommonToolBar.TVRightCallBack {
            override fun tvRightClickListener() {

                val userInfoData = UserInfoData(
                    "0",
                    "sahk",
                    mViewBinding.etName.text.toString(),
                    0,
                    10,
                    null,
                    "你以为你是她的唯一，你以为...",
                    "133*****124",
                    "",
                    "",
                    0,
                    10,
                    20,
                    "",
                    "",
                    "",
                    "",
                    "",
                    url,
                    0,
                    0,
                    true,
                    0,
                    "",
                    10,
                    true,
                    "",
                    "",
                    "",
                    ""
                )
                mineViewModel.changeUserInfo(userInfoData)
            }

        }

        mViewBinding.llSex.clicks().subscribe {
            XPopup.Builder(this).asBottomList(
                "", sexArray
            ) { position, text ->
                selectSex = sexArray[position]
                mViewBinding.tvSex.text = selectSex
            }.show()
        }



        mViewBinding.llImage.clicks().subscribe {
            openGallery(1, {
                it?.let {
                    if (it.isNotEmpty()) {
                        imageUrl = it[0].realPath.toString()
                        mineViewModel.uploadFile(mutableListOf(imageUrl))
                        mViewBinding.sivImage.loadCircle(this@MineChangeUserInfoActivity,imageUrl)
                    }
                }

            })
        }
    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return mineViewModel.uC
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
    }




}