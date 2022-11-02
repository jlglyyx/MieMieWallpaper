package com.yang.module_mine.ui.activity

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.appbar.AppBarLayout
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.module_mine.R
import com.yang.module_mine.databinding.ActMyBalanceBinding
import kotlin.math.abs

/**
 * @ClassName: MyBalanceActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/4 17:09
 */
@Route(path = AppConstant.RoutePath.MINE_MY_BALANCE_ACTIVITY)
class MyBalanceActivity : BaseActivity<ActMyBalanceBinding>() {

    lateinit var mAdapter: BaseQuickAdapter<String, BaseViewHolder>

    private var alphaPercent = 0f

    override fun initViewBinding(): ActMyBalanceBinding {
        return bind(ActMyBalanceBinding::inflate)
    }

    override fun initData() {

    }

    override fun initView() {

        mViewBinding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        //滑动状态
            alphaPercent = abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
            if (alphaPercent <= 0.2f) {
                alphaPercent = 0f
            }
            if (alphaPercent >= 0.8f) {
                alphaPercent = 1f
            }
            mViewBinding.commonToolBar.tvCenterContent.alpha = alphaPercent
        })

        mViewBinding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        mAdapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_balance) {


            override fun convert(helper: BaseViewHolder?, item: String?) {

            }

        }
        mViewBinding.recyclerView.adapter = mAdapter

        mAdapter.replaceData(mutableListOf<String>().apply {
            for (i in 0..20){
                add("$i")
            }
        })

    }

    override fun initViewModel() {

    }


}