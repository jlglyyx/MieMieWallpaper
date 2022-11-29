package com.yang.module_main.ui.fragment

import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjq.shape.view.ShapeImageView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.*
import com.yang.module_main.R
import com.yang.module_main.databinding.FraSearchWallpaperBinding
import com.yang.module_main.viewmodel.WallpaperViewModel


/**
 * @ClassName: SearchUserFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.SEARCH_USER_FRAGMENT)
class SearchUserFragment : BaseLazyFragment<FraSearchWallpaperBinding>(),
    OnRefreshLoadMoreListener {

    @InjectViewModel
    lateinit var wallpaperViewModel: WallpaperViewModel

    private lateinit var mAdapter: BaseQuickAdapter<UserInfoData, BaseViewHolder>

    override fun initViewBinding(): FraSearchWallpaperBinding {
        return bind(FraSearchWallpaperBinding::inflate)
    }

    override fun initView() {
        wallpaperViewModel.wallType =
            arguments?.getInt(AppConstant.Constant.WALL_TYPE) ?: wallpaperViewModel.wallType
        initRecyclerView()
        mViewBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(this)
        registerRefreshAndRecyclerView(mViewBinding.smartRefreshLayout, mAdapter)
    }

    override fun initData() {
        LiveDataBus.instance.with(AppConstant.Constant.KEYWORD).observe(this) {
            wallpaperViewModel.keyword = it.toString()
            onRefresh(mViewBinding.smartRefreshLayout)
        }
    }

    private fun initRecyclerView() {

        mAdapter = object : BaseQuickAdapter<UserInfoData, BaseViewHolder>(R.layout.item_fans) {
            override fun convert(helper: BaseViewHolder, item: UserInfoData) {
                val imageView = helper.getView<ImageView>(R.id.iv_image)
                imageView.loadCircle(mContext, item.userAttr)
                helper.setText(R.id.tv_name, item.userName)
                    .setText(R.id.tv_desc, "粉丝：${item.userFan} 关注：${item.userAttention}")
//                val imageView = helper.getView<ShapeImageView>(R.id.iv_image)
//                loadSpaceRadius(mContext,item.wallUrl,20f,imageView,3,30f)
//                helper.setText(R.id.tv_title,item.wallName)
//                    .setText(R.id.tv_like_num,"${item.likeNum}")
//                    .setText(R.id.stv_vip, if (item.isVip) "原创" else "平台")
            }
        }
        mViewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
        mAdapter.notifyDataSetChanged()
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
//                buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
//                    .withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(requireContext(), com.yang.lib_common.R.anim.fade_in, com.yang.lib_common.R.anim.fade_out))
//                    .withString(AppConstant.Constant.DATA,mAdapter.data.toJson())
//                    .withInt(AppConstant.Constant.INDEX,position)
//                    .withString(AppConstant.Constant.KEYWORD,wallpaperViewModel.keyword)
//                    .withInt(AppConstant.Constant.PAGE_NUMBER,wallpaperViewModel.pageNum)
//                    .navigation()

            }
        }

    }


    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
        wallpaperViewModel.mSearchUserInfoData.observe(this) {
            mViewBinding.smartRefreshLayout.smartRefreshLayoutData(it, mAdapter, wallpaperViewModel)
        }

    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return wallpaperViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        wallpaperViewModel.pageNum = 1
        wallpaperViewModel.searchUser()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        wallpaperViewModel.pageNum++
        wallpaperViewModel.searchUser()

    }
}