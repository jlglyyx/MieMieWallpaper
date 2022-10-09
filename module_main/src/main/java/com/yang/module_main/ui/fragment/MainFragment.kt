package com.yang.module_main.ui.fragment

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.dialog.FilterTaskDialog
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.getDefaultMMKV
import com.yang.lib_common.widget.CommonToolBar
import com.yang.module_main.adapter.TaskAdapter
import com.yang.module_main.databinding.FraMainBinding
import com.yang.module_main.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @ClassName: MainFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 15:38
 */
@Route(path = AppConstant.RoutePath.MAIN_FRAGMENT)
class MainFragment : BaseLazyFragment<FraMainBinding>(), OnRefreshLoadMoreListener {

    @InjectViewModel
    lateinit var mainViewModel: MainViewModel

    lateinit var taskAdapter: TaskAdapter

    override fun initViewBinding(): FraMainBinding {
        return bind(FraMainBinding::inflate)
    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mainViewModel.uC
    }

    override fun initView() {
        mViewBinding.commonToolBar.imageAddCallBack = object : CommonToolBar.ImageAddCallBack{
            override fun imageAddClickListener() {
                buildARouter(AppConstant.RoutePath.ADD_TASK_ACTIVITY).navigation()
            }

        }

        mViewBinding.ivX.clicks().subscribe {
            mViewBinding.etSearch.setText("")
        }

        mViewBinding.tvFilter.clicks().subscribe {
            XPopup.Builder(requireContext()).asCustom(FilterTaskDialog(requireContext())).show()
        }

        mViewBinding.tvSearch.clicks().subscribe {
            onRefresh(mViewBinding.includeContainer.smartRefreshLayout)
        }

        mViewBinding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s.toString())){
                    mViewBinding.ivX.visibility = View.GONE
                }else{
                    mViewBinding.ivX.visibility = View.VISIBLE
                }
            }

        })

        initSmartRefreshLayout()
        initRecyclerView()
    }






    override fun initData() {

        mainViewModel.getTaskList()
    }

    private fun initSmartRefreshLayout() {
        mViewBinding.includeContainer.smartRefreshLayout.setOnRefreshLoadMoreListener(this)

    }

    private fun initRecyclerView() {
        taskAdapter = TaskAdapter(mutableListOf())
        taskAdapter.setOnItemClickListener { adapter, view, position ->
            buildARouter(AppConstant.RoutePath.TASK_DETAIL_ACTIVITY).navigation()
        }
        mViewBinding.includeContainer.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        mViewBinding.includeContainer.recyclerView.adapter = taskAdapter
        registerRefreshAndRecyclerView(
            mViewBinding.includeContainer.smartRefreshLayout,
            taskAdapter
        )
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
        mainViewModel.taskLiveData.observe(this, Observer {
            when {
                mViewBinding.includeContainer.smartRefreshLayout.isRefreshing -> {
                    mainViewModel.uC.refreshEvent.call()
                    if (it.isNullOrEmpty()) {
                        mainViewModel.showRecyclerViewEmptyEvent()
                    } else {
                        taskAdapter.replaceData(it)
                    }
                }
                mViewBinding.includeContainer.smartRefreshLayout.isLoading -> {
                    mainViewModel.uC.loadMoreEvent.call()
                    if (it.isNullOrEmpty()) {
                        mViewBinding.includeContainer.smartRefreshLayout.setNoMoreData(true)
                    } else {
                        mViewBinding.includeContainer.smartRefreshLayout.setNoMoreData(false)
                        taskAdapter.addData(it)
                    }
                }
                else -> {
                    if (it.isNullOrEmpty()) {
                        mainViewModel.showRecyclerViewEmptyEvent()
                    } else {
                        taskAdapter.replaceData(it)
                    }
                }
            }
        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mainViewModel.pageNum = 1
        mainViewModel.getTaskList()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mainViewModel.pageNum++
        mainViewModel.getTaskList()
    }

}