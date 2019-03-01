package com.gcml.module_mine;

import android.content.Intent;
import android.os.Bundle;

import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;

public class MyMoneyPacketActivity extends StateBaseActivity {
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_my_money_packet;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
