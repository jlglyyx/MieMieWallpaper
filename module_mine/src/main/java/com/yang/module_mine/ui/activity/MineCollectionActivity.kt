package com.yang.module_mine.ui.activity

import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yang.lib_common.adapter.TabAndViewPagerAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.databinding.ViewCustomTopTabBinding
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.module_mine.databinding.ActMineFansBinding

/**
 * @ClassName: MineCollectionActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/4 17:09
 */
@Route(path = AppConstant.RoutePath.MINE_COLLECTION_ACTIVITY)
class MineCollectionActivity : BaseActivity<ActMineFansBinding>() {

    private var mTitles: MutableList<String> = arrayListOf("我的收藏", "我的下载")

    private var index = 0

    private val tabTextSize = 15f
    private val tabSelectTextSize = 16f

    private lateinit var mFragments: MutableList<Fragment>

    override fun initViewBinding(): ActMineFansBinding {
        return bind(ActMineFansBinding::inflate)
    }

    override fun initData() {
        mFragments = mutableListOf()
        mFragments.add(
            buildARouter(AppConstant.RoutePath.MINE_COLLECTION_FRAGMENT).withInt(
                AppConstant.Constant.INDEX,
                0
            )
                .navigation() as Fragment
        )
        mFragments.add(
            buildARouter(AppConstant.RoutePath.MINE_DOWN_FRAGMENT).withInt(
                AppConstant.Constant.INDEX,
                1
            )
                .navigation() as Fragment
        )
        index = intent.getIntExtra(AppConstant.Constant.INDEX, index)
    }

    override fun initView() {

        initViewPager()
        initTabLayout()

        mViewBinding.apply {
            ivBack.clicks().subscribe {
                finish()
            }
        }

    }

    override fun initViewModel() {

    }


    private fun initViewPager() {

        mViewBinding.viewPager.adapter = TabAndViewPagerAdapter(this, mFragments, mTitles)
        mViewBinding.viewPager.offscreenPageLimit = mFragments.size
        mViewBinding.viewPager.setCurrentItem(index, false)

    }

    private fun initTabLayout() {

        TabLayoutMediator(
            mViewBinding.tabLayout, mViewBinding.viewPager
        ) { tab, position ->
            tab.text = mTitles[position]
            val tabView = ViewCustomTopTabBinding.inflate(LayoutInflater.from(this))
            tabView.tvTitle.text = mTitles[position]
            tab.customView = tabView.root
            if (position == index) {
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
}