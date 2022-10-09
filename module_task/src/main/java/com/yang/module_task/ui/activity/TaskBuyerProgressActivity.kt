package com.yang.module_task.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxj.xpopup.XPopup
import com.yang.lib_common.adapter.PictureSelectAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.MediaInfoBean
import com.yang.lib_common.dialog.ConfirmDialog
import com.yang.lib_common.dialog.ImageViewPagerDialog
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.CommonToolBar
import com.yang.module_task.R
import com.yang.module_task.databinding.ActTaskBuyerProgressBinding
import com.yang.module_task.dialog.TaskDetailDialog
import java.util.ArrayList

/**
 * @ClassName: TaskBuyerProgressActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/29 13:38
 */
@Route(path = AppConstant.RoutePath.TASK_BUYER_PROGRESS_ACTIVITY)
class TaskBuyerProgressActivity :BaseActivity<ActTaskBuyerProgressBinding>(){

    private lateinit var taskPictureSelectAdapter: PictureSelectAdapter

    private lateinit var payPictureSelectAdapter: PictureSelectAdapter

    private lateinit var commentPictureSelectAdapter: PictureSelectAdapter

    private var taskData: MutableList<MediaInfoBean> = mutableListOf()

    private var payData: MutableList<MediaInfoBean> = mutableListOf()

    private var commentData: MutableList<MediaInfoBean> = mutableListOf()

    private lateinit var taskImageView:ImageView

    private lateinit var payImageView:ImageView

    private lateinit var commentImageView:ImageView

    private val TASK_CODE = 100

    private val PAY_CODE = 101

    private val COMMENT_CODE = 102

    private val max = 6

    override fun initViewBinding(): ActTaskBuyerProgressBinding {
        return bind(ActTaskBuyerProgressBinding::inflate)
    }

    override fun initData() {
    }

    override fun initView() {
        mViewBinding.commonToolBar.tVRightCallBack = object : CommonToolBar.TVRightCallBack {
            override fun tvRightClickListener() {
               showShort("已通知卖家尽快支付，请您耐心等待")
            }
        }
        mViewBinding.tvDetail.clicks().subscribe {
            XPopup.Builder(this).asCustom(TaskDetailDialog(this)).show()
        }


        initTaskRecyclerView()
        initPayRecyclerView()
        initCommentRecyclerView()
        ViewLayoutChangeUtil().add(findViewById(android.R.id.content))
    }

    override fun initViewModel() {

    }


    private fun initTaskRecyclerView(){
        taskPictureSelectAdapter = PictureSelectAdapter(R.layout.item_picture_select,taskData,false)
        mViewBinding.taskRecyclerView.adapter = taskPictureSelectAdapter
        mViewBinding.taskRecyclerView.layoutManager = GridLayoutManager(this, 3)
        val requestPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            it.forEach { data ->
                if (!data.value) {
                    showShort("打开权限")
                    return@registerForActivityResult
                }
            }
            buildARouter(AppConstant.RoutePath.PICTURE_SELECT_ACTIVITY)
                .withParcelableArrayList(AppConstant.Constant.DATA, taskPictureSelectAdapter.data as ArrayList)
                .withInt(AppConstant.Constant.NUM,6)
                .navigation(this,TASK_CODE)
        }

        taskImageView = ImageView(this).apply {
            setImageResource(R.drawable.iv_add)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundResource(android.R.color.darker_gray)
            val i =
                (getScreenPx(this@TaskBuyerProgressActivity)[0] - 20f.dip2px(this@TaskBuyerProgressActivity)) / 3
            layoutParams = ViewGroup.LayoutParams(i, i)
            setOnClickListener {
                requestPermission.launch(arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
        }
        taskPictureSelectAdapter.addFooterView(taskImageView)
        taskPictureSelectAdapter.setOnItemLongClickListener { adapter, view, position ->
            taskPictureSelectAdapter.remove(position)
            if (!taskImageView.isVisible) {
                taskImageView.visibility = View.VISIBLE
            }
            return@setOnItemLongClickListener false
        }
        taskPictureSelectAdapter.setOnItemClickListener { adapter, view, position ->
            val imageList =(adapter.data as MutableList<MediaInfoBean>).map {
                it.filePath
            } as MutableList<String>
            val imageViewPagerDialog =
                ImageViewPagerDialog(this, imageList , position)
            XPopup.Builder(this).asCustom(imageViewPagerDialog).show()
        }
    }
    private fun initPayRecyclerView(){
        payPictureSelectAdapter = PictureSelectAdapter(R.layout.item_picture_select,payData,false)
        mViewBinding.payRecyclerView.adapter = payPictureSelectAdapter
        mViewBinding.payRecyclerView.layoutManager = GridLayoutManager(this, 3)
        val requestPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            it.forEach { data ->
                if (!data.value) {
                    showShort("打开权限")
                    return@registerForActivityResult
                }
            }
            buildARouter(AppConstant.RoutePath.PICTURE_SELECT_ACTIVITY)
                .withParcelableArrayList(AppConstant.Constant.DATA, payPictureSelectAdapter.data as ArrayList)
                .withInt(AppConstant.Constant.NUM,6)
                .navigation(this,PAY_CODE)
        }

        payImageView = ImageView(this).apply {
            setImageResource(R.drawable.iv_add)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundResource(android.R.color.darker_gray)
            val i =
                (getScreenPx(this@TaskBuyerProgressActivity)[0] - 20f.dip2px(this@TaskBuyerProgressActivity)) / 3
            layoutParams = ViewGroup.LayoutParams(i, i)
            setOnClickListener {
                requestPermission.launch(arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
        }
        payPictureSelectAdapter.addFooterView(payImageView)
        payPictureSelectAdapter.setOnItemLongClickListener { adapter, view, position ->
            payPictureSelectAdapter.remove(position)
            if (!payImageView.isVisible) {
                payImageView.visibility = View.VISIBLE
            }
            return@setOnItemLongClickListener false
        }
        payPictureSelectAdapter.setOnItemClickListener { adapter, view, position ->
            val imageList =(adapter.data as MutableList<MediaInfoBean>).map {
                it.filePath
            } as MutableList<String>
            val imageViewPagerDialog =
                ImageViewPagerDialog(this, imageList , position)
            XPopup.Builder(this).asCustom(imageViewPagerDialog).show()
        }
    }
    private fun initCommentRecyclerView(){
        commentPictureSelectAdapter = PictureSelectAdapter(R.layout.item_picture_select,commentData,false)
        mViewBinding.commentRecyclerView.adapter =commentPictureSelectAdapter
        mViewBinding.commentRecyclerView.layoutManager = GridLayoutManager(this, 3)
        val requestPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            it.forEach { data ->
                if (!data.value) {
                    showShort("打开权限")
                    return@registerForActivityResult
                }
            }
            buildARouter(AppConstant.RoutePath.PICTURE_SELECT_ACTIVITY)
                .withParcelableArrayList(AppConstant.Constant.DATA, commentPictureSelectAdapter.data as ArrayList)
                .withInt(AppConstant.Constant.NUM,6)
                .navigation(this,COMMENT_CODE)
        }

        commentImageView = ImageView(this).apply {
            setImageResource(R.drawable.iv_add)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundResource(android.R.color.darker_gray)
            val i =
                (getScreenPx(this@TaskBuyerProgressActivity)[0] - 20f.dip2px(this@TaskBuyerProgressActivity)) / 3
            layoutParams = ViewGroup.LayoutParams(i, i)
            setOnClickListener {
                requestPermission.launch(arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
        }
        commentPictureSelectAdapter.addFooterView(commentImageView)
        commentPictureSelectAdapter.setOnItemLongClickListener { adapter, view, position ->
            taskPictureSelectAdapter.remove(position)
            if (!commentImageView.isVisible) {
                commentImageView.visibility = View.VISIBLE
            }
            return@setOnItemLongClickListener false
        }
        commentPictureSelectAdapter.setOnItemClickListener { adapter, view, position ->
            val imageList =(adapter.data as MutableList<MediaInfoBean>).map {
                it.filePath
            } as MutableList<String>
            val imageViewPagerDialog =
                ImageViewPagerDialog(this, imageList , position)
            XPopup.Builder(this).asCustom(imageViewPagerDialog).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val mediaInfoBeans = data?.getParcelableArrayListExtra<MediaInfoBean>(AppConstant.Constant.DATA)
        if (mediaInfoBeans.isNullOrEmpty()){
            return
        }
        when(resultCode == Activity.RESULT_OK){
            requestCode == TASK_CODE ->{
                if (mediaInfoBeans.size >= max){
                    taskImageView.visibility = View.GONE
                }else{
                    taskImageView.visibility = View.VISIBLE
                }
                taskPictureSelectAdapter.setNewData(mediaInfoBeans)
            }
            requestCode == PAY_CODE ->{
                if (mediaInfoBeans.size >= max){
                    payImageView.visibility = View.GONE
                }else{
                    payImageView.visibility = View.VISIBLE
                }
                payPictureSelectAdapter.setNewData(mediaInfoBeans)
            }
            requestCode == COMMENT_CODE ->{
                if (mediaInfoBeans.size >= max){
                    commentImageView.visibility = View.GONE
                }else{
                    commentImageView.visibility = View.VISIBLE
                }
                commentPictureSelectAdapter.setNewData(mediaInfoBeans)
            }
        }

    }

}