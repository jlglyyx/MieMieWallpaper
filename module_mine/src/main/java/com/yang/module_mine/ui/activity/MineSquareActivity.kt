package com.yang.module_mine.ui.activity

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.WallpaperDynamicAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.ImageTintView
import com.yang.module_mine.R
import com.yang.module_mine.databinding.ActMineSquareBinding
import com.yang.module_mine.viewmodel.MineViewModel
import java.util.*

/**
 * @ClassName: MineSquareActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/16 15:49
 */
@Route(path = AppConstant.RoutePath.MINE_SQUARE_ACTIVITY)
class MineSquareActivity : BaseActivity<ActMineSquareBinding>() , OnRefreshLoadMoreListener {
    @InjectViewModel
    lateinit var mineViewModel: MineViewModel


    private lateinit var mAdapter: WallpaperDynamicAdapter

    private var userId = ""


    override fun initViewBinding(): ActMineSquareBinding {
        return bind(ActMineSquareBinding::inflate)
    }

    override fun initView() {

        initRecyclerView()
        mViewBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(this)
        registerRefreshAndRecyclerView(mViewBinding.smartRefreshLayout, mAdapter)


    }

    override fun initData() {

        userId = intent.getStringExtra(AppConstant.Constant.USER_ID)?:userId
        UserInfoHold.userInfo?.apply {
            if (!TextUtils.equals(id, userId)) {
                mViewBinding.commonToolBar.centerContent = "更多作品"
            }
        }

        mViewBinding.smartRefreshLayout.autoRefresh()


        mineViewModel.mWallpaperDynamicData.observe(this) {

            mViewBinding.smartRefreshLayout.smartRefreshLayoutData(it, mAdapter, mineViewModel)
        }
    }


    private fun initRecyclerView() {

        mAdapter = WallpaperDynamicAdapter()
        mViewBinding.recyclerView.itemAnimator = null
        mViewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(this@MineSquareActivity).resumeRequests()
                } else {
                    Glide.with(this@MineSquareActivity).pauseRequests()
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




    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return mineViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        mineViewModel.pageNum = 1
        mineViewModel.getDynamicList(userId)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        mineViewModel.pageNum++
        mineViewModel.getDynamicList(userId)

    }
}