package com.gcml.biz.followup.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.biz.followup.FragmentUtils;
import com.gcml.biz.followup.R;
import com.gzq.lib_resource.LazyFragment;

import java.util.ArrayList;

public class FollowUpMainFragment extends LazyFragment {


    // toolbar
    private ImageView ivToolbarLeft;
    private TextView tvToolbarLeft;
    private TextView tvToolbarRight;
    private TextView tvToolbarTitle;

    private TabLayout tlFollowUp;
    private ViewPager vpFollowUp;

    private ArrayList<String> tabLables;
    private ArrayList<FollowUpMainTabFragment> tabFragments;

    public FollowUpMainFragment() {

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow_up_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivToolbarLeft = (ImageView) view.findViewById(R.id.ivToolbarLeft);
        tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        tvToolbarRight = (TextView) view.findViewById(R.id.tvToolbarRight);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
        ivToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setVisibility(View.GONE);
        tvToolbarRight.setVisibility(View.VISIBLE);
        tvToolbarRight.setText("新增随访");
        tvToolbarTitle.setText("随访");
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
        tvToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAction();
            }
        });

        tlFollowUp = (TabLayout) view.findViewById(R.id.tlFollowUp);
        vpFollowUp = (ViewPager) view.findViewById(R.id.vpFollowUp);
        tlFollowUp.setupWithViewPager(vpFollowUp);

        tabLables = new ArrayList<>();
        tabLables.add("待随访");
        tabLables.add("随访失约");
        tabLables.add("已随访");
        tabLables.add("已取消");

        tabFragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tabFragments.add(FollowUpMainTabFragment.newInstance(i, tabLables.get(i)));
        }

        vpFollowUp.setOffscreenPageLimit(4);

        vpFollowUp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return tabFragments.get(i);
            }

            @Override
            public int getCount() {
                return tabFragments == null ? 0 : tabFragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabLables.get(position);
            }
        });
    }

    private void onAction() {
        showAddFragment();
    }

    private void onBack() {
        FragmentUtils.finish(getActivity());
    }

    private void showAddFragment() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        String tag = FollowUpAddOrUpdateFragment.class.getName();
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new FollowUpAddOrUpdateFragment();
        }
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.hide(this);

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.flContainer, fragment, tag);
        }

        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }
}
