package com.yang.module_mine.ui.activity

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yang.lib_common.adapter.TabAndViewPagerAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.databinding.ViewCustomTopTabBinding
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.module_mine.databinding.ActMyBalanceBinding
import com.yang.module_mine.databinding.ActMyFansBinding
import com.yang.module_mine.databinding.ActMyRightsBinding

/**
 * @ClassName: MyFansActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/4 17:09
 */
@Route(path = AppConstant.RoutePath.MINE_MY_FANS_ACTIVITY)
class MyFansActivity : BaseActivity<ActMyFansBinding>() {

    private var index = 0

    private val tabTextSize = 15f
    private val tabSelectTextSize = 16f

    private var mTitles : MutableList<String> = arrayListOf("关注","粉丝")

    private lateinit var mFragments: MutableList<Fragment>

    override fun initViewBinding(): ActMyFansBinding {
        return bind(ActMyFansBinding::inflate)
    }

    override fun initData() {
        mFragments = mutableListOf()
        mTitles.forEachIndexed{ i,_ ->
            mFragments.add(buildARouter(AppConstant.RoutePath.MINE_FANS_FRAGMENT).withInt(AppConstant.Constant.TYPE,i)
                .navigation() as Fragment
            )
        }

        index = intent.getIntExtra(AppConstant.Constant.INDEX,index)
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
        mViewBinding.viewPager.setCurrentItem(index,false)

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