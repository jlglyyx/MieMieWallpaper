package com.yang.module_main.ui.activity

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.luck.picture.lib.entity.LocalMedia
import com.lxj.xpopup.XPopup
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.PictureAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.dialog.ImageViewPagerDialog
import com.yang.lib_common.dialog.SearchRecyclerViewDialog
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.CommonToolBar
import com.yang.module_main.databinding.ActAddWallpaperBinding
import com.yang.module_main.viewmodel.WallpaperViewModel

/**
 * @ClassName: AddWallpaperActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/14 9:31
 */
@Route(path = AppConstant.RoutePath.ADD_WALLPAPER_ACTIVITY)
class AddWallpaperActivity : BaseActivity<ActAddWallpaperBinding>() {

    @InjectViewModel
    lateinit var wallpaperViewModel: WallpaperViewModel

    private lateinit var mPictureAdapter: PictureAdapter


    private lateinit var imageView: ImageView

    private var selectLocalMedia = mutableListOf<LocalMedia>()

    override fun initViewBinding(): ActAddWallpaperBinding {
        return bind(ActAddWallpaperBinding::inflate)
    }

    override fun initData() {
    }

    override fun initView() {
        mViewBinding.commonToolBar.tVRightCallBack = object : CommonToolBar.TVRightCallBack {
            override fun tvRightClickListener() {
                val etDynamicText = mViewBinding.etDynamic.text.toString()
                if (mPictureAdapter.data.isEmpty() && TextUtils.isEmpty(etDynamicText)){
                    showShort("要说些什么才能发表哦")
                    return
                }
                wallpaperViewModel.publishDynamic(mPictureAdapter.data,UserInfoHold.userId?:"",etDynamicText)
            }
        }

        mViewBinding.tvLocation.clicks().subscribe {
            val searchRecyclerViewDialog = SearchRecyclerViewDialog(this)
            searchRecyclerViewDialog.searchRecyclerViewDialogCallBack = object : SearchRecyclerViewDialog.SearchRecyclerViewDialogCallBack{
                override fun getText(s: String) {
                    mViewBinding.tvLocation.text = s
                }
            }
            XPopup.Builder(this).asCustom(searchRecyclerViewDialog).show()
        }
        mViewBinding.tvVisibility.clicks().subscribe {
            XPopup.Builder(this).asBottomList("", arrayOf("公开","仅自己")
            ) { position, text ->
                mViewBinding.tvVisibility.text = text
            }.show()
        }

        initRecyclerView()
    }



    override fun initUIChangeLiveData(): UIChangeLiveData {
        return wallpaperViewModel.uC
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
        wallpaperViewModel.pictureListLiveData.observe(this)  {
            finish()
        }
    }



    private fun initRecyclerView() {
        mPictureAdapter = PictureAdapter(null)
        mViewBinding.recyclerView.adapter = mPictureAdapter
        mViewBinding.recyclerView.layoutManager = GridLayoutManager(this, 3)

        imageView = ImageView(this).apply {
            setImageResource(com.yang.lib_common.R.drawable.iv_add)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundResource(android.R.color.darker_gray)
            val i =
                (getScreenPx(this@AddWallpaperActivity)[0] - 20f.dip2px(this@AddWallpaperActivity)) / 3
            layoutParams = ViewGroup.LayoutParams(i, i)
            setOnClickListener {
                openGallery(9, {
                    it?.let { data ->
                        selectLocalMedia = data
                        if (data.isNotEmpty()) {
                            val map = data.map { item ->
                                item.compressPath ?: item.realPath.toString()
                            } as MutableList<String>
                            //wallpaperViewModel.uploadFile(map)
                            if (map.size >= 9) {
                                imageView.visibility = View.GONE
                            } else {
                                imageView.visibility = View.VISIBLE
                            }
                            mPictureAdapter.setNewData(map)
                        }

                    }

                }, selectData = selectLocalMedia)
            }
        }
        mPictureAdapter.addFooterView(imageView)
        mPictureAdapter.setOnItemLongClickListener { adapter, view, position ->
            mPictureAdapter.remove(position)
            if (!imageView.isVisible) {
                imageView.visibility = View.VISIBLE
            }
            return@setOnItemLongClickListener false
        }
        mPictureAdapter.setOnItemClickListener { adapter, view, position ->
            val imageList =adapter.data as MutableList<String>
            val imageViewPagerDialog =
                ImageViewPagerDialog(this, imageList , position)
            XPopup.Builder(this).asCustom(imageViewPagerDialog).show()
        }
    }


}