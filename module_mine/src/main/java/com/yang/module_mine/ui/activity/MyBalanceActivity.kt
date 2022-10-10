package com.yang.module_mine.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.module_mine.R
import com.yang.module_mine.databinding.ActMyBalanceBinding

/**
 * @ClassName: MyBalanceActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/4 17:09
 */
@Route(path = AppConstant.RoutePath.MINE_MY_BALANCE_ACTIVITY)
class MyBalanceActivity : BaseActivity<ActMyBalanceBinding>() {

    lateinit var mAdapter: BaseQuickAdapter<String, BaseViewHolder>

    override fun initViewBinding(): ActMyBalanceBinding {
        return bind(ActMyBalanceBinding::inflate)
    }

    override fun initData() {

    }

    override fun initView() {

        mViewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
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