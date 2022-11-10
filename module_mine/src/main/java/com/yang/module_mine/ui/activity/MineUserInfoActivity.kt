package com.yang.module_mine.ui.activity


import android.text.Html
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjq.shape.view.ShapeImageView
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.CommonToolBar
import com.yang.lib_common.widget.ErrorReLoadView
import com.yang.module_mine.R
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

    private var sexArray = arrayOf("玉树临风大帅哥~", "可可爱爱小仙女~")

    private var sexIconArray = arrayOf(com.yang.lib_common.R.drawable.iv_man, com.yang.lib_common.R.drawable.iv_woman)

    private lateinit var mAdapter: BaseQuickAdapter<WallpaperData, BaseViewHolder>


    override fun initViewBinding(): ActMineUserInfoBinding {
        return bind(ActMineUserInfoBinding::inflate)
    }

    override fun initData() {
        mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.LOADING
        mineViewModel.getWallpaper()
        val id = intent.getStringExtra(AppConstant.Constant.ID)
        UserInfoHold.userInfo?.apply {
            if (userImage.isNullOrEmpty()){
                mViewBinding.sivImg.loadImage(this@MineUserInfoActivity, com.yang.lib_common.R.drawable.iv_attr)
            }else{
                mViewBinding.sivImg.loadCircle(this@MineUserInfoActivity,userImage)
            }
            if (!TextUtils.equals(id, this.id)) {
                mViewBinding.commonToolBar.rightContentVisible = false
            }
            mViewBinding.tvName.text = userName
            mViewBinding.tvAccount.text = "账号：${userAccount}"
            if (userVipLevel == 0){
                mViewBinding.tvVipLevel.text = "暂未开通会员"
                mViewBinding.tvVipLevel.setTextColor(getColor(com.yang.lib_common.R.color.textColor_999999))
            }else{
                mViewBinding.tvVipLevel.text = Html.fromHtml(String.format(getString(com.yang.lib_common.R.string.string_vip_level), userVipLevel),Html.FROM_HTML_OPTION_USE_CSS_COLORS)
            }
            mViewBinding.tvFanAttentionNum.text = Html.fromHtml(String.format(getString(com.yang.lib_common.R.string.string_fans_attention_num), userFan,userAttention),Html.FROM_HTML_OPTION_USE_CSS_COLORS)
            mViewBinding.tvInfo.text = sexArray[userSex]
            mViewBinding.ivSex.setImageResource(sexIconArray[userSex])
            mViewBinding.tvDesc.text = userDescribe?:"人生在世总要留点什么吧..."
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
            openGallery(1,{
                it?.let {
                    if (it.isNotEmpty()){
                        mViewBinding.ivBg.imageUrl = it[0].realPath.toString()
                    }
                }

            })
        }

        mViewBinding.errorReLoadView.onClick = {
            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.LOADING
            mineViewModel.getWallpaper()
        }

        initRecyclerView()
    }

    private fun initRecyclerView(){
        mAdapter = object : BaseQuickAdapter<WallpaperData, BaseViewHolder>(R.layout.item_collection_image) {
            override fun convert(helper: BaseViewHolder, item: WallpaperData) {
                val imageView = helper.getView<ShapeImageView>(R.id.iv_image)
                loadSpaceRadius(mContext,item.imageUrl,20f,imageView,3,30f)
                helper.setText(R.id.tv_title,item.title)
                    .setText(R.id.tv_like_num,"${item.likeNum}")
                    .setText(R.id.stv_vip, if (item.isVip) "原创" else "平台")
            }
        }
        mViewBinding.recyclerView.adapter = mAdapter
    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mineViewModel.uC
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)

        mineViewModel.mWallpaperData.observe(this){
            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.NORMAL
            if (it.isNullOrEmpty()) {
                mineViewModel.showRecyclerViewEmptyEvent()
                mAdapter.setEmptyView(com.yang.lib_common.R.layout.view_empty_data)
            } else {
                mAdapter.replaceData(it)

            }
        }

        mineViewModel.uC.requestFailEvent.observe(this){
            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.ERROR
        }
    }







}