package com.gcml.biz.followup;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class FragmentUtils {

    public static void finish(FragmentActivity activity) {
        if (activity == null) {
            return;
        }

        FragmentManager fm = activity.getSupportFragmentManager();
        if (fm == null) {
            return;
        }

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            fm.executePendingTransactions();
            return;
        }

        activity.finish();
    }
}
