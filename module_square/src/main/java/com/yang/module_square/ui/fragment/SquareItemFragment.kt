package com.yang.module_square.ui.fragment

import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
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
import com.yang.lib_common.widget.GridNinePictureView
import com.yang.module_square.R
import com.yang.module_square.databinding.FraSquareItemBinding
import com.yang.module_square.viewmodel.SquareViewModel


/**
 * @ClassName: MainItemFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.SQUARE_ITEM_FRAGMENT)
class SquareItemFragment : BaseLazyFragment<FraSquareItemBinding>(), OnRefreshLoadMoreListener {

    @InjectViewModel
    lateinit var squareViewModel: SquareViewModel


    private lateinit var mAdapter: BaseQuickAdapter<WallpaperData, BaseViewHolder>

    private var tabIndex = 0
    private var tabId = ""

    override fun initViewBinding(): FraSquareItemBinding {
        return bind(FraSquareItemBinding::inflate)
    }

    override fun initView() {
        tabIndex = arguments?.getInt(AppConstant.Constant.TYPE) ?: 0
        tabId = arguments?.getString(AppConstant.Constant.ID, "") ?: ""
        squareViewModel.wallType =
            arguments?.getInt(AppConstant.Constant.WALL_TYPE) ?: squareViewModel.wallType
//        initBanner()
        initRecyclerView()
        mViewBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(this)
        registerRefreshAndRecyclerView(mViewBinding.smartRefreshLayout, mAdapter)


    }

    override fun initData() {
        onRefresh(mViewBinding.smartRefreshLayout)
    }


    private fun initRecyclerView() {

        mAdapter = object : BaseQuickAdapter<WallpaperData, BaseViewHolder>(R.layout.item_dynamic) {
            override fun convert(helper: BaseViewHolder, item: WallpaperData) {
                helper.setGone(R.id.view_top, helper.bindingAdapterPosition == 0)
                val gridNinePictureView = helper.getView<GridNinePictureView>(R.id.gridNinePictureView)
                gridNinePictureView.data = mutableListOf<String>().apply {
                    for (i in 0..10){
                        add(getRealUrl(item.imageUrl).toString())
                    }

                }

                val imageView = helper.getView<ImageView>(R.id.iv_image)
                imageView.loadCircle(mContext,getRealUrl(item.imageUrl).toString())
//                loadSpaceRadius(mContext, item.imageUrl, 10f, imageView, 2, 10f)
//                helper.setText(R.id.tv_title,item.title)
//                .setText(R.id.tv_like_num,"${item.likeNum}")
//                    .setText(R.id.stv_vip, if (item.isVip) "原创" else "平台")
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



        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
//            item?.let {
//                buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
//                    .withOptionsCompat(
//                        ActivityOptionsCompat.makeCustomAnimation(
//                            requireContext(),
//                            com.yang.lib_common.R.anim.fade_in,
//                            com.yang.lib_common.R.anim.fade_out
//                        )
//                    )
//                    .withString(AppConstant.Constant.DATA, mAdapter.data.toJson())
//                    .withInt(AppConstant.Constant.ORDER, squareViewModel.order)
//                    .withInt(AppConstant.Constant.INDEX, position)
//                    .withInt(AppConstant.Constant.PAGE_NUMBER, squareViewModel.pageNum)
//                    .navigation()
//            }
        }

    }


    override fun initViewModel() {

        InjectViewModelProxy.inject(this)

        squareViewModel.mWallpaperData.observe(this) {
            mViewBinding.smartRefreshLayout.smartRefreshLayoutData(it, mAdapter, squareViewModel)
        }

    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return squareViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        squareViewModel.pageNum = 1
        squareViewModel.getWallpaper(tabId)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        squareViewModel.pageNum++
        squareViewModel.getWallpaper(tabId)

    }
}