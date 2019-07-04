package com.gcml.biz.followup.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.biz.followup.R;
import com.gzq.lib_resource.LazyFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class FollowUpTabFragment extends LazyFragment {

    public static FollowUpTabFragment newInstance(int i, String label) {
        FollowUpTabFragment fragment = new FollowUpTabFragment();
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

    public FollowUpTabFragment() {
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
        return inflater.inflate(R.layout.fragment_follow_up_tab, container, false);
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

        }
    };

    @Override
    protected void onPageResume() {
        super.onPageResume();
        autoRefresh();
    }

    private void autoRefresh() {
        if (canAutoRefresh && srlRefresh != null) {
            srlRefresh.autoRefresh();
        }
    }

    private int itemFollowUpLayout = R.layout.item_follow_up;

    private class FollowUpHolder extends RecyclerView.ViewHolder {

        public FollowUpHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void onBind(int i) {

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
            return 0;
        }
    }
}
