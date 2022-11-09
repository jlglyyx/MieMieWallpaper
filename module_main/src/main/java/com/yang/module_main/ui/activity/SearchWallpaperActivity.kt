package com.yang.module_main.ui.activity

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.TabAndViewPagerAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.databinding.ViewCustomTopTabBinding
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.CommonSearchToolBar
import com.yang.module_main.R
import com.yang.module_main.data.SearchFindData
import com.yang.module_main.databinding.ActSearchWallpaperBinding
import com.yang.module_main.databinding.ItemSearchHistoryBinding
import com.yang.module_main.viewmodel.WallpaperViewModel

/**
 * @ClassName: SearchWallpaperActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/19 17:13
 */
@Route(path = AppConstant.RoutePath.SEARCH_WALLPAPER_ACTIVITY)
class SearchWallpaperActivity:BaseActivity<ActSearchWallpaperBinding>() {

    private val tabTextSize = 13f
    private val tabSelectTextSize = 14f

    @InjectViewModel
    lateinit var wallpaperViewModel: WallpaperViewModel

    private lateinit var mAdapter: BaseQuickAdapter<SearchFindData, BaseViewHolder>

    private var mTitles : MutableList<String> = arrayListOf("静态壁纸","动态壁纸","动态","用户")

    private lateinit var mFragments: MutableList<Fragment>

    override fun initViewBinding(): ActSearchWallpaperBinding {
        return bind(ActSearchWallpaperBinding::inflate)
    }


    override fun initData() {

        wallpaperViewModel.getSearchFind()

        mFragments = mutableListOf()
        mFragments.add(buildARouter(AppConstant.RoutePath.SEARCH_WALLPAPER_FRAGMENT)
            .withInt(AppConstant.Constant.WALL_TYPE,AppConstant.Constant.WALL_STATIC_TYPE)
            .navigation() as Fragment)
        mFragments.add(buildARouter(AppConstant.RoutePath.SEARCH_WALLPAPER_FRAGMENT)
            .withInt(AppConstant.Constant.WALL_TYPE,AppConstant.Constant.WALL_VIDEO_TYPE)
            .navigation() as Fragment)
        mFragments.add(buildARouter(AppConstant.RoutePath.SEARCH_DYNAMIC_FRAGMENT)
            .withInt(AppConstant.Constant.WALL_TYPE,AppConstant.Constant.WALL_VIDEO_TYPE)
            .navigation() as Fragment)
        mFragments.add(buildARouter(AppConstant.RoutePath.SEARCH_USER_FRAGMENT)
            .withInt(AppConstant.Constant.WALL_TYPE,AppConstant.Constant.WALL_VIDEO_TYPE)
            .navigation() as Fragment)
    }

    override fun initView() {

        initViewPager()
        initTabLayout()

        mViewBinding.apply {
            commonToolBar.canEdit(true)
            commonToolBar.etSearch.hint = "请输入搜索内容"
            commonToolBar.searchListener = object :CommonSearchToolBar.OnSearchListener{
                override fun onSearch(text: String) {
                    doSearch(text)
                }
            }
            commonToolBar.etSearch.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()){
                    llSearch.visibility = View.VISIBLE
                    llViewPager.visibility = View.GONE
                }
            }

            tvChange.clicks().subscribe {
                wallpaperViewModel.getSearchFind()
            }

            val mutableList = mutableListOf<String>().apply {
                add("1111111111111张三222张三222张三222张三222张三222张三222张三222张三222张三222张三")
                add("春")
                add("222")
                add("222")
                add("222")
                add("222")
                add("1")
                add("1")
                add("1")
                add("1")
                add("222")
                add("张三222张三222张三222张三222张三222张三222张三222张三222张三222张三")
                add("222")
                add("222")
                add("222")
                add("222")
                add("222")
                add("4q1")
            }

            val mapIndexed = mutableList.mapIndexed { index, s ->
                SearchFindData("$index", s)
            }

            flowLayout.addChildView(R.layout.item_search_history, mutableList){ view, position, item ->
                view.setOnClickListener {
                    doSearch(item)
//                    showShort("${position}  "+item)
                }
                val bind = ItemSearchHistoryBinding.bind(view)
                bind.stvTv.text = item
            }

            mAdapter = object : BaseQuickAdapter<SearchFindData, BaseViewHolder>(R.layout.item_search_find) {
                override fun convert(helper: BaseViewHolder, item: SearchFindData) {
                    helper.setText(R.id.stv_tv,item.title)
                    .setText(R.id.stv_order,"${helper.bindingAdapterPosition+1}")
                }
            }

            mViewBinding.recyclerView.adapter = mAdapter
            mAdapter.setNewData(mapIndexed)
            mAdapter.setOnItemClickListener { adapter, view, position ->
                doSearch(mAdapter.getItem(position)?.title?:"")
            }
        }


    }

    fun doSearch(keyword:String){
        mViewBinding.viewPager.setCurrentItem(0,false)
        LiveDataBus.instance.with(AppConstant.Constant.KEYWORD).postValue(keyword)
        mViewBinding.commonToolBar.etSearch.setText(keyword)
        mViewBinding.commonToolBar.etSearch.setSelection(keyword.length)
        hideSoftInput(this,mViewBinding.commonToolBar.etSearch)
        mViewBinding.apply {
            llSearch.visibility = View.GONE
            llViewPager.visibility = View.VISIBLE
        }
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
        wallpaperViewModel.mSearchFindData.observe(this){
            if (!it.isNullOrEmpty()){
                mAdapter.replaceData(it)
            }
        }
    }

    private fun initViewPager() {

        mViewBinding.viewPager.adapter = TabAndViewPagerAdapter(this, mFragments, mTitles)
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
            val tabView = ViewCustomTopTabBinding.inflate(LayoutInflater.from(this))
            tabView.tvTitle.text = mTitles[position]
            tab.customView = tabView.root
            if (position == 0) {
                tabView.tvTitle.setTextColor(getColor(com.yang.lib_common.R.color.appColor))
                tabView.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,tabSelectTextSize)
            } else {
                tabView.tvTitle.setTextColor(getColor(com.yang.lib_common.R.color.textColor_666666))
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
                    tvTitle.setTextColor(getColor(com.yang.lib_common.R.color.textColor_666666))
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val customView = tab.customView
                customView?.apply {
                    val tvTitle = findViewById<TextView>(com.yang.lib_common.R.id.tv_title)
                    tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,tabSelectTextSize)
                    tvTitle.setTextColor(getColor(com.yang.lib_common.R.color.appColor))
                }
            }

        })
    }

    override fun onBackPressed() {
        if (mViewBinding.llViewPager.visibility == View.VISIBLE){
            mViewBinding.commonToolBar.etSearch.setText("")
        }else{
            super.onBackPressed()
        }
    }
}