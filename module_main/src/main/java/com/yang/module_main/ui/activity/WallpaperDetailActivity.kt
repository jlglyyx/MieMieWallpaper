package com.yang.module_main.ui.activity

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.R
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.down.thread.MultiMoreThreadDownload
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.module_main.data.WallpaperData
import com.yang.module_main.databinding.ActWallpaperDetailBinding
import com.yang.module_main.databinding.ViewWallpaperDetailBinding
import com.yang.module_main.viewmodel.MainViewModel
import java.io.File

/**
 * @ClassName: WallpaperDetailActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/13 9:05
 */
@Route(path = AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
class WallpaperDetailActivity : BaseActivity<ActWallpaperDetailBinding>() {

    private var isFirst = true

    @InjectViewModel
    lateinit var mainViewModel: MainViewModel

    private lateinit var mWallpaperViewPagerAdapter: WallpaperViewPagerAdapter

    override fun initViewBinding(): ActWallpaperDetailBinding {
        return bind(ActWallpaperDetailBinding::inflate)
    }

    override fun initData() {
        initSmartRefreshLayout()
        initViewPager()
        val mWallpaperData =
            intent.getStringExtra(AppConstant.Constant.DATA)?.fromJson<WallpaperData>()
        mWallpaperData?.apply {
            if (imageName.isImage()){
                Glide.with(this@WallpaperDetailActivity)
                    .load(imageUrl)
                    .preload(
                        getScreenPx(this@WallpaperDetailActivity)[0],
                        getScreenPx(this@WallpaperDetailActivity)[1]
                    )
            }
            mWallpaperViewPagerAdapter.addData(this)
        }
        mainViewModel.getWallpaper()
    }

    override fun initView() {


    }

    private fun initSmartRefreshLayout() {
        mViewBinding.smartRefreshLayout.setEnableRefresh(false)
        mViewBinding.smartRefreshLayout.setOnLoadMoreListener {
            mainViewModel.pageNum++
            mainViewModel.getWallpaper()
        }
    }

    private fun initViewPager() {

        mWallpaperViewPagerAdapter = WallpaperViewPagerAdapter(mutableListOf())
        mViewBinding.viewPager.adapter = mWallpaperViewPagerAdapter
    }

    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
        mainViewModel.mWallpaperData.observe(this) {
            mViewBinding.smartRefreshLayout.finishLoadMore()
            if (it.isNullOrEmpty()) {
                mViewBinding.smartRefreshLayout.setNoMoreData(true)
            } else {
                mViewBinding.smartRefreshLayout.setNoMoreData(false)
                mWallpaperViewPagerAdapter.addDataAll(it.apply {
                    if (isFirst) {
                        findLast {
                            TextUtils.equals(
                                mWallpaperViewPagerAdapter.data[0].imageUrl,
                                it.imageUrl
                            )
                        }?.let { find ->
                            remove(find)
                            isFirst = false
                        }
                    }
                })
            }

        }
    }

    inner class WallpaperViewPagerAdapter(var data: MutableList<WallpaperData>) :
        RecyclerView.Adapter<WallpaperViewPagerAdapter.ImageViewPagerViewHolder>() {


        override fun onViewDetachedFromWindow(holder: ImageViewPagerViewHolder) {
            super.onViewDetachedFromWindow(holder)
            try {
                if (data[holder.layoutPosition].imageName.isVideo()) {
                    GSYVideoManager.releaseAllVideos()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onViewAttachedToWindow(holder: ImageViewPagerViewHolder) {
            super.onViewAttachedToWindow(holder)
            try {
                if (data[holder.layoutPosition].imageName.isVideo()) {
                    payVideo(holder,holder.layoutPosition)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


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
            holder.clControl.setOnClickListener {
                holder.clControl.visibility = View.GONE
            }
            holder.stvSetWallpaper.clicks().subscribe {
                downAndSetWallpaper(data[position].imageUrl, data[position].imageName)
            }
            holder.iivDown.clicks().subscribe {
                downAndSetWallpaper(data[position].imageUrl, data[position].imageName, true)
            }
            try {
                if (data[position + 1].imageName.isVideo()) {

                } else {
                    Glide.with(this@WallpaperDetailActivity)
                        .load(data[position + 1].imageUrl)
                        .dontAnimate()
                        .preload(
                            getScreenPx(this@WallpaperDetailActivity)[0],
                            getScreenPx(this@WallpaperDetailActivity)[1]
                        )
                }
            } catch (e: Exception) {

            }
            val isVideo = data[position].imageUrl.isVideo()
            if (isVideo) {
                holder.ivImage.visibility = View.GONE
                payVideo(holder,position)
                holder.gsyVideoPlayer.findViewById<View>(com.shuyu.gsyvideoplayer.R.id.surface_container)
                    .setOnClickListener {
                        holder.clControl.visibility = View.VISIBLE
                    }
            } else {
                holder.gsyVideoPlayer.visibility = View.GONE
                holder.ivImage.setOnPhotoTapListener { view, x, y ->
                    holder.clControl.visibility = View.VISIBLE
                }
                Glide.with(this@WallpaperDetailActivity)
                    .load(data[position].imageUrl)
                    .error(R.drawable.iv_image_error)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(holder.ivImage.drawable)
                    .into(holder.ivImage)
            }

        }

        override fun getItemCount(): Int {
            return data.size
        }


        fun addData(addData: WallpaperData) {
            data.add(addData)
            notifyDataSetChanged()
        }

        fun addDataAll(addData: MutableList<WallpaperData>) {
            data.addAll(addData)
            notifyDataSetChanged()
        }

        fun payVideo(holder: WallpaperViewPagerAdapter.ImageViewPagerViewHolder,position:Int) {
            holder.gsyVideoPlayer.setUp(data[position].imageUrl, true, null, null, "")
            holder.gsyVideoPlayer.startPlayLogic()
        }


        inner class ImageViewPagerViewHolder(itemView: ViewWallpaperDetailBinding) :
            RecyclerView.ViewHolder(itemView.root) {
            var ivImage = itemView.ivImage
            var stvSetWallpaper = itemView.stvSetWallpaper
            var iivDown = itemView.iivDown
            var clContainer = itemView.clContainer
            var clControl = itemView.clControl
            var gsyVideoPlayer = itemView.detailPlayer

        }


    }

    private fun downAndSetWallpaper(
        imageUrl: String,
        imageName: String,
        justDown: Boolean = false
    ) {
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
                    save2Album(file, getString(R.string.app_name), this@WallpaperDetailActivity)
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

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mainViewModel.uC
    }


    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }
}