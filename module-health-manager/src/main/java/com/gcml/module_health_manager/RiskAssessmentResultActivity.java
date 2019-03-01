package com.gcml.module_health_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.jaeger.library.StatusBarUtil;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/healthmanager/risk/assement/deal/result")
public class RiskAssessmentResultActivity extends StateBaseActivity {
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_risk_assessment_deal_result;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        showSuccess();
        mToolbar.setBackgroundColor(Box.getColor(android.R.color.transparent));
        mTvTitle.setText("风险评估结果");

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }

    @Override
    public void initView() {

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    public void clickPass(View view) {
        ToastUtils.showShort("通过");
        finish();
    }

    public void clickAlert(View view) {
        ToastUtils.showShort("修改");
        finish();
    }
}
