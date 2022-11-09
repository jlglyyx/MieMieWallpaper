package com.yang.module_main.ui.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hjq.shape.view.ShapeTextView
import com.lxj.xpopup.XPopup
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.MBannerAdapter
import com.yang.lib_common.adapter.TabAndViewPagerFragmentAdapter
import com.yang.lib_common.base.ui.fragment.BaseFragment
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.BannerBean
import com.yang.lib_common.databinding.ViewCustomTabBinding
import com.yang.lib_common.databinding.ViewCustomTopTabBinding
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.showShort
import com.yang.lib_common.widget.ErrorReLoadView
import com.yang.module_main.R
import com.yang.module_main.databinding.FraMainBinding
import com.yang.module_main.ui.dialog.FilterDialog
import com.yang.module_main.viewmodel.MainViewModel
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator

/**
 * @ClassName: MainFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.MAIN_FRAGMENT)
class MainFragment : BaseFragment<FraMainBinding>() {

    @InjectViewModel(true)
    lateinit var mainViewModel: MainViewModel

    private val tabTextSize = 16f
    private val tabSelectTextSize = 17f

    private  var mTitles = mutableListOf<String>()

    private var mFragments = mutableListOf<Fragment>()

    override fun initViewBinding(): FraMainBinding {
        return bind(FraMainBinding::inflate)
    }

    override fun initView() {


        mViewBinding.commonToolBar.etSearch.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.SEARCH_WALLPAPER_ACTIVITY).navigation()
        }

        mViewBinding.llMore.setOnClickListener {

            XPopup.Builder(requireContext())
                .atView(mViewBinding.tabLayout)
                .asCustom(FilterDialog(requireContext()).apply {
                    block = {
                        it.sllContainer.shapeDrawableBuilder.setSolidColor(
                            ContextCompat.getColor(
                                requireContext(),
                                com.yang.lib_common.R.color.color_F3F4F6
                            )
                        ).intoBackground()
                        it.recyclerView.layoutManager = GridLayoutManager(requireContext(), 5)
                        val mAdapter = object :
                            BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_filter_tab) {
                            override fun convert(helper: BaseViewHolder, item: String) {
                                helper.setText(R.id.tv_title, item)
                                val tvTitle = helper.getView<ShapeTextView>(R.id.tv_title)
                                if (helper.absoluteAdapterPosition == mViewBinding.tabLayout.selectedTabPosition){
                                    tvTitle.shapeDrawableBuilder.setSolidColor(requireContext().getColor(
                                        com.yang.lib_common.R.color.appColor)).intoBackground()
                                    tvTitle.setTextColor(requireContext().getColor(
                                        com.yang.lib_common.R.color.white))
                                }else{
                                    tvTitle.shapeDrawableBuilder.setSolidColor(requireContext().getColor(
                                        com.yang.lib_common.R.color.white)).intoBackground()
                                    tvTitle.setTextColor(requireContext().getColor(
                                        com.yang.lib_common.R.color.textColor_666666))
                                }
                            }
                        }
                        mAdapter.setOnItemClickListener { _, _, position ->
                            mViewBinding.viewPager.setCurrentItem(position, false)
                            dismiss()
                        }
                        it.recyclerView.adapter = mAdapter
                        mAdapter.setNewData(mTitles)
                    }
                }).show()
        }

        mViewBinding.errorReLoadView.onClick = {
            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.LOADING
            mainViewModel.getTabs()
        }

    }

    override fun initData() {
        mainViewModel.wallType = arguments?.getInt(AppConstant.Constant.WALL_TYPE)?:mainViewModel.wallType
        mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.LOADING
        mainViewModel.getTabs()
    }


    private fun initViewPager() {

        mViewBinding.viewPager.adapter = TabAndViewPagerFragmentAdapter(this, mFragments, mTitles)
        mViewBinding.viewPager.offscreenPageLimit = mFragments.size

        val view: View = mViewBinding.viewPager.getChildAt(0)
        if (view is RecyclerView) {
            view.overScrollMode = View.OVER_SCROLL_NEVER
        }
    }

    private fun initTabLayout() {

        TabLayoutMediator(
            mViewBinding.tabLayout, mViewBinding.viewPager
        ) { tab, position ->
            tab.text = mTitles[position]
            val tabView = ViewCustomTopTabBinding.inflate(LayoutInflater.from(requireContext()))
            tabView.tvTitle.text = mTitles[position]
            tab.customView = tabView.root
            if (position == 0) {
                tabView.tvTitle.setTextColor(requireContext().getColor(com.yang.lib_common.R.color.appColor))
                tabView.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,tabSelectTextSize)
            } else {
                tabView.tvTitle.setTextColor(requireContext().getColor(com.yang.lib_common.R.color.textColor_666666))
                tabView.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,tabTextSize)
            }
            tab.view.setOnLongClickListener { true }
        }.attach()


        mViewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val customView = tab.customView
                customView?.apply {
                    val tvTitle = findViewById<TextView>(com.yang.lib_common.R.id.tv_title)
                    tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,tabTextSize)
                    tvTitle.setTextColor(requireContext().getColor(com.yang.lib_common.R.color.textColor_666666))
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val customView = tab.customView
                customView?.apply {
                    val tvTitle = findViewById<TextView>(com.yang.lib_common.R.id.tv_title)
                    tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,tabSelectTextSize)
                    tvTitle.setTextColor(requireContext().getColor(com.yang.lib_common.R.color.appColor))
                }
            }

        })
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
        mainViewModel.mWallpaperTabData.observe(this) {
            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.NORMAL
            it.mapIndexed { index, wallpaperTabData ->
                mFragments.add(
                    buildARouter(AppConstant.RoutePath.MAIN_ITEM_FRAGMENT)
                        .withInt(AppConstant.Constant.TYPE, index)
                        .withString(AppConstant.Constant.ID, wallpaperTabData.id)
                        .withInt(AppConstant.Constant.WALL_TYPE, mainViewModel.wallType)
                        .navigation() as Fragment
                )

                // TODO:  删除
                mFragments.add(
                    buildARouter(AppConstant.RoutePath.MAIN_ITEM_FRAGMENT)
                        .withInt(AppConstant.Constant.TYPE, index)
                        .withString(AppConstant.Constant.ID, wallpaperTabData.id)
                        .withInt(AppConstant.Constant.WALL_TYPE, mainViewModel.wallType)
                        .navigation() as Fragment
                )
                wallpaperTabData.name
            }.apply {
                mTitles.addAll(this as MutableList<String>)
                // TODO:  删除
                mTitles.addAll(this as MutableList<String>)
            }
            initViewPager()
            initTabLayout()
        }
        mainViewModel.uC.requestFailEvent.observe(this){
            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.ERROR
        }

    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mainViewModel.uC
    }
}