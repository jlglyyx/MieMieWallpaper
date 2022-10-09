package com.yang.module_main.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxj.xpopup.XPopup
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.MediaInfoBean
import com.yang.lib_common.dialog.ImageViewPagerDialog
import com.yang.lib_common.dialog.PayTaskDialog
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.CommonToolBar
import com.yang.module_main.R
import com.yang.lib_common.adapter.PictureSelectAdapter
import com.yang.module_main.data.TaskData
import com.yang.module_main.databinding.ActAddTaskBinding
import com.yang.module_main.viewmodel.MainViewModel
import java.util.*

@Route(path = AppConstant.RoutePath.ADD_TASK_ACTIVITY)
class AddTaskActivity : BaseActivity<ActAddTaskBinding>() {

    @InjectViewModel
    lateinit var mainViewModel: MainViewModel

    private lateinit var pictureSelectAdapter: PictureSelectAdapter

    private var data: MutableList<MediaInfoBean> = mutableListOf()

    private lateinit var imageView:ImageView

    private var mPayTaskDialog: PayTaskDialog? = null

    private val taskData = TaskData()

    override fun initViewBinding(): ActAddTaskBinding {
        return bind(ActAddTaskBinding::inflate)
    }


    override fun initData() {
    }

    override fun initView() {
        mViewBinding.commonToolBar.tVRightCallBack = object : CommonToolBar.TVRightCallBack {
            override fun tvRightClickListener() {
                checkForm()
            }
        }

        initRecyclerView()

        ViewLayoutChangeUtil().add(findViewById(android.R.id.content))
    }



    override fun initUIChangeLiveData(): UIChangeLiveData {
        return mainViewModel.uC
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
        mainViewModel.pictureListLiveData.observe(this, Observer {
            addTask()
        })
    }



    private fun initRecyclerView() {
        pictureSelectAdapter = PictureSelectAdapter(R.layout.item_picture_select,data,false)
        mViewBinding.recyclerView.adapter = pictureSelectAdapter
        mViewBinding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        val registerForActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK){
                    val mediaInfoBeans = it.data?.getParcelableArrayListExtra<MediaInfoBean>(AppConstant.Constant.DATA)
                    if (mediaInfoBeans?.size!! >= 9){
                        imageView.visibility = View.GONE
                    }else{
                        imageView.visibility = View.VISIBLE
                    }
                    pictureSelectAdapter.setNewData(mediaInfoBeans)
                }
            }

        val requestPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            it.forEach { data ->
                if (!data.value) {
                    showShort("打开权限")
                    return@registerForActivityResult
                }
            }
            registerForActivityResult.launch(Intent(this@AddTaskActivity,PictureSelectActivity::class.java)
                .putParcelableArrayListExtra(AppConstant.Constant.DATA, pictureSelectAdapter.data as ArrayList
                ))
        }

        imageView = ImageView(this).apply {
            setImageResource(R.drawable.iv_add)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundResource(android.R.color.darker_gray)
            val i =
                (getScreenPx(this@AddTaskActivity)[0] - 20f.dip2px(this@AddTaskActivity)) / 3
            layoutParams = ViewGroup.LayoutParams(i, i)
            setOnClickListener {
                requestPermission.launch(arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
        }
        pictureSelectAdapter.addFooterView(imageView)
        pictureSelectAdapter.setOnItemLongClickListener { adapter, view, position ->
            pictureSelectAdapter.remove(position)
            if (!imageView.isVisible) {
                imageView.visibility = View.VISIBLE
            }
            return@setOnItemLongClickListener false
        }
        pictureSelectAdapter.setOnItemClickListener { adapter, view, position ->
            val imageList =(adapter.data as MutableList<MediaInfoBean>).map {
                it.filePath
            } as MutableList<String>
                val imageViewPagerDialog =
                ImageViewPagerDialog(this, imageList , position)
            XPopup.Builder(this).asCustom(imageViewPagerDialog).show()
        }
    }

    private fun checkForm(){
        val etTaskTitle = mViewBinding.etTaskTitle.text.toString()
        val etTaskContent = mViewBinding.etTaskContent.text.toString()
        val etTaskShop = mViewBinding.etTaskShop.text.toString()
        val etTaskLink = mViewBinding.etTaskLink.text.toString()
        val etTaskKeyword = mViewBinding.etTaskKeyWord.text.toString()
        val etTaskNumber = mViewBinding.etTaskNumber.text.toString()
        val etTaskPrice = mViewBinding.etTaskPrice.text.toString()
        val etTaskCommission = mViewBinding.etTaskCommission.text.toString()

        if (TextUtils.isEmpty(etTaskTitle)){
            showShort("请输入任务标题")
            return
        }
        if (TextUtils.isEmpty(etTaskContent)){
            showShort("请输入任务内容")
            return
        }
        if (TextUtils.isEmpty(etTaskShop)){
            showShort("请输入店铺名称")
            return
        }
        if (TextUtils.isEmpty(etTaskLink)){
            showShort("请输入宝贝链接")
            return
        }
        if (TextUtils.isEmpty(etTaskKeyword)){
            showShort("请输入宝贝关键词")
            return
        }
        if (pictureSelectAdapter.data.isNullOrEmpty()){
            showShort("请上传宝贝图片")
            return
        }
        if (TextUtils.isEmpty(etTaskNumber)){
            showShort("请输入放单数量")
            return
        }
        if (TextUtils.isEmpty(etTaskPrice)){
            showShort("请输入商品金额")
            return
        }
        if (TextUtils.isEmpty(etTaskCommission)){
            showShort("请输入商品佣金")
            return
        }
        taskData.apply {
            taskTitle = etTaskTitle
            taskContent = etTaskContent
            taskShop = etTaskShop
            taskLink = etTaskLink
            taskKeyword = etTaskKeyword
            taskNumber = etTaskNumber.toInt()
            taskPrice = etTaskPrice
            taskCommission = etTaskCommission
        }

        if (null == mPayTaskDialog){
            mPayTaskDialog = PayTaskDialog(this@AddTaskActivity)
            mPayTaskDialog!!.onItemClickListener = object :PayTaskDialog.OnItemClickListener{
                override fun onCancelClickListener() {
                    mPayTaskDialog!!.dismiss()
                }

                override fun onConfirmClickListener() {
                    uploadFile(pictureSelectAdapter.data)
                }

            }
        }
        XPopup.Builder(this@AddTaskActivity).asCustom(mPayTaskDialog).show()

    }

    private fun addTask() {
        taskData.userId = getUserInfo()?.id
        taskData.imageUrls = mainViewModel.pictureListLiveData.value?.formatWithSymbol("#")
        mainViewModel.insertTask(taskData)
    }

    private fun uploadFile(data: MutableList<MediaInfoBean>) {
        mainViewModel.uploadFile(data)
    }
}