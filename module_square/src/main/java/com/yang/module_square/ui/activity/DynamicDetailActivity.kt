package com.yang.module_square.ui.activity

import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.kuaishou.weapon.p0.WeaponHI.mContext
import com.lxj.xpopup.XPopup
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.CommentAdapter
import com.yang.lib_common.adapter.ImageViewPagerAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.CommentData
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.dialog.EditBottomDialog
import com.yang.lib_common.dialog.ImageViewPagerDialog
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.GridNinePictureView
import com.yang.module_square.R
import com.yang.module_square.databinding.ActDynamicDetailBinding
import com.yang.module_square.viewmodel.SquareViewModel
import com.youth.banner.transformer.ScaleInTransformer


@Route(path = AppConstant.RoutePath.DYNAMIC_DETAIL_ACTIVITY)
class DynamicDetailActivity : BaseActivity<ActDynamicDetailBinding>() {

    private lateinit var commentAdapter: CommentAdapter

    private var mWallpaperDynamicData: WallpaperDynamicData? = null

    @InjectViewModel
    lateinit var squareViewModel: SquareViewModel


    override fun initViewBinding(): ActDynamicDetailBinding {
        return bind(ActDynamicDetailBinding::inflate)
    }



    override fun initData() {
        val sid = intent.getStringExtra(AppConstant.Constant.ID)
        val openComment = intent.getBooleanExtra(AppConstant.Constant.OPEN_COMMENT,false)
        val data = intent.getStringExtra(AppConstant.Constant.DATA)
        mWallpaperDynamicData = data?.fromJson(WallpaperDynamicData::class.java)
//        squareViewModel.getImageItemData(sid ?: "")
        addViewHistory()

        if (openComment){
            openCommentDialog()
        }
    }

    override fun initView() {
        mWallpaperDynamicData?.apply {

            mViewBinding.gridNinePictureView.data = wallList.map {
                getRealUrl(it.imageUrl).toString()
            } as MutableList<String>
            mViewBinding.gridNinePictureView.imageCallback = object : GridNinePictureView.ImageCallback{
                override fun imageClickListener(position: Int) {
                    buildARouter(AppConstant.RoutePath.WALLPAPER_DETAIL_ACTIVITY)
                        .withOptionsCompat(
                            ActivityOptionsCompat.makeCustomAnimation(
                                this@DynamicDetailActivity,
                                com.yang.lib_common.R.anim.fade_in,
                                com.yang.lib_common.R.anim.fade_out
                            )
                        )
                        .withString(AppConstant.Constant.DATA, wallList.toJson())
                        .withInt(AppConstant.Constant.INDEX, position)
                        .withBoolean(AppConstant.Constant.TO_LOAD, false)
                        .navigation()
                }

            }

            mViewBinding.ivImage.loadCircle(this@DynamicDetailActivity, getRealUrl(userImage).toString())
            mViewBinding.tvName.text = userName
            mViewBinding.tvAttention.text = if (isAttention) "取消关注" else "+关注"
            mViewBinding.tvText.text = dynamicContent
            mViewBinding.tvFabulousNum.text = formatNumUnit(dynamicLikeNum)
            mViewBinding.tvCommentNum.text = formatNumUnit(dynamicCommentNum)
            mViewBinding.tvForwardNum.text = formatNumUnit(dynamicForwardNum)



        }

        mViewBinding.apply {

            ivImage.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                    AppConstant.Constant.ID,
                    ""
                ).navigation()
            }
            tvAttention.clicks().subscribe {

            }

            tvSendComment.clicks().subscribe {
                openCommentDialog()
            }

            llFabulous.setOnClickListener {

                itvFabulous.tintClick = !itvFabulous.tintClick
//                tvFabulousNum.text =
            }
            llComment.setOnClickListener {
                openCommentDialog()
            }
            llForward.setOnClickListener {

            }
        }




        initRecyclerView()

    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
    }

    private fun insertComment(comment: String) {
        val mutableMapOf = mutableMapOf<String, String>()
        mutableMapOf[AppConstant.Constant.COMMENT] = comment
//        squareViewModel.insertComment(mutableMapOf)
    }

    private fun addViewHistory() {
//        squareViewModel.addViewHistory("", "")
    }

    private fun scrollToPosition(view: View) {
        val intArray = IntArray(2)
        view.getLocationOnScreen(intArray)
        mViewBinding.nestedScrollView.scrollTo(intArray[0], intArray[1])
    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return squareViewModel.uC
    }

    private fun initRecyclerView() {
        mViewBinding.recyclerView.layoutManager = LinearLayoutManager(this)

        commentAdapter =
            CommentAdapter(mutableListOf()).also {
                it.setOnItemChildClickListener { adapter, view, position ->
                    val item = commentAdapter.getItem(position)
                    item?.let {
                        when (view.id) {
                            com.yang.lib_common.R.id.siv_img -> {
                                buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                                    AppConstant.Constant.ID,
                                    ""
                                ).navigation()
                            }
                            com.yang.lib_common.R.id.siv_reply_img -> {
                                buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                                    AppConstant.Constant.ID,
                                    ""
                                ).navigation()
                            }
                            com.yang.lib_common.R.id.tv_reply -> {
                                XPopup.Builder(this@DynamicDetailActivity)
                                    .autoOpenSoftInput(true)
                                    .asCustom(EditBottomDialog(this@DynamicDetailActivity).apply {
                                        dialogCallBack = object : EditBottomDialog.DialogCallBack {
                                            override fun getComment(s: String) {
                                                when (it.itemType) {
                                                    AppConstant.Constant.PARENT_COMMENT_TYPE -> {
                                                        it.addSubItem(CommentData( AppConstant.Constant.CHILD_COMMENT_TYPE,  AppConstant.Constant.CHILD_COMMENT_TYPE).apply {
                                                            comment = s
                                                            parentId = it.id

                                                        })
                                                        commentAdapter.collapse(position)
                                                        commentAdapter.expand(position)
                                                    }
                                                    AppConstant.Constant.CHILD_COMMENT_TYPE, AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE -> {
                                                        it.parentId?.let { mParentId ->
                                                            val mPosition =
                                                                commentAdapter.data.indexOf(
                                                                    commentAdapter.data.findLast {
                                                                        TextUtils.equals(
                                                                            it.parentId,
                                                                            mParentId
                                                                        )
                                                                    }?.apply {
                                                                        addSubItem(
                                                                            CommentData(
                                                                                AppConstant.Constant.CHILD_COMMENT_TYPE,
                                                                                AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE
                                                                            ).apply {
                                                                                comment = s
                                                                                parentId = mParentId
                                                                            })
                                                                    })

                                                            commentAdapter.collapse(mPosition)
                                                            commentAdapter.expand(mPosition)
                                                        }
                                                    }
                                                }
                                                insertComment(s)
                                            }

                                        }
                                    }).show()
                            }
                            else -> {

                            }
                        }
                    }
                }
            }
        mViewBinding.recyclerView.adapter = commentAdapter
    }

    private fun openCommentDialog(){
        XPopup.Builder(this).autoOpenSoftInput(true).asCustom(EditBottomDialog(this).apply {
            dialogCallBack = object : EditBottomDialog.DialogCallBack {
                override fun getComment(s: String) {
                    commentAdapter.addData(0, CommentData(0, 0).apply {
                        comment = s
                    })
                    insertComment(s)
                    commentAdapter.getViewByPosition(
                        mViewBinding.recyclerView,
                        0,
                        com.yang.lib_common.R.id.siv_img
                    )?.let { it1 ->
                        it1.isFocusable = true
                        it1.requestFocus()
                        scrollToPosition(it1)
                    }
                    mViewBinding.nestedScrollView.fullScroll(View.FOCUS_DOWN)
                }

            }
        }).show()
    }
}