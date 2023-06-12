package com.yang.module_mine.ui.activity

import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.R
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.dialog.FilterDialog
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.smartRefreshLayoutData
import com.yang.module_mine.adapter.WalletDetailAdapter
import com.yang.module_mine.adapter.WalletDetailFilterAdapter
import com.yang.module_mine.databinding.ActMineWalletDetailBinding
import com.yang.module_mine.viewmodel.MineViewModel

/**
 * @ClassName: MineWalletDetailActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/4 16:58
 */
@Route(path = AppConstant.RoutePath.MINE_WALLET_DETAIL_ACTIVITY)
class MineWalletDetailActivity:BaseActivity<ActMineWalletDetailBinding>(), OnRefreshLoadMoreListener {

    @InjectViewModel
    lateinit var mineViewModel: MineViewModel

    private val smartRefreshLayout:SmartRefreshLayout by lazy {
        mViewBinding.refreshRecyclerview.smartRefreshLayout
    }
    private val recyclerView:RecyclerView by lazy {
        mViewBinding.refreshRecyclerview.recyclerView
    }

    private var mWalletDetailFilterAdapter: WalletDetailFilterAdapter? = null


    private lateinit var mWalletDetailAdapter: WalletDetailAdapter

    private val allList = mutableListOf("默认","支付","广告券",)
//    private val allList = mutableListOf("默认","支付","积分","广告券",)

    private val filterList = mutableListOf("默认","时间升序","时间降序")

    override fun initViewBinding(): ActMineWalletDetailBinding {
        return bind(ActMineWalletDetailBinding::inflate)
    }

    override fun initData() {
        smartRefreshLayout.autoRefresh()
    }

    override fun initView() {
        initRecyclerView()
        smartRefreshLayout.setOnRefreshLoadMoreListener(this)
        registerRefreshAndRecyclerView(smartRefreshLayout, mWalletDetailAdapter)

        mViewBinding.apply {

            llAll.setOnClickListener {
                initFilterDialog(ivAll,allList){ item,position ->
                    when(position){
                        0 -> {
                            mineViewModel.payType = null
                        }
                        1 -> {
                            mineViewModel.payType = 2
                        }
                        2 -> {
                            mineViewModel.payType = 4
                        }

//                        2 -> {
//                            mineViewModel.payType = 3
//                        }
//                        3 -> {
//                            mineViewModel.payType = 4
//                        }

                    }
                    if (position == 0){
                        tvAll.text = "全部"

                    }else{
                        item?.let {
                            tvAll.text = item
                        }
                    }
                    onRefresh(smartRefreshLayout)
                }
            }

            llFilter.setOnClickListener {
                initFilterDialog(ivFilter,filterList){ item,position ->
                    when(position){
                        0 -> {
                            mineViewModel.order = position
                        }
                        1 -> {
                            mineViewModel.order = position
                        }
                        2 -> {
                            mineViewModel.order = 0
                        }
                    }

                    if (position == 0){
                        tvFilter.text = "筛选"
                    }else{
                        item?.let {
                            tvFilter.text = item
                        }
                    }
                    onRefresh(smartRefreshLayout)
                }
            }
        }


    }

    private fun initRecyclerView() {

        mWalletDetailAdapter = WalletDetailAdapter()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(this@MineWalletDetailActivity).resumeRequests()
                } else {
                    Glide.with(this@MineWalletDetailActivity).pauseRequests()
                }
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mWalletDetailAdapter


        mWalletDetailAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mWalletDetailAdapter.getItem(position)
            item?.let {
//                buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
//                    .withOptionsCompat(
//                        ActivityOptionsCompat.makeCustomAnimation(
//                            this@MineWalletDetailActivity,
//                            R.anim.fade_in,
//                            R.anim.fade_out
//                        )
//                    )
//                    .withString(AppConstant.Constant.DATA, mWalletDetailAdapter.data.toJson())
//                    .withBoolean(AppConstant.Constant.IS_COLLECTION, true)
//                    .withInt(AppConstant.Constant.INDEX, position)
//                    .withInt(AppConstant.Constant.PAGE_NUMBER, mineViewModel.pageNum)
//                    .navigation()
            }
        }

    }


    private fun initFilterDialog(imageView:ImageView,list:MutableList<String>,mBlock:(item:String?,position:Int) -> Unit){
        XPopup.Builder(this@MineWalletDetailActivity)
            .atView(mViewBinding.llContainer)
            .asCustom(FilterDialog(this@MineWalletDetailActivity).apply {
                block = {
                    it.recyclerView.layoutManager = LinearLayoutManager(this@MineWalletDetailActivity)
                    if (null == mWalletDetailFilterAdapter){
                        mWalletDetailFilterAdapter = WalletDetailFilterAdapter()
                    }
                    mWalletDetailFilterAdapter!!.setOnItemClickListener { _, _, position ->
                        val item = mWalletDetailFilterAdapter!!.getItem(position)
                        mBlock.invoke(item,position)
                        dismiss()

                    }
                    it.recyclerView.adapter = mWalletDetailFilterAdapter
                    mWalletDetailFilterAdapter!!.setNewData(list)
                }
                showBlock = {
                    imageView.setImageResource(R.drawable.iv_up_to)
                }
                dismissBlock = {
                    imageView.setImageResource(R.drawable.iv_down_to)
                }
            }).show()

    }


    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
        mineViewModel.mWalletDetailListData.observe(this) {

            smartRefreshLayout.smartRefreshLayoutData(it, mWalletDetailAdapter, mineViewModel)
        }
    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return mineViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        mineViewModel.pageNum = 1
        mineViewModel.getWalletDetail()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        mineViewModel.pageNum++
        mineViewModel.getWalletDetail()

    }
}