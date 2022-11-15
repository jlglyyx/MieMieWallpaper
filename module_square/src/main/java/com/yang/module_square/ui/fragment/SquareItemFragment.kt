package com.yang.module_square.ui.fragment

import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.bytedance.msdk.adapter.util.UIUtils.setText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjq.shape.view.ShapeImageView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.fragment.BaseLazyFragment
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.GridNinePictureView
import com.yang.lib_common.widget.ImageTintView
import com.yang.module_square.R
import com.yang.module_square.databinding.FraSquareItemBinding
import com.yang.module_square.viewmodel.SquareViewModel
import java.util.*


/**
 * @ClassName: MainItemFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/9/30 16:31
 */
@Route(path = AppConstant.RoutePath.SQUARE_ITEM_FRAGMENT)
class SquareItemFragment : BaseLazyFragment<FraSquareItemBinding>(), OnRefreshLoadMoreListener {

    @InjectViewModel
    lateinit var squareViewModel: SquareViewModel


    private lateinit var mAdapter: BaseQuickAdapter<WallpaperDynamicData, BaseViewHolder>

    private var tabIndex = 0
    private var tabId = ""

    override fun initViewBinding(): FraSquareItemBinding {
        return bind(FraSquareItemBinding::inflate)
    }

    override fun initView() {
        tabIndex = arguments?.getInt(AppConstant.Constant.TYPE) ?: 0
        tabId = arguments?.getString(AppConstant.Constant.ID, "") ?: ""
        squareViewModel.wallType =
            arguments?.getInt(AppConstant.Constant.WALL_TYPE) ?: squareViewModel.wallType
//        initBanner()
        initRecyclerView()
        mViewBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(this)
        registerRefreshAndRecyclerView(mViewBinding.smartRefreshLayout, mAdapter)


    }

    override fun initData() {
        onRefresh(mViewBinding.smartRefreshLayout)


        val random = Random()
        squareViewModel.mWallpaperData.observe(this) {
            val map = it.map {
                WallpaperDynamicData("","","张三","https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.dtstatic.com%2Fuploads%2Fblog%2F202102%2F17%2F20210217200025_1047e.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.dtstatic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1671070327&t=a13aee1017acd74719745a498224b256",1,false,"讲真的\n\n\n你真的很美",10246,1201520,2120000000, mutableListOf<WallpaperData>().apply {
                    for (i in 0..random.nextInt(12)){
                        add(it)
                    }
                })
            } as MutableList
            mViewBinding.smartRefreshLayout.smartRefreshLayoutData(map, mAdapter, squareViewModel)
//            mViewBinding.smartRefreshLayout.smartRefreshLayoutData(it, mAdapter, squareViewModel)
        }
    }


    private fun initRecyclerView() {

        mAdapter = object : BaseQuickAdapter<WallpaperDynamicData, BaseViewHolder>(R.layout.item_dynamic) {
            override fun convert(helper: BaseViewHolder, item: WallpaperDynamicData) {
                helper.setGone(R.id.view_top, helper.bindingAdapterPosition == 0)
                val gridNinePictureView = helper.getView<GridNinePictureView>(R.id.gridNinePictureView)
                gridNinePictureView.data = item.wallList.map {
                    getRealUrl(it.imageUrl).toString()
                } as MutableList<String>
                gridNinePictureView.imageCallback = object :GridNinePictureView.ImageCallback{
                    override fun imageClickListener(position: Int) {
                        buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
                    .withOptionsCompat(
                        ActivityOptionsCompat.makeCustomAnimation(
                            requireContext(),
                            com.yang.lib_common.R.anim.fade_in,
                            com.yang.lib_common.R.anim.fade_out
                        )
                    )
                    .withString(AppConstant.Constant.DATA, item.wallList.toJson())
                    .withInt(AppConstant.Constant.INDEX, position)
                    .withBoolean(AppConstant.Constant.TO_LOAD, false)
                    .navigation()
                    }

                }

                val imageView = helper.getView<ImageView>(R.id.iv_image)
                imageView.loadCircle(mContext,getRealUrl(item.userImage).toString())
                helper.setText(R.id.tv_name,item.userName)
                .setText(R.id.tv_attention,if (item.isAttention) "取消关注" else "+关注")
                .setText(R.id.tv_text,item.dynamicContent)
                .setText(R.id.tv_fabulous_num,formatNumUnit(item.dynamicLikeNum))
                .setText(R.id.tv_comment_num,formatNumUnit(item.dynamicCommentNum))
                .setText(R.id.tv_forward_num,formatNumUnit(item.dynamicForwardNum))
                .addOnClickListener(R.id.iv_image)
                .addOnClickListener(R.id.tv_attention)
                .addOnClickListener(R.id.tv_content)
                .addOnClickListener(R.id.ll_fabulous)
                .addOnClickListener(R.id.ll_forward)

            }
        }
        mViewBinding.recyclerView.itemAnimator = null
        mViewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(mContext).resumeRequests()
                } else {
                    Glide.with(mContext).pauseRequests()
                }
            }
        })


        mViewBinding.recyclerView.adapter = mAdapter

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                when(view.id){
                    R.id.iv_image ->{
                        buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                            AppConstant.Constant.ID,
                            ""
                        ).navigation()
                    }
                    R.id.tv_attention ->{
                        it.isAttention = !it.isAttention
                        mAdapter.notifyItemChanged(position)

                    }
                    R.id.tv_content ->{
                        buildARouter(AppConstant.RoutePath.DYNAMIC_DETAIL_ACTIVITY)
                            .withString(AppConstant.Constant.DATA, item.toJson())
                            .withBoolean(AppConstant.Constant.OPEN_COMMENT, true)
                            .navigation()
                    }
                    R.id.ll_fabulous ->{
                        val itvFabulous = view.findViewById<ImageTintView>(R.id.itv_fabulous)
                        itvFabulous.tintClick = !itvFabulous.tintClick
                    }
                    R.id.ll_forward ->{


                    }
                }

            }

        }



        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            item?.let {
                buildARouter(AppConstant.RoutePath.DYNAMIC_DETAIL_ACTIVITY)
                    .withString(AppConstant.Constant.DATA, item.toJson())
                    .withBoolean(AppConstant.Constant.OPEN_COMMENT, false)
                    .navigation()

            }
        }

    }


    override fun initViewModel() {

        InjectViewModelProxy.inject(this)




    }

    override fun initUIChangeLiveData(): UIChangeLiveData {
        return squareViewModel.uC
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        squareViewModel.pageNum = 1
        squareViewModel.getWallpaper(tabId)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        squareViewModel.pageNum++
        squareViewModel.getWallpaper(tabId)

    }
}