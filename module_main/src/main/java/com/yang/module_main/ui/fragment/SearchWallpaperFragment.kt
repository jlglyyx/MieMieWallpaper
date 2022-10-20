package com.yang.module_main.ui.fragment

import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.module_main.R
import com.yang.module_main.data.WallpaperData
import com.yang.module_main.databinding.FraMainItemBinding
import com.yang.module_main.databinding.FraSearchWallpaperBinding
import com.yang.module_main.viewmodel.WallpaperViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * @ClassName: MainItemFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.SEARCH_WALLPAPER_FRAGMENT)
class SearchWallpaperFragment : BaseLazyFragment<FraSearchWallpaperBinding>() ,OnRefreshLoadMoreListener{

    @InjectViewModel
    lateinit var wallpaperViewModel: WallpaperViewModel

    private lateinit var mAdapter: BaseQuickAdapter<WallpaperData, BaseViewHolder>

    override fun initViewBinding(): FraSearchWallpaperBinding {
        return bind(FraSearchWallpaperBinding::inflate)
    }

    override fun initView() {
        initRecyclerView()
        mViewBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(this)
        registerRefreshAndRecyclerView(mViewBinding.smartRefreshLayout,mAdapter)
    }

    override fun initData() {
        LiveDataBus.instance.with(AppConstant.Constant.KEYWORD).observe(this){
            wallpaperViewModel.keyword = it.toString()
            onRefresh(mViewBinding.smartRefreshLayout)
        }
    }

    private fun initRecyclerView() {

        mAdapter = object : BaseQuickAdapter<WallpaperData, BaseViewHolder>(R.layout.item_image) {
            override fun convert(helper: BaseViewHolder, item: WallpaperData) {
                loadRadius(mContext,AppConstant.ClientInfo.IMAGE_MODULE+item.imageUrl,20f,helper.getView(R.id.iv_image))
            }
        }
        mViewBinding.recyclerView.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
                    .withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(requireContext(), com.yang.lib_common.R.anim.fade_in, com.yang.lib_common.R.anim.fade_out))
                    .withString(AppConstant.Constant.DATA,mAdapter.data.toJson())
                    .withInt(AppConstant.Constant.INDEX,position)
                    .withInt(AppConstant.Constant.PAGE_NUMBER,wallpaperViewModel.pageNum)
                    .navigation()
            }
        }

    }


    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
        wallpaperViewModel.mWallpaperData.observe(this){
            when {
                mViewBinding.smartRefreshLayout.isRefreshing -> {
                    wallpaperViewModel.uC.refreshEvent.call()
                    if (it.isNullOrEmpty()) {
                        wallpaperViewModel.showRecyclerViewEmptyEvent()
                    } else {
                        mAdapter.replaceData(it)
                        mViewBinding.smartRefreshLayout.setNoMoreData(false)
                    }
                }
                mViewBinding.smartRefreshLayout.isLoading -> {
                    wallpaperViewModel.uC.loadMoreEvent.call()
                    if (it.isNullOrEmpty()) {
                        mViewBinding.smartRefreshLayout.setNoMoreData(true)
                    } else {
                        mViewBinding.smartRefreshLayout.setNoMoreData(false)
                        lifecycleScope.launch {
                            delay(500)
                            mAdapter.addData(it)
                        }
                    }
                }
                else -> {
                    if (it.isNullOrEmpty()) {
                        wallpaperViewModel.showRecyclerViewEmptyEvent()
                    } else {
                        mAdapter.replaceData(it)
                        mViewBinding.smartRefreshLayout.setNoMoreData(false)
                    }
                }
            }
        }

    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return wallpaperViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        wallpaperViewModel.pageNum = 1
        wallpaperViewModel.getWallpaper()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        wallpaperViewModel.pageNum++
        wallpaperViewModel.getWallpaper()

    }
}