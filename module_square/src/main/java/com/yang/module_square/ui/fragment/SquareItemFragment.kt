package com.yang.module_square.ui.fragment

import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.WallpaperDynamicAdapter
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.smartRefreshLayoutData
import com.yang.lib_common.util.toJson
import com.yang.lib_common.widget.ImageTintView
import com.yang.module_square.databinding.FraSquareItemBinding
import com.yang.module_square.viewmodel.SquareViewModel
import java.util.*


/**
 * @ClassName: SquareItemFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.SQUARE_ITEM_FRAGMENT)
class SquareItemFragment : BaseLazyFragment<FraSquareItemBinding>(), OnRefreshLoadMoreListener {

    @InjectViewModel
    lateinit var squareViewModel: SquareViewModel


    private lateinit var mAdapter: WallpaperDynamicAdapter

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
        LiveDataBus.instance.with(AppConstant.Constant.KEYWORD).observe(this){
            squareViewModel.keyword = it.toString()
            onRefresh(mViewBinding.smartRefreshLayout)
        }
        onRefresh(mViewBinding.smartRefreshLayout)

    }


    private fun initRecyclerView() {

        mAdapter = WallpaperDynamicAdapter()
        mViewBinding.recyclerView.itemAnimator = null
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

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                when(view.id){
                    com.yang.lib_common.R.id.iv_image,com.yang.lib_common.R.id.tv_name ->{
                        buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                            AppConstant.Constant.USER_ID,
                            it.userId
                        ).navigation()
                    }
                    com.yang.lib_common.R.id.tv_attention ->{
                        it.isAttention = !it.isAttention
                        mAdapter.notifyItemChanged(position)

                    }
                    com.yang.lib_common.R.id.tv_content ->{
                        buildARouter(AppConstant.RoutePath.DYNAMIC_DETAIL_ACTIVITY)
                            .withBoolean(AppConstant.Constant.OPEN_COMMENT, true)
                            .withString(AppConstant.Constant.ID, it.id)
                            .navigation()
                    }
                    com.yang.lib_common.R.id.ll_fabulous ->{
                        val itvFabulous = view.findViewById<ImageTintView>(com.yang.lib_common.R.id.itv_fabulous)
                        itvFabulous.tintClick = !itvFabulous.tintClick
                    }
                    com.yang.lib_common.R.id.ll_forward ->{


                    }
                }

            }

        }



        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                buildARouter(AppConstant.RoutePath.DYNAMIC_DETAIL_ACTIVITY)
                    .withBoolean(AppConstant.Constant.OPEN_COMMENT, false)
                    .withString(AppConstant.Constant.ID, it.id)
                    .navigation()

            }
        }

    }


    override fun initViewModel() {

        InjectViewModelProxy.inject(this)


        squareViewModel.mWallpaperDynamicData.observe(this){
            mViewBinding.smartRefreshLayout.smartRefreshLayoutData(it,mAdapter,squareViewModel)
        }


    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return squareViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        squareViewModel.pageNum = 1
        squareViewModel.getDynamicList()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        squareViewModel.pageNum++
        squareViewModel.getDynamicList()

    }
}