package com.yang.module_mine.ui.activity

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yang.lib_common.adapter.TabAndViewPagerAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
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

    private var mTitles : MutableList<String> = arrayListOf("关注","粉丝")

    private lateinit var mFragments: MutableList<Fragment>

    override fun initViewBinding(): ActMyFansBinding {
        return bind(ActMyFansBinding::inflate)
    }

    override fun initData() {
        mFragments = mutableListOf()
        mTitles.forEach { _ ->
            mFragments.add( buildARouter(AppConstant.RoutePath.MINE_FANS_FRAGMENT)
                .navigation() as Fragment
            )
        }
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
}