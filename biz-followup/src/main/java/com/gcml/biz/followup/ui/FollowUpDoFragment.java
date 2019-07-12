package com.gcml.biz.followup.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.biz.followup.R;
import com.gzq.lib_resource.LazyFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowUpDoFragment extends LazyFragment {

    // toolbar
    private ImageView ivToolbarLeft;
    private TextView tvToolbarLeft;
    private TextView tvToolbarRight;
    private TextView tvToolbarTitle;

    private ConstraintLayout clContainer;
    private NestedScrollView nsvContainer;
    private LinearLayout llContainer;
    private ConstraintLayout clResident;
    private ImageView ivResidentAvatar;
    private TextView tvResidentName;
    private TextView tvResidentGenderAge;
    private TextView tvResidentTag;
    private CircleImageView civCall;
    private ConstraintLayout clResidentAddress;
    private TextView tvResidentAddress;
    private TextView tvCopyResidentAddress;
    private ConstraintLayout clFollowUpDoctor;
    private TextView tvFollowUpContentLabel;
    private TextView tvFollowUpContent;
    private ConstraintLayout clFollowUpContent;
    private TextView tvFollowUpDoctorLabel;
    private TextView tvFollowUpDoctor;
    private ConstraintLayout clFollowUpTime;
    private TextView tvFollowUpTimeLabel;
    private TextView tvFollowUpTime;
    private ImageView ivFollowUpTimeNext;
    private ConstraintLayout clFollowUpType;
    private TextView tvFollowUpTypeLabel;
    private TextView tvFollowUpType;
    private ImageView ivFollowUpTypeNext;
    private ConstraintLayout clFollowUpResult;
    private TextView tvFollowUpResultLabel;
    private TextView tvFollowUpTemplate;
    private ImageView ivFollowUpNext;
    private EditText etFollowUpResult;
    private TextView tvAction;

    public FollowUpDoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow_up_do, container, false);
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
//        tvToolbarRight.setVisibility(View.GONE);
        tvToolbarTitle.setText("随访");
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

        clResident = (ConstraintLayout) view.findViewById(R.id.clResident);
        ivResidentAvatar = (ImageView) view.findViewById(R.id.ivResidentAvatar);
        tvResidentName = (TextView) view.findViewById(R.id.tvResidentName);
        tvResidentGenderAge = (TextView) view.findViewById(R.id.tvResidentGenderAge);

        tvResidentTag = (TextView) view.findViewById(R.id.tvResidentTag);
        civCall = (CircleImageView) view.findViewById(R.id.civCall);

        clResidentAddress = (ConstraintLayout) view.findViewById(R.id.clResidentAddress);
        tvResidentAddress = (TextView) view.findViewById(R.id.tvResidentAddress);
        tvCopyResidentAddress = (TextView) view.findViewById(R.id.tvCopyResidentAddress);

        clFollowUpContent = (ConstraintLayout) view.findViewById(R.id.clFollowUpContent);
        tvFollowUpContentLabel = (TextView) view.findViewById(R.id.tvFollowUpContentLabel);
        tvFollowUpContent = (TextView) view.findViewById(R.id.tvFollowUpContent);

        clFollowUpDoctor = (ConstraintLayout) view.findViewById(R.id.clFollowUpDoctor);
        tvFollowUpDoctorLabel = (TextView) view.findViewById(R.id.tvFollowUpDoctorLabel);
        tvFollowUpDoctor = (TextView) view.findViewById(R.id.tvFollowUpDoctor);

        clFollowUpTime = (ConstraintLayout) view.findViewById(R.id.clFollowUpTime);
        tvFollowUpTimeLabel = (TextView) view.findViewById(R.id.tvFollowUpTimeLabel);
        tvFollowUpTime = (TextView) view.findViewById(R.id.tvFollowUpTime);
        ivFollowUpTimeNext = (ImageView) view.findViewById(R.id.ivFollowUpTimeNext);

        clFollowUpType = (ConstraintLayout) view.findViewById(R.id.clFollowUpType);
        tvFollowUpTypeLabel = (TextView) view.findViewById(R.id.tvFollowUpTypeLabel);
        tvFollowUpType = (TextView) view.findViewById(R.id.tvFollowUpType);
        ivFollowUpTypeNext = (ImageView) view.findViewById(R.id.ivFollowUpTypeNext);

        clFollowUpResult = (ConstraintLayout) view.findViewById(R.id.clFollowUpResult);
        tvFollowUpResultLabel = (TextView) view.findViewById(R.id.tvFollowUpResultLabel);
        tvFollowUpTemplate = (TextView) view.findViewById(R.id.tvFollowUpTemplate);
        ivFollowUpNext = (ImageView) view.findViewById(R.id.ivFollowUpNext);
        etFollowUpResult = (EditText) view.findViewById(R.id.etFollowUpResult);

        tvAction = (TextView) view.findViewById(R.id.tvAction);
        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAction();
            }
        });


    }

    private void onAction() {

    }

    private void onBack() {

    }
}
