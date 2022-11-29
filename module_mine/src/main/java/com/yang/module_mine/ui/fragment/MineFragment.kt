package com.yang.module_mine.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import com.alibaba.android.arouter.facade.annotation.Route
import com.huawei.hms.hmsscankit.ScanUtil
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.adapter.MBannerAdapter
import com.yang.lib_common.base.ui.fragment.BaseFragment
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.BannerBean
import com.yang.lib_common.dialog.ShareDialog
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.*
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.helper.AdManager
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.*
import com.yang.module_mine.adapter.MoreFunctionAdapter
import com.yang.module_mine.data.MoreFunctionData
import com.yang.module_mine.databinding.FraMineBinding
import com.yang.module_mine.viewmodel.MineViewModel
import com.youth.banner.indicator.CircleIndicator
import java.util.HashMap


/**
 * @ClassName: MineFragment
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 15:38
 */
@Route(path = AppConstant.RoutePath.MINE_FRAGMENT)
class MineFragment : BaseFragment<FraMineBinding>(), OnRefreshListener {

    @InjectViewModel
    lateinit var mineViewModel: MineViewModel


    lateinit var moreFunctionAdapter: MoreFunctionAdapter



    private var buildBitmap: Bitmap? = null


    override fun initViewBinding(): FraMineBinding {
        return bind(FraMineBinding::inflate)
    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mineViewModel.uC
    }

    override fun setStatusPadding(): Boolean {
        return true
    }


    override fun initView() {
//        mViewBinding.appBarLayout.setPadding(0, getStatusBarHeight(requireActivity()), 0, 0)

        mViewBinding.smartRefreshLayout.setOnRefreshListener(this)
        finishRefreshLoadMore(mViewBinding.smartRefreshLayout)

        mViewBinding.tvLogin.clicks().subscribe {
            buildARouterLogin(requireContext())
        }
        mViewBinding.clHeadLogin.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_USER_INFO_ACTIVITY).withString(AppConstant.Constant.USER_ID,UserInfoHold.userId).navigation()
        }
        mViewBinding.llWallet.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_BALANCE_ACTIVITY).navigation()
        }
        mViewBinding.tvWalletDetail.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_WALLET_DETAIL_ACTIVITY).navigation()

        }
        mViewBinding.icvMyWork.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_SQUARE_ACTIVITY).withString(AppConstant.Constant.USER_ID,UserInfoHold.userId).navigation()
        }
        mViewBinding.icvMyRights.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_RIGHTS_ACTIVITY).navigation()
        }
        mViewBinding.icvTaskHistory.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_TASK_HISTORY_ACTIVITY).navigation()
        }
        mViewBinding.llSign.clicks().subscribe {
            mineViewModel.sign(UserInfoHold.userId.toString())
        }
        mViewBinding.ivErCode.clicks().subscribe {


            XPopup.Builder(requireContext()).asCustom(ShareDialog(requireContext()).apply {
                onItemClickListener = object : ShareDialog.OnItemClickListener {
                    override fun onCancelClickListener() {
                    }

                    override fun onConfirmClickListener(type: Int) {
                        when(type){
                            0 ->{
                                val platform = ShareSDK.getPlatform(QQ.NAME)
                                val shareParams = Platform.ShareParams()
                                shareParams.shareType = Platform.SHARE_TEXT
                                shareParams.title = "测试分享的标题"
                                shareParams.text = "测试文本"
                                platform.platformActionListener = object : PlatformActionListener{
                                    override fun onComplete(
                                        p0: Platform?,
                                        p1: Int,
                                        p2: HashMap<String, Any>?
                                    ) {
                                        Log.i(TAG, "onComplete:$p0 $p1  $p2 ")
                                    }

                                    override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
                                        Log.i(TAG, "onError: $p0 $p1  $p2")
                                    }

                                    override fun onCancel(p0: Platform?, p1: Int) {
                                        Log.i(TAG, "onCancel:$p0 $p1  ")
                                    }

                                }
                                platform.share(shareParams)
                            }
                            1 ->{
                                val platform = ShareSDK.getPlatform(Wechat.NAME)
                                val shareParams = Platform.ShareParams()
                                shareParams.shareType = Platform.SHARE_TEXT
                                shareParams.title = "测试分享的标题"
                                shareParams.text = "测试文本"
                                platform.share(shareParams)
                            }
                            2 ->{
                                val mClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, "复制复制复制"))
                                showShort("复制成功!")
                            }
                        }
                    }

                }
            }).show()
        }

        mViewBinding.stvAwardVip.clicks().subscribe {
            AdManager.instance.showReward(requireActivity()){
                mViewBinding.stvAwardVip.text = "观看广告1/3"
            }
        }
        mViewBinding.stvAwardAd.clicks().subscribe {
            UserInfoHold.userInfo?.apply {
                if (userIsSign!!){
                    //是否领取签到奖励
                    if (!userSignReceive!!){
                        val createUser = createUser()
                        createUser.userSignReceive = true
                        mineViewModel.updateUserInfo(createUser)
                    }else{
                        showShort("当日奖励已领取")
                    }
                }else{
                    showShort("请先签到")
                }
            }

        }
        mViewBinding.llAttention.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_FANS_ACTIVITY).withInt(AppConstant.Constant.INDEX,0).navigation()
        }
        mViewBinding.llFan.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_FANS_ACTIVITY).withInt(AppConstant.Constant.INDEX,1).navigation()
        }
        mViewBinding.icvMyCollection.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_COLLECTION_ACTIVITY).withInt(AppConstant.Constant.INDEX,0).navigation()
        }
        mViewBinding.icvMyDown.clicks().subscribe {
            buildARouter(AppConstant.RoutePath.MINE_COLLECTION_ACTIVITY).withInt(AppConstant.Constant.INDEX,1).navigation()
        }

        initUserInfo(UserInfoHold.userInfo)

        initBanner()
        initRecyclerView()

    }


    override fun initData() {

        //刷新用户信息
        LiveDataBus.instance.with(AppConstant.Constant.REFRESH).observe(this) {
            onRefresh(mViewBinding.smartRefreshLayout)
        }
        //微信获取token
        LiveDataBus.instance.with(AppConstant.WeChatConstant.CODE).observe(this) {
            setMMKVValue(AppConstant.WeChatConstant.CODE,it.toString())
            mineViewModel.getWeChatToken(it.toString())
        }
    }


    private fun initUserInfo(userInfoData: UserInfoData?) {

        if (null == userInfoData) {
            mViewBinding.clHead.visibility = View.VISIBLE
            mViewBinding.clHeadLogin.visibility = View.GONE
        } else {
            mViewBinding.clHead.visibility = View.GONE
            mViewBinding.clHeadLogin.visibility = View.VISIBLE
        }

        userInfoData?.let {
            updateUserInfo(it)
            mViewBinding.apply {
                tvName.text = it.userName
                tvAccount.text = it.userPhone
                tvDesc.text = it.userDescribe
                tvAttention.text = it.userAttention?.formatNumUnit()
                tvFan.text = it.userFan?.formatNumUnit()
//                if (it.userVipLevel != 0 && !it.userVipExpired){
//                    tvName.setTextColor(requireContext().getColor(R.color.red))
//                }
                tvMoney.text = "￥"+it.userMoney.toString()
                tvIntegral.text = it.userIntegral.toString()
                tvAdTicket.text = it.userAdNum.toString()
//                tvMoney.text = "￥"+it.userMoney.formatNumUnit()
//                tvIntegral.text = it.userIntegral.formatNumUnit()
//                tvAdTicket.text = it.userAdNum.formatNumUnit()

                if (it.userIsSign!!) {
                    tvSign.text = "已签到${it.userSign}天"
                    stvAwardAd.text = "每日签到1/1"
                }else{
                    tvSign.text = "签到"
                    stvAwardAd.text = "每日签到0/1"
                }
                llSign.isClickable = !it.userIsSign!!
                loadCircle(requireContext(), it.userAttr, sivHead)
                if (null == buildBitmap) {
                    buildBitmap = ScanUtil.buildBitmap("http://www.yyxjlgl.tk/module-main/image/apk/miemie_1.0.0.apk", 500, 500)
                    ivErCode.setImageBitmap(buildBitmap)
                }
            }
        }
    }




    private fun initRecyclerView() {
        moreFunctionAdapter = MoreFunctionAdapter(mutableListOf<MoreFunctionData>().apply {
//            add(
//                MoreFunctionData(
//                    R.drawable.iv_message,
//                    "消息通知",
//                    AppConstant.RoutePath.MINE_WEB_ACTIVITY,
//                    AppConstant.ClientInfo.BASE_WEB_URL + "/pages/message/messageList"
//                )
//            )
//            add(
//                MoreFunctionData(
//                    R.drawable.iv_kf,
//                    "客服中心",
//                    AppConstant.RoutePath.MINE_WEB_ACTIVITY,
//                    AppConstant.ClientInfo.BASE_WEB_URL + "/pages/service/customService"
//                )
//            )
            add(
                MoreFunctionData(
                    R.drawable.iv_suggestion,
                    "意见反馈",
                    AppConstant.RoutePath.MINE_SUGGESTION_ACTIVITY,
                    ""
                )
            )
            add(
                MoreFunctionData(
                    R.drawable.iv_about,
                    "关于App",
                    AppConstant.RoutePath.MINE_ABOUT_ACTIVITY,
                    ""
                )
            )
            add(
                MoreFunctionData(
                    R.drawable.iv_setting,
                    "设置",
                    AppConstant.RoutePath.MINE_SETTING_ACTIVITY,
                    ""
                )
            )
        })

        moreFunctionAdapter.setOnItemClickListener { adapter, view, position ->
            val item = moreFunctionAdapter.getItem(position)
            item?.let {
                buildARouter(it.routePath).withString(AppConstant.Constant.TITLE, it.name)
                    .withString(AppConstant.Constant.URL, it.url).navigation()
            }
        }

        mViewBinding.recyclerView.adapter = moreFunctionAdapter
        mViewBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
    }

    private fun initBanner() {
        mViewBinding.banner.addBannerLifecycleObserver(this)
            .setAdapter(MBannerAdapter(mutableListOf<BannerBean>().apply {
                add(BannerBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F23%2F20160623142756_YyXNw.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=9fc850da14dfd73a1d7c1c3f068e3d57"))
                add(BannerBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201607%2F29%2F20160729224352_rVhZA.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=326f6993d0c1ee5c0048b33fe9f0a6dc"))
                add(BannerBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F15%2F20150815231707_JWQjx.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660986299&t=e3951129e167261b3f4c7c739becc463"))
            }), true)
            .setBannerRound(20f)
            .isAutoLoop(true)
            .indicator = CircleIndicator(requireContext())
    }

    private fun createUser():UserInfoData{
        return "{}".fromJson<UserInfoData>().apply {
            id = UserInfoHold.userId
        }
    }

    override fun initViewModel() {
        InjectViewModelProxy.inject(this)
        mineViewModel.mUserInfoData.observe(this, Observer {
            initUserInfo(it)
        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        mineViewModel.getUserInfo(UserInfoHold.userId ?: "")
    }
}