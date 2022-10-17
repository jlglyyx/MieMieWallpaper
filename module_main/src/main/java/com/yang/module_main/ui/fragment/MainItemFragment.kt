package com.yang.module_main.ui.fragment

import android.app.WallpaperManager
import android.app.WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
import android.app.WallpaperManager.ACTION_CROP_AND_SET_WALLPAPER
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import androidx.core.content.ContentProviderCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.huawei.hms.mlsdk.common.MLApplicationSetting.BundleKeyConstants.AppInfo.packageName
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.MBannerAdapter
import com.yang.lib_common.app.BaseApplication
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.BannerBean
import com.yang.lib_common.down.thread.MultiMoreThreadDownload
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.service.CustomWallpaperService
import com.yang.lib_common.util.*
import com.yang.module_main.R
import com.yang.module_main.data.WallpaperData
import com.yang.module_main.databinding.FraMainItemBinding
import com.yang.module_main.ui.activity.MainActivity
import com.yang.module_main.viewmodel.MainViewModel
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator


/**
 * @ClassName: MainItemFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.MAIN_ITEM_FRAGMENT)
class MainItemFragment : BaseLazyFragment<FraMainItemBinding>() {

    @InjectViewModel
    lateinit var mainViewModel: MainViewModel

    private lateinit var mAdapter: BaseQuickAdapter<WallpaperData, BaseViewHolder>
    private lateinit var mTopAdapter: BaseQuickAdapter<String, BaseViewHolder>
    private var type = 0

    override fun initViewBinding(): FraMainItemBinding {
        return bind(FraMainItemBinding::inflate)
    }

    override fun initView() {
        type = arguments?.getInt(AppConstant.Constant.TYPE) ?: 0
        initBanner()
        initRecyclerView()
    }

    override fun initData() {


    }

    private fun initBanner() {
        mViewBinding.banner.addBannerLifecycleObserver(this)
            .setAdapter(MBannerAdapter(mutableListOf<BannerBean>().apply {
                add(BannerBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57"))
                add(BannerBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201607%2F29%2F20160729224352_rVhZA.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=326f6993d0c1ee5c0048b33fe9f0a6dc"))
                add(BannerBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F15%2F20150815231707_JWQjx.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=e3951129e167261b3f4c7c739becc463"))
            }), true)
            .addBannerLifecycleObserver(this)
            .isAutoLoop(true)
            .setBannerRound2(20f)
            .setIndicatorGravity(IndicatorConfig.Direction.LEFT)
            .indicator = CircleIndicator(requireContext())
    }

    private fun initRecyclerView() {
        mAdapter = object : BaseQuickAdapter<WallpaperData, BaseViewHolder>(R.layout.item_image) {
            override fun convert(helper: BaseViewHolder, item: WallpaperData) {
                loadRadius(mContext,item.imageUrl,20f,helper.getView(R.id.iv_image))
            }
        }
        mViewBinding.recyclerView.adapter = mAdapter

        mTopAdapter = object :
            BaseQuickAdapter<String, BaseViewHolder>(if (type == 0) R.layout.item_top_image else R.layout.item_top_type_image) {
            override fun convert(helper: BaseViewHolder, item: String) {
                if (type == 0){
                    loadCircle(mContext,item,helper.getView(R.id.iv_image))
                }else{
                    loadRadius(mContext,item,5f,helper.getView(R.id.iv_image))
                }
            }
        }
        mViewBinding.topRecyclerView.adapter = mTopAdapter
        mAdapter.setNewData(mutableListOf<WallpaperData>().apply {
            add(WallpaperData().apply {
                imageUrl = "https://img1.baidu.com/it/u=3622442929,3246643478&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=32fc8f0a742874ae750f14f937b6cb6a"
            })
            add(WallpaperData().apply {
                imageUrl = "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl = "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl = "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl = "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl = "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl = "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })
            add(WallpaperData().apply {
                imageUrl = "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1"
            })

        })

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY).withString(AppConstant.Constant.ID,it.id).navigation()

            }
        }

        mTopAdapter.setNewData(mutableListOf<String>().apply {
            add("https://img1.baidu.com/it/u=3622442929,3246643478&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=32fc8f0a742874ae750f14f937b6cb6a")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")

        })
    }


    override fun initViewModel() {

        InjectViewModelProxy.inject(this)

    }
}