package com.yang.module_main.ui.activity

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.TabAndViewPagerAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.px2dip
import com.yang.lib_common.util.showShort
import com.yang.lib_common.*
import com.yang.module_main.databinding.ActMainBinding
import com.yang.module_main.viewmodel.MainViewModel
@Route(path = AppConstant.RoutePath.MAIN_ACTIVITY)
class MainActivity : BaseActivity<ActMainBinding>() {

    @InjectViewModel
    lateinit var mainViewModel: MainViewModel

    private lateinit var mFragments: MutableList<Fragment>

    private var mTitles = arrayListOf("首页", "工作台","我的")

    private var mImages = arrayListOf(R.drawable.iv_home, R.drawable.iv_task, R.drawable.iv_mine)

    private var mSelectImages = arrayListOf(R.drawable.iv_home_select, R.drawable.iv_task_select,R.drawable.iv_mine_select)

    private var lastTime = 0L


    override fun initViewBinding(): ActMainBinding {
        return bind(ActMainBinding::inflate)
    }

    override fun initData() {
        mFragments = mutableListOf<Fragment>().apply {
            add(buildARouter(AppConstant.RoutePath.MAIN_FRAGMENT).navigation() as Fragment)
            add(buildARouter(AppConstant.RoutePath.MAIN_FRAGMENT).navigation() as Fragment)
            add(buildARouter(AppConstant.RoutePath.MAIN_FRAGMENT).navigation() as Fragment)
        }
    }

    override fun initView() {

        initViewPager()
        initTabLayout()


    }

    private fun initViewPager() {

        val tabAndViewPagerAdapter = TabAndViewPagerAdapter(this, mFragments, mTitles)
        mViewBinding.viewPager.adapter = tabAndViewPagerAdapter
        mViewBinding.viewPager.isUserInputEnabled = false
        mViewBinding.viewPager.offscreenPageLimit = mFragments.size
    }

    private fun initTabLayout() {

        TabLayoutMediator(
            mViewBinding.tabLayout, mViewBinding.viewPager, true, false
        ) { tab, position ->
            tab.text = mTitles[position]
            tab.setIcon(mImages[position])
            if (position == 0) {
                tab.setIcon(mSelectImages[position])
//                (tab.view.getChildAt(0) as ImageView).imageTintList =
//                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.colorBar))
            } else {
                tab.setIcon(mImages[position])
//                (tab.view.getChildAt(0) as ImageView).imageTintList =
//                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.grey))
            }
            tab.view.setOnLongClickListener { true }
        }.attach()

        mViewBinding.tabLayout.post {
            mViewBinding.viewPager.setPadding(0, 0, 0, mViewBinding.tabLayout.height + 10f.px2dip(this))
        }

        mViewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.setIcon(mImages[tab.position])
//                (tab.view.getChildAt(0) as ImageView).imageTintList =
//                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.grey))
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.setIcon(mSelectImages[tab.position])
//                (tab.view.getChildAt(0) as ImageView).imageTintList =
//                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.colorBar))

            }

        })
    }

    override fun initViewModel() {

        InjectViewModelProxy.inject(this)

    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis-lastTime > 1000){
            lastTime = currentTimeMillis
            showShort(getString(R.string.string_close_application))
        }else{
            moveTaskToBack(true)
        }
    }
}