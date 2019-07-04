package com.gcml.biz.followup.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.biz.followup.R;
import com.gzq.lib_resource.LazyFragment;

import java.util.ArrayList;

public class FollowUpMainFragment extends LazyFragment {


    private TabLayout tlFollowUp;
    private ViewPager vpFollowUp;

    private ArrayList<String> tabLables;
    private ArrayList<FollowUpTabFragment> tabFragments;

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
            tabFragments.add(FollowUpTabFragment.newInstance(i, tabLables.get(i)));
        }

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
}
