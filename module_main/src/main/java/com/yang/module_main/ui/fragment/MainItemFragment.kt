package com.yang.module_main.ui.fragment

import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.MBannerAdapter
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.BannerBean
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.module_main.R
import com.yang.module_main.data.WallpaperData
import com.yang.module_main.data.WallpaperTopData
import com.yang.module_main.databinding.FraMainItemBinding
import com.yang.module_main.ui.dialog.FilterDialog
import com.yang.module_main.viewmodel.MainViewModel
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * @ClassName: MainItemFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.MAIN_ITEM_FRAGMENT)
class MainItemFragment : BaseLazyFragment<FraMainItemBinding>() ,OnRefreshLoadMoreListener{

    @InjectViewModel
    lateinit var mainViewModel: MainViewModel

    private lateinit var mAdapter: BaseQuickAdapter<WallpaperData, BaseViewHolder>
    private lateinit var mTopAdapter: BaseQuickAdapter<WallpaperTopData, BaseViewHolder>
    private var tabIndex = 0
    private var tabId = ""

    override fun initViewBinding(): FraMainItemBinding {
        return bind(FraMainItemBinding::inflate)
    }

    override fun initView() {
        tabIndex = arguments?.getInt(AppConstant.Constant.TYPE)?:0
        tabId = arguments?.getString(AppConstant.Constant.ID,"")?:""
//        initBanner()
        initRecyclerView()
        mViewBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(this)
        registerRefreshAndRecyclerView(mViewBinding.smartRefreshLayout,mAdapter)

        mViewBinding.llSort.setOnClickListener {
            XPopup.Builder(requireContext())
                .atView(mViewBinding.llSort)
                .hasShadowBg(true) // 去掉半透明背景
                .popupPosition(PopupPosition.Bottom)
                .asCustom(FilterDialog(requireContext()).apply {
                    block = {
                        it.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val mAdapter = object :
                            BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_filter_sort) {
                            override fun convert(helper: BaseViewHolder, item: String) {
                                helper.setText(R.id.tv_title,item)

                            }
                        }
                        it.recyclerView.adapter = mAdapter
                        mAdapter.setNewData(mutableListOf<String>().apply {
                            add("最新上传")
                            add("最多下载")
                        })
                        mAdapter.setOnItemClickListener { _, _, position ->
                            val item = mAdapter.getItem(position)
                            mViewBinding.tvTitle.text = item
                            mainViewModel.pageNum = 1
                            mainViewModel.order = position
                            mainViewModel.getWallpaper(tabId)
                            dismiss()
                        }
                    }
                }).show()
        }
    }

    override fun initData() {
        onRefresh(mViewBinding.smartRefreshLayout)
    }

    private fun initBanner() {
        mViewBinding.banner.addBannerLifecycleObserver(this)
            .setAdapter(MBannerAdapter(mutableListOf<BannerBean>().apply {
                add(BannerBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57"))
                add(BannerBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201607%2F29%2F20160729224352_rVhZA.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=326f6993d0c1ee5c0048b33fe9f0a6dc"))
                add(BannerBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F15%2F20150815231707_JWQjx.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=e3951129e167261b3f4c7c739becc463"))
            }), true)
            .addBannerLifecycleObserver(this)
            .isAutoLoop(true)
            .setBannerRound2(20f)
            .setIndicatorGravity(IndicatorConfig.Direction.LEFT)
            .indicator = CircleIndicator(requireContext())
    }

    private fun initRecyclerView() {

        mAdapter = object : BaseQuickAdapter<WallpaperData, BaseViewHolder>(R.layout.item_image) {
            override fun convert(helper: BaseViewHolder, item: WallpaperData) {
                loadRadius(mContext,AppConstant.ClientInfo.IMAGE_MODULE+item.imageUrl,20f,helper.getView(R.id.iv_image))
            }
        }

        mViewBinding.recyclerView.adapter = mAdapter

        mTopAdapter = object :
            BaseQuickAdapter<WallpaperTopData, BaseViewHolder>(if (tabIndex == 0) R.layout.item_top_image else R.layout.item_top_type_image) {
            override fun convert(helper: BaseViewHolder, item: WallpaperTopData) {
                if (tabIndex == 0){
                    loadCircle(mContext,item.url,helper.getView(R.id.iv_image))
                }else{
                    loadRadius(mContext,item.url,5f,helper.getView(R.id.iv_image))
                }
            }
        }
        mViewBinding.topRecyclerView.adapter = mTopAdapter


        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
                    .withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(requireContext(), com.yang.lib_common.R.anim.fade_in, com.yang.lib_common.R.anim.fade_out))
                    .withString(AppConstant.Constant.DATA,mAdapter.data.toJson())
                    .withInt(AppConstant.Constant.ORDER,mainViewModel.order)
                    .withInt(AppConstant.Constant.INDEX,position)
                    .withInt(AppConstant.Constant.PAGE_NUMBER,mainViewModel.pageNum)
                    .navigation()
            }
        }
        mTopAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                WallpaperUtil.setWallpaper(requireContext(), "")
            }
        }

        mTopAdapter.setNewData(mutableListOf<WallpaperTopData>().apply {
            add(WallpaperTopData("AR壁纸","https://pic1.zhimg.com/v2-c8abae935169a75f5efce1c9230554d9_r.jpg?source=1940ef5c"))
            add(WallpaperTopData("AR壁纸","http://bizihu.com/data/12031020.jpg"))
            add(WallpaperTopData("AR壁纸","https://pic2.zhimg.com/v2-f783660f6bd3e2875dda9ad7874cd834_r.jpg?source=1940ef5c"))
        })
    }


    override fun initViewModel() {

        InjectViewModelProxy.inject(this)

        mainViewModel.mWallpaperData.observe(this){
            when {
                mViewBinding.smartRefreshLayout.isRefreshing -> {
                    mainViewModel.uC.refreshEvent.call()
                    if (it.isNullOrEmpty()) {
                        mainViewModel.showRecyclerViewEmptyEvent()
                    } else {
                        mAdapter.replaceData(it)
                        mViewBinding.smartRefreshLayout.setNoMoreData(false)
                    }
                }
                mViewBinding.smartRefreshLayout.isLoading -> {
                    mainViewModel.uC.loadMoreEvent.call()
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
                        mainViewModel.showRecyclerViewEmptyEvent()
                    } else {
                        mAdapter.replaceData(it)
                        mViewBinding.smartRefreshLayout.setNoMoreData(false)
                    }
                }
            }
        }

    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return mainViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        mainViewModel.pageNum = 1
        mainViewModel.getWallpaper(tabId)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        mainViewModel.pageNum++
        mainViewModel.getWallpaper(tabId)

    }
}