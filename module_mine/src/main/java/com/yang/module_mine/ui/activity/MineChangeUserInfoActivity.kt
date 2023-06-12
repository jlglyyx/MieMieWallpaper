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

    private var selectSex = 0

    private var sexArray = arrayOf("男", "女","保密")

    private var url: String? = null



    override fun initViewBinding(): ActChangeUserInfoBinding {
        return bind(ActChangeUserInfoBinding::inflate)
    }


    override fun initData() {
        val userInfo = UserInfoHold.userInfo

        userInfo?.apply {
            if (userAttr.isNullOrEmpty()){
                mViewBinding.sivImage.loadImage(this@MineChangeUserInfoActivity, com.yang.lib_common.R.drawable.iv_attr)
            }else{
                mViewBinding.sivImage.loadCircle(this@MineChangeUserInfoActivity,userAttr)
            }
            mViewBinding.etName.setText(userName)
            mViewBinding.tvSex.text = sexArray[userSex?:2]
            selectSex = userSex?:2
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

                val userInfoData = "{}".fromJson<UserInfoData>().apply {
                    id = UserInfoHold.userId
                    userAttr = url
                    userSex = selectSex
                    userName = mViewBinding.etName.text.toString()
                    userDescribe = mViewBinding.etDesc.text.toString()
                }
                mineViewModel.updateUserInfo(userInfoData)
            }

        }

        mViewBinding.llSex.clicks().subscribe {
            XPopup.Builder(this).asBottomList(
                "", sexArray
            ) { position, text ->
                selectSex = position
                mViewBinding.tvSex.text = sexArray[position]
            }.show()
        }



        mViewBinding.llImage.clicks().subscribe {
            openGallery(1, {
                it?.let {
                    if (it.isNotEmpty()) {
                        imageUrl = it[0].realPath.toString()
                        mineViewModel.uploadFile(mutableListOf(imageUrl),false)
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