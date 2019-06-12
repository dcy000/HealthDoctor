package com.gcml.module_health_manager.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.gcml.module_health_manager.R;
import com.sjtu.yifei.annotation.Route;

//@Route(path = "/health/manager/webview")
public class TestWeb extends AppCompatActivity {
    private WebView mWebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        mWebview = (WebView) findViewById(R.id.webview);
        mWebview.loadUrl("http://www.medppp.com/Sheet/SheetViewG?fl=FL0000000506");
    }
}
