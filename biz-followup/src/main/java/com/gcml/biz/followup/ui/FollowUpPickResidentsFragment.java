package com.gcml.biz.followup.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.biz.followup.FragmentUtils;
import com.gcml.biz.followup.R;
import com.gzq.lib_resource.LazyFragment;

/**
 *  选择居民
 */
public class FollowUpPickResidentsFragment extends LazyFragment {

    // toolbar
    private ImageView ivToolbarLeft;
    private TextView tvToolbarLeft;
    private TextView tvToolbarRight;
    private TextView tvToolbarTitle;

    private RecyclerView rvResidentsPicked;
    private EditText etSearch;
    private TextView tvLabel;
    private RecyclerView rvResidentsToPick;


    public FollowUpPickResidentsFragment() {
        // Required empty public constructor
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
        initView(view,savedInstanceState);
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
        tvToolbarTitle.setText("选择患者");
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
    }

    private void onAction() {

    }

    private void onBack() {
        FragmentUtils.finish(getActivity());
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
        }



        public void onBind(int i) {

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
            return 0;
        }
    }
}
