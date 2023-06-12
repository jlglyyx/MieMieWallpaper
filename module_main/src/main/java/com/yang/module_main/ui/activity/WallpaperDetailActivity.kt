package com.yang.module_main.ui.activity

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.google.gson.reflect.TypeToken
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.umeng.analytics.AnalyticsConfig.enable
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.R
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.down.thread.MultiMoreThreadDownload
import com.yang.lib_common.helper.AdManager
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.module_main.databinding.ActWallpaperDetailBinding
import com.yang.module_main.databinding.ViewWallpaperDetailBinding
import com.yang.module_main.viewmodel.WallpaperViewModel
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
    lateinit var mWallpaperViewModel: WallpaperViewModel

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
    private var userId = ""


    /**
     * 是否加载更多数据
     */
    private var toLoad = true

    /**
     * 是否显示右侧操作界面
     */
    private var isDown = false


    override fun initViewBinding(): ActWallpaperDetailBinding {
        return bind(ActWallpaperDetailBinding::inflate)
    }

    override fun initData() {
        initSmartRefreshLayout()
        initViewPager()
        mWallpaperDataList =
            intent.getStringExtra(AppConstant.Constant.DATA)
                ?.fromJson<MutableList<WallpaperData>>(object :
                    TypeToken<MutableList<WallpaperData>>() {}.type)
        order =
            intent.getIntExtra(AppConstant.Constant.ORDER, order)
        index =
            intent.getIntExtra(AppConstant.Constant.INDEX, index)

        mWallpaperViewModel.pageNum =
            intent.getIntExtra(AppConstant.Constant.PAGE_NUMBER, 0) + 1

        userId =
            intent.getStringExtra(AppConstant.Constant.USER_ID) ?: userId



        keyword = intent.getStringExtra(AppConstant.Constant.KEYWORD) ?: keyword



        toLoad = intent.getBooleanExtra(AppConstant.Constant.TO_LOAD, toLoad)

        isDown = intent.getBooleanExtra(AppConstant.Constant.IS_DOWN, isDown)


        mViewBinding.smartRefreshLayout.setEnableLoadMore(toLoad)

        mWallpaperDataList?.apply {


            if (index != -1) {
                mWallpaperData = this[index]
                mViewBinding.commonToolBar.centerContent = mWallpaperData?.wallName
                mWallpaperData?.let {
                    if (it.imageName!!.isImage()) {
                        preload(this@WallpaperDetailActivity, it.wallUrl)
                    }
                    if (toLoad) {
                        //加载数据  收藏数据
                        mWallpaperViewModel.order = order
                        mWallpaperViewModel.getWallpaper(it.tabId, keyword, userId)
                    }
                    mWallpaperViewPagerAdapter.addDataAll(this)
                    mViewBinding.viewPager.setCurrentItem(index, false)

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
                mWallpaperViewModel.pageNum++
                mWallpaperViewModel.getWallpaper(it.tabId, keyword, userId)
            }

        }
    }

    private fun initViewPager() {

        mWallpaperViewPagerAdapter = WallpaperViewPagerAdapter(mutableListOf())
        mViewBinding.viewPager.adapter = mWallpaperViewPagerAdapter
        for (i in 0 until mViewBinding.viewPager.childCount) {
            val view: View = mViewBinding.viewPager.getChildAt(i)
            if (view is RecyclerView) {
                view.itemAnimator = null
                break
            }
        }
        mViewBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val wallpaperData = mWallpaperViewPagerAdapter.data[position]
                mViewBinding.commonToolBar.centerContent =
                    wallpaperData.wallDesc
                if (!isDown) {
                    mWallpaperViewModel.queryWallpaperDetail(wallpaperData.id)
                    mWallpaperViewModel.currentPosition = position
                }

            }

        })
    }

    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
        mWallpaperViewModel.mWallpaperData.observe(this) {
            mViewBinding.smartRefreshLayout.finishLoadMore()
            if (it.isNullOrEmpty()) {
                mViewBinding.smartRefreshLayout.setNoMoreData(true)
            } else {
                mViewBinding.smartRefreshLayout.setNoMoreData(false)
                mWallpaperViewPagerAdapter.addDataAll(it.apply {
                    if (isFirst) {
                        findLast {
                            TextUtils.equals(
                                mWallpaperViewPagerAdapter.data[0].wallUrl,
                                it.wallUrl
                            )
                        }?.let { find ->
                            remove(find)
                            isFirst = false
                        }
                    }
                })
            }

        }

        mWallpaperViewModel.mWallpaperDetailData.observe(this){
            it?.let {
                mWallpaperViewPagerAdapter.data[mWallpaperViewModel.currentPosition] = it
                mWallpaperViewPagerAdapter.notifyItemChanged(mWallpaperViewModel.currentPosition)
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

            try {
                if (data[position + 1].imageName!!.isVideo()) {

                } else {
                    preload(this@WallpaperDetailActivity, data[position + 1].wallUrl)
                }
            } catch (e: Exception) {

            }
            initWallpaperView(holder, position)
            addClickListener(holder, position)

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

        private fun payVideo(
            holder: WallpaperViewPagerAdapter.ImageViewPagerViewHolder,
            position: Int
        ) {
            holder.gsyVideoPlayer.setUp(data[position].wallUrl, true, null, null, "")
            holder.gsyVideoPlayer.startPlayLogic()
        }


        private fun initWallpaperView(
            holder: WallpaperViewPagerAdapter.ImageViewPagerViewHolder,
            position: Int
        ) {

            val currentData = data[position]
            holder.apply {

                if (isDown) {
                    llRightControl.visibility = View.GONE
                } else {
                    llRightControl.visibility = View.VISIBLE
                }

                clControl.setPadding(0, 0, 0, BarUtils.getNavBarHeight())

                ivHead.loadCircle(this@WallpaperDetailActivity, currentData.userAttr)

                iivLikeNum.mViewItemImageBinding.tvTv.text =
                    if (currentData.likeNum == null) "0" else currentData.likeNum?.formatNumUnit()

                iivLikeNum.mViewItemImageBinding.ivImage.tintClick = currentData.isCollection

                viewStatus(if (currentData.isAttention) View.GONE else View.VISIBLE,stvAttention)


                val isVideo = currentData.wallUrl!!.isVideo()
                if (isVideo) {
                    ivImage.visibility = View.GONE
                    payVideo(holder, position)
                    gsyVideoPlayer.findViewById<View>(com.shuyu.gsyvideoplayer.R.id.surface_container)
                        .setOnClickListener {
                            clControl.visibility = View.VISIBLE
                        }
                } else {
                    gsyVideoPlayer.visibility = View.GONE
                    ivImage.setOnPhotoTapListener { view, x, y ->
                        clControl.visibility = View.VISIBLE
                    }
                    ivImage.setOnClickListener {
                        clControl.visibility = View.VISIBLE
                    }
                    ivImage.loadBgImage(this@WallpaperDetailActivity, currentData.wallUrl)
                }
            }
        }


        private fun addClickListener(
            holder: WallpaperViewPagerAdapter.ImageViewPagerViewHolder,
            position: Int
        ) {
            val currentData = data[position]
            holder.apply {

                ivHead.clicks().subscribe {
                    buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                        AppConstant.Constant.USER_ID,
                        currentData.userId
                    ).navigation()
                }

                stvAttention.clicks().subscribe {
                    mWallpaperViewModel.addFollow(UserInfoHold.userId, currentData.userId,currentData.id,!currentData.isAttention)
                    mWallpaperViewModel.currentPosition = position
//                    holder.stvAttention.visibility = View.INVISIBLE
                }


                iivLikeNum.setOnClickListener {
                    mWallpaperViewModel.addOrCancelCollect(
                        currentData.id,
                        currentData.userId,
                        currentData.isCollection
                    )
                    mWallpaperViewModel.currentPosition = position
                }


                iivShare.clicks().subscribe {

                }



                clControl.setOnClickListener {
                    holder.clControl.visibility = View.GONE
                }
                stvSetWallpaper.clicks().subscribe {
                    if (isDown) {
                        WallpaperUtil.setWallpaper(
                            this@WallpaperDetailActivity,
                            currentData.wallUrl!!
                        )
                    } else {
                        AdManager.instance.showReward(this@WallpaperDetailActivity) {
                            currentData.imageName = "${System.currentTimeMillis()}.jpg"
                            downAndSetWallpaper(
                                currentData.wallUrl ?: "",
                                currentData.imageName ?: ""
                            )
                        }
                    }
                }
                iivDown.clicks().subscribe {
                    AdManager.instance.showReward(this@WallpaperDetailActivity) {
                        currentData.imageName = "${System.currentTimeMillis()}.jpg"
                        downAndSetWallpaper(
                            currentData.wallUrl ?: "",
                            currentData.imageName ?: "",
                            true
                        )
                    }
                }

            }
        }


        inner class ImageViewPagerViewHolder(itemView: ViewWallpaperDetailBinding) :
            RecyclerView.ViewHolder(itemView.root) {
            var ivImage = itemView.ivImage
            var stvSetWallpaper = itemView.stvSetWallpaper
            var clContainer = itemView.clContainer
            var clControl = itemView.clControl
            var llRightControl = itemView.llRightControl
            var gsyVideoPlayer = itemView.detailPlayer

            var ivHead = itemView.ivHead
            var stvAttention = itemView.stvAttention
            var iivLikeNum = itemView.iivLikeNum
            var iivShare = itemView.iivShare
            var iivDown = itemView.iivDown

        }


    }

    /**
     * 下载和设置壁纸
     */
    private fun downAndSetWallpaper(
        imageUrl: String,
        imageName: String,
        justDown: Boolean = false
    ) {
        MultiMoreThreadDownload.Builder(this)
            .parentFilePath(externalCacheDir?.absolutePath ?: cacheDir.absolutePath)
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
                    save2Album(
                        file,
                        getString(com.yang.module_main.R.string.app_name),
                        this@WallpaperDetailActivity
                    )
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
        return mWallpaperViewModel.uC
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