package com.yang.module_mine.ui.fragment

import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjq.shape.view.ShapeImageView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.module_mine.R
import com.yang.module_mine.databinding.FraMyFansBinding
import com.yang.module_mine.viewmodel.MineViewModel


/**
 * @ClassName: MyCollectionFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.MINE_COLLECTION_FRAGMENT)
class MyCollectionFragment : BaseLazyFragment<FraMyFansBinding>(), OnRefreshLoadMoreListener {


    @InjectViewModel
    lateinit var mineViewModel: MineViewModel

    private lateinit var mAdapter: BaseQuickAdapter<WallpaperData, BaseViewHolder>

    override fun initViewBinding(): FraMyFansBinding {
        return bind(FraMyFansBinding::inflate)
    }

    override fun initView() {
        initRecyclerView()
        mViewBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(this)
        registerRefreshAndRecyclerView(mViewBinding.smartRefreshLayout, mAdapter)
    }

    override fun initData() {

        onRefresh(mViewBinding.smartRefreshLayout)

    }

    private fun initRecyclerView() {

        mAdapter = object :
            BaseQuickAdapter<WallpaperData, BaseViewHolder>(R.layout.item_collection_image) {
            override fun convert(helper: BaseViewHolder, item: WallpaperData) {
                val imageView = helper.getView<ShapeImageView>(R.id.iv_image)
                imageView.shapeDrawableBuilder.setSolidColor(getRandomColor()).intoBackground()
                loadSpaceRadius(mContext, item.imageUrl, 10f, helper.getView(R.id.iv_image), 4, 30f)
                helper.setText(R.id.tv_title, item.title)
                    .setText(R.id.tv_like_num, "${item.likeNum}")
                    .setText(R.id.stv_vip, if (item.isVip) "原创" else "平台")
            }
        }
        mViewBinding.recyclerView.adapter = mAdapter


        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
//                buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
//                    .withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(requireContext(), com.yang.lib_common.R.anim.fade_in, com.yang.lib_common.R.anim.fade_out))
//                    .withString(AppConstant.Constant.DATA,mAdapter.data.toJson())
//                    .withInt(AppConstant.Constant.INDEX,position)
//                    .withInt(AppConstant.Constant.PAGE_NUMBER,wallpaperViewModel.pageNum)
//                    .navigation()
            }
        }

    }


    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
        mineViewModel.mCollectionWallpaperData.observe(this) {
            mViewBinding.smartRefreshLayout.smartRefreshLayoutData(it, mAdapter, mineViewModel)
        }

    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return mineViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        mineViewModel.pageNum = 1
        mineViewModel.getCollectionWallpaper()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        mineViewModel.pageNum++
        mineViewModel.getCollectionWallpaper()

    }
}