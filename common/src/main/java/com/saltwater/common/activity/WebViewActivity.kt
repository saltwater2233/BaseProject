package com.saltwater.common.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi
import com.saltwater.common.R
import com.saltwater.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : BaseActivity() {

    companion object {
        private const val URL = "url"

        fun newInstance(context: Context, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(URL, url)
            context.startActivity(intent)
        }
    }



    override fun bindLayout(): Int {
        return R.layout.activity_web_view
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        img_back.setOnClickListener {
            finish()
        }

        img_share.setOnClickListener {
            val uri = Uri.parse(webView.url)
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = uri
            startActivity(intent)
        }

        pb_progress.max = 100
        pb_progress

        val webSettings = webView.settings;
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
        }
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        //允许js代码
        webSettings.javaScriptEnabled = true
        //允许SessionStorage/LocalStorage存储
        webSettings.domStorageEnabled = true
        //禁用放缩
        webSettings.displayZoomControls = false
        webSettings.builtInZoomControls = false
        //禁用文字缩放
        webSettings.textZoom = 100

        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true)
        webSettings.setAppCachePath(getDir("appcache", 0).path)
        //允许WebView使用File协议
        webSettings.allowFileAccess = true

        //自动加载图片
        webSettings.loadsImagesAutomatically = true

        webView.webChromeClient = WebChromeClient()

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                pb_progress.progress = newProgress
                if (newProgress == 100) {
                    pb_progress.visibility = View.GONE
                } else {
                    pb_progress.visibility = View.VISIBLE
                }
            }
        }

        webView.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }

        }



        intent.getStringExtra(URL)?.let { webView.loadUrl(it) }

    }

    override fun initData() {
    }


    override fun onBackPressed() {
        super.onBackPressed()
        /* if (webView.canGoBack()) {
             webView.goBack()
         } else {
             super.onBackPressed()
         }*/

    }


}
