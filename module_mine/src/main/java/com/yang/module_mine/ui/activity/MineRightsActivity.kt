package com.yang.module_mine.ui.activity

import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alipay.sdk.app.EnvUtils
import com.alipay.sdk.app.PayTask
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.R
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.*
import com.yang.module_mine.adapter.PayTypeAdapter
import com.yang.module_mine.adapter.VipPackageAdapter
import com.yang.module_mine.adapter.VipRightsAdapter
import com.yang.module_mine.data.PayTypeData
import com.yang.module_mine.data.VipPackageData
import com.yang.module_mine.data.VipRightsData
import com.yang.module_mine.databinding.ActMineRightsBinding
import com.yang.module_mine.viewmodel.MineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @ClassName: MineRightsActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/4 17:09
 */
@Route(path = AppConstant.RoutePath.MINE_RIGHTS_ACTIVITY)
class MineRightsActivity : BaseActivity<ActMineRightsBinding>() {

    @InjectViewModel
    lateinit var mineViewModel:MineViewModel

    private lateinit var mVipPackageAdapter: VipPackageAdapter
    private lateinit var mPayTypeAdapter: PayTypeAdapter
    private lateinit var mVipRightsAdapter: VipRightsAdapter


    override fun initViewBinding(): ActMineRightsBinding {
        return bind(ActMineRightsBinding::inflate)
    }

    override fun initData() {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX)
    }

    override fun onResume() {
        super.onResume()
        mineViewModel.getUserInfo(UserInfoHold.userId)
    }

    override fun initView() {

        initVipRecyclerView()
        initPayRecyclerView()
        initRightsRecyclerView()
        mineViewModel.getVipPackage()
        mViewBinding.apply {
            stvPay.clicks().subscribe {
                openPay()
            }
        }
    }

    private fun initUserInfo(userInfo: UserInfoData?){
        userInfo?.apply {
            if (userAttr.isNullOrEmpty()) {
                mViewBinding.sivImg.loadImage(
                    this@MineRightsActivity,
                    R.drawable.iv_attr
                )
            } else {
                mViewBinding.sivImg.loadCircle(this@MineRightsActivity, userAttr)
            }
            mViewBinding.tvName.text = userName
            mViewBinding.tvVipTime.visibility = View.GONE
            if (userVipLevel == 0 || userVipLevel == null) {
                mViewBinding.tvVipLevel.text = "暂未开通会员"
                mViewBinding.tvVipLevel.setTextColor(getColor(R.color.textColor_999999))
            } else {
                if (userVipExpired){
                    mViewBinding.tvVipLevel.text = "会员已到期"
                }else{
                    mViewBinding.tvVipLevel.text = Html.fromHtml(
                        String.format(
                            getString(R.string.string_vip_level),
                            userVipLevel
                        ), Html.FROM_HTML_OPTION_USE_CSS_COLORS
                    )
                    mViewBinding.tvVipTime.text = userVipTime.emptyOtherString(""," 到期")
                    mViewBinding.tvVipTime.visibility = View.VISIBLE
                }
            }

            val payTypeData = mPayTypeAdapter.data[1]
            payTypeData.balance = "￥"+(userMoney?:"0.0")
            mPayTypeAdapter.setData(1,payTypeData)
            Log.i(TAG, "initUserInfo: $userMoney  ${payTypeData.toJson()}")
        }
    }

    override fun initUIChangeLiveData(): UIChangeLiveData? {
        return mineViewModel.uC
    }

    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
        mineViewModel.mAlipayBody.observe(this){
            lifecycleScope.launch {
                val async = async(Dispatchers.IO) {
                    val alipay = PayTask(this@MineRightsActivity)
                    val payV2 = alipay.payV2(it, true)
                    payV2
                }
                val await = async.await()
                val resultStatus = await["resultStatus"]
                val result = await["result"]

                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    showShort("支付成功")
                    Log.i(TAG, "initViewModel: ${result}")
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    showShort("支付失败")
                    Log.i(TAG, "initViewModel: ${result}")
                }

                Log.i(TAG, "openPay: ${await.toJson()}")
            }
        }


        mineViewModel.mUserInfoData.observe(this) {
            initUserInfo(it)
        }

        mineViewModel.mVipPackageListData.observe(this){
            mVipPackageAdapter.replaceData(it)
        }
    }



    private fun openPay(){
        if (null == mVipPackageAdapter.getSelectItem()){
            showShort("请选择会员套餐")
            return
        }

        mPayTypeAdapter.getSelectItem()?.apply {
            mineViewModel.openVip(id, mVipPackageAdapter.getSelectItem()!!.id)
        }

//        val req = SendAuth.Req()
//        req.state = "miemie_${System.currentTimeMillis()}"
//        req.scope = "snsapi_userinfo"
//        BaseApplication.weChatApi.sendReq(req)

//        val request =  PayReq()
//        request.appId = AppConstant.WeChatConstant.WECHAT_PAY_ID
//        request.partnerId = "1900000109"
//        request.prepayId= "1101000000140415649af9fc314aa427"
//        request.packageValue = "Sign=WXPay"
//        request.nonceStr= "1101000000140429eb40476f8896f4c9"
//        request.timeStamp= "1398746574"
//        request.sign= "oR9d8PuhnIc+YZ8cBHFCwfgpaK9gd7vaRvkYD7rthRAZ\\/X+QBhcCYL21N7cHCTUxbQ+EAt6Uy+lwSN22f5YZvI45MLko8Pfso0jm46v5hqcVwrk6uddkGuT+Cdvu4WBqDzaDjnNa5UK3GfE1Wfl2gHxIIY5lLdUgWFts17D4WuolLLkiFZV+JSHMvH7eaLdT9N5GBovBwu5yYKUR7skR8Fu+LozcSqQixnlEZUfyE55feLOQTUYzLmR9pNtPbPsu6WVhbNHMS3Ss2+AehHvz+n64GDmXxbX++IOBvm2olHu3PsOUGRwhudhVf7UcGcunXt8cqNjKNqZLhLw4jq\\/xDg=="
//        BaseApplication.weChatApi.sendReq(request)
    }


    private fun initVipRecyclerView() {
        mVipPackageAdapter = VipPackageAdapter()
        mViewBinding.vipRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mViewBinding.vipRecyclerView.adapter = mVipPackageAdapter

//        val list = mutableListOf<VipPackageData>().apply {
//            add(VipPackageData("0", "限时5折", "1年", "90", "180", true))
//            add(VipPackageData("1", "超值低价", "3个月", "27", "45"))
//            add(VipPackageData("2", "最划算", "1个月", "9.9", "15"))
//
//
//        }
        mVipPackageAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mVipPackageAdapter.getItem(position)
            item?.let {
                if (!it.isSelect) {
                    mVipPackageAdapter.data.forEach { find ->
                        find.isSelect = false
                    }
                    it.isSelect = true
                    mVipPackageAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initPayRecyclerView() {
        mPayTypeAdapter = PayTypeAdapter()
        mViewBinding.payRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, true)
        mViewBinding.payRecyclerView.adapter = mPayTypeAdapter


        val list = mutableListOf<PayTypeData>().apply {
            add(PayTypeData("1", "支付宝", R.drawable.iv_alipay, "",false))
//            add(PayTypeData("2", "微信", R.drawable.iv_we_chat, "",false))
            add(PayTypeData("0", "余额", R.drawable.iv_yue, "￥0.0",true))
        }
        mPayTypeAdapter.setNewData(list)

        mPayTypeAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mPayTypeAdapter.getItem(position)
            item?.let {
                if (!it.isSelect) {
                    mPayTypeAdapter.data.forEach { find ->
                        find.isSelect = false
                    }
                    it.isSelect = true
                    mPayTypeAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    private fun initRightsRecyclerView() {
        mVipRightsAdapter = VipRightsAdapter()
        mViewBinding.rightsRecyclerView.adapter = mVipRightsAdapter

        val list = mutableListOf<VipRightsData>().apply {
            add(VipRightsData(R.drawable.iv_vip_no_ad,"非任务免广告"))
            add(VipRightsData(R.drawable.iv_vip,"会员专属标识"))
            add(VipRightsData(R.drawable.iv_right_down,"壁纸无限下载"))
            add(VipRightsData(R.drawable.iv_kf,"专属客服"))
        }
        mVipRightsAdapter.setNewData(list)
    }
}