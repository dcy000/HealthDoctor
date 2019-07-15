package com.gcml.biz.followup.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcml.biz.followup.R;
import com.gcml.biz.followup.model.FollowUpRepository;
import com.gcml.biz.followup.model.entity.FollowUpEntity;
import com.gcml.biz.followup.model.entity.FollowUpList;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.LazyFragment;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.UserEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class FollowUpMainTabFragment extends LazyFragment {

    public static FollowUpMainTabFragment newInstance(int i, String label) {
        FollowUpMainTabFragment fragment = new FollowUpMainTabFragment();
        Bundle args = new Bundle();
        args.putInt("index", i);
        args.putString("label", label);
        fragment.setArguments(args);
        return fragment;
    }

    private int index;
    private String label = "";

    private boolean canAutoRefresh = true;
    private SmartRefreshLayout srlRefresh;
    private RecyclerView rvFollowUp;
    private ConstraintLayout clTip;

    private FollowUpAdapter adapter;

    private FollowUpRepository repository = new FollowUpRepository();

    public FollowUpMainTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            index = arguments.getInt("index", 0);
            label = arguments.getString("label", "");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow_up_main_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srlRefresh = (SmartRefreshLayout) view.findViewById(R.id.srlRefresh);
        srlRefresh.setOnRefreshListener(onRefreshListener);

        clTip = (ConstraintLayout) view.findViewById(R.id.clTip);

        rvFollowUp = (RecyclerView) view.findViewById(R.id.rvFollowUp);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFollowUp.setLayoutManager(layoutManager);
        adapter = new FollowUpAdapter();
        rvFollowUp.setAdapter(adapter);
    }

    private OnRefreshListener onRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            refreshData();
        }
    };

    private void refreshData() {
        UserEntity user = Box.getSessionManager().getUser();
        if (user == null) {
            srlRefresh.finishRefresh();
            return;
        }

        repository.followUpList(user.getDocterid() + "", label, 1, 1000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        srlRefresh.finishRefresh();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<FollowUpList>() {
                    @Override
                    public void onNext(FollowUpList followUpList) {
                        if (followUpList.getCount() == 0
                                || followUpList.getData() == null
                                || followUpList.getData().size() == 0) {
                            clTip.setVisibility(View.VISIBLE);

                            if (!followUps.isEmpty()) {
                                followUps.clear();
                                adapter.notifyDataSetChanged();
                            }
                            canAutoRefresh = true;
                            return;
                        }
                        canAutoRefresh = false;
                        clTip.setVisibility(View.GONE);
                        followUps.clear();
                        followUps.addAll(followUpList.getData());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        canAutoRefresh = true;
                    }
                });
    }

    @Override
    protected void onPageResume() {
        super.onPageResume();
        //设置状态栏的颜色

        autoRefresh();
    }

    private void autoRefresh() {
        if (canAutoRefresh && srlRefresh != null) {
            srlRefresh.autoRefresh();
        }
    }


    /**
     * 随访
     *
     * @param entity
     */
    private void showFollowUp(FollowUpEntity entity) {

        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        String tag = FollowUpDoFragment.class.getName();
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = FollowUpDoFragment.newInstance(entity);
        }

        FragmentTransaction transaction = fm.beginTransaction();

        Fragment fragmentToHide = fm.findFragmentByTag(FollowUpMainFragment.class.getName());
        if (fragmentToHide != null) {
            transaction.hide(fragmentToHide);
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.flContainer, fragment, tag);
        }

        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();

    }

    /**
     * 查看随访
     *
     * @param entity
     */
    private void showFollowUpDetails(FollowUpEntity entity) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        String tag = FollowUpDetailFragment.class.getName();
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = FollowUpDetailFragment.newInstance(entity);
        }

        FragmentTransaction transaction = fm.beginTransaction();

        Fragment fragmentToHide = fm.findFragmentByTag(FollowUpMainFragment.class.getName());
        if (fragmentToHide != null) {
            transaction.hide(fragmentToHide);
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.flContainer, fragment, tag);
        }

        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }

    /**
     * 取消随访
     *
     * @param entity
     */
    private void showCancelFollowUp(FollowUpEntity entity) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        String tag = FollowUpCancelFragment.class.getName();
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = FollowUpCancelFragment.newInstance(entity);
        }

        FragmentTransaction transaction = fm.beginTransaction();

        Fragment fragmentToHide = fm.findFragmentByTag(FollowUpMainFragment.class.getName());
        if (fragmentToHide != null) {
            transaction.hide(fragmentToHide);
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.flContainer, fragment, tag);
        }

        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }

    /**
     * 修改随访
     *
     * @param entity
     */
    private void showUpdateFollowUp(FollowUpEntity entity) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        String tag = FollowUpAddOrUpdateFragment.class.getName();
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = FollowUpAddOrUpdateFragment.newInstance(entity);
        }

        FragmentTransaction transaction = fm.beginTransaction();

        Fragment fragmentToHide = fm.findFragmentByTag(FollowUpMainFragment.class.getName());
        if (fragmentToHide != null) {
            transaction.hide(fragmentToHide);
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.flContainer, fragment, tag);
        }

        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }

    private ArrayList<FollowUpEntity> followUps = new ArrayList<>();

    private int itemFollowUpLayout = R.layout.item_follow_up;

    private class FollowUpHolder extends RecyclerView.ViewHolder {

        private final ImageView ivFollowAvatar;
        private final TextView tvFollower;
        private final TextView tvFollowLabel;
        private final TextView tvFollowTime;
        private final TextView tvFollowName;
        private final TextView tvFollowTag;
        private final TextView tvCreate;
        private final TextView tvAction;
        private final TextView tvUpdate;
        private final TextView tvCancel;

        public FollowUpHolder(@NonNull View itemView) {
            super(itemView);
            tvFollowLabel = (TextView) itemView.findViewById(R.id.tvFollowLabel);
            tvFollowTime = (TextView) itemView.findViewById(R.id.tvFollowTime);
            ivFollowAvatar = (ImageView) itemView.findViewById(R.id.ivFollowAvatar);
            tvFollowName = (TextView) itemView.findViewById(R.id.tvFollowName);
            tvFollowTag = (TextView) itemView.findViewById(R.id.tvFollowTag);
            tvFollower = (TextView) itemView.findViewById(R.id.tvFollower);
            tvCreate = (TextView) itemView.findViewById(R.id.tvCreate);
            tvAction = (TextView) itemView.findViewById(R.id.tvAction);
            tvUpdate = (TextView) itemView.findViewById(R.id.tvUpdate);
            tvCancel = (TextView) itemView.findViewById(R.id.tvCancel);

            tvAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    FollowUpEntity entity = followUps.get(position);
                    if ("待随访".equals(entity.getFollowStatus())) {
                        // 随访

                        showFollowUp(entity);
                    } else {
                        // 查看
                        showFollowUpDetails(entity);
                    }
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    FollowUpEntity entity = followUps.get(position);
                    showCancelFollowUp(entity);
                }
            });

            tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    FollowUpEntity entity = followUps.get(position);
                    showUpdateFollowUp(entity);
                }
            });
        }


        public void onBind(int i) {
            FollowUpEntity entity = followUps.get(i);
            String planTitle = entity.getPlanTitle();
            planTitle = TextUtils.isEmpty(planTitle) ? "" : planTitle;
            tvFollowLabel.setText(planTitle);
            tvFollowTime.setText(entity.getPlanDate());

            ResidentBean resident = entity.getResident();
            if (resident != null) {
                Glide.with(FollowUpMainTabFragment.this)
                        .load(resident.getUserPhoto())
                        .apply(RequestOptions.placeholderOf(R.drawable.common_ic_avatar_default))
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivFollowAvatar);
                String name = resident.getBname();
                name = TextUtils.isEmpty(name) ? "" : name;
                tvFollowName.setText(name);

                String userType = resident.getUserType();
                userType = TextUtils.isEmpty(userType) ? "" : userType;
                tvFollowTag.setSelected(!userType.equals("正常居民"));
                tvFollowTag.setText(userType);
            }

            UserEntity planDoctor = entity.getPlanDoctor();
            String doctorName = planDoctor.getDoctername();
            doctorName = TextUtils.isEmpty(doctorName) ? "" : "随访医生： " + doctorName;
            tvFollower.setText(doctorName);

            String createDoctorName = entity.getCreateDoctorName();
            createDoctorName = TextUtils.isEmpty(createDoctorName) ? "" : "创建人： " + createDoctorName + " | ";
            String createdOn = entity.getCreatedOn();
            createdOn = TextUtils.isEmpty(createdOn) ? "" : createdOn;
            tvCreate.setText(createDoctorName + createdOn);

            String followStatus = entity.getFollowStatus();
            if ("待随访".equals(followStatus)) {
                tvAction.setText("随访");
                tvAction.setBackgroundResource(R.drawable.followup_bg_3f86fc);
                tvAction.setTextColor(Color.parseColor("#3F86FC"));
                tvCancel.setVisibility(View.VISIBLE);
                tvUpdate.setVisibility(View.VISIBLE);
            } else {
                tvAction.setText("查看");
                tvAction.setBackgroundResource(R.drawable.followup_bg_gray);
                tvAction.setTextColor(Color.parseColor("#909399"));
                tvCancel.setVisibility(View.GONE);
                tvUpdate.setVisibility(View.GONE);
            }
        }
    }

    private class FollowUpAdapter extends RecyclerView.Adapter<FollowUpHolder> {

        @NonNull
        @Override
        public FollowUpHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(itemFollowUpLayout, viewGroup, false);
            return new FollowUpHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FollowUpHolder followUpHolder, int i) {
            followUpHolder.onBind(i);
        }

        @Override
        public int getItemCount() {
            return followUps != null ? followUps.size() : 0;
        }
    }
}
