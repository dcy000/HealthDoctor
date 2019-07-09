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
import com.gcml.biz.followup.model.FollowUpRepository;
import com.gcml.biz.followup.model.entity.FollowUpEntity;
import com.gcml.biz.followup.model.entity.FollowUpList;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.LazyFragment;
import com.gzq.lib_resource.bean.UserEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

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

    private FollowUpRepository repository = new FollowUpRepository();

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
                            return;
                        }
                        clTip.setVisibility(View.GONE);
                        followUps.clear();
                        followUps.addAll(followUpList.getData());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
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

    private ArrayList<FollowUpEntity> followUps = new ArrayList<>();

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
