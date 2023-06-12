package com.yang.module_mine.ui.activity


import android.text.Html
import android.text.TextUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjq.shape.view.ShapeImageView
import com.lxj.xpopup.XPopup
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.dialog.ImageViewPagerDialog
import com.yang.lib_common.interceptor.UrlInterceptor.Companion.url
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.CommonToolBar
import com.yang.lib_common.widget.ErrorReLoadView
import com.yang.module_mine.R
import com.yang.module_mine.databinding.ActMineUserInfoBinding
import com.yang.module_mine.viewmodel.MineViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    private var userId = ""

    private var sexArray = arrayOf("玉树临风大帅哥~", "可可爱爱小仙女~", "暂时保密哦")

    private var url: String? = null

    private var sexIconArray =
        arrayOf(
            com.yang.lib_common.R.drawable.iv_man,
            com.yang.lib_common.R.drawable.iv_woman,
            com.yang.lib_common.R.drawable.iv_sex_bm
        )

    private lateinit var mAdapter: BaseQuickAdapter<String, BaseViewHolder>


    override fun initViewBinding(): ActMineUserInfoBinding {
        return bind(ActMineUserInfoBinding::inflate)
    }

    override fun initData() {


        mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.LOADING
        userId = intent.getStringExtra(AppConstant.Constant.USER_ID) ?: ""
        mViewBinding.commonToolBar.rightContentVisible = false
        mineViewModel.getUserInfo(userId)
        mineViewModel.getDynamicList(userId)

    }

    override fun initView() {


        mViewBinding.apply {
            commonToolBar.tVRightCallBack = object : CommonToolBar.TVRightCallBack {
                override fun tvRightClickListener() {
                    buildARouter(AppConstant.RoutePath.MINE_CHANGE_USER_INFO_ACTIVITY).navigation()
                }
            }



            ivBg.clicks().subscribe {
                openGallery(1, {
                    it?.let {
                        if (it.isNotEmpty()) {

                            mineViewModel.uploadFile(
                                mutableListOf(it[0].realPath.toString()),
                                false
                            )
                        }
                    }

                })
            }

            errorReLoadView.onClick = {
                errorReLoadView.status = ErrorReLoadView.Status.LOADING
                mineViewModel.getWallpaper(userId)
            }


            llMore.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_SQUARE_ACTIVITY).withString(
                    AppConstant.Constant.USER_ID,
                    userId
                ).navigation()
            }


            stvSendMessage.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_SEND_MESSAGE_ACTIVITY).withString(
                    AppConstant.Constant.ID,
                    userId
                ).navigation()
            }
        }

        initRecyclerView()
    }

    private fun initUserInfo(userInfo: UserInfoData?) {
        userInfo?.apply {
            if (userAttr.isNullOrEmpty()) {
                mViewBinding.sivImg.loadImage(
                    this@MineUserInfoActivity,
                    com.yang.lib_common.R.drawable.iv_attr
                )
            } else {
                mViewBinding.sivImg.loadCircle(this@MineUserInfoActivity, userAttr)
            }
            mViewBinding.commonToolBar.rightContentVisible =
                TextUtils.equals(UserInfoHold.userId, this.id)
            mViewBinding.commonToolBar.centerContent =
                if (TextUtils.equals(UserInfoHold.userId, this.id)) "我的资料" else "他的资料"
            mViewBinding.tvName.text = userName
            mViewBinding.tvAccount.text = "账号：${userPhone}"
            if (userVipLevel == 0 || userVipLevel == null) {
                mViewBinding.tvVipLevel.text = "暂未开通会员"
                mViewBinding.tvVipLevel.setTextColor(getColor(com.yang.lib_common.R.color.textColor_999999))
            } else {
                if (userVipExpired) {
                    mViewBinding.tvVipLevel.text = "会员已到期"
                } else {
                    mViewBinding.tvVipLevel.text = Html.fromHtml(
                        String.format(
                            getString(com.yang.lib_common.R.string.string_vip_level),
                            userVipLevel
                        ), Html.FROM_HTML_OPTION_USE_CSS_COLORS
                    )
                }


            }
            mViewBinding.tvFanAttentionNum.text = Html.fromHtml(
                String.format(
                    getString(com.yang.lib_common.R.string.string_fans_attention_num),
                    userFan ?: 0,
                    userAttention ?: 0
                ), Html.FROM_HTML_OPTION_USE_CSS_COLORS
            )
            mViewBinding.tvInfo.text = sexArray[userSex ?: 2]
            mViewBinding.ivSex.setImageResource(sexIconArray[userSex ?: 2])
            mViewBinding.tvDesc.text = userDescribe ?: "人生在世总要留点什么吧..."


            mViewBinding.ivBg.imageUrl =
                getRealUrl(userInfoBg) ?: "${AppConstant.ClientInfo.IMAGE_MODULE}image/login.png"
            lifecycle.addObserver(mViewBinding.ivBg)
        }
    }

    private fun initRecyclerView() {
        mAdapter = object :
            BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_collection_image) {
            override fun convert(helper: BaseViewHolder, item: String) {
                val imageView = helper.getView<ShapeImageView>(R.id.iv_image)
                loadSpaceRadius(mContext, item, 8f, imageView, 3, 15f)
            }
        }
        mViewBinding.recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                XPopup.Builder(this).asCustom(ImageViewPagerDialog(this,mAdapter.data.map {
                    getRealUrl(it)
                } as MutableList<String>,position)).show()
//                buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
//                    .withOptionsCompat(
//                        ActivityOptionsCompat.makeCustomAnimation(
//                            this@MineUserInfoActivity,
//                            com.yang.lib_common.R.anim.fade_in,
//                            com.yang.lib_common.R.anim.fade_out
//                        )
//                    )
//                    .withString(AppConstant.Constant.DATA, mAdapter.data.toJson())
//                    .withInt(AppConstant.Constant.INDEX, position)
//                    .withBoolean(AppConstant.Constant.TO_LOAD, false)
//                    .navigation()
            }
        }


    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mineViewModel.uC
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)

        mineViewModel.mWallpaperDynamicData.observe(this) {
            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.NORMAL
            if (it.isNullOrEmpty()) {
                mineViewModel.showRecyclerViewEmptyEvent()
                mAdapter.setEmptyView(
                    com.yang.lib_common.R.layout.view_empty_data,
                    mViewBinding.recyclerView
                )
            } else {
                val list = mutableListOf<String>()
                it.forEach { item ->
                    val listItem = item.resourceUrls?.fromJson<MutableList<String>>()
                    listItem?.let {
                        list.addAll(listItem)
                    }
                }
                mAdapter.replaceData(list)
            }
        }

        mineViewModel.uC.requestFailEvent.observe(this) {
            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.ERROR
        }


        LiveDataBus.instance.with(AppConstant.Constant.REFRESH_USER_INFO).observe(this) {

            initUserInfo(UserInfoHold.userInfo)
        }

        mineViewModel.pictureListLiveData.observe(this) {
            url = it[0]
            val userInfoData = "{}".fromJson<UserInfoData>().apply {
                id = UserInfoHold.userId
                userInfoBg = url
            }
            mineViewModel.updateUserInfo(userInfoData, false)
        }
        mineViewModel.mUserInfoData.observe(this) {
            initUserInfo(it)
        }
    }


}