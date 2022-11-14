package com.yang.module_mine.ui.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
 * @ClassName: MainItemFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.MINE_FANS_FRAGMENT)
class MyFansFragment : BaseLazyFragment<FraMyFansBinding>() ,OnRefreshLoadMoreListener{

    @InjectViewModel
    lateinit var mineViewModel: MineViewModel

    private lateinit var mAdapter: BaseQuickAdapter<String, BaseViewHolder>

    private var fanType = 0

    override fun initViewBinding(): FraMyFansBinding {
        return bind(FraMyFansBinding::inflate)
    }

    override fun initView() {
        initRecyclerView()
        mViewBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(this)
        registerRefreshAndRecyclerView(mViewBinding.smartRefreshLayout,mAdapter)
    }

    override fun initData() {
        fanType = arguments?.getInt(AppConstant.Constant.TYPE) ?:fanType
        mViewBinding.smartRefreshLayout.autoRefresh()
    }

    private fun initRecyclerView() {

        mAdapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_fans) {
            @SuppressLint("ClickableViewAccessibility")
            override fun convert(helper: BaseViewHolder, item: String) {
                val imageView = helper.getView<ImageView>(R.id.iv_image)
                imageView.loadCircle(mContext,"https://img1.baidu.com/it/u=1924711271,453761707&fm=253&fmt=auto&app=138&f=JPG?w=500&h=500")
                helper.setText(R.id.tv_desc,"粉丝：110 关注：20002")
//                    .setText(R.id.tv_like_num,"${item.likeNum}")
//                    .setText(R.id.stv_vip, if (item.isVip) "原创" else "平台")
            }
        }
        mViewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mViewBinding.recyclerView.adapter = mAdapter

        mAdapter.setNewData(mutableListOf<String>().apply {
            add("")
            add("")
            add("")
            add("")
            add("")
            add("")
            add("")
            add("")
            add("")
            add("")
            add("")
        })

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
//        wallpaperViewModel.mWallpaperData.observe(this){
//            mViewBinding.smartRefreshLayout.smartRefreshLayoutData(it,mAdapter,wallpaperViewModel)
//        }

    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return mineViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        mineViewModel.pageNum = 1
//        wallpaperViewModel.getWallpaper()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        mineViewModel.pageNum++
//        wallpaperViewModel.getWallpaper()

    }
}