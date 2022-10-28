package com.yang.module_main.ui.fragment

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lxj.xpopup.XPopup
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.MBannerAdapter
import com.yang.lib_common.adapter.TabAndViewPagerFragmentAdapter
import com.yang.lib_common.base.ui.fragment.BaseFragment
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.BannerBean
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.showShort
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


    private lateinit var mTitles : MutableList<String>

    private lateinit var mFragments: MutableList<Fragment>

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
                            it.sllContainer.shapeDrawableBuilder.setSolidColor(ContextCompat.getColor(requireContext(),
                                com.yang.lib_common.R.color.color_F3F4F6)).intoBackground()
                            it.recyclerView.layoutManager = GridLayoutManager(requireContext(), 5)
                            val mAdapter = object :
                                BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_filter_tab) {
                                override fun convert(helper: BaseViewHolder, item: String) {
                                    helper.setText(R.id.tv_title,item)

                                }
                            }
                            mAdapter.setOnItemClickListener { _, _, position ->
                                mViewBinding.viewPager.setCurrentItem(position,false)
                                dismiss()
                            }
                            it.recyclerView.adapter = mAdapter
                            mAdapter.setNewData(mTitles)
                        }
                    }).show()
        }

    }

    override fun initData() {
        mainViewModel.getTabs(0)
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
            tab.view.setOnLongClickListener { true }
        }.attach()


        mViewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {


            }

        })
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
        mainViewModel.mWallpaperTabData.observe(this){
            mFragments = mutableListOf()
            mTitles = mutableListOf()
            it.mapIndexed { index, wallpaperTabData ->
                mFragments.add( buildARouter(AppConstant.RoutePath.MAIN_ITEM_FRAGMENT)
                    .withInt(AppConstant.Constant.TYPE, index)
                    .withString(AppConstant.Constant.ID, wallpaperTabData.id)
                    .navigation() as Fragment)
                wallpaperTabData.name
            }.apply {
                mTitles.addAll(this as MutableList<String>)
            }
            initViewPager()
            initTabLayout()
        }


    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mainViewModel.uC
    }
}