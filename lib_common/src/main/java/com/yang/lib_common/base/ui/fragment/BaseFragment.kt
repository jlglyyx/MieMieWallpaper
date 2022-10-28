package com.yang.lib_common.base.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yang.lib_common.R
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.getStatusBarHeight

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var emptyView: View? = null

    lateinit var mContext: Context

    private var uC: UIChangeLiveData? = null

    private var loadingPopupView: LoadingPopupView? = null

    private var _mViewBinding: VB? = null

    val mViewBinding get() = _mViewBinding!!

    lateinit var inflater: LayoutInflater

    var container: ViewGroup? = null

    val TAG = this.javaClass.simpleName


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.inflater = inflater
        this.container = container
        _mViewBinding = initViewBinding()
        mContext = requireContext()
        if (setStatusPadding()) {
            _mViewBinding!!.root.setPadding(0, getStatusBarHeight(requireActivity()), 0, 0)
        }
        return _mViewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        uC = initUIChangeLiveData()
        initData()
        initView()
        registerListener()
        Log.e(TAG, "OpenView===: $TAG")
    }

    open fun initUIChangeLiveData(): UIChangeLiveData? {//在ViewModel层操作ui
        return null
    }

    open fun setStatusPadding(): Boolean {
        return false
    }

    abstract fun initViewBinding(): VB

    abstract fun initData()

    abstract fun initView()

    abstract fun initViewModel()

    inline fun <reified T : VB> bind(crossinline bind: (LayoutInflater, ViewGroup?, Boolean) -> T) =
        bind(layoutInflater, container, false)

    fun <T : BaseViewModel> getViewModel(@NonNull clazz: Class<T>): T {

        return ViewModelProvider(requireActivity()).get(clazz)
    }

    fun <T : BaseViewModel> getViewModel(
        @NonNull factory: ViewModelProvider.Factory,
        @NonNull clazz: Class<T>
    ): T {

        return ViewModelProvider(requireActivity(), factory).get(clazz)
    }

    fun finishRefreshLoadMore(smartRefreshLayout: SmartRefreshLayout) {
        uC?.let { uC ->
            uC.refreshEvent.observe(this, Observer {
                smartRefreshLayout.finishRefresh()
            })
            uC.loadMoreEvent.observe(this, Observer {
                smartRefreshLayout.finishLoadMore()
            })
        }
    }

    fun showRecyclerViewEvent(adapter: BaseQuickAdapter<*, *>) {
        uC?.let { uC ->
            uC.showRecyclerViewEvent.observe(this, Observer {
                if (it == AppConstant.LoadingViewEnum.ERROR_VIEW) {
                    emptyView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.view_error_data, null, false)
                } else if (it == AppConstant.LoadingViewEnum.EMPTY_VIEW) {
                    emptyView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.view_empty_data, null, false)
                }
                adapter.setNewData(null)
                adapter.emptyView = emptyView
            })
        }
    }

    fun registerRefreshAndRecyclerView(
        smartRefreshLayout: SmartRefreshLayout,
        adapter: BaseQuickAdapter<*, *>
    ) {
        uC?.let { uC ->
            uC.refreshEvent.observe(this, Observer {
                smartRefreshLayout.finishRefresh()
            })
            uC.loadMoreEvent.observe(this, Observer {
                smartRefreshLayout.finishLoadMore()
            })
            uC.showRecyclerViewEvent.observe(this, Observer {
                if (it == AppConstant.LoadingViewEnum.ERROR_VIEW) {
                    emptyView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.view_error_data, null, false)
                } else if (it == AppConstant.LoadingViewEnum.EMPTY_VIEW) {
                    emptyView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.view_empty_data, null, false)
                }
                adapter.setNewData(null)
                adapter.emptyView = emptyView
            })
        }
    }

    private fun registerListener() {
        uC?.let { uC ->
            uC.showLoadingEvent.observe(this, Observer {
                if (loadingPopupView == null) {
                    loadingPopupView =
                        XPopup.Builder(requireContext())
                            .isViewMode(true)
                            .dismissOnTouchOutside(false)
                            .hasShadowBg(false)
                            .asLoading(it)
                } else {
                    loadingPopupView?.setTitle(it)
                }
                if (!loadingPopupView?.isShow!!) {
                    loadingPopupView?.show()
                }
            })

            uC.dismissDialogEvent.observe(this, Observer {
                loadingPopupView?.dismiss()
            })
        }

    }

    private fun unRegisterListener() {
        uC?.let { uC ->
            uC.showLoadingEvent.removeObservers(this)
            uC.dismissDialogEvent.removeObservers(this)
            uC.refreshEvent.removeObservers(this)
            uC.loadMoreEvent.removeObservers(this)
            uC.finishActivityEvent.removeObservers(this)
            uC.requestSuccessEvent.removeObservers(this)
            uC.requestFailEvent.removeObservers(this)
            uC.showRecyclerViewEvent.removeObservers(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unRegisterListener()
        uC = null
        _mViewBinding = null
        loadingPopupView?.dismiss()
        loadingPopupView = null
    }
}