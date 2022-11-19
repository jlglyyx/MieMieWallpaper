package com.yang.lib_common.dialog

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.jakewharton.rxbinding4.view.clicks
import com.lxj.xpopup.impl.FullScreenPopupView
import com.tencent.smtt.sdk.*
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.databinding.DialogPrivacyAgreementBinding
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks

/**
 * @ClassName: PrivacyAgreementDialog
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/18 9:36
 */
class PrivacyAgreementDialog(context: Context) : FullScreenPopupView(context) {

    private val mBinding by lazy {
        DialogPrivacyAgreementBinding.bind(contentView)
    }

    var onConfirm: () -> Unit = {}
    var onCancel: () -> Unit = {}


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_privacy_agreement
    }


    override fun onCreate() {
        super.onCreate()

        mBinding.apply {

            initWebView()

            tvCancel.setOnClickListener {
                onCancel()
            }
            tvConfirm.setOnClickListener {
                onConfirm()
            }
        }


    }

    private fun initWebView() {


        mBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(p0: WebView?, url: String): Boolean {
                Log.i("TAG", "shouldOverrideUrlLoading: $url")
                if (url.contains("privacy_agreement")) {
                    buildARouter(AppConstant.RoutePath.MINE_WEB_ACTIVITY).withString(
                        AppConstant.Constant.TITLE,
                        "隐私政策"
                    ).withString(
                        AppConstant.Constant.URL,
                        AppConstant.ClientInfo.PRIVACY_AGREEMENT
                    ).navigation()
                    return true
                } else if (url.contains("service_agreement")) {
                    buildARouter(AppConstant.RoutePath.MINE_WEB_ACTIVITY).withString(
                        AppConstant.Constant.TITLE,
                        "服务协议"
                    ).withString(
                        AppConstant.Constant.URL,
                        AppConstant.ClientInfo.SERVICE_AGREEMENT
                    ).navigation()
                    return true
                } else {
                    return super.shouldOverrideUrlLoading(p0, url)
                }
            }

            override fun onPageStarted(p0: WebView?, p1: String?, p2: Bitmap?) {
                super.onPageStarted(p0, p1, p2)
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)

            }

            override fun onReceivedError(p0: WebView?, p1: Int, p2: String?, p3: String?) {
                super.onReceivedError(p0, p1, p2, p3)
            }
        }

        mBinding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(p0: WebView?, p1: Int) {
                super.onProgressChanged(p0, p1)
            }
        }

        mBinding.webView.loadUrl(AppConstant.ClientInfo.PRIVACY_AND_SERVICE_AGREEMENT)
        mBinding.webView.settings.apply {
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = false //禁止缩放
            displayZoomControls = false //禁止缩放
            pluginsEnabled = true
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK//关闭webview中缓存
            allowFileAccess = true //设置可以访问文件
            javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
            loadsImagesAutomatically = true //支持自动加载图片
            defaultTextEncodingName = "utf-8"//设置编码格式
        }
        QbSdk.clearAllWebViewCache(context, true)

    }


    override fun doDismissAnimation() {
//        super.doDismissAnimation()
    }

    override fun onDismiss() {
        super.onDismiss()
        mBinding.webView.destroy()
    }
}