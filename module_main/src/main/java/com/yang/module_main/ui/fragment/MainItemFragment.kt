package com.yang.module_main.ui.fragment

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.navi.R.id.iv
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yang.lib_common.adapter.TabAndViewPagerAdapter
import com.yang.lib_common.adapter.TabAndViewPagerFragmentAdapter
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.px2dip
import com.yang.module_main.R
import com.yang.module_main.databinding.FraMainBinding
import com.yang.module_main.databinding.FraMainItemBinding

/**
 * @ClassName: MainItemFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.MAIN_ITEM_FRAGMENT)
class MainItemFragment:BaseLazyFragment<FraMainItemBinding>() {

    private lateinit var mAdapter:BaseQuickAdapter<String,BaseViewHolder>
    private lateinit var mTopAdapter:BaseQuickAdapter<String,BaseViewHolder>
    private var type = 0

    override fun initViewBinding(): FraMainItemBinding {
        return bind(FraMainItemBinding::inflate)
    }

    override fun initView() {
        type = arguments?.getInt(AppConstant.Constant.TYPE)?:0

        initRecyclerView()
    }

    override fun initData() {



    }

    private fun initRecyclerView(){
        mAdapter = object : BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_image){
            override fun convert(helper: BaseViewHolder, item: String) {
                Glide.with(mContext).load(item).into(helper.getView(R.id.iv_image))
            }
        }
        mViewBinding.recyclerView.adapter = mAdapter

        mTopAdapter = object : BaseQuickAdapter<String,BaseViewHolder>(if(type == 0) R.layout.item_top_image else R.layout.item_top_type_image){
            override fun convert(helper: BaseViewHolder, item: String) {
                Glide.with(mContext).load(item).into(helper.getView(R.id.iv_image))
            }
        }
        mViewBinding.topRecyclerView.adapter = mTopAdapter
        mAdapter.setNewData(mutableListOf<String>().apply {
            add("https://img1.baidu.com/it/u=3622442929,3246643478&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=32fc8f0a742874ae750f14f937b6cb6a")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")
            add("https://img1.baidu.com/it/u=3009731526,373851691&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665334800&t=100657a3bd66774828ea8d66ba8ddae1")

        })
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