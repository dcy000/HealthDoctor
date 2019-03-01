package com.gcml.module_health_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gcml.module_health_manager.api.HealthManagerRouterApi;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/healthmanager/risk/assement/deal")
public class RiskAssessmentDealActivity extends StateBaseActivity {
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_risk_assessment_deal;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        showSuccess();
        mTvTitle.setText("风险评估处理");
    }

    @Override
    public void initView() {

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    public void clickSure(View view) {
        Routerfit.register(HealthManagerRouterApi.class).skipRiskAssessmentResultActivity();
    }
}
