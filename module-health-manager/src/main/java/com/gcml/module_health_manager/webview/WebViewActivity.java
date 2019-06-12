package com.gcml.module_health_manager.webview;

import android.content.Intent;
import android.os.Bundle;

import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/health/manager/webview")
public class WebViewActivity extends BaseX5WebViewActivity {

    private String url;
    private String title;

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        super.initParams(intentArgument, bundleArgument);
        url = intentArgument.getStringExtra("url");
        title = intentArgument.getStringExtra("title");
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    protected String loadUrl(com.tencent.smtt.sdk.WebView webView) {
        webView.loadUrl(url);
        return url;
    }

    @Override
    protected String setTitle() {
        return title;
    }

    @Override
    protected void addJavascriptInterface(com.tencent.smtt.sdk.WebView webView) {

    }

    @Override
    protected void removeJavascriptInterface(com.tencent.smtt.sdk.WebView webView) {

    }

    @Override
    protected void onWebViewPageStart(com.tencent.smtt.sdk.WebView webView) {

    }

    @Override
    protected void onWebViewPageFinished(com.tencent.smtt.sdk.WebView webView) {

    }

    @Override
    protected void onWebViewPageReceivedError(com.tencent.smtt.sdk.WebView webView) {

    }

    @Override
    protected void onPageLoadingProgress(com.tencent.smtt.sdk.WebView webView, int progress) {

    }
}
