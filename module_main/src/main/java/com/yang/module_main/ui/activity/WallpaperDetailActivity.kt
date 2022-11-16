package com.yang.module_main.ui.activity

import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.bytedance.tea.crash.R.string.app_name
import com.google.gson.reflect.TypeToken
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.R
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.down.thread.MultiMoreThreadDownload
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.data.WallpaperData
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


    /**
     * 带进来所有的数据
     */
    private var mWallpaperDataList: MutableList<WallpaperData>? = null


    /**
     * 带进来选中的数据
     */
    private var mWallpaperData: WallpaperData? = null


    /**
     * 带进来的排序规则
     */
    private var order = 0


    /**
     * 带进来的第几个数据
     */
    private var index = -1


    /**
     * 搜索关键字
     */
    private var keyword = ""


    /**
     * 是否是收藏数据
     */
    private var isCollection = false


    /**
     * 是否加载更多数据
     */
    private var toLoad = true


    override fun initViewBinding(): ActWallpaperDetailBinding {
        return bind(ActWallpaperDetailBinding::inflate)
    }

    override fun initData() {
        initSmartRefreshLayout()
        initViewPager()
        mWallpaperDataList =
            intent.getStringExtra(AppConstant.Constant.DATA)?.fromJson<MutableList<WallpaperData>>(object : TypeToken<MutableList<WallpaperData>>(){}.type)
        order =
            intent.getIntExtra(AppConstant.Constant.ORDER, order)
        index =
            intent.getIntExtra(AppConstant.Constant.INDEX, index)

        mainViewModel.pageNum =
            intent.getIntExtra(AppConstant.Constant.PAGE_NUMBER, 0)+1

        isCollection =
            intent.getBooleanExtra(AppConstant.Constant.IS_COLLECTION, isCollection)



        keyword = intent.getStringExtra(AppConstant.Constant.KEYWORD)?:keyword



        toLoad = intent.getBooleanExtra(AppConstant.Constant.TO_LOAD, toLoad)


        mViewBinding.smartRefreshLayout.setEnableLoadMore(toLoad)

        mWallpaperDataList?.apply {


            if (index != -1) {
                mWallpaperData = this[index]
                mViewBinding.commonToolBar.centerContent = mWallpaperData?.title
                mWallpaperData?.let {
                    if (it.imageName!!.isImage()) {
                        preload(this@WallpaperDetailActivity,it.imageUrl)
                    }
                    if (toLoad){
                        //加载数据
                        mainViewModel.order = order
                        mainViewModel.getWallpaper(it.tabId,keyword,isCollection)
                    }
                    mWallpaperViewPagerAdapter.addDataAll(this)
                    mViewBinding.viewPager.setCurrentItem(index,false)

                }
            }

        }
    }

    override fun initView() {





    }

    private fun initSmartRefreshLayout() {
        mViewBinding.smartRefreshLayout.setEnableRefresh(false)
        mViewBinding.smartRefreshLayout.setOnLoadMoreListener {
            mWallpaperData?.let {
                mainViewModel.pageNum++
                mainViewModel.getWallpaper(it.tabId,keyword,isCollection)
            }

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
                if (data[holder.layoutPosition].imageName!!.isVideo()) {
                    GSYVideoManager.releaseAllVideos()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onViewAttachedToWindow(holder: ImageViewPagerViewHolder) {
            super.onViewAttachedToWindow(holder)
            try {
                if (data[holder.layoutPosition].imageName!!.isVideo()) {
                    payVideo(holder, holder.layoutPosition)
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

            mViewBinding.commonToolBar.centerContent = data[position].title
            holder.clControl.setPadding(0,0,0,BarUtils.getNavBarHeight())

            holder.ivHead.loadCircle(this@WallpaperDetailActivity,data[position].imageUrl)

            holder.ivHead.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(AppConstant.Constant.ID,data[position].userId).navigation()
            }
            holder.stvAttention.clicks().subscribe {
                holder.stvAttention.visibility = View.INVISIBLE
            }

            holder.iivLikeNum.setOnClickListener {
                holder.iivLikeNum.mViewItemImageBinding.ivImage.tintClick = !holder.iivLikeNum.mViewItemImageBinding.ivImage.tintClick
            }
            holder.iivShare.clicks().subscribe {

            }



            holder.clControl.setOnClickListener {
                holder.clControl.visibility = View.GONE
            }
            holder.stvSetWallpaper.clicks().subscribe {
                data[position].imageName = "${System.currentTimeMillis()}.jpg"
                downAndSetWallpaper(data[position].imageUrl?:"", data[position].imageName?:"")
            }
            holder.iivDown.clicks().subscribe {
                data[position].imageName = "${System.currentTimeMillis()}.jpg"
                downAndSetWallpaper(data[position].imageUrl?:"", data[position].imageName?:"", true)
            }
            try {
                if (data[position + 1].imageName!!.isVideo()) {

                } else {
                    preload(this@WallpaperDetailActivity,data[position + 1].imageUrl)
                }
            } catch (e: Exception) {

            }
            val isVideo = data[position].imageUrl!!.isVideo()
            if (isVideo) {
                holder.ivImage.visibility = View.GONE
                payVideo(holder, position)
                holder.gsyVideoPlayer.findViewById<View>(com.shuyu.gsyvideoplayer.R.id.surface_container)
                    .setOnClickListener {
                        holder.clControl.visibility = View.VISIBLE
                    }
            } else {
                holder.gsyVideoPlayer.visibility = View.GONE
                holder.ivImage.setOnPhotoTapListener { view, x, y ->
                    holder.clControl.visibility = View.VISIBLE
                }
                holder.ivImage.setOnClickListener {
                    holder.clControl.visibility = View.VISIBLE
                }

                holder.ivImage.loadBgImage(this@WallpaperDetailActivity,data[position].imageUrl)

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

        fun payVideo(holder: WallpaperViewPagerAdapter.ImageViewPagerViewHolder, position: Int) {
            holder.gsyVideoPlayer.setUp(data[position].imageUrl, true, null, null, "")
            holder.gsyVideoPlayer.startPlayLogic()
        }


        inner class ImageViewPagerViewHolder(itemView: ViewWallpaperDetailBinding) :
            RecyclerView.ViewHolder(itemView.root) {
            var ivImage = itemView.ivImage
            var stvSetWallpaper = itemView.stvSetWallpaper
            var clContainer = itemView.clContainer
            var clControl = itemView.clControl
            var gsyVideoPlayer = itemView.detailPlayer

            var ivHead = itemView.ivHead
            var stvAttention = itemView.stvAttention
            var iivLikeNum = itemView.iivLikeNum
            var iivShare = itemView.iivShare
            var iivDown = itemView.iivDown

        }


    }

    private fun downAndSetWallpaper(
        imageUrl: String,
        imageName: String,
        justDown: Boolean = false
    ) {
        MultiMoreThreadDownload.Builder(this)
            .parentFilePath(externalCacheDir?.absolutePath?:cacheDir.absolutePath)
            .filePath(imageName).fileUrl(AppConstant.ClientInfo.IMAGE_MODULE + imageUrl)
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
                    save2Album(file, getString(com.yang.module_main.R.string.app_name), this@WallpaperDetailActivity)
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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}