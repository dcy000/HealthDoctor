package com.gcml.biz.followup.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcml.biz.followup.FragmentUtils;
import com.gcml.biz.followup.R;
import com.gcml.biz.followup.model.FollowUpRepository;
import com.gcml.biz.followup.model.entity.HealthTagEntity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.LazyFragment;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.UserEntity;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 选择居民
 */
public class FollowUpPickResidentsFragment extends LazyFragment {

    public interface CallBack {
        void onPickedResidents(List<ResidentBean> residents);
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private HealthTagEntity tagEntity;

    public static FollowUpPickResidentsFragment newInstance(HealthTagEntity entity, ArrayList<ResidentBean> residentsPicked) {
        FollowUpPickResidentsFragment fragment = new FollowUpPickResidentsFragment();
        Bundle args = new Bundle();
        args.putParcelable("healthTag", entity);
        args.putParcelableArrayList("residentsPicked", residentsPicked);
        fragment.setArguments(args);
        return fragment;
    }

    // toolbar
    private ImageView ivToolbarLeft;
    private TextView tvToolbarLeft;
    private TextView tvToolbarRight;
    private TextView tvToolbarTitle;

    private RecyclerView rvResidentsPicked;
    private EditText etSearch;
    private TextView tvLabel;
    private RecyclerView rvResidentsToPick;
    private ToPickAdapter toPickAdapter;
    private PickedAdapter pickedAdapter;

    private ArrayList<ResidentBean> residentsPicked = new ArrayList<>();

    private ArrayList<ResidentBean> residentsToPick = new ArrayList<>();

    private FollowUpRepository repository = new FollowUpRepository();

    public FollowUpPickResidentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            tagEntity = arguments.getParcelable("healthTag");
            ArrayList<ResidentBean> residents = arguments.getParcelableArrayList("residentsPicked");
            if (residents != null) {
                residentsPicked.addAll(residents);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow_up_pick_residents, container, false);
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
        tvToolbarRight.setText("确认");
        tvToolbarTitle.setText("居民");
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

        rvResidentsPicked = (RecyclerView) view.findViewById(R.id.rvResidentsPicked);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        rvResidentsToPick = (RecyclerView) view.findViewById(R.id.rvResidentsToPick);

        tvLabel.setText(tagEntity.getText());

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager1.setReverseLayout(true);
        pickedAdapter = new PickedAdapter();
        rvResidentsPicked.setLayoutManager(layoutManager1);
        rvResidentsPicked.setAdapter(pickedAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        toPickAdapter = new ToPickAdapter();
        rvResidentsToPick.setAdapter(toPickAdapter);
        rvResidentsToPick.setLayoutManager(layoutManager);

        RxTextView.textChanges(etSearch)
                .debounce(200, TimeUnit.MILLISECONDS)
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {
                        return charSequence.toString();
                    }
                })
                .flatMap(new Function<String, ObservableSource<List<ResidentBean>>>() {
                    @Override
                    public ObservableSource<List<ResidentBean>> apply(String s) throws Exception {
                        UserEntity user = Box.getSessionManager().getUser();
                        int docterid = user == null ? 0 : user.getDocterid();
                        return repository.residentList(
                                docterid,
                                tagEntity.getText(),
                                s,
                                0,
                                1,
                                1000)
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<ResidentBean>>() {
                    @Override
                    public void onNext(List<ResidentBean> residentBeans) {
                        residentsToPick.clear();
                        residentsToPick.addAll(residentBeans);
                        toPickAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void onAction() {
        if (callBack != null && !residentsPicked.isEmpty()) {
            callBack.onPickedResidents(residentsPicked);
        }
        FragmentUtils.finish(getActivity());
    }

    private void onBack() {
        FragmentUtils.finish(getActivity());
    }

    public boolean containsPicked(ResidentBean resident) {
        boolean contains = false;
        for (ResidentBean residentBean : residentsPicked) {
            if (residentBean != null && residentBean.getBid() == resident.getBid()) {
                contains = true;
            }
        }
        return contains;
    }

    public boolean removePicked(ResidentBean resident) {
        ResidentBean removed = null;
        for (ResidentBean residentBean : residentsPicked) {
            if (residentBean != null && residentBean.getBid() == resident.getBid()) {
                removed = residentBean;
            }
        }

        if (removed != null) {
            return residentsPicked.remove(removed);
        }

        return false;
    }

    private View.OnClickListener onToPickClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvResidentsToPick.getChildAdapterPosition(v);
            ResidentBean residentBean = residentsToPick.get(position);

            if (containsPicked(residentBean)) {
                removePicked(residentBean);
            } else {
                residentsPicked.add(residentBean);
            }

            toPickAdapter.notifyDataSetChanged();
            pickedAdapter.notifyDataSetChanged();
        }
    };

    private View.OnClickListener onPickedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvResidentsPicked.getChildAdapterPosition(v);
            boolean removed = residentsPicked.remove(residentsPicked.get(position));
            if (removed) {
                pickedAdapter.notifyDataSetChanged();
                toPickAdapter.notifyDataSetChanged();
            }
        }
    };


    int pickedLayout = R.layout.item_follow_picked_residents;

    private class PickedHolder extends RecyclerView.ViewHolder {
        private final ImageView ivResidentAvatar;
        private final ImageView ivResidentDelete;

        public PickedHolder(@NonNull View itemView) {
            super(itemView);
            ivResidentAvatar = ((ImageView) itemView.findViewById(R.id.ivResidentAvatar));
            ivResidentDelete = ((ImageView) itemView.findViewById(R.id.ivResidentDelete));
            itemView.setOnClickListener(onPickedClickListener);
        }

        public void onBind(int i) {
            ResidentBean resident = residentsPicked.get(i);
            String userPhoto = resident.getUserPhoto();
            Glide.with(FollowUpPickResidentsFragment.this)
                    .load(userPhoto)
                    .apply(RequestOptions.placeholderOf(R.drawable.common_ic_avatar_default))
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivResidentAvatar);
        }
    }

    private class PickedAdapter extends RecyclerView.Adapter<PickedHolder> {

        @NonNull
        @Override
        public PickedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(pickedLayout, viewGroup, false);
            return new PickedHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PickedHolder pickedHolder, int i) {
            pickedHolder.onBind(i);
        }

        @Override
        public int getItemCount() {
            return residentsPicked != null ? residentsPicked.size() : 0;
        }
    }


    private int toPickLayout = R.layout.item_follow_up_to_pick_resident;

    private class ToPickHolder extends RecyclerView.ViewHolder {

        private final ImageView ivChecked;
        private final ImageView ivResidentAvatar;
        private final TextView tvResidentName;
        private final TextView tvResidentTag;
        private final TextView tvResidentCount;
        private final TextView tvFollowUpLastTime;

        public ToPickHolder(@NonNull View itemView) {
            super(itemView);
            ivChecked = (ImageView) itemView.findViewById(R.id.ivChecked);
            ivResidentAvatar = (ImageView) itemView.findViewById(R.id.ivResidentAvatar);
            tvResidentName = (TextView) itemView.findViewById(R.id.tvResidentName);
            tvResidentTag = (TextView) itemView.findViewById(R.id.tvResidentTag);
            tvResidentCount = (TextView) itemView.findViewById(R.id.tvResidentCount);
            tvFollowUpLastTime = (TextView) itemView.findViewById(R.id.tvFollowUpLastTime);
            itemView.setOnClickListener(onToPickClickListener);
        }

        public void onBind(int i) {
            ResidentBean resident = residentsToPick.get(i);
            Glide.with(FollowUpPickResidentsFragment.this)
                    .load(resident.getUserPhoto())
                    .apply(RequestOptions.placeholderOf(R.drawable.common_ic_avatar_default))
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivResidentAvatar);
            tvResidentName.setText(resident.getBname());

            tvResidentTag.setVisibility(View.GONE);
            tvResidentTag.setText(tagEntity.getText());

            String countDesc = "今年已随访：" + resident.getCurrentYearCount() + "次";
            tvResidentCount.setText(countDesc);

            String time = resident.getRecentResultData();
            String timeDesc = TextUtils.isEmpty(time) ? "" : "上次随访：" + time;
            tvFollowUpLastTime.setText(timeDesc);

            boolean contains = containsPicked(resident);

            ivChecked.setSelected(contains);
        }
    }

    private class ToPickAdapter extends RecyclerView.Adapter<ToPickHolder> {

        @NonNull
        @Override
        public ToPickHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(toPickLayout, viewGroup, false);
            return new ToPickHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ToPickHolder toPickHolder, int i) {
            toPickHolder.onBind(i);
        }

        @Override
        public int getItemCount() {
            return residentsToPick != null ? residentsToPick.size() : 0;
        }
    }
}
