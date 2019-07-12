package com.gcml.biz.followup.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.biz.followup.FragmentUtils;
import com.gcml.biz.followup.R;
import com.gcml.biz.followup.model.FollowUpRepository;
import com.gcml.biz.followup.model.entity.HealthTagEntity;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.LazyFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * 选择居名健康状态
 */
public class FollowUpPickHealthStatusFragment extends LazyFragment {

    public interface Callback {
        void onPickedHealthStatus(HealthTagEntity entity);
    }

    private Callback callback;

    // toolbar
    private ImageView ivToolbarLeft;
    private TextView tvToolbarLeft;
    private TextView tvToolbarRight;
    private TextView tvToolbarTitle;
    private SmartRefreshLayout srlRefresh;
    private RecyclerView rvHealthTag;

    private FollowUpRepository repository = new FollowUpRepository();
    private HealthTagAdapter adapter;

    public FollowUpPickHealthStatusFragment() {
        // Required empty public constructor
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow_up_pick_health_status, container, false);
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
//        tvToolbarRight.setText("");
//        tvToolbarTitle.setText("");
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

        srlRefresh = (SmartRefreshLayout) view.findViewById(R.id.srlRefresh);
        rvHealthTag = (RecyclerView) view.findViewById(R.id.rvHealthTag);
        adapter = new HealthTagAdapter();
        srlRefresh.setOnRefreshListener(onRefreshListener);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                if (viewType == layoutHeader) {
                    return 3;
                } else if (viewType == layoutContent) {
                    return 1;
                }
                return 1;
            }
        });

        rvHealthTag.setLayoutManager(layoutManager);
        rvHealthTag.setAdapter(adapter);
    }

    private void onAction() {

    }

    private void onBack() {
        FragmentUtils.finish(getActivity());
    }


    @Override
    protected void onPageResume() {
        super.onPageResume();
        srlRefresh.autoRefresh();
    }

    private OnRefreshListener onRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            refreshData();
        }
    };

    private void refreshData() {
        repository.healthTagList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (srlRefresh != null) {
                            srlRefresh.finishRefresh();
                        }
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<Object>>() {
                    @Override
                    public void onNext(List<Object> objects) {
                        items.clear();
                        items.addAll(objects);
                        selected = 1;
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }

    private View.OnClickListener onTagClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvHealthTag.getChildAdapterPosition(v);
            if (selected != position) {
                selected = position;
                adapter.notifyDataSetChanged();
            }

            Object o = items.get(selected);
            if (o instanceof HealthTagEntity) {
                HealthTagEntity entity = (HealthTagEntity) o;
                if (callback != null) {
                    callback.onPickedHealthStatus(entity);
                }
                FragmentUtils.finish(getActivity());
            }
        }
    };

    private int selected = 1;

    private ArrayList<Object> items = new ArrayList<>();
    public int layoutHeader = R.layout.item_follow_up_health_tag_header;
    public int layoutContent = R.layout.item_follow_up_health_tag_content;

    private class HealthTagHeaderHolder extends HealthTagHolder {

        private final TextView tvHealthTagLabel;

        public HealthTagHeaderHolder(@NonNull View itemView) {
            super(itemView);
            tvHealthTagLabel = (TextView) itemView.findViewById(R.id.tvHealthTagLabel);
        }

        @Override
        public void onBind(int i) {
            super.onBind(i);
            Object o = items.get(i);
            if (o instanceof String) {
                String label = (String) o;
                tvHealthTagLabel.setText(label);
            }
        }
    }

    private class HealthTagContentHolder extends HealthTagHolder {

        private final TextView tvHealthTag;

        public HealthTagContentHolder(@NonNull View itemView) {
            super(itemView);
            tvHealthTag = (TextView) itemView.findViewById(R.id.tvHealthTag);
            tvHealthTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagClickListener != null) {
                        onTagClickListener.onClick(itemView);
                    }
                }
            });
        }

        @Override
        public void onBind(int i) {
            super.onBind(i);
            Object o = items.get(i);
            if (o instanceof HealthTagEntity) {
                HealthTagEntity entity = (HealthTagEntity) o;
                tvHealthTag.setText(entity.getText());
                tvHealthTag.setSelected(selected == i);
            }
        }
    }

    private class HealthTagHolder extends RecyclerView.ViewHolder {

        public HealthTagHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void onBind(int i) {

        }
    }

    private class HealthTagAdapter extends RecyclerView.Adapter<HealthTagHolder> {

        @NonNull
        @Override
        public HealthTagHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            if (viewType == layoutHeader) {
                View view = inflater.inflate(R.layout.item_follow_up_health_tag_header, viewGroup, false);
                return new HealthTagHeaderHolder(view);
            }
            if (viewType == layoutContent) {
                View view = inflater.inflate(R.layout.item_follow_up_health_tag_content, viewGroup, false);
                return new HealthTagContentHolder(view);
            }
            return new HealthTagHolder(new View(viewGroup.getContext()));
        }

        @Override
        public void onBindViewHolder(@NonNull HealthTagHolder healthTagholder, int i) {
            healthTagholder.onBind(i);
        }

        @Override
        public int getItemViewType(int position) {
            Object o = items.get(position);
            if (o instanceof String) {
                return R.layout.item_follow_up_health_tag_header;
            } else if (o instanceof HealthTagEntity) {
                return R.layout.item_follow_up_health_tag_content;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }
    }
}
