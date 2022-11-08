package com.yang.module_mine.ui.activity


import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
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


    private lateinit var mAdapter: BaseQuickAdapter<WallpaperData, BaseViewHolder>


    override fun initViewBinding(): ActMineUserInfoBinding {
        return bind(ActMineUserInfoBinding::inflate)
    }

    override fun initData() {
//        mineViewModel.getWallpaper()
        val id = intent.getStringExtra(AppConstant.Constant.ID)
        UserInfoHold.userInfo.let {
            Glide.with(this).load(
                it?.userImage
                    ?: "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic%2F39%2Fb7%2F53%2F39b75357f98675e2d6d5dcde1fb805a3.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1642840086&t=2a7574a5d8ecc96669ac3e050fe4fd8e"
            ).error(com.yang.lib_common.R.drawable.iv_image_error)
                .placeholder(com.yang.lib_common.R.drawable.iv_image_placeholder).into(mViewBinding.sivImg)
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
            openGallery(1,{
                it?.let {
                    if (it.isNotEmpty()){
                        mViewBinding.ivBg.imageUrl = it[0].realPath.toString()
                    }
                }

            })
        }

//        initRecyclerView()
//        showRecyclerViewEvent(mAdapter)
    }

    private fun initRecyclerView(){
        mAdapter = object : BaseQuickAdapter<WallpaperData, BaseViewHolder>(R.layout.item_collection_image) {
            override fun convert(helper: BaseViewHolder, item: WallpaperData) {
                val imageView = helper.getView<ShapeImageView>(R.id.iv_image)
                imageView.shapeDrawableBuilder.setSolidColor(getRandomColor()).intoBackground()
                loadSpaceRadius(mContext,item.imageUrl,20f,helper.getView(R.id.iv_image),3,30f)
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
            if (it.isNullOrEmpty()) {
                mineViewModel.showRecyclerViewEmptyEvent()
            } else {
                mAdapter.replaceData(it)

            }
        }
    }







}