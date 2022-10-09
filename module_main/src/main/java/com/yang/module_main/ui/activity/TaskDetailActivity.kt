package com.yang.module_main.ui.activity

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxj.xpopup.XPopup
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.dialog.ImageViewPagerDialog
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.CommonToolBar
import com.yang.module_main.R
import com.yang.module_main.adapter.TaskDetailAdapter
import com.yang.module_main.data.TaskData
import com.yang.module_main.databinding.ActTaskDetailBinding
import com.yang.module_main.viewmodel.MainViewModel

/**
 * @Author Administrator
 * @ClassName TaskDetailActivity
 * @Description
 * @Date 2021/8/2 17:19
 */
@Route(path = AppConstant.RoutePath.TASK_DETAIL_ACTIVITY)
class TaskDetailActivity : BaseActivity<ActTaskDetailBinding>() {

    @InjectViewModel
    lateinit var mainViewModel: MainViewModel


    private var id:String? = ""
    private var type:Boolean = true

    override fun initViewBinding(): ActTaskDetailBinding {
        return bind(ActTaskDetailBinding::inflate)
    }

    override fun initData() {
        id = intent.getStringExtra(AppConstant.Constant.ID)
        type = intent.getBooleanExtra(AppConstant.Constant.TYPE,true)
        getTaskDetail()
        mainViewModel.taskLiveData.observe(this, Observer {
            val taskData = it[0]
            initItemMainContentImage(taskData)
            initItemMainContentText(taskData)
        })
    }

    private fun initItemMainContentText(item: TaskData) {
        mViewBinding.tvTaskTitle.text = item.taskTitle
        mViewBinding.tvTaskContent.text = item.taskContent
        mViewBinding.tvTaskShop.text = item.taskShop
        mViewBinding.tvTaskLink.text = item.taskLink
        mViewBinding.tvTaskKeyWord.text = item.taskKeyword
        mViewBinding.tvTaskNumber.text = "${item.taskNumber}"
        mViewBinding.tvTaskPrice.text = item.taskPrice
        mViewBinding.tvTaskCommission.text = item.taskCommission
    }

    private fun initItemMainContentImage(item: TaskData) {
        mViewBinding.recyclerView.layoutManager = GridLayoutManager(this@TaskDetailActivity,3)
        val dynamicAdapter = TaskDetailAdapter(
            R.layout.view_item_grid_nine_picture,
            item.imageUrls?.symbolToList("#")!!
        )
        mViewBinding.recyclerView.adapter = dynamicAdapter
        dynamicAdapter.setOnItemClickListener { adapter, view, position ->
            val imageViewPagerDialog =
                ImageViewPagerDialog(
                    this@TaskDetailActivity,
                    item.imageUrls?.symbolToList("#")!!,
                    position,
                    true
                )
            XPopup.Builder(this@TaskDetailActivity).asCustom(imageViewPagerDialog).show()
        }
    }

    override fun initView() {
        mViewBinding.commonToolBar.rightContentVisible = type
        initRecyclerView()
        lifecycle.addObserver(mViewBinding.autoScrollListView)

        mViewBinding.commonToolBar.tVRightCallBack = object : CommonToolBar.TVRightCallBack{
            override fun tvRightClickListener() {
                mViewBinding.commonToolBar.rightContentVisible = false
            }
        }

    }



    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return mainViewModel.uC
    }


    private fun getTaskDetail() {
        mainViewModel.getTaskList()
    }


    private fun initRecyclerView() {

    }


}