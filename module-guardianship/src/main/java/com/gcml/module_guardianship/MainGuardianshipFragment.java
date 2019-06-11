package com.gcml.module_guardianship;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gzq.lib_resource.adapter.PageFragmentAdapter;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.ScreenUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

/**
 * Created by gzq on 19-2-3.
 */

@Route(path = "/guardianship/main")
public class MainGuardianshipFragment extends StateBaseFragment implements View.OnClickListener, NumberChange {
    private EditText mEtGotoSearch;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titleString = {"VIP居民(0)", "普通居民(0)"};
    private ViewPager mViewPager;
    private SlidingTabLayout mTitleTabLayout;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.guardianship_fragment_main;
    }

    @Override
    public void initParams(Bundle bundle) {
        StateBaseFragment vipResidentFragment = Routerfit.register(GuardianshipRouterApi.class).getVipResidentFragment();
        ((VipResidentFragment) vipResidentFragment).setNumberChange(this);
        fragments.add(vipResidentFragment);
        StateBaseFragment normalResidentFragment = Routerfit.register(GuardianshipRouterApi.class).getNormalResidentFragment();
        ((NormalResidentFragment) normalResidentFragment).setNumberChange(this);
        fragments.add(normalResidentFragment);
    }

    @Override
    public void initView(View view) {
        mEtGotoSearch = (EditText) view.findViewById(R.id.et_goto_search);
        mEtGotoSearch.setOnClickListener(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mEtGotoSearch.getLayoutParams());
        layoutParams.topMargin = ScreenUtils.getStatusBarHeight(mContext);
        mEtGotoSearch.setLayoutParams(layoutParams);

        mViewPager = view.findViewById(R.id.vp_msg);
        mTitleTabLayout = view.findViewById(R.id.layout_tab);
        mTitleTabLayout.setViewPager(mViewPager, titleString, getActivity(), fragments);
    }

    @Override
    public IPresenter obtainPresenter() {

        return null;
    }


    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.et_goto_search) {
            Routerfit.register(GuardianshipRouterApi.class).skipSearchFamilyActivity();
        }
    }

    @Override
    public void vipNumberChanged(int num) {
//        mTitleTabLayout.getTabAt(0).setText("VIP居民(" + num + ")");
        mTitleTabLayout.getTitleView(0).setText("VIP居民(" + num + ")");
    }

    @Override
    public void normalNumberChanged(int num) {
//        mTitleTabLayout.getTabAt(1).setText("普通居民(" + num + ")");
        mTitleTabLayout.getTitleView(1).setText("普通居民(" + num + ")");
    }
}
