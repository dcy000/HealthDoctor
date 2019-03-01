package com.gcml.module_health_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_health_manager.api.HealthManagerRouterApi;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.bean.HealthManagerReportBean;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/healthmanager/health/approval")
public class HealthManagerApprovalActivity extends StateBaseActivity {
    private RecyclerView mRv;
    private ArrayList<HealthManagerReportBean> healthManagerReportBeans = new ArrayList<HealthManagerReportBean>() {{
        add(new HealthManagerReportBean("张三", "2019.1.1", "待处理事项：风险评估"));
        add(new HealthManagerReportBean("李四", "2019.1.1", "待处理事项：健康任务"));
        add(new HealthManagerReportBean("王五", "2019.1.1", "待处理事项：风险评估"));
    }};
    private BaseQuickAdapter<HealthManagerReportBean, BaseViewHolder> adapter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_health_manager_approval;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("健康管理审批");
        mRv = (RecyclerView) findViewById(R.id.rv);
        initRv();
    }

    private void initRv() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(adapter = new BaseQuickAdapter<HealthManagerReportBean, BaseViewHolder>(R.layout.item_layout_health_manager_approval, healthManagerReportBeans) {
            @Override
            protected void convert(BaseViewHolder helper, HealthManagerReportBean item) {
                Glide.with(Box.getApp())
                        .load(Box.getString(R.string.head_img))
                        .into(((CircleImageView) helper.getView(R.id.civ_head)));
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_commit_time, item.getCommitTime());
                helper.setText(R.id.tv_audit_time, item.getAuditTime());
                helper.getView(R.id.tv_see).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Routerfit.register(HealthManagerRouterApi.class).skipRiskAssessmentDealActivity();
                    }
                });
            }
        });

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

}
