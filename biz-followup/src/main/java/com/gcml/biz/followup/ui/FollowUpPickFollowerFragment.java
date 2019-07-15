package com.gcml.biz.followup.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
 * 选择随访医生
 */
public class FollowUpPickFollowerFragment extends LazyFragment {

    private ToPickAdapter toPickAdapter;

    interface Callback {
        void onPickedFollower(UserEntity entity);
    }

    private Callback callback;

    private EditText etSearch;
    private RecyclerView rvFollowerToPick;

    private FollowUpRepository repository = new FollowUpRepository();

    public static FollowUpPickFollowerFragment newInstacne() {
        FollowUpPickFollowerFragment fragment = new FollowUpPickFollowerFragment();
        return fragment;
    }

    private UserEntity follower;

    // toolbar
    private ImageView ivToolbarLeft;
    private TextView tvToolbarLeft;
    private TextView tvToolbarRight;
    private TextView tvToolbarTitle;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public FollowUpPickFollowerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            follower = getArguments().getParcelable("follower");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow_up_pick_follower, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivToolbarLeft = (ImageView) view.findViewById(R.id.ivToolbarLeft);
        tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        tvToolbarRight = (TextView) view.findViewById(R.id.tvToolbarRight);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
        ivToolbarLeft.setVisibility(View.GONE);
        tvToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarRight.setVisibility(View.GONE);
        tvToolbarLeft.setText("取消");
        tvToolbarTitle.setText("随访人");
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

        etSearch = (EditText) view.findViewById(R.id.etSearch);
        rvFollowerToPick = (RecyclerView) view.findViewById(R.id.rvFollowerToPick);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        toPickAdapter = new ToPickAdapter();
        rvFollowerToPick.setLayoutManager(layoutManager);
        rvFollowerToPick.setAdapter(toPickAdapter);

        RxTextView.textChanges(etSearch)
                .debounce(200, TimeUnit.MILLISECONDS)
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {
                        return charSequence.toString();
                    }
                })
                .flatMap(new Function<String, ObservableSource<List<UserEntity>>>() {
                    @Override
                    public ObservableSource<List<UserEntity>> apply(String s) throws Exception {
                        return repository.doctorList(
                                s,
                                1,
                                1000)
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<UserEntity>>() {
                    @Override
                    public void onNext(List<UserEntity> doctors) {
                        followerToPick.clear();
                        followerToPick.addAll(doctors);
                        toPickAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void onAction() {

    }

    private void onBack() {
        FragmentUtils.finish(getActivity());
    }

    private View.OnClickListener onPickClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvFollowerToPick.getChildAdapterPosition(v);
            UserEntity entity = followerToPick.get(position);
            if (entity != null && callback != null) {
                callback.onPickedFollower(entity);
                onBack();
            }
        }
    };

    private ArrayList<UserEntity> followerToPick = new ArrayList<>();

    private int toPickLayout = R.layout.item_follow_up_to_pick_follower;

    private class ToPickHolder extends RecyclerView.ViewHolder {

        private final ImageView ivFollowerAvatar;
        private final TextView tvFollowerName;

        public ToPickHolder(@NonNull View itemView) {
            super(itemView);
            ivFollowerAvatar = ((ImageView) itemView.findViewById(R.id.ivFollowerAvatar));
            tvFollowerName = ((TextView) itemView.findViewById(R.id.tvFollowerName));
            itemView.setOnClickListener(onPickClickListener);
        }

        public void onBind(int i) {
            UserEntity entity = followerToPick.get(i);
            Glide.with(FollowUpPickFollowerFragment.this)
                    .load(entity.getDocterPhoto())
                    .apply(RequestOptions.placeholderOf(R.drawable.common_ic_avatar_default))
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivFollowerAvatar);

            String doctername = entity.getDoctername();
            String nameDesc = TextUtils.isEmpty(doctername) ? "" : doctername;
            tvFollowerName.setText(nameDesc);

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
            return followerToPick != null ? followerToPick.size() : 0;
        }
    }
}
