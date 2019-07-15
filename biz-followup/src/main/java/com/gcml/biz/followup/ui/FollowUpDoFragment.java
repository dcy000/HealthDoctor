package com.gcml.biz.followup.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.gcml.biz.followup.model.entity.FollowUpEntity;
import com.gcml.biz.followup.model.entity.FollowUpUpdateBody;
import com.gcml.biz.followup.model.entity.HealthTagEntity;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.LazyFragment;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.UserEntity;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 随访
 */
public class FollowUpDoFragment extends LazyFragment {

    private FollowUpEntity followUpEntity;

    public static FollowUpDoFragment newInstance(FollowUpEntity entity) {
        FollowUpDoFragment fragment = new FollowUpDoFragment();
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
    private ConstraintLayout clResident;
    private ImageView ivResidentAvatar;
    private TextView tvResidentName;
    private TextView tvResidentGenderAge;
    private TextView tvResidentTag;
    private CircleImageView civCall;
    private ConstraintLayout clResidentAddress;
    private TextView tvResidentAddress;
    private TextView tvCopyResidentAddress;
    private ConstraintLayout clFollowUpDoctor;
    private TextView tvFollowUpContentLabel;
    private TextView tvFollowUpContent;
    private ConstraintLayout clFollowUpContent;
    private TextView tvFollowUpDoctorLabel;
    private TextView tvFollowUpDoctor;
    private ConstraintLayout clFollowUpTime;
    private TextView tvFollowUpTimeLabel;
    private TextView tvFollowUpTime;
    private ImageView ivFollowUpTimeNext;
    private ConstraintLayout clFollowUpType;
    private TextView tvFollowUpTypeLabel;
    private TextView tvFollowUpType;
    private ImageView ivFollowUpTypeNext;
    private ConstraintLayout clFollowUpResult;
    private TextView tvFollowUpResultLabel;
    private TextView tvFollowUpTemplate;
    private ImageView ivFollowUpNext;
    private EditText etFollowUpResult;
    private TextView tvAction;

    private FollowUpRepository repository = new FollowUpRepository();

    SimpleDateFormat formatUI = new SimpleDateFormat("MM月dd日 E hh:mm", Locale.CHINA);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
    private Date time = new Date();

    private int templateIndex = 0;
    private List<HealthTagEntity> templates = new ArrayList<>();
    private ArrayList<String> templateDescs = new ArrayList<>();

    private int typeIndex = 0;
    private List<HealthTagEntity> types = new ArrayList<>();
    private ArrayList<String> typeDescs = new ArrayList<>();


    public FollowUpDoFragment() {
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
        return inflater.inflate(R.layout.fragment_follow_up_do, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        initData();
    }

    private void initView(@NonNull View view) {
        ivToolbarLeft = (ImageView) view.findViewById(R.id.ivToolbarLeft);
        tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        tvToolbarRight = (TextView) view.findViewById(R.id.tvToolbarRight);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
        ivToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setVisibility(View.GONE);
        tvToolbarRight.setVisibility(View.GONE);
        tvToolbarTitle.setText("随访");
//        tvToolbarLeft.setText("取消");
//        tvToolbarRight.setText("提交");
        tvToolbarRight.setTextColor(Color.parseColor("#909399"));
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

        clContainer = (ConstraintLayout) view.findViewById(R.id.clContainer);
        nsvContainer = (NestedScrollView) view.findViewById(R.id.nsvContainer);
        llContainer = (LinearLayout) view.findViewById(R.id.llContainer);

        clResident = (ConstraintLayout) view.findViewById(R.id.clResident);
        ivResidentAvatar = (ImageView) view.findViewById(R.id.ivResidentAvatar);
        tvResidentName = (TextView) view.findViewById(R.id.tvResidentName);
        tvResidentGenderAge = (TextView) view.findViewById(R.id.tvResidentGenderAge);

        tvResidentTag = (TextView) view.findViewById(R.id.tvResidentTag);
        civCall = (CircleImageView) view.findViewById(R.id.civCall);
        civCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        clResidentAddress = (ConstraintLayout) view.findViewById(R.id.clResidentAddress);
        tvResidentAddress = (TextView) view.findViewById(R.id.tvResidentAddress);
        tvCopyResidentAddress = (TextView) view.findViewById(R.id.tvCopyResidentAddress);

        clFollowUpContent = (ConstraintLayout) view.findViewById(R.id.clFollowUpContent);
        tvFollowUpContentLabel = (TextView) view.findViewById(R.id.tvFollowUpContentLabel);
        tvFollowUpContent = (TextView) view.findViewById(R.id.tvFollowUpContent);

        clFollowUpDoctor = (ConstraintLayout) view.findViewById(R.id.clFollowUpDoctor);
        tvFollowUpDoctorLabel = (TextView) view.findViewById(R.id.tvFollowUpDoctorLabel);
        tvFollowUpDoctor = (TextView) view.findViewById(R.id.tvFollowUpDoctor);

        clFollowUpTime = (ConstraintLayout) view.findViewById(R.id.clFollowUpTime);
        tvFollowUpTimeLabel = (TextView) view.findViewById(R.id.tvFollowUpTimeLabel);
        tvFollowUpTime = (TextView) view.findViewById(R.id.tvFollowUpTime);
        ivFollowUpTimeNext = (ImageView) view.findViewById(R.id.ivFollowUpTimeNext);
        clFollowUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickTimeActionSheet();
            }
        });

        clFollowUpType = (ConstraintLayout) view.findViewById(R.id.clFollowUpType);
        tvFollowUpTypeLabel = (TextView) view.findViewById(R.id.tvFollowUpTypeLabel);
        tvFollowUpType = (TextView) view.findViewById(R.id.tvFollowUpType);
        ivFollowUpTypeNext = (ImageView) view.findViewById(R.id.ivFollowUpTypeNext);
        clFollowUpType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickTypeActionSheet();
            }
        });

        clFollowUpResult = (ConstraintLayout) view.findViewById(R.id.clFollowUpResult);
        tvFollowUpResultLabel = (TextView) view.findViewById(R.id.tvFollowUpResultLabel);
        tvFollowUpTemplate = (TextView) view.findViewById(R.id.tvFollowUpTemplate);
        ivFollowUpNext = (ImageView) view.findViewById(R.id.ivFollowUpNext);
        etFollowUpResult = (EditText) view.findViewById(R.id.etFollowUpResult);
        clFollowUpResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickResultTemplateActionSheet();
            }
        });

        RxTextView.textChanges(etFollowUpResult)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<CharSequence>() {
                    @Override
                    public void onNext(CharSequence charSequence) {
                        boolean validateData = validateData();
                        tvAction.setEnabled(validateData);
                    }
                });

        tvAction = (TextView) view.findViewById(R.id.tvAction);
        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAction();
            }
        });
    }

    private void call() {

    }

    private void showPickResultTemplateActionSheet() {
        if (!templates.isEmpty()) {
            templateDescs.clear();
            for (HealthTagEntity tag : templates) {
                templateDescs.add(tag.getText());
            }
            showPickTemplateActionSheetWithIndex(templateDescs, templateListener, templateIndex);
            return;
        }
        repository.followUpResultTemplates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<HealthTagEntity>>() {
                    @Override
                    public void onNext(List<HealthTagEntity> tagEntities) {
                        templates.clear();
                        templates.addAll(tagEntities);
                        if (!templates.isEmpty()) {
                            templateDescs.clear();
                            for (HealthTagEntity tag : templates) {
                                templateDescs.add(tag.getText());
                            }
                            showPickTemplateActionSheetWithIndex(templateDescs, templateListener, templateIndex);
                            return;
                        }
                        ToastUtils.showShort("无可选模版");
                    }
                });
    }


    private OnOptionsSelectListener templateListener = new OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int options2, int options3, View v) {
            templateIndex = options1;
            showTemplate();
            boolean validateData = validateData();
            tvAction.setEnabled(validateData);
        }
    };

    private void showTemplate() {
        if (templates.size() == 0) {
            return;
        }

        if (templateIndex >= templates.size() || templateIndex < 0) {
            templateIndex = 0;
        }

        if (tvFollowUpTemplate != null) {
            tvFollowUpTemplate.setText(templateDescs.get(templateIndex));
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

    private void showPickTypeActionSheet() {
        if (!types.isEmpty()) {
            typeDescs.clear();
            for (HealthTagEntity tag : types) {
                typeDescs.add(tag.getText());
            }
            showPickTemplateActionSheetWithIndex(typeDescs, typeListener, typeIndex);
            return;
        }
        repository.followUpTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<HealthTagEntity>>() {
                    @Override
                    public void onNext(List<HealthTagEntity> tagEntities) {
                        types.clear();
                        types.addAll(tagEntities);
                        if (!types.isEmpty()) {
                            typeDescs.clear();
                            for (HealthTagEntity tag : types) {
                                typeDescs.add(tag.getText());
                            }
                            showPickTemplateActionSheetWithIndex(typeDescs, typeListener, typeIndex);
                            return;
                        }
                        ToastUtils.showShort("无可选随访方式");
                    }
                });
    }


    private OnOptionsSelectListener typeListener = new OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int options2, int options3, View v) {
            typeIndex = options1;
            showType();
            boolean validateData = validateData();
            tvAction.setEnabled(validateData);
        }
    };

    private void showType() {
        if (types.size() == 0) {
            return;
        }

        if (typeIndex >= types.size() || typeIndex < 0) {
            typeIndex = 0;
        }

        if (tvFollowUpType != null) {
            tvFollowUpType.setText(typeDescs.get(typeIndex));
        }
    }

    private OnTimeSelectListener timeListener = new OnTimeSelectListener() {

        @Override
        public void onTimeSelect(Date date, View v) {//选中事件回调
            time = date;

            tvFollowUpTime.setText(format.format(date));
//                tvTime.setText(getTime(date));
            boolean validateData = validateData();
            tvAction.setEnabled(validateData);
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

    private void initData() {
        if (followUpEntity == null) {
            return;
        }

        ResidentBean resident = followUpEntity.getResident();
        if (resident != null) {

            String userPhoto = resident.getUserPhoto();
            Glide.with(FollowUpDoFragment.this)
                    .load(userPhoto)
                    .apply(RequestOptions.placeholderOf(R.drawable.common_ic_avatar_default))
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivResidentAvatar);

            String bname = resident.getBname();
            bname = TextUtils.isEmpty(bname) ? "" : bname;
            tvResidentName.setText(bname);
            int age = resident.getAge();
            String gender = resident.getSex();
            gender = TextUtils.isEmpty(gender) ? "" : gender + " · ";
            tvResidentGenderAge.setText(gender + age + "岁");

            String userType = resident.getUserType();
            userType = TextUtils.isEmpty(userType) ? "" : userType;
            tvResidentTag.setText(userType);
            tvResidentTag.setSelected(!"正常居民".equals(userType));

            String address = resident.getDz();
            address = TextUtils.isEmpty(address) ? "" : address;
            tvResidentAddress.setText(address);
        }

        String planContent = followUpEntity.getPlanContent();
        planContent = TextUtils.isEmpty(planContent) ? "" : planContent;
        tvFollowUpContent.setText(planContent);

        UserEntity planDoctor = followUpEntity.getPlanDoctor();
        if (planDoctor != null) {
            String doctorName = planDoctor.getDoctername();
            doctorName = TextUtils.isEmpty(doctorName) ? "" : doctorName;
            tvFollowUpDoctor.setText(doctorName);
        }

        tvFollowUpTime.setText(formatUI.format(time));
//        tvFollowUpType.setText("");
//        tvFollowUpTemplate.setText("");

        boolean validateData = validateData();
        tvAction.setEnabled(validateData);

    }

    private void onAction() {
        String timeFormat = time == null ? "" : this.format.format(time);
        String result = etFollowUpResult.getText().toString();
        FollowUpUpdateBody body = new FollowUpUpdateBody();

        body.setId(followUpEntity.getId());
        body.setFollowStatus("已随访");
        body.setResultContent(result);
        if (!types.isEmpty()) {
            HealthTagEntity type = types.get(typeIndex);
            body.setCategoryId(type.getTypeId());
            body.setCategoryName(type.getText());
        }
        body.setResultDate(timeFormat);
        int planDoctorId = followUpEntity.getPlanDoctorId();
        body.setResultDoctorId(planDoctorId);

        if (!templates.isEmpty()) {
            HealthTagEntity t = templates.get(templateIndex);
            body.setResultTypeId(t.getTypeId());
            body.setResultTitle(t.getValue());
        }

        repository.updateFollowUp(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        ToastUtils.showShort("提交中...");
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ToastUtils.showShort("提交成功");
                        onBack();
                    }
                });
    }

    private boolean validateData() {
        String timeFormat = time == null ? "" : this.format.format(time);
        String template = tvFollowUpTemplate.getText().toString();
        String type = tvFollowUpType.getText().toString();
        String result = etFollowUpResult.getText().toString();
        return !TextUtils.isEmpty(timeFormat)
                && !TextUtils.isEmpty(template)
                && !TextUtils.isEmpty(type)
                && !TextUtils.isEmpty(result);
    }

    private void onBack() {
        FragmentUtils.finish(getActivity());
    }
}
