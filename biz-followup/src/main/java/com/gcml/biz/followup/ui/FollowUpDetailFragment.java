package com.gcml.biz.followup.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.biz.followup.R;
import com.gzq.lib_resource.LazyFragment;

/**
 * 随访详情
 */
public class FollowUpDetailFragment extends LazyFragment {

    // toolbar
    private ImageView ivToolbarLeft;
    private TextView tvToolbarLeft;
    private TextView tvToolbarRight;
    private TextView tvToolbarTitle;

    private ConstraintLayout clContainer;
    private NestedScrollView nsvContainer;
    private LinearLayout llContainer;

    private ConstraintLayout clFollowUpStatus;
    private TextView tvFollowUpStatus;
    private TextView tvFollowUpTime;

    private ConstraintLayout clFollowUpContent;
    private TextView tvFollowUpContentLabel;
    private TextView tvFollowUpContent;

    private ConstraintLayout clFollowUpResult;
    private TextView tvFollowUpResultLabel;
    private TextView tvFollowUpResult;

    private ConstraintLayout clFollowUpType;
    private TextView tvFollowUpTypeLabel;
    private TextView tvFollowUpType;

    private ConstraintLayout clFollowUpDoctor;
    private TextView tvFollowUpDoctorLabel;
    private TextView tvFollowUpDoctor;

    private ConstraintLayout clResident;
    private TextView tvFollowUpResidentLabel;
    private TextView tvFollowUpResident;

    private ConstraintLayout clResidentAge;
    private TextView tvResidentAgeLabel;
    private TextView tvResidentAge;

    private ConstraintLayout clResidentGender;
    private TextView tvResidentGenderLabel;
    private TextView tvResidentGender;

    private ConstraintLayout clResidentHealthTag;
    private TextView tvResidentHealthTagLabel;
    private RecyclerView rvHealthTag;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivToolbarLeft = (ImageView) view.findViewById(R.id.ivToolbarLeft);
        tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        tvToolbarRight = (TextView) view.findViewById(R.id.tvToolbarRight);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
        ivToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setVisibility(View.GONE);
        tvToolbarRight.setVisibility(View.GONE);
        tvToolbarTitle.setText("随访详情");
//        tvToolbarLeft.setText("取消");
//        tvToolbarRight.setText("提交");
        tvToolbarRight.setTextColor(Color.parseColor("#909399"));
        tvToolbarLeft.setOnClickListener(new View.OnClickListener() {
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

        clContainer = (ConstraintLayout) view.findViewById(R.id.clContainer);
        nsvContainer = (NestedScrollView) view.findViewById(R.id.nsvContainer);
        llContainer = (LinearLayout) view.findViewById(R.id.llContainer);

        clFollowUpStatus = (ConstraintLayout) view.findViewById(R.id.clFollowUpStatus);
        tvFollowUpStatus = (TextView) view.findViewById(R.id.tvFollowUpStatus);
        tvFollowUpTime = (TextView) view.findViewById(R.id.tvFollowUpTime);

        clFollowUpContent = (ConstraintLayout) view.findViewById(R.id.clFollowUpContent);
        tvFollowUpContentLabel = (TextView) view.findViewById(R.id.tvFollowUpContentLabel);
        tvFollowUpContent = (TextView) view.findViewById(R.id.tvFollowUpContent);

        clFollowUpResult = (ConstraintLayout) view.findViewById(R.id.clFollowUpResult);
        tvFollowUpResultLabel = (TextView) view.findViewById(R.id.tvFollowUpResultLabel);
        tvFollowUpResult = (TextView) view.findViewById(R.id.tvFollowUpResult);

        clFollowUpType = (ConstraintLayout) view.findViewById(R.id.clFollowUpType);
        tvFollowUpTypeLabel = (TextView) view.findViewById(R.id.tvFollowUpTypeLabel);
        tvFollowUpType = (TextView) view.findViewById(R.id.tvFollowUpType);

        clFollowUpDoctor = (ConstraintLayout) view.findViewById(R.id.clFollowUpDoctor);
        tvFollowUpDoctorLabel = (TextView) view.findViewById(R.id.tvFollowUpDoctorLabel);
        tvFollowUpDoctor = (TextView) view.findViewById(R.id.tvFollowUpDoctor);

        clResident = (ConstraintLayout) view.findViewById(R.id.clResident);
        tvFollowUpResidentLabel = (TextView) view.findViewById(R.id.tvFollowUpResidentLabel);
        tvFollowUpResident = (TextView) view.findViewById(R.id.tvFollowUpResident);

        clResidentAge = (ConstraintLayout) view.findViewById(R.id.clResidentAge);
        tvResidentAgeLabel = (TextView) view.findViewById(R.id.tvResidentAgeLabel);
        tvResidentAge = (TextView) view.findViewById(R.id.tvResidentAge);

        clResidentGender = (ConstraintLayout) view.findViewById(R.id.clResidentGender);
        tvResidentGenderLabel = (TextView) view.findViewById(R.id.tvResidentGenderLabel);
        tvResidentGender = (TextView) view.findViewById(R.id.tvResidentGender);

        clResidentHealthTag = (ConstraintLayout) view.findViewById(R.id.clResidentHealthTag);
        tvResidentHealthTagLabel = (TextView) view.findViewById(R.id.tvResidentHealthTagLabel);
        rvHealthTag = (RecyclerView) view.findViewById(R.id.rvHealthTag);

    }

    private void onAction() {

    }

    private void onBack() {

    }
}
