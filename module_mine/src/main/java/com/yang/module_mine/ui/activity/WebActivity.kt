package com.yang.module_mine.ui.activity

import android.graphics.Bitmap
import android.util.Log
import android.webkit.JavascriptInterface
import com.alibaba.android.arouter.facade.annotation.Route
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.*
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.base.ui.fragment.BaseFragment
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.showShort
import com.yang.lib_common.widget.CommonToolBar
import com.yang.module_mine.databinding.ActWebBinding

/**
 * @ClassName: ActWebBinding
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/3 9:10
 */
@Route(path = AppConstant.RoutePath.MINE_WEB_ACTIVITY)
class WebActivity:BaseActivity<ActWebBinding>() {

    private var url = "http://192.168.158.123:8080/messageList/"

    override fun initViewBinding(): ActWebBinding {
        return bind(ActWebBinding::inflate)
    }

    override fun initData() {

    }

    override fun initView() {

        val map = mutableMapOf<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        val intent = intent
        val title = intent.getStringExtra(AppConstant.Constant.TITLE)
        val url = intent.getStringExtra(AppConstant.Constant.URL)
        title?.let {
            mViewBinding.commonToolBar.centerContent = it
        }
        url?.let {
            this.url = it
        }
        initWebView()

        mViewBinding.commonToolBar.imageBackCallBack = object :CommonToolBar.ImageBackCallBack{
            override fun imageBackClickListener() {
                if (mViewBinding.webView.canGoBack()){
                    mViewBinding.webView.goBack()
                }else{
                    finish()
                }
            }
        }

        mViewBinding.commonToolBar.imageAddCallBack = object : CommonToolBar.ImageAddCallBack{
            override fun imageAddClickListener() {
                mViewBinding.webView.loadUrl("javascript:a()")
            }

        }

    }

    override fun initViewModel() {
    }

    private fun initWebView() {
        mViewBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
                return super.shouldOverrideUrlLoading(p0, p1)
            }
            override fun onPageStarted(p0: WebView?, p1: String?, p2: Bitmap?) {
                super.onPageStarted(p0, p1, p2)
                mViewBinding.mLoadingProgressView.loadStart = true
                Log.i(TAG, "onPageStarted: 加载开始 $p1  $p2")
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                mViewBinding.mLoadingProgressView.loadStart = false
                Log.i(TAG, "onPageFinished: 加载完成 $p1")

            }

            override fun onReceivedError(p0: WebView?, p1: Int, p2: String?, p3: String?) {
                super.onReceivedError(p0, p1, p2, p3)
            }
        }

        mViewBinding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(p0: WebView?, p1: Int) {
                super.onProgressChanged(p0, p1)
                mViewBinding.mLoadingProgressView.mProgress = p1
                Log.i(TAG, "onProgressChanged: $p1")
            }
        }

        mViewBinding.webView.addJavascriptInterface(this@WebActivity,"nativeMethod")

        mViewBinding.webView.loadUrl(url)
        mViewBinding.webView.settings.apply {
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


        Log.i(TAG, "initWebView: ${mViewBinding.webView.settingsExtension == null}   ${mViewBinding.webView.x5WebViewExtension == null}")
        QbSdk.clearAllWebViewCache(this, true)

    }


    @JavascriptInterface
    fun finishThis(){
        finish()
    }
    @JavascriptInterface
    fun setTitle(title:String){
        mViewBinding.commonToolBar.centerContent = title
    }
    @JavascriptInterface
    fun show(title:String){
        showShort(title)
    }


    override fun onResume() {
        super.onResume()
        mViewBinding.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mViewBinding.webView.onPause()
    }

    override fun onBackPressed() {
        if (mViewBinding.webView.canGoBack()) {
            mViewBinding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewBinding.webView.destroy()
    }


}