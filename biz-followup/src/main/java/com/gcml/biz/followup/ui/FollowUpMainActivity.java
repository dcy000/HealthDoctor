package com.gcml.biz.followup.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.gcml.biz.followup.R;
import com.githang.statusbar.StatusBarCompat;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.BaseActivity;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/followup/main")
public class FollowUpMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_up_main);
//        StatusBarCompat.setFitsSystemWindows(getWindow(), true);
        StatusBarCompat.setStatusBarColor(this, Box.getColor(R.color.white));
        showFragment();
    }

    private void showFragment() {
        String tag = FollowUpMainFragment.class.getName();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new FollowUpMainFragment();
        }
        FragmentTransaction transaction = fm.beginTransaction();
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.flContainer, fragment, tag);
        }
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }

}
