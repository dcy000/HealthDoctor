package com.gcml.module_guardianship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gcml.module_guardianship.bean.HandRingHealthDataBena;
import com.gcml.module_guardianship.bean.WatchInformationBean;
import com.gcml.module_guardianship.presenter.ResidentDetailPresenter;
import com.gcml.module_guardianship.wrap.StatusTextView;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.CallPhoneUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.jessyan.autosize.utils.AutoSizeUtils;

@Route(path = "/guardianship/resident/detail")
public class ResidentDetailActivity extends StateBaseActivity implements View.OnClickListener {
    ResidentDetailPresenter residentDetailPresenter = new ResidentDetailPresenter(this);
    private ResidentBean guardianshipBean;
    private int vip;
    private List<String> dataMenu;
    private CircleImageView mCvHead;
    private TextView mTvName;
    private TextView mTvHeight;
    private TextView mTvWeight;
    private TextView mTvBloodType;
    private TextView mTvDataMore;
    private ConstraintLayout handContainer;
    private RecyclerView mRvMenu;
    private TextView mTvAddress;
    private BaseQuickAdapter<String, BaseViewHolder> adapter;
    private TextView mTvPressrueValue;
    private TextView mTvHeatData;
    private ImageView mIvDialPhone;
    private TextView mTvHealthModify;
    private LinearLayout statusLayout;

    @Override
    protected void onStart() {
        super.onStart();
        residentDetailPresenter.preData(guardianshipBean.getBid());
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_resident_details;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        guardianshipBean = intentArgument.getParcelableExtra("data");
        vip = intentArgument.getIntExtra("vip", 0);
        dataMenu = vip == 1 ? Arrays.asList("监护圈", "健康检测记录", "远程监控")
                : Arrays.asList("健康检测记录");
    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("居民详情");
        mLlRight.setVisibility(View.VISIBLE);
        mTvRight.setVisibility(View.GONE);
        mIvRight.setImageResource(R.drawable.icon_mobile_phone_blue);
        mCvHead = findViewById(R.id.ivHead);
        mCvHead.setOnClickListener(this);
        mIvDialPhone = findViewById(R.id.ivDialPhone);
        mIvDialPhone.setOnClickListener(this);

        statusLayout = findViewById(R.id.llHealtStatus);

        mTvName = findViewById(R.id.tvName);
        mTvHeight = findViewById(R.id.tvHeightValue);
        mTvWeight = findViewById(R.id.tvWeightValue);
        mTvPressrueValue = findViewById(R.id.tvPressrueValue);
        mTvHeatData = findViewById(R.id.tvHeatData);
        mTvBloodType = findViewById(R.id.tvBloodType);
        mTvDataMore = findViewById(R.id.tvHandData);
        mTvDataMore.setOnClickListener(this);
        handContainer = findViewById(R.id.constraintLayout4);
        mRvMenu = findViewById(R.id.rvMenu);
        mTvAddress = findViewById(R.id.tvAddress);

        mTvHealthModify = findViewById(R.id.tvHealthModify);
        mTvHealthModify.setOnClickListener(this);

        fillData();
        initMenu();
    }

    private void fillData() {
        if (vip == 1) {
            handContainer.setVisibility(View.VISIBLE);
        } else {
            handContainer.setVisibility(View.GONE);
        }
        Glide.with(Box.getApp())
                .load(guardianshipBean.getUserPhoto())
                .into(mCvHead);
        mTvName.setText(guardianshipBean.getBname());
        mTvHeight.setText(guardianshipBean.getHeight() == 0 ? "未填写" : guardianshipBean.getHeight() + "");
        mTvWeight.setText(guardianshipBean.getWeight() == 0 ? "未填写" : guardianshipBean.getWeight() + "");
        mTvBloodType.setText(TextUtils.isEmpty(guardianshipBean.getBloodType()) ? "未填写" : guardianshipBean.getBloodType());
        mTvAddress.setText(TextUtils.isEmpty(guardianshipBean.getDz()) ? "暂未填写" : guardianshipBean.getDz());

        String userType = guardianshipBean.getUserType();
        updateHealthStatus(userType);
        if (TextUtils.isEmpty(userType)) {
            findViewById(R.id.constraintLayout2).setVisibility(View.GONE);
        }
    }

    public void updateHealthStatus(String userType) {
        if (userType != null) {
            String[] status = userType.split(",");
            statusLayout.removeAllViews();
            for (int i = 0; i < status.length; i++) {
                if (TextUtils.isEmpty(status[i])) {
                    continue;
                }
                StatusTextView statusTextView = new StatusTextView(this);
                statusTextView.setStatus(status[i]);
                statusLayout.addView(statusTextView);
            }
        }
    }


    private void initMenu() {
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_health_data_menu, dataMenu) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_title, item);
            }
        };
        mRvMenu.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (vip == 2) {
                if (position == 0) {
                    //健康检测记录
                    Routerfit.register(GuardianshipRouterApi.class).skipDetectionRecordTypeActivity(guardianshipBean);
                }
                return;
            }
            if (position == 0) {
                //监护圈
                Routerfit.register(GuardianshipRouterApi.class).skipCustodyCircleActivity(guardianshipBean);
            } else if (position == 1) {
                //健康检测记录
                Routerfit.register(GuardianshipRouterApi.class).skipDetectionRecordTypeActivity(guardianshipBean);
            } else if (position == 2) {
                //远程监控
                Routerfit.register(GuardianshipRouterApi.class).skipRemoteMeasureActivity(
                        guardianshipBean.getWatchCode(), guardianshipBean.getBid() + "");
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return residentDetailPresenter;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tvHandData) {
            ToastUtils.showShort("该功能还在开发中...");
        } else if (i == R.id.ivHead) {
            Routerfit.register(GuardianshipRouterApi.class).skipPersonDetailActivity(guardianshipBean);
        } else if (i == R.id.ivDialPhone) {
            showVoiceOrVideoConnectDialog(guardianshipBean);
        } else if (i == R.id.tvHealthModify) {
            startActivityForResult(new Intent(this, ModifyHealthStatusActivity.class)
                            .putExtra("userId", guardianshipBean.getBid())
                            .putExtra("healthStatus", guardianshipBean.getUserType()),

                    119);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 119) {
            if (data == null) {
                return;
            }

            guardianshipBean.setUserType(data.getStringExtra("userType"));
            updateHealthStatus(data.getStringExtra("userType"));
        }
    }

    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);
        HandRingHealthDataBena handRingHealthDataBena = (HandRingHealthDataBena) objects[0];
        String pressureValue = String.format("%s/%s", handRingHealthDataBena.getLowPressure(), handRingHealthDataBena.getHighPressure());
        mTvPressrueValue.setText(pressureValue);
        mTvHeatData.setText(handRingHealthDataBena.getHeartRate() + "");
    }

    @Override
    protected void clickToolbarRight() {
        Routerfit.register(GuardianshipRouterApi.class).skipResidentLocationDetailActivity(guardianshipBean);
    }

    private void showVoiceOrVideoConnectDialog(ResidentBean familyBean) {
        FDialog.build()
                .setSupportFM(getSupportFragmentManager())
                .setLayoutId(R.layout.dialog_layout_voice_video_connect)
                .setWidth(AutoSizeUtils.pt2px(this, 710))
                .setDimAmount(0.5f)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, FDialog dialog) {
                        holder.getView(R.id.tv_video_connect).setOnClickListener(v -> {
                            ToastUtils.showShort("视频通话");
                            String wyyxId = familyBean.getWyyxId();
                            if (!TextUtils.isEmpty(wyyxId)) {
                                Routerfit.register(CommonRouterApi.class).getCallServiceImp()
                                        .launchNoCheckWithCallFamily(ResidentDetailActivity.this, wyyxId);
                            }
                            dialog.dismiss();
                        });

                        holder.getView(R.id.tv_voice_connect).setOnClickListener(v -> {
                            getWatchInfo(familyBean);
                            dialog.dismiss();
                        });

                        holder.getView(R.id.tv_cancel_connect).setOnClickListener(v -> dialog.dismiss());
                    }
                })
                .setOutCancel(false)
                .setShowBottom(true)
                .show();
    }

    private void getWatchInfo(ResidentBean guardianshipBean) {
        Box.getRetrofit(GuardianshipApi.class)
                .getWatchInfo(guardianshipBean.getWatchCode())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<WatchInformationBean>() {
                    @Override
                    public void onNext(WatchInformationBean watchInformationBean) {
                        showPhoneTipsDialog(guardianshipBean.getBname(), watchInformationBean.getDeviceMobileNo());
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showPhoneTipsDialog(guardianshipBean.getBname(), guardianshipBean.getTel());
                    }
                });
    }

    private void showPhoneTipsDialog(String name, String phone) {

        FDialog.build()
                .setSupportFM(getSupportFragmentManager())
                .setLayoutId(R.layout.dialog_layout_phone_tips)
                .setWidth(AutoSizeUtils.pt2px(this, 540))
                .setOutCancel(false)
                .setDimAmount(0.5f)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, FDialog dialog) {
                        holder.setText(R.id.tv_title, name + "的电话号码");
                        holder.setText(R.id.tv_message, phone);

                        holder.setOnClickListener(R.id.tv_confirm, v -> {
                            CallPhoneUtils.instance().callPhone(ResidentDetailActivity.this, phone);
                            dialog.dismiss();
                        });

                        holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());
                    }
                })
                .show();
        List<String> list = null;

        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.equals("", list.get(i))) {
            }
        }
    }
}
