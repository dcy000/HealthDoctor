package com.gcml.biz.followup.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.biz.followup.R;
import com.gzq.lib_resource.LazyFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowUpDetailFragment extends LazyFragment {


    public FollowUpDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow_up_detail, container, false);
    }

}
