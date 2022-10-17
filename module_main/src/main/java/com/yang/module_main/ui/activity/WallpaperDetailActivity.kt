package com.yang.module_main.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.AppUtils
import com.bumptech.glide.Glide
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.R
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.down.thread.MultiMoreThreadDownload
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.module_main.data.WallpaperData
import com.yang.module_main.databinding.ActWallpaperDetailBinding
import com.yang.module_main.databinding.ViewWallpaperDetailBinding
import com.yang.module_main.viewmodel.MainViewModel
import kotlinx.coroutines.*
import java.io.File

/**
 * @ClassName: WallpaperDetailActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/13 9:05
 */
@Route(path = AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
class WallpaperDetailActivity : BaseActivity<ActWallpaperDetailBinding>() {

    @InjectViewModel
    lateinit var mainViewModel: MainViewModel

    private lateinit var mWallpaperViewPagerAdapter:WallpaperViewPagerAdapter

    override fun initViewBinding(): ActWallpaperDetailBinding {
        return bind(ActWallpaperDetailBinding::inflate)
    }

    override fun initData() {
        mainViewModel.getWallpaper()
    }

    override fun initView() {
        initSmartRefreshLayout()
        initViewPager()
    }

    private fun initSmartRefreshLayout() {
        mViewBinding.smartRefreshLayout.setEnableRefresh(false)
        mViewBinding.smartRefreshLayout.setOnLoadMoreListener {
            mainViewModel.pageNum++
            mainViewModel.getWallpaper()
        }
    }

    private fun initViewPager() {

        mWallpaperViewPagerAdapter =  WallpaperViewPagerAdapter(mutableListOf())
        mViewBinding.viewPager.adapter = mWallpaperViewPagerAdapter
    }

    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
        mainViewModel.mWallpaperData.observe(this){
            mViewBinding.smartRefreshLayout.finishLoadMore()
            if (it.isNullOrEmpty()){
                mViewBinding.smartRefreshLayout.setNoMoreData(true)
            }else{
                mViewBinding.smartRefreshLayout.setNoMoreData(false)
                mWallpaperViewPagerAdapter.addData(it)
            }

        }
    }

    inner class WallpaperViewPagerAdapter(var data: MutableList<WallpaperData>) :
        RecyclerView.Adapter<WallpaperViewPagerAdapter.ImageViewPagerViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): WallpaperViewPagerAdapter.ImageViewPagerViewHolder {
            val inflate = ViewWallpaperDetailBinding.inflate(LayoutInflater.from(parent.context))
            inflate.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            return ImageViewPagerViewHolder(inflate)
        }

        override fun onBindViewHolder(
            holder: WallpaperViewPagerAdapter.ImageViewPagerViewHolder,
            position: Int
        ) {
            holder.clControl.clicks().subscribe {
                    holder.clControl.visibility = View.GONE
            }
            holder.ivImage.clicks().subscribe {
                holder.clControl.visibility = View.VISIBLE
            }
            holder.stvSetWallpaper.clicks().subscribe {
                downAndSetWallpaper(data[position].imageUrl,data[position].imageName)
            }
            holder.iivDown.clicks().subscribe {
                downAndSetWallpaper(data[position].imageUrl, data[position].imageName,true)
            }
            Glide.with(holder.ivImage)
                .load(data[position].imageUrl)
                .error(R.drawable.iv_image_error)
                .fitCenter()
                .placeholder(R.drawable.iv_image_placeholder)
                .into(holder.ivImage)
        }

        override fun getItemCount(): Int {
            return data.size
        }


        fun addData(addData: MutableList<WallpaperData>){
            data.addAll(addData)
            notifyDataSetChanged()
        }


        inner class ImageViewPagerViewHolder(itemView: ViewWallpaperDetailBinding) :
            RecyclerView.ViewHolder(itemView.root) {
            var ivImage = itemView.ivImage
            var stvSetWallpaper = itemView.stvSetWallpaper
            var iivDown = itemView.iivDown
            var clContainer = itemView.clContainer
            var clControl = itemView.clControl

        }


    }

    private fun downAndSetWallpaper(imageUrl: String,imageName:String, justDown: Boolean = false) {
        MultiMoreThreadDownload.Builder(this)
            .parentFilePath(cacheDir.absolutePath)
            .filePath(imageName).fileUrl(imageUrl)
            .downListener(object : MultiMoreThreadDownload.DownListener {
                override fun downSuccess(file: File) {
                    Log.i(TAG, "downSuccess: ${file.exists()}")
                    dismissDialog()
                    if (!justDown) {
                        WallpaperUtil.setWallpaper(
                            this@WallpaperDetailActivity,
                            file.absolutePath
                        )
                    }
                    save2Album(file,getString(R.string.app_name),this@WallpaperDetailActivity)
                }

                override fun downStart() {
                    showDialog()
                }

                override fun downError(message: String) {
                    showDialog("下载失败:$message")
                    Log.i(TAG, "downError: $message")
                }

                override fun downPercent(percent: Int) {

                }
            }).showNotice(false).build().start()
    }
}