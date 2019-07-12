package com.gcml.biz.followup.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.biz.followup.FragmentUtils;
import com.gcml.biz.followup.R;
import com.gcml.biz.followup.model.entity.HealthTagEntity;
import com.gzq.lib_resource.LazyFragment;
import com.gzq.lib_resource.bean.UserEntity;


/**
 * 新增随访 / 修改随访
 */
public class FollowUpAddOrUpdateFragment extends LazyFragment {

    // toolbar
    private ImageView ivToolbarLeft;
    private TextView tvToolbarLeft;
    private TextView tvToolbarRight;
    private TextView tvToolbarTitle;

    // 居民健康状况
    private ConstraintLayout clResidentHealthStatus;
    private TextView tvResidentHealthStatusContent;
    private ImageView ivResidentHealthStatusContent;

    // 居民
    private ConstraintLayout clResidents;
    private TextView tvResidentsContent;
    private ImageView ivResidentsContent;
    private RecyclerView rvResidents;

    // 随访内容
    private ConstraintLayout clFollowUp;
    private TextView tvFollowUpContent;
    private ImageView ivFollowUpContent;
    private EditText etFollowUpContent;

    // 随访时间
    private ConstraintLayout clTime;
    private TextView tvTimeContent;
    private ImageView ivTimeContent;

    // 随访者
    private ConstraintLayout clFollower;
    private TextView tvFollowerContent;
    private ImageView ivFollowerContent;

    public FollowUpAddOrUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow_up_add_or_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);

    }

    private void initView(@NonNull View view, Bundle savedInstanceState) {
        ivToolbarLeft = (ImageView) view.findViewById(R.id.ivToolbarLeft);
        tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        tvToolbarRight = (TextView) view.findViewById(R.id.tvToolbarRight);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
        ivToolbarLeft.setVisibility(View.GONE);
        tvToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarRight.setVisibility(View.VISIBLE);
        tvToolbarLeft.setText("取消");
        tvToolbarRight.setText("完成");
        tvToolbarTitle.setText("新增随访");
//        tvToolbarTitle.setText("修改随访计划");
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


        clResidentHealthStatus = (ConstraintLayout) view.findViewById(R.id.clResidentHealthStatus);
        tvResidentHealthStatusContent = (TextView) view.findViewById(R.id.tvResidentHealthStatusContent);
        ivResidentHealthStatusContent = (ImageView) view.findViewById(R.id.ivResidentHealthStatusContent);
        clResidentHealthStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickResidentHealthStatus();
            }
        });


        clResidents = (ConstraintLayout) view.findViewById(R.id.clResidents);
        tvResidentsContent = (TextView) view.findViewById(R.id.tvResidentsContent);
        ivResidentsContent = (ImageView) view.findViewById(R.id.ivResidentsContent);
        rvResidents = (RecyclerView) view.findViewById(R.id.rvResidents);
        clResidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickResidents();
            }
        });

        clFollowUp = (ConstraintLayout) view.findViewById(R.id.clFollowUp);
        tvFollowUpContent = (TextView) view.findViewById(R.id.tvFollowUpContent);
        ivFollowUpContent = (ImageView) view.findViewById(R.id.ivFollowUpContent);
        etFollowUpContent = (EditText) view.findViewById(R.id.etFollowUpContent);
        clFollowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickFollowUp();
            }
        });

        clTime = (ConstraintLayout) view.findViewById(R.id.clTime);
        tvTimeContent = (TextView) view.findViewById(R.id.tvTimeContent);
        ivTimeContent = (ImageView) view.findViewById(R.id.ivTimeContent);
        clTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickTime();
            }
        });

        clFollower = (ConstraintLayout) view.findViewById(R.id.clFollower);
        tvFollowerContent = (TextView) view.findViewById(R.id.tvFollowerContent);
        ivFollowerContent = (ImageView) view.findViewById(R.id.ivFollowerContent);
        clFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickFollower();
            }
        });

    }

    private void onPickFollower() {
        showPickFollowerFragment();
    }


    private void onPickTime() {

    }

    private void onPickFollowUp() {

    }

    private void onPickResidents() {
        showPickResidentsFragment();
    }

    private void onPickResidentHealthStatus() {
        showPickHealthStatusFragment();
    }

    private void onAction() {

    }

    private void onBack() {
        FragmentUtils.finish(getActivity());
    }

    private UserEntity follower;

    private FollowUpPickFollowerFragment.Callback followerCallback = new FollowUpPickFollowerFragment.Callback() {
        @Override
        public void onPickedFollower(UserEntity entity) {
            follower = entity;
        }
    };

    private void showPickFollowerFragment() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        String tag = FollowUpPickFollowerFragment.class.getName();
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new FollowUpPickFollowerFragment();
        }

        ((FollowUpPickFollowerFragment) fragment).setCallback(followerCallback);

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

    private HealthTagEntity tagEntity;

    private FollowUpPickHealthStatusFragment.Callback callback = new FollowUpPickHealthStatusFragment.Callback() {
        @Override
        public void onPickedHealthStatus(HealthTagEntity entity) {
            tagEntity = entity;
        }
    };

    private void showPickHealthStatusFragment() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        String tag = FollowUpPickHealthStatusFragment.class.getName();
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new FollowUpPickHealthStatusFragment();
        }

        ((FollowUpPickHealthStatusFragment) fragment).setCallback(callback);

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


    private void showPickResidentsFragment() {
        if (tagEntity == null) {
            return;
        }

        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        String tag = FollowUpPickResidentsFragment.class.getName();
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = FollowUpPickResidentsFragment.newInstacne(tagEntity);
        }

//        ((FollowUpPickResidentsFragment) fragment).setCallback(callback);

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
