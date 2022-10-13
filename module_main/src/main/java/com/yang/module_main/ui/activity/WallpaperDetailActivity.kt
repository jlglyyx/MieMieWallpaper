package com.yang.module_main.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.AppUtils
import com.bumptech.glide.Glide
import com.yang.lib_common.R
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.down.thread.MultiMoreThreadDownload
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.module_main.data.WallpaperData
import com.yang.module_main.databinding.ActWallpaperDetailBinding
import com.yang.module_main.databinding.ViewWallpaperDetailBinding
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

    override fun initViewBinding(): ActWallpaperDetailBinding {
        return bind(ActWallpaperDetailBinding::inflate)
    }

    override fun initData() {
    }

    override fun initView() {
        initSmartRefreshLayout()
        initViewPager()
        val externalStoragePublicDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)

        Log.i(TAG, "initView: $externalStoragePublicDirectory  \n  $")

    }

    private fun initSmartRefreshLayout() {
        mViewBinding.smartRefreshLayout.setEnableRefresh(false)
        mViewBinding.smartRefreshLayout.setOnLoadMoreListener {

        }
    }

    private fun initViewPager() {

        val data = mutableListOf<WallpaperData>().apply {
            add(WallpaperData().apply {
                imageUrl =
                    "https://img1.baidu.com/it/u=3622442929,3246643478&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=32fc8f0a742874ae750f14f937b6cb6a"
                imageName="1.jpg"
            })
            add(WallpaperData().apply {
                imageUrl =
                    "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
                imageName="1.mp4"
            })
            add(WallpaperData().apply {
                imageUrl =
                    "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl =
                    "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl =
                    "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl =
                    "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl =
                    "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl =
                    "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
        }
        mViewBinding.viewPager.adapter = WallpaperViewPagerAdapter(data)
    }

    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
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
            holder.ivImage.clicks().subscribe {

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


        inner class ImageViewPagerViewHolder(itemView: ViewWallpaperDetailBinding) :
            RecyclerView.ViewHolder(itemView.root) {
            var ivImage = itemView.ivImage
            var stvSetWallpaper = itemView.stvSetWallpaper
            var iivDown = itemView.iivDown

        }


    }

    private fun downAndSetWallpaper(imageUrl: String,imageName:String, justDown: Boolean = false) {


        MultiMoreThreadDownload.Builder(this)
            .parentFilePath(obbDir.absolutePath)
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
                    } else {
                        showShort("下载成功")
                    }
//                    lifecycleScope.launch(Dispatchers.IO) {
//                        val async = async(Dispatchers.IO) {
//                            save2Album(
//                                BitmapFactory.decodeFile(file.absolutePath),
//                                getString(R.string.app_name),
//                                Bitmap.CompressFormat.JPEG, 100, true,"_"+file.name
//                            )
//                            true
//                        }
//                        if (async.await()) {
//                            toDeleteFile(file,this@WallpaperDetailActivity)
//                        }
//                    }
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