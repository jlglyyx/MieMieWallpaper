package com.yang.module_mine.ui.fragment

import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjq.shape.view.ShapeImageView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.loadSpaceRadius
import com.yang.lib_common.util.smartRefreshLayoutData
import com.yang.module_mine.R
import com.yang.module_mine.databinding.FraMineFansBinding
import com.yang.module_mine.viewmodel.MineViewModel


/**
 * @ClassName: MineCollectionFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.MINE_DOWN_FRAGMENT)
class MineDownFragment : BaseLazyFragment<FraMineFansBinding>(), OnRefreshListener {


    @InjectViewModel
    lateinit var mineViewModel: MineViewModel

    private lateinit var mAdapter: BaseQuickAdapter<WallpaperData, BaseViewHolder>

    override fun initViewBinding(): FraMineFansBinding {
        return bind(FraMineFansBinding::inflate)
    }

    override fun initView() {
        initRecyclerView()
        mViewBinding.smartRefreshLayout.setOnRefreshListener(this)
        mViewBinding.smartRefreshLayout.setEnableLoadMore(false)
        registerRefreshAndRecyclerView(mViewBinding.smartRefreshLayout, mAdapter)
    }

    override fun initData() {
        mViewBinding.smartRefreshLayout.autoRefresh()
    }

    private fun initRecyclerView() {

        mAdapter = object :
            BaseQuickAdapter<WallpaperData, BaseViewHolder>(R.layout.item_down_image) {
            override fun convert(helper: BaseViewHolder, item: WallpaperData) {
                val imageView = helper.getView<ShapeImageView>(R.id.iv_image)
                loadSpaceRadius(mContext, item.wallUrl, 10f, imageView, 4, 30f)
            }
        }
        mViewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(mContext).resumeRequests()
                } else {
                    Glide.with(mContext).pauseRequests()
                }
            }
        })
        mViewBinding.recyclerView.adapter = mAdapter

        mAdapter.setOnItemLongClickListener { adapter, view, position ->
            mAdapter.remove(position)
            mAdapter.notifyItemRemoved(position)
            return@setOnItemLongClickListener true
        }

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
        mineViewModel.mDownWallpaperData.observe(this){
            mViewBinding.smartRefreshLayout.smartRefreshLayoutData(it,mAdapter,mineViewModel)
        }

    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return mineViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mineViewModel.getDownWallpaper()
    }


}