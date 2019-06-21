package com.gcml.module_health_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_health_manager.api.HealthManagerRouterApi;
import com.gcml.module_health_manager.bean.MenuBena;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

@Route(path = "/medical/literature/activity")
public class MedicalLiteratureActivity extends StateBaseActivity {
    private RecyclerView mRview;
    private ArrayList<MenuBena> menuTop = new ArrayList<MenuBena>() {{
        add(new MenuBena(R.drawable.healthmanager_ic_health_task, "药物化合物查询"));
        add(new MenuBena(R.drawable.healthmanager_ic_health_report, "疾病症状查询"));
        add(new MenuBena(R.drawable.healthmanager_ic_risk_assessment, "基因查询"));
        add(new MenuBena(R.drawable.healthmanager_ic_health_measure, "通用关系查询"));
    }};
    private BaseQuickAdapter<MenuBena, BaseViewHolder> adapterTop;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_medical_literature;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("医学文献");
        mRview = (RecyclerView) findViewById(R.id.rview);

        mRview.setLayoutManager(new LinearLayoutManager(this));
        mRview.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 2));
        mRview.setAdapter(adapterTop = new BaseQuickAdapter<MenuBena, BaseViewHolder>(R.layout.item_layout_menu, menuTop) {
            @Override
            protected void convert(BaseViewHolder helper, MenuBena item) {
                helper.setText(R.id.title, item.getTitle());
                ((ImageView) helper.getView(R.id.iv_resource)).setImageResource(item.getImgResource());
            }
        });

        adapterTop.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    Routerfit.register(HealthManagerRouterApi.class).skipWebViewActivity("http://www.medppp.com/Sheet/SheetViewG?fl=FL0000000506", "药物化合物查询");
                } else if (position == 1) {
                    Routerfit.register(HealthManagerRouterApi.class).skipWebViewActivity("http://www.medppp.com/Sheet/SheetViewG?fl=FL0000000507", "疾病症状查询");
                } else if (position == 2) {
                    Routerfit.register(HealthManagerRouterApi.class).skipWebViewActivity("http://www.medppp.com/Sheet/SheetViewG?fl=FL0000000508", "基因查询");
                } else if (position == 3) {
                    Routerfit.register(HealthManagerRouterApi.class).skipWebViewActivity("http://www.medppp.com/Sheet/SheetViewG?fl=FL0000000519", "通用关系查询");
                }
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

}
