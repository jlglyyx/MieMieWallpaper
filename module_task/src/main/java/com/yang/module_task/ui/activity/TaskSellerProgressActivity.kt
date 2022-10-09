package com.yang.module_task.ui.activity

import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxj.xpopup.XPopup
import com.yang.lib_common.adapter.PictureAdapter
import com.yang.lib_common.adapter.PictureSelectAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.MediaInfoBean
import com.yang.lib_common.dialog.ConfirmDialog
import com.yang.lib_common.dialog.ImageViewPagerDialog
import com.yang.lib_common.dialog.PayTaskDialog
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.CommonToolBar
import com.yang.module_task.R
import com.yang.module_task.databinding.ActTaskSellerProgressBinding
import com.yang.module_task.dialog.TaskDetailDialog

/**
 * @ClassName: TaskSellerProgressActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/29 13:38
 */
@Route(path = AppConstant.RoutePath.TASK_SELLER_PROGRESS_ACTIVITY)
class TaskSellerProgressActivity :BaseActivity<ActTaskSellerProgressBinding>(){

    private lateinit var taskPictureAdapter: PictureAdapter

    private lateinit var payPictureAdapter: PictureAdapter

    private lateinit var commentPictureAdapter: PictureAdapter

    private var taskData: MutableList<String> = mutableListOf<String>().apply {
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
    }

    private var payData: MutableList<String> = mutableListOf<String>().apply {
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
    }

    private var commentData: MutableList<String> = mutableListOf<String>().apply {
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57")
    }

    private var confirmDialog:ConfirmDialog? = null

    private var payTaskDialog:PayTaskDialog? = null

    override fun initViewBinding(): ActTaskSellerProgressBinding {
        return bind(ActTaskSellerProgressBinding::inflate)
    }

    override fun initData() {
    }

    override fun initView() {
        initTaskRecyclerView()
        initPayRecyclerView()
        initCommentRecyclerView()
        mViewBinding.commonToolBar.tVRightCallBack = object : CommonToolBar.TVRightCallBack {
            override fun tvRightClickListener() {
                if (null == confirmDialog){
                    confirmDialog = ConfirmDialog(this@TaskSellerProgressActivity)
                }
                XPopup.Builder(this@TaskSellerProgressActivity).asCustom(confirmDialog).show()
            }
        }

        mViewBinding.tvDetail.clicks().subscribe {
            XPopup.Builder(this).asCustom(TaskDetailDialog(this)).show()
        }

        mViewBinding.btPay.clicks().subscribe {
            if (null == payTaskDialog){
                payTaskDialog = PayTaskDialog(this)
            }
            XPopup.Builder(this).asCustom(payTaskDialog).show()
        }
    }

    override fun initViewModel() {
    }


    private fun initTaskRecyclerView(){
        taskPictureAdapter = PictureAdapter(taskData)
        mViewBinding.taskRecyclerView.adapter = taskPictureAdapter
        mViewBinding.taskRecyclerView.layoutManager = GridLayoutManager(this, 3)
        taskPictureAdapter.setOnItemClickListener { adapter, view, position ->
            val imageList = adapter.data as MutableList<String>
            val imageViewPagerDialog =
                ImageViewPagerDialog(this, imageList , position)
            XPopup.Builder(this).asCustom(imageViewPagerDialog).show()
        }
    }
    private fun initPayRecyclerView(){
        payPictureAdapter = PictureAdapter(payData)
        mViewBinding.payRecyclerView.adapter = payPictureAdapter
        mViewBinding.payRecyclerView.layoutManager = GridLayoutManager(this, 3)
        payPictureAdapter.setOnItemClickListener { adapter, view, position ->
            val imageList = adapter.data as MutableList<String>
            val imageViewPagerDialog =
                ImageViewPagerDialog(this, imageList , position)
            XPopup.Builder(this).asCustom(imageViewPagerDialog).show()
        }
    }
    private fun initCommentRecyclerView(){
        commentPictureAdapter = PictureAdapter(commentData)
        mViewBinding.commentRecyclerView.adapter =commentPictureAdapter
        mViewBinding.commentRecyclerView.layoutManager = GridLayoutManager(this, 3)
        commentPictureAdapter.setOnItemClickListener { adapter, view, position ->
            val imageList = adapter.data  as MutableList<String>
            val imageViewPagerDialog =
                ImageViewPagerDialog(this, imageList , position)
            XPopup.Builder(this).asCustom(imageViewPagerDialog).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        payTaskDialog?.dismiss()
        payTaskDialog = null
        confirmDialog?.dismiss()
        confirmDialog = null
    }

}