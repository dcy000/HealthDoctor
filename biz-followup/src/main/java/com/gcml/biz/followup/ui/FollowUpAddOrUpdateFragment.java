package com.gcml.biz.followup.ui;


import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcml.biz.followup.FragmentUtils;
import com.gcml.biz.followup.R;
import com.gcml.biz.followup.model.FollowUpRepository;
import com.gcml.biz.followup.model.entity.FollowUpBody;
import com.gcml.biz.followup.model.entity.FollowUpEntity;
import com.gcml.biz.followup.model.entity.FollowUpUpdateBody;
import com.gcml.biz.followup.model.entity.HealthTagEntity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.LazyFragment;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.UserEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 新增随访 / 修改随访
 */
public class FollowUpAddOrUpdateFragment extends LazyFragment {

    private ActionCallback actionCallback;

    public void setActionCallback(ActionCallback actionCallback) {
        this.actionCallback = actionCallback;
    }

    private FollowUpEntity followUpEntity;

    public static FollowUpAddOrUpdateFragment newInstance(FollowUpEntity entity) {
        FollowUpAddOrUpdateFragment fragment = new FollowUpAddOrUpdateFragment();
        Bundle args = new Bundle();
        args.putParcelable("followUp", entity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            followUpEntity = arguments.getParcelable("followUp");
        }
    }

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
    private ImageView ivResidentsNext;
    private RecyclerView rvResidents;
    private PickedResidentsAdapter residentsAdapter;

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
    private ImageView ivFollowerAvatar;

    private FollowUpRepository repository = new FollowUpRepository();

    private HealthTagEntity tagEntity;
    private ArrayList<ResidentBean> residentsPicked = new ArrayList<>();

    private int templateIndex = 0;
    private List<HealthTagEntity> tags = new ArrayList<>();

    private ArrayList<String> templates = new ArrayList<>();

//    {
//        templates.add("健康体检");
//        templates.add("高血压随访");
//        templates.add("糖尿病随访");
//        templates.add("老年人随访");
//    }

    private Date time;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
    private SimpleDateFormat formatUI = new SimpleDateFormat("MM月dd日 E hh:mm", Locale.CHINA);
    private UserEntity follower;

    public FollowUpAddOrUpdateFragment() {
        // Required empty public constructor
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

        if (followUpEntity == null) {
            tvToolbarTitle.setText("新增随访");
        } else {
            tvToolbarTitle.setText("修改随访计划");
        }

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
        showHealthStatusTag();

        clResidents = (ConstraintLayout) view.findViewById(R.id.clResidents);
        tvResidentsContent = (TextView) view.findViewById(R.id.tvResidentsContent);
        ivResidentsContent = (ImageView) view.findViewById(R.id.ivResidentsContent);
        ivResidentsNext = (ImageView) view.findViewById(R.id.ivResidentsNext);
        rvResidents = (RecyclerView) view.findViewById(R.id.rvResidents);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(true);
        residentsAdapter = new PickedResidentsAdapter();
        rvResidents.setLayoutManager(layoutManager);
        rvResidents.setAdapter(residentsAdapter);

        if (followUpEntity == null) {
            // 新增随访
            if (residentsPicked == null || residentsPicked.isEmpty()) {
                ivResidentsContent.setVisibility(View.VISIBLE);
            } else {
                ivResidentsContent.setVisibility(View.GONE);
                tvResidentsContent.setText(residentsPicked.size() + "人");
            }
            clResidents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPickResidents();
                }
            });
        }


        clFollowUp = (ConstraintLayout) view.findViewById(R.id.clFollowUp);
        tvFollowUpContent = (TextView) view.findViewById(R.id.tvFollowUpContent);
        ivFollowUpContent = (ImageView) view.findViewById(R.id.ivFollowUpContent);
        etFollowUpContent = (EditText) view.findViewById(R.id.etFollowUpContent);
        etFollowUpContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == etFollowUpContent && hasFocus) {
//                    getKeyboardHeight();
                } else {
                    // 隐藏软键盘
                    etFollowUpContent.clearFocus();
                    InputMethodManager imm = (InputMethodManager)
                            etFollowUpContent.getContext().getApplicationContext()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etFollowUpContent.getWindowToken(), 0);
                }
            }
        });
        clFollowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickFollowUp();
            }
        });

        if (followUpEntity != null) {
            // 修改随访
            clResidentHealthStatus.setVisibility(View.GONE);
            rvResidents.setVisibility(View.GONE);
            ivResidentsNext.setVisibility(View.GONE);
            ResidentBean resident = followUpEntity.getResident();
            if (resident != null) {
                String bname = resident.getBname();
                bname = TextUtils.isEmpty(bname) ? "" : bname;
                tvResidentsContent.setText(bname);
            }

            String planContent = followUpEntity.getPlanContent();
            planContent = TextUtils.isEmpty(planContent) ? "" : planContent;
            etFollowUpContent.setText(planContent);
            try {
                time = format.parse(followUpEntity.getPlanDate());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            follower = followUpEntity.getPlanDoctor();
        }
        showTemplate();

        clTime = (ConstraintLayout) view.findViewById(R.id.clTime);
        tvTimeContent = (TextView) view.findViewById(R.id.tvTimeContent);
        ivTimeContent = (ImageView) view.findViewById(R.id.ivTimeContent);
        clTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickTime();
            }
        });

        showTime();

        clFollower = (ConstraintLayout) view.findViewById(R.id.clFollower);
        tvFollowerContent = (TextView) view.findViewById(R.id.tvFollowerContent);
        ivFollowerContent = (ImageView) view.findViewById(R.id.ivFollowerContent);
        ivFollowerAvatar = (ImageView) view.findViewById(R.id.ivFollowerAvatar);
        clFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickFollower();
            }
        });

        showFollower();


    }

    private void showTime() {
        if (time == null) {
            ivTimeContent.setVisibility(View.VISIBLE);
            tvTimeContent.setVisibility(View.GONE);
        } else {
            ivTimeContent.setVisibility(View.GONE);
            tvTimeContent.setVisibility(View.VISIBLE);
            tvTimeContent.setText(formatUI.format(time));
        }
    }

    private void onPickResidentHealthStatus() {
        showPickHealthStatusFragment();
    }

    private void onPickResidents() {
        showPickResidentsFragment();
    }

    private void onPickTime() {
        showPickTimeActionSheet();
    }

    private void onPickFollowUp() {
        showPickTemplateActionSheet();
    }

    private void onPickFollower() {
        showPickFollowerFragment();
    }

    private void onAction() {
        UserEntity user = Box.getSessionManager().getUser();
        if (user == null) {
            ToastUtils.showShort("请重新登陆");
            return;
        }

        if (followUpEntity == null && tagEntity == null) {
            ToastUtils.showShort("请选择居民健康状况");
            return;
        }

        if (followUpEntity == null && residentsPicked.isEmpty()) {
            ToastUtils.showShort("请选择居民");
            return;
        }

        String template = tvFollowUpContent.getText().toString();
        if (TextUtils.isEmpty(template)) {
            ToastUtils.showShort("请选择随访模版");
            return;
        }

        String s = etFollowUpContent.getText().toString();
        if (TextUtils.isEmpty(s)) {
            ToastUtils.showShort("请输入随访内容");
            return;
        }

        if (time == null) {
            ToastUtils.showShort("请选择时间");
            return;
        }

        if (follower == null) {
            ToastUtils.showShort("请选择随访医生");
            return;
        }

        if (followUpEntity != null) {
            updateFollowUp();
            return;
        }


        ArrayList<FollowUpBody> bodies = new ArrayList<>();

        for (ResidentBean resident : residentsPicked) {
            FollowUpBody body = new FollowUpBody();
            body.setCreateDoctorId(user.getDocterid());
            body.setUserId(resident.getBid());
            if (!tags.isEmpty()) {
                HealthTagEntity tagEntity = tags.get(templateIndex);
                body.setPlanTitle(tagEntity.getText());
                body.setPlanTypeId(tagEntity.getTypeId());
            }
            body.setPlanContent(etFollowUpContent.getText().toString());
            body.setPlanDate(format.format(time));
            body.setPlanDoctorId(follower.getDocterid());
            bodies.add(body);
        }

        repository.addFollowUpList(bodies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        ToastUtils.showShort("添加随访...");
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("添加成功");
                        if (actionCallback != null) {
                            actionCallback.onComplete();
                        }
                        onBack();
                    }
                });

    }

    private void updateFollowUp() {
        String timeFormat = time == null ? "" : this.format.format(time);
        String result = etFollowUpContent.getText().toString();
        FollowUpUpdateBody body = new FollowUpUpdateBody();

        body.setId(followUpEntity.getId());
        body.setFollowStatus(followUpEntity.getFollowStatus());
        body.setResultContent(result);
        body.setResultDate(timeFormat);
        int planDoctorId = followUpEntity.getPlanDoctorId();
        body.setResultDoctorId(planDoctorId);

        if (!tags.isEmpty()) {
            HealthTagEntity t = tags.get(templateIndex);
            body.setResultTypeId(t.getTypeId());
            body.setResultTitle(t.getText());
        }

        repository.updateFollowUp(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        ToastUtils.showShort("修改随访...");
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ToastUtils.showShort("修改成功");
                        if (actionCallback != null) {
                            actionCallback.onComplete();
                        }
                        onBack();
                    }
                });
    }

    private void onBack() {
        FragmentUtils.finish(getActivity());
    }

    private void showPickTemplateActionSheet() {

        if (!tags.isEmpty()) {
            templates.clear();
            for (HealthTagEntity tag : tags) {
                templates.add(tag.getText());
            }
            showPickTemplateActionSheetWithIndex(templates, listener, templateIndex);
            return;
        }
        repository.followUpTemplates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<HealthTagEntity>>() {
                    @Override
                    public void onNext(List<HealthTagEntity> tagEntities) {
                        tags.clear();
                        tags.addAll(tagEntities);
                        if (!tags.isEmpty()) {
                            templates.clear();
                            for (HealthTagEntity tag : tags) {
                                templates.add(tag.getText());
                            }
                            showPickTemplateActionSheetWithIndex(templates, listener, templateIndex);
                            return;
                        }
                        ToastUtils.showShort("无可选模版");
                    }
                });
    }


    private OnOptionsSelectListener listener = new OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int options2, int options3, View v) {
            templateIndex = options1;
            showTemplate();
        }
    };

    private void showTemplate() {
        if (templates.size() == 0) {
            repository.followUpTemplates()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new CommonObserver<List<HealthTagEntity>>() {
                        @Override
                        public void onNext(List<HealthTagEntity> tagEntities) {
                            tags.clear();
                            tags.addAll(tagEntities);
                            if (!tags.isEmpty()) {
                                templates.clear();

                                int size = tags.size();
                                for (int i = 0; i < size; i++) {
                                    HealthTagEntity entity = tags.get(i);
                                    if (followUpEntity != null) {
                                        if (TextUtils.equals(followUpEntity.getPlanTitle(), entity.getText())) {
                                            templateIndex = i;
                                        }
                                    }
                                    templates.add(entity.getText());
                                }

                                if (templateIndex >= templates.size() || templateIndex < 0) {
                                    templateIndex = 0;
                                }

                                if (tvFollowUpContent != null && templates.size() > 0) {
                                    tvFollowUpContent.setText(templates.get(templateIndex));
                                    if (followUpEntity == null) {
                                        etFollowUpContent.setText(tags.get(templateIndex).getValue());
                                    }
                                } else {
                                    tvFollowerContent.setText("");
                                }
                            }
//                            ToastUtils.showShort("无可选模版");
                        }
                    });
            return;
        }

        if (templateIndex >= templates.size() || templateIndex < 0) {
            templateIndex = 0;
        }

        if (tvFollowUpContent != null) {
            tvFollowUpContent.setText(templates.get(templateIndex));
            if (followUpEntity == null) {
                etFollowUpContent.setText(tags.get(templateIndex).getValue());
            }
        }
    }

    private void showPickTemplateActionSheetWithIndex(List<String> items, OnOptionsSelectListener listener, int index) {
        if (getActivity() == null) {
            return;
        }
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(getActivity(), listener)
                .setDecorView(getActivity().findViewById(android.R.id.content))
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(21)
                .setContentTextSize(21)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setDividerColor(Color.parseColor("#EEEEEE"))
                .isCenterLabel(false)
                .setOutSideCancelable(true)
                .build();
        pickerView.setPicker(items);
        pickerView.setSelectOptions(index);//设置默认选中
        pickerView.show();
    }


    private OnTimeSelectListener timeListener = new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, View v) {//选中事件回调
            time = date;
            showTime();
        }
    };

    public void showPickTimeActionSheet() {
        if (getActivity() == null) {
            return;
        }
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(1900, 0, 1);
        endDate.set(2199, 11, 31);

        TimePickerView pvTime = new TimePickerBuilder(getActivity(), timeListener)
                .setDecorView(getActivity().findViewById(android.R.id.content))
                .setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确认")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
//                .setTitleSize(20)//标题文字大小
//                .setTitleText("Title")//标题文字
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(21)
                .setContentTextSize(21)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setDividerColor(Color.parseColor("#EEEEEE"))
                .isCenterLabel(true)
                .setOutSideCancelable(true)
                .isCyclic(false)//是否循环滚动
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .build();
        pvTime.show();
    }


    private FollowUpPickFollowerFragment.Callback followerCallback = new FollowUpPickFollowerFragment.Callback() {
        @Override
        public void onPickedFollower(UserEntity entity) {
            follower = entity;

            showFollower();
        }
    };

    private void showFollower() {
        if (follower == null) {
            ivFollowerContent.setVisibility(View.VISIBLE);
            ivFollowerAvatar.setVisibility(View.GONE);
        } else {
            ivFollowerContent.setVisibility(View.GONE);
            ivFollowerAvatar.setVisibility(View.VISIBLE);
            Glide.with(FollowUpAddOrUpdateFragment.this)
                    .load(follower.getDocterPhoto())
                    .apply(RequestOptions.placeholderOf(R.drawable.common_ic_avatar_default))
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivFollowerAvatar);
        }
    }

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


    private FollowUpPickHealthStatusFragment.Callback healthStatusCallback = new FollowUpPickHealthStatusFragment.Callback() {
        @Override
        public void onPickedHealthStatus(HealthTagEntity entity) {
            tagEntity = entity;

            showHealthStatusTag();

        }
    };

    private void showHealthStatusTag() {
        if (tagEntity == null) {
            ivResidentHealthStatusContent.setVisibility(View.VISIBLE);
            tvResidentHealthStatusContent.setVisibility(View.GONE);
        } else {
            ivResidentHealthStatusContent.setVisibility(View.GONE);
            tvResidentHealthStatusContent.setVisibility(View.VISIBLE);
            tvResidentHealthStatusContent.setText(tagEntity.getText());
        }
    }

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

        ((FollowUpPickHealthStatusFragment) fragment).setCallback(healthStatusCallback);

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


    private FollowUpPickResidentsFragment.CallBack residentsCallBack = new FollowUpPickResidentsFragment.CallBack() {
        @Override
        public void onPickedResidents(List<ResidentBean> residents) {
            residentsPicked.clear();
            residentsPicked.addAll(residents);
            if (residentsPicked == null || residentsPicked.isEmpty()) {
                ivResidentsContent.setVisibility(View.VISIBLE);
            } else {
                ivResidentsContent.setVisibility(View.GONE);
                tvResidentsContent.setText(residentsPicked.size() + "人");
            }
            residentsAdapter.notifyDataSetChanged();
        }
    };

    private void showPickResidentsFragment() {
        if (tagEntity == null) {
            ToastUtils.showShort("请先选择居民健康状况");
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
            fragment = FollowUpPickResidentsFragment.newInstance(tagEntity, residentsPicked);
        }

        ((FollowUpPickResidentsFragment) fragment).setCallBack(residentsCallBack);

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

    private int residentLayout = R.layout.item_follow_up_resident;

    private class PickedResidentsHolder extends RecyclerView.ViewHolder {

        private final ImageView ivResidentAvatar;

        public PickedResidentsHolder(@NonNull View itemView) {
            super(itemView);
            ivResidentAvatar = ((ImageView) itemView.findViewById(R.id.ivResidentAvatar));
        }

        public void onBind(int i) {
            ResidentBean resident = residentsPicked.get(i);
            String userPhoto = resident.getUserPhoto();
            Glide.with(FollowUpAddOrUpdateFragment.this)
                    .load(userPhoto)
                    .apply(RequestOptions.placeholderOf(R.drawable.common_ic_avatar_default))
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivResidentAvatar);
        }
    }

    private class PickedResidentsAdapter extends RecyclerView.Adapter<PickedResidentsHolder> {

        @NonNull
        @Override
        public PickedResidentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(residentLayout, viewGroup, false);
            return new PickedResidentsHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PickedResidentsHolder pickedResidentsHolder, int i) {
            pickedResidentsHolder.onBind(i);
        }

        @Override
        public int getItemCount() {
            return residentsPicked != null ? residentsPicked.size() : 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        actionCallback = null;
    }

}
