package com.yang.module_square.ui.activity

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.huawei.hms.ads.id
import com.lxj.xpopup.XPopup
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.CommentAdapter
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.CommentData
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.data.UserInfoHold.userName
import com.yang.lib_common.data.WallpaperDynamicCommentData
import com.yang.lib_common.data.WallpaperDynamicData
import com.yang.lib_common.dialog.EditBottomDialog
import com.yang.lib_common.dialog.ImageViewPagerDialog
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.*
import com.yang.lib_common.widget.GridNinePictureView
import com.yang.module_square.databinding.ActDynamicDetailBinding
import com.yang.module_square.viewmodel.SquareViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Route(path = AppConstant.RoutePath.DYNAMIC_DETAIL_ACTIVITY)
class DynamicDetailActivity : BaseActivity<ActDynamicDetailBinding>() {

    private lateinit var commentAdapter: CommentAdapter

    @InjectViewModel
    lateinit var squareViewModel: SquareViewModel

    private var mid: String = ""

    private var mPosition: Int = -1

    private var mType: Int = -1

    private var mDynamicData: WallpaperDynamicData? = null


    override fun initViewBinding(): ActDynamicDetailBinding {
        return bind(ActDynamicDetailBinding::inflate)
    }


    override fun initData() {
        mid = intent.getStringExtra(AppConstant.Constant.ID) ?: ""
        val openComment = intent.getBooleanExtra(AppConstant.Constant.OPEN_COMMENT, false)
        squareViewModel.getDynamicDetail(mid)
        squareViewModel.getDynamicCommentList(mid)

        addViewHistory()

        if (openComment) {
            openCommentDialog()
        }
    }

    override fun initView() {


        mViewBinding.apply {

            ivImage.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                    AppConstant.Constant.USER_ID,
                    mDynamicData?.userId
                ).navigation()
            }
            tvName.clicks().subscribe {
                buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                    AppConstant.Constant.USER_ID,
                    mDynamicData?.userId
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
                    item?.let { dataItem ->
                        when (view.id) {
                            com.yang.lib_common.R.id.siv_img,
                            com.yang.lib_common.R.id.tv_name,
                            -> {
                                //点击头像跳转个人信息页面
                                buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                                    AppConstant.Constant.USER_ID,
                                    dataItem.data?.userId
                                ).navigation()
                            }
                            com.yang.lib_common.R.id.siv_img,
                            com.yang.lib_common.R.id.tv_reply_name -> {
                                //点击头像跳转个人信息页面
                                buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(
                                    AppConstant.Constant.USER_ID,
                                    dataItem.data?.replyUserId
                                ).navigation()
                            }
                            com.yang.lib_common.R.id.tv_open_comment -> {
                                //展开更多数据
                                if (dataItem.isExpanded) {
                                    commentAdapter.collapse(position, false, true)
                                } else {
                                    commentAdapter.expand(position, false, true)
                                }
                            }
                            com.yang.lib_common.R.id.ll_sq -> {
                                //展开更多数据
                                commentAdapter.collapse(position, false, true)
                            }
                            else -> {

                            }
                        }
                    }
                }

                it.setOnItemClickListener { adapter, view, position ->
                    val item = commentAdapter.getItem(position)
                    item?.let {
                        //点击回复
                        XPopup.Builder(this@DynamicDetailActivity)
                            .autoOpenSoftInput(true)
                            .asCustom(EditBottomDialog(this@DynamicDetailActivity).apply {
                                dialogCallBack = object : EditBottomDialog.DialogCallBack {
                                    override fun getComment(s: String) {
                                        when (it.itemType) {
                                            //父层级回复
                                            AppConstant.Constant.PARENT_COMMENT_TYPE -> {

                                                insertComment(s, it.data?.id, null)
                                                mPosition = position
                                                mType = AppConstant.Constant.CHILD_COMMENT_TYPE
                                            }
                                            //子层级回复
                                            AppConstant.Constant.CHILD_COMMENT_TYPE, AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE -> {
                                                if (TextUtils.equals(
                                                        it.data?.userId,
                                                        UserInfoHold.userId
                                                    )
                                                ) {
                                                    mType = AppConstant.Constant.CHILD_COMMENT_TYPE
                                                    insertComment(s, it.data?.parentId, null)
                                                } else {
                                                    mType =
                                                        AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE
                                                    insertComment(
                                                        s,
                                                        it.data?.parentId,
                                                        it.data?.userId
                                                    )
                                                }
                                                mPosition = position
                                            }
                                        }

                                    }

                                }
                            }).show()
                    }
                }
            }
        mViewBinding.recyclerView.adapter = commentAdapter
    }

    private fun insertComment(comment: String, parentId: String?, replyUserId: String?) {

        squareViewModel.insertDynamicComment(
            UserInfoHold.userId,
            parentId,
            mid,
            replyUserId,
            comment
        )


    }

    private fun openCommentDialog() {
        XPopup.Builder(this).autoOpenSoftInput(true).asCustom(EditBottomDialog(this).apply {
            dialogCallBack = object : EditBottomDialog.DialogCallBack {
                override fun getComment(s: String) {


                    insertComment(s, null, null)
                    mPosition = 0
                    mType = AppConstant.Constant.PARENT_COMMENT_TYPE

                }

            }
        }).show()
    }


    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
        squareViewModel.mWallpaperDynamicDetailData.observe(this) {
            mDynamicData = it
            it?.apply {
                resourceUrls?.fromJson<MutableList<String>>()?.let {
                    mViewBinding.gridNinePictureView.data = it
                }
                mViewBinding.gridNinePictureView.imageCallback =
                    object : GridNinePictureView.ImageCallback {
                        override fun imageClickListener(position: Int) {
                            XPopup.Builder(this@DynamicDetailActivity)
                                .asCustom(ImageViewPagerDialog(this@DynamicDetailActivity,
                                    resourceUrls?.fromJson<MutableList<String>>()!!.map {
                                        getRealUrl(it)
                                    } as MutableList<String>, position)).show()
                        }

                    }
                mViewBinding.ivImage.loadCircle(
                    this@DynamicDetailActivity,
                    getRealUrl(userAttr).toString()
                )
                mViewBinding.tvName.text = userName
                mViewBinding.tvAttention.text = if (isAttention) "取消关注" else "+关注"
                mViewBinding.tvText.text = content
                mViewBinding.tvFabulousNum.text =
                    if (likeNum == null) "0" else likeNum?.formatNumUnit()
                mViewBinding.tvCommentNum.text =
                    if (commentNum == null) "0" else commentNum?.formatNumUnit()
                mViewBinding.tvForwardNum.text =
                    if (forwardNum == null) "0" else forwardNum?.formatNumUnit()
                mViewBinding.tvDegree.text =
                    "浏览" + if (seeNum == null) "0" else seeNum?.formatNumUnit() + "次"
                mViewBinding.tvTime.text = createTime
                mViewBinding.tvLocation.text = sendLocation
            }
        }

        squareViewModel.mWallpaperDynamicCommentData.observe(this) {
            val mutableListOf = mutableListOf<CommentData>()
            it.forEach { item ->
                val element = CommentData(
                    AppConstant.Constant.PARENT_COMMENT_TYPE,
                    AppConstant.Constant.PARENT_COMMENT_TYPE
                ).apply {
                    data = item
                }
                mutableListOf.add(element)
                item.children?.forEach { child ->
                    if (child.replyUserId.isNullOrEmpty()) {
                        element.addSubItem(CommentData(
                            AppConstant.Constant.CHILD_COMMENT_TYPE,
                            AppConstant.Constant.CHILD_COMMENT_TYPE
                        ).apply {
                            data = child
                        })
                    } else {
                        element.addSubItem(CommentData(
                            AppConstant.Constant.CHILD_COMMENT_TYPE,
                            AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE
                        ).apply {
                            data = child
                        })
                    }
                }
            }
            commentAdapter.replaceData(mutableListOf)

        }


        squareViewModel.mWallpaperDynamicCommentDetailData.observe(this) {

            when (mType) {
                AppConstant.Constant.PARENT_COMMENT_TYPE -> {
                    commentAdapter.addData(
                        mPosition,
                        CommentData(
                            AppConstant.Constant.PARENT_COMMENT_TYPE,
                            AppConstant.Constant.PARENT_COMMENT_TYPE
                        ).apply {
                            data = it
                        })
                    commentAdapter.getViewByPosition(
                        mViewBinding.recyclerView,
                        mPosition,
                        com.yang.lib_common.R.id.siv_img
                    )?.let { it1 ->
                        it1.isFocusable = true
                        it1.requestFocus()
                        scrollToPosition(it1)
                    }
                    mViewBinding.nestedScrollView.fullScroll(View.FOCUS_DOWN)
                }
                AppConstant.Constant.CHILD_COMMENT_TYPE -> {
                    val item = commentAdapter.getItem(mPosition)
                    item?.addSubItem(0,
                        CommentData(
                            AppConstant.Constant.CHILD_COMMENT_TYPE,
                            AppConstant.Constant.CHILD_COMMENT_TYPE
                        ).apply {
                            data = it
                        })
//                    commentAdapter.collapse(mPosition)
//                    commentAdapter.notifyItemInserted(mPosition)
//                    commentAdapter.notifyDataSetChanged()
//                    commentAdapter.expand(mPosition)
                    commentAdapter.collapse(mPosition, false)
                    commentAdapter.expand(mPosition, false)

                }
                AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE -> {
                    val item = commentAdapter.getItem(mPosition)
                    item?.addSubItem(0,
                        CommentData(
                            AppConstant.Constant.CHILD_COMMENT_TYPE,
                            AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE
                        ).apply {
                            data = it
                        })
//                    commentAdapter.collapse(mPosition)
//                    commentAdapter.notifyDataSetChanged()
                    commentAdapter.collapse(mPosition, false)
                    commentAdapter.expand(mPosition, false)
                }
            }
        }
    }
}