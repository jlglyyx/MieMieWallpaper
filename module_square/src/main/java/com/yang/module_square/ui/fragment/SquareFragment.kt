package com.yang.module_square.ui.fragment

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lxj.xpopup.XPopup
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.TabAndViewPagerFragmentAdapter
import com.yang.lib_common.adapter.TabMoreAdapter
import com.yang.lib_common.base.ui.fragment.BaseFragment
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.databinding.ViewCustomTopTabBinding
import com.yang.lib_common.dialog.FilterDialog
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.lib_common.widget.ErrorReLoadView
import com.yang.module_square.databinding.FraSquareBinding
import com.yang.module_square.viewmodel.SquareViewModel

/**
 * @ClassName: MainFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.SQUARE_FRAGMENT)
class SquareFragment : BaseLazyFragment<FraSquareBinding>() {

    @InjectViewModel(true)
    lateinit var squareViewModel: SquareViewModel

    private val tabTextSize = 16f
    private val tabSelectTextSize = 17f

    private  var mTitles = mutableListOf<String>()

    private var mFragments = mutableListOf<Fragment>()

    private var mTabMoreAdapter: TabMoreAdapter? = null

    override fun initViewBinding(): FraSquareBinding {
        return bind(FraSquareBinding::inflate)
    }

    override fun initView() {


        mViewBinding.commonToolBar.etSearch.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.SEARCH_WALLPAPER_ACTIVITY).navigation()
        }
        mViewBinding.commonToolBar.ivAdd.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.ADD_WALLPAPER_ACTIVITY).navigation()
        }

//        mViewBinding.llMore.setOnClickListener {
//
//            XPopup.Builder(requireContext())
//                .atView(mViewBinding.tabLayout)
//                .asCustom(FilterDialog(requireContext()).apply {
//                    block = {
//                        it.sllContainer.shapeDrawableBuilder.setSolidColor(
//                            ContextCompat.getColor(
//                                requireContext(),
//                                com.yang.lib_common.R.color.color_F3F4F6
//                            )
//                        ).intoBackground()
//                        it.recyclerView.layoutManager = GridLayoutManager(requireContext(), 5)
//                        if (null == mTabMoreAdapter){
//                            mTabMoreAdapter = TabMoreAdapter()
//                        }
//                        mTabMoreAdapter!!.currentPosition = mViewBinding.tabLayout.selectedTabPosition
//                        mTabMoreAdapter!!.setOnItemClickListener { _, _, position ->
//                            mViewBinding.viewPager.setCurrentItem(position, false)
//                            dismiss()
//                        }
//                        it.recyclerView.adapter = mTabMoreAdapter
//                        mTabMoreAdapter!!.setNewData(mTitles)
//                    }
//                }).show()
//        }
//
//        mViewBinding.errorReLoadView.onClick = {
//            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.LOADING
//            squareViewModel.getTabs()
//        }

        mFragments.add(
            buildARouter(AppConstant.RoutePath.SQUARE_ITEM_FRAGMENT)
                .navigation() as Fragment
        )
        mTitles.add("广场")
        initViewPager()
        initTabLayout()
    }

    override fun initData() {
//        squareViewModel.wallType = arguments?.getInt(AppConstant.Constant.WALL_TYPE)?:squareViewModel.wallType
//        mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.LOADING
//        squareViewModel.getTabs()
//
//
//        squareViewModel.mWallpaperTabData.observe(this) {
            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.NORMAL
//            it.mapIndexed { index, wallpaperTabData ->
//                mFragments.add(
//                    buildARouter(AppConstant.RoutePath.SQUARE_ITEM_FRAGMENT)
//                        .withInt(AppConstant.Constant.TYPE, index)
//                        .withString(AppConstant.Constant.ID, wallpaperTabData.id)
//                        .withInt(AppConstant.Constant.WALL_TYPE, squareViewModel.wallType)
//                        .navigation() as Fragment
//                )
//
//                wallpaperTabData.name
//            }.apply {
//                mTitles.addAll(this as MutableList<String>)
//            }
//            initViewPager()
//            initTabLayout()
//        }
//        squareViewModel.uC.requestFailEvent.observe(this){
//            mViewBinding.errorReLoadView.status = ErrorReLoadView.Status.ERROR
//        }
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


    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return squareViewModel.uC
    }
}