package com.gcml.biz.followup.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.biz.followup.FragmentUtils;
import com.gcml.biz.followup.R;
import com.gcml.biz.followup.model.entity.FollowUpEntity;
import com.gzq.lib_resource.LazyFragment;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.UserEntity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 随访详情
 */
public class FollowUpDetailFragment extends LazyFragment {

    private FollowUpEntity followUpEntity;
    private TagAdapter tagAdapter;

    public static FollowUpDetailFragment newInstance(FollowUpEntity entity) {
        FollowUpDetailFragment fragment = new FollowUpDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("followUp", entity);
        fragment.setArguments(args);
        return fragment;
    }

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            followUpEntity = arguments.getParcelable("followUp");
        }
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
        initView(view);

        initData();

    }

    private void initData() {
        if (followUpEntity == null) {
            return;
        }

        String followStatus = followUpEntity.getFollowStatus();
        followStatus = TextUtils.isEmpty(followStatus) ? "" : followStatus;
        String planDate = followUpEntity.getPlanDate();
        planDate = TextUtils.isEmpty(planDate) ? "" : planDate;
        tvFollowUpTime.setText(planDate);
        if ("已取消".equals(followStatus)) {
            tvFollowUpStatus.setText("随访已取消");
            clFollowUpStatus.setEnabled(false);
            tvFollowUpResultLabel.setText("取消原因");
            clFollowUpContent.setVisibility(View.VISIBLE);
            clFollowUpResult.setVisibility(View.VISIBLE);
            clFollowUpType.setVisibility(View.GONE);

            String planContent = followUpEntity.getPlanContent();
            planContent = TextUtils.isEmpty(planContent) ? "" : planContent;
            tvFollowUpContent.setText(planContent);

            String resultContent = followUpEntity.getResultContent();
            resultContent = TextUtils.isEmpty(resultContent) ? "" : resultContent;
            tvFollowUpResult.setText(resultContent);

        } else if ("已随访".equals(followStatus)) {
            tvFollowUpStatus.setText("随访成功");
            clFollowUpStatus.setEnabled(true);
            tvFollowUpResultLabel.setText("随访结果");
            clFollowUpContent.setVisibility(View.VISIBLE);
            clFollowUpResult.setVisibility(View.VISIBLE);
            clFollowUpType.setVisibility(View.VISIBLE);
//            tvFollowUpTypeLabel.setText("");

            String planContent = followUpEntity.getPlanContent();
            planContent = TextUtils.isEmpty(planContent) ? "" : planContent;
            tvFollowUpContent.setText(planContent);

            String resultContent = followUpEntity.getResultContent();
            resultContent = TextUtils.isEmpty(resultContent) ? "" : resultContent;
            tvFollowUpResult.setText(resultContent);

            String categoryName = followUpEntity.getCategoryName();
            categoryName = TextUtils.isEmpty(categoryName) ? "" : categoryName;
            tvFollowUpType.setText(categoryName);
        } else if ("随访失约".equals(followStatus)) {
            tvFollowUpStatus.setText("随访失约");
            clFollowUpStatus.setEnabled(false);
            clFollowUpContent.setVisibility(View.VISIBLE);
            clFollowUpResult.setVisibility(View.GONE);
            clFollowUpType.setVisibility(View.GONE);

            String planContent = followUpEntity.getPlanContent();
            planContent = TextUtils.isEmpty(planContent) ? "" : planContent;
            tvFollowUpContent.setText(planContent);
        }

        UserEntity planDoctor = followUpEntity.getPlanDoctor();
        String doctorName = planDoctor.getDoctername();
        doctorName = TextUtils.isEmpty(doctorName) ? "" : doctorName;
        tvFollowUpDoctor.setText(doctorName);


        ResidentBean resident = followUpEntity.getResident();
        if (resident != null) {

            String bname = resident.getBname();
            bname = TextUtils.isEmpty(bname) ? "" : bname;
            tvFollowUpResident.setText(bname);

            int age = resident.getAge();
            String gender = resident.getSex();
            gender = TextUtils.isEmpty(gender) ? "" : gender;

            tvResidentAge.setText(age + "岁");
            tvResidentGender.setText(gender);
            String userType = resident.getUserType();
            userType = TextUtils.isEmpty(userType) ? "" : userType;
            String[] split = userType.split(",");
            tags.clear();
            tags.addAll(Arrays.asList(split));
            tagAdapter.notifyDataSetChanged();
        }
    }

    private void initView(@NonNull View view) {
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

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        tagAdapter = new TagAdapter();
        rvHealthTag.setLayoutManager(layoutManager);
        rvHealthTag.setAdapter(tagAdapter);
    }

    private void onAction() {

    }

    private void onBack() {
        FragmentUtils.finish(getActivity());
    }

    private ArrayList<String> tags = new ArrayList<>();

    private class TagHolder extends RecyclerView.ViewHolder {

        private final TextView tvTag;

        public TagHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = ((TextView) itemView.findViewById(R.id.tvTag));
        }

        public void onBind(int i) {
            String s = tags.get(i);
            tvTag.setText(s);
            tvTag.setSelected(!"正常居民".equals(s));
        }
    }

    private class TagAdapter extends RecyclerView.Adapter<TagHolder> {

        @NonNull
        @Override
        public TagHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.item_follow_up_health_tag, viewGroup, false);
            return new TagHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TagHolder tagHolder, int i) {
            tagHolder.onBind(i);
        }

        @Override
        public int getItemCount() {
            return tags != null ? tags.size() : 0;
        }
    }

}
