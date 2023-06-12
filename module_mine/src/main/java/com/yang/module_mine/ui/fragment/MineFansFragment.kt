package com.yang.module_mine.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.showShort
import com.yang.lib_common.util.smartRefreshLayoutData
import com.yang.module_mine.R
import com.yang.module_mine.adapter.MineFansAdapter
import com.yang.module_mine.databinding.FraMineFansBinding
import com.yang.module_mine.viewmodel.MineViewModel


/**
 * @ClassName: MineFansFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.MINE_FANS_FRAGMENT)
class MineFansFragment : BaseLazyFragment<FraMineFansBinding>() ,OnRefreshLoadMoreListener{

    @InjectViewModel
    lateinit var mineViewModel: MineViewModel

    private lateinit var mAdapter: MineFansAdapter

    private var fanType = 0

    override fun initViewBinding(): FraMineFansBinding {
        return bind(FraMineFansBinding::inflate)
    }

    override fun initView() {
        initRecyclerView()
        mViewBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(this)
        registerRefreshAndRecyclerView(mViewBinding.smartRefreshLayout,mAdapter)
    }

    override fun initData() {
        fanType = arguments?.getInt(AppConstant.Constant.TYPE) ?:fanType
        mViewBinding.smartRefreshLayout.autoRefresh()
    }


    private fun initRecyclerView() {

        mAdapter = MineFansAdapter()

        mViewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mViewBinding.recyclerView.adapter = mAdapter


        mAdapter.setOnItemChildClickListener { aadapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                when(view.id){
                    R.id.tv_attention ->{
                        showShort("取消关注")
                        mineViewModel.addFollow(UserInfoHold.userId,it.followUserId)
                    }
                }
            }
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                    AppConstant.Constant.USER_ID,
                    it.followUserId
                ).navigation()
            }
        }

    }


    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
        mineViewModel.mFansData.observe(this){
            mViewBinding.smartRefreshLayout.smartRefreshLayoutData(it,mAdapter,mineViewModel)
        }

    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return mineViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        mineViewModel.pageNum = 1
        mineViewModel.getMineFollowList(UserInfoHold.userId,fanType)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        mineViewModel.pageNum++
        mineViewModel.getMineFollowList(UserInfoHold.userId,fanType)

    }
}