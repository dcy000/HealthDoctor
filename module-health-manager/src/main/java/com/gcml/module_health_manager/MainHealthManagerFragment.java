package com.gcml.module_health_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.module_health_manager.activity.ContractManageActivity;
import com.gcml.module_health_manager.api.HealthManagerRouterApi;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

/**
 * Created by gzq on 19-2-3.
 */
@Route(path = "/healthmanager/main")
public class MainHealthManagerFragment extends StateBaseFragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_business, container, false);
        initEvent(view);
        return view;
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.fragment_business;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        initEvent(view);
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    private void initEvent(View view) {
        view.findViewById(R.id.llAbnormalData).setOnClickListener(v -> {

        });
        view.findViewById(R.id.llSignDoctor).setOnClickListener(v -> {
            Routerfit.register(HealthManagerRouterApi.class).skipFamilyDoctorServiceActivity();
        });
        view.findViewById(R.id.llHealthManage).setOnClickListener(v -> {
            Routerfit.register(HealthManagerRouterApi.class).skipHealthMeasureActivity();
        });

        view.findViewById(R.id.llContractmanage).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ContractManageActivity.class));
        });
        view.findViewById(R.id.llFllowUpManage).setOnClickListener(v -> {
            Routerfit.register(HealthManagerRouterApi.class).skipFollowupMainActivity();
        });
        view.findViewById(R.id.llMedicalLterature).setOnClickListener(v -> {
            Routerfit.register(HealthManagerRouterApi.class).skipMedicalLiteratureActivity();
        });
    }

}
