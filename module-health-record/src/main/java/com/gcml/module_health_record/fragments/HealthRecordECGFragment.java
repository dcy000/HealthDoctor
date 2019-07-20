package com.gcml.module_health_record.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gcml.module_health_record.R;
import com.gcml.module_health_record.RecycleBaseFragment;
import com.gcml.module_health_record.bean.CholesterolHistory;
import com.gcml.module_health_record.bean.ECGHistory;
import com.gcml.module_health_record.bean.WeightHistory;
import com.gcml.module_health_record.network.HealthRecordRepository;
import com.gcml.module_health_record.others.XindianAdapter;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.utils.data.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class HealthRecordECGFragment extends RecycleBaseFragment implements View.OnClickListener {

    private RecyclerView mXindiantu;
    private int bid;
    private HealthRecordRepository repository;
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private boolean isStart;
    private SimpleDateFormat formatUI = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
    private String startDate = null, endDate = null;

    @Override
    protected int initLayout() {
        return R.layout.health_record_fragment_health_record_ecg;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        bid = bundle.getInt("bid", 0);
        repository = new HealthRecordRepository();
        repository.userId = bid + "";
        mXindiantu = view.findViewById(R.id.xindiantu);
        mTvStartTime = view.findViewById(R.id.tv_start_time);
        mTvEndTime = view.findViewById(R.id.tv_end_time);
        mTvStartTime.setOnClickListener(this);
        mTvEndTime.setOnClickListener(this);
        getData();
    }

    private int selectEndYear;
    private int selectEndMonth;
    private int selectEndDay;
    private int selectEndHour;
    private int selectEndMinnute;
    private int selectEndSecond;
    private String endMillisecond;
    private int selectStartYear;
    private int selectStartMonth;
    private int selectStartDay;
    private String startMillisecond;

    private void getData() {
        Calendar calendar = Calendar.getInstance();
        selectEndYear = calendar.get(Calendar.YEAR);
        selectEndMonth = calendar.get(Calendar.MONTH) + 1;
        selectEndDay = calendar.get(Calendar.DATE);
        selectEndHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectEndMinnute = calendar.get(Calendar.MINUTE);
        selectEndSecond = calendar.get(Calendar.SECOND);
        endMillisecond = TimeUtils.string2Milliseconds(selectEndYear + "-" + selectEndMonth + "-" +
                        selectEndDay + "-" + selectEndHour + "-" + selectEndMinnute + "-" + selectEndSecond,
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")) + "";

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        Date weekAgoDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(weekAgoDate);
        String[] date = result.split("-");
        selectStartYear = Integer.parseInt(date[0]);
        selectStartMonth = Integer.parseInt(date[1]);
        selectStartDay = Integer.parseInt(date[2]);
        startMillisecond = TimeUtils.string2Milliseconds(selectStartYear + "-" + selectStartMonth + "-" +
                selectStartDay, new SimpleDateFormat("yyyy-MM-dd")) + "";
        mTvStartTime.setText(selectStartYear + "年" + selectStartMonth + "月" + selectStartDay + "日");
        mTvEndTime.setText(selectEndYear + "年" + selectEndMonth + "月" + selectEndDay + "日");
        startDate = startMillisecond;
        endDate = endMillisecond;
        repository
                .getECGHistory(startMillisecond, endMillisecond, "9")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<ECGHistory>>() {
                    @Override
                    public void onNext(List<ECGHistory> ecgHistories) {
                        refreshData(ecgHistories,"9");
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void refreshData(List<ECGHistory> response, String temp) {
        List<ECGHistory> ecgs=new ArrayList<>();
        for (ECGHistory data : response) {
            if (!TextUtils.isEmpty(data.result) && !data.result.contains("重新测试")) {
               ecgs.add(data);
            }
        }
        mXindiantu.setLayoutManager(new LinearLayoutManager(getContext()));
        if (isAdded()) {
            mXindiantu.setAdapter(new XindianAdapter(R.layout.health_record_item_message, ecgs,
                    getResources().getStringArray(R.array.ecg_measureres)));
        }
    }
    public void showTimeDialog(boolean isStart) {
        if (getActivity() == null) {
            return;
        }
        this.isStart = isStart;
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(1990, 11, 30);
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
                .setContentTextSize(18)
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

    private OnTimeSelectListener timeListener = new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, View v) {//选中事件回调

            if (isStart) {
                startDate = TimeUtils.date2Milliseconds(date) + "";
                mTvStartTime.setText(formatUI.format(date));
            } else {
                endDate = TimeUtils.date2Milliseconds(date) + "";
                mTvEndTime.setText(formatUI.format(date));
            }

            repository
                    .getECGHistory(startDate, endDate, "9")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<List<ECGHistory>>() {
                        @Override
                        public void onNext(List<ECGHistory> ecgHistories) {
                            refreshData(ecgHistories,"9");
                        }

                        @Override
                        public void onError(Throwable e) {
                            refreshErrorData("暂无该项数据");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    };
    public void refreshErrorData(String message) {
        ToastUtils.showShort(message);
//        mTvEmptyDataTips.setText("啊哦!你还没有测量数据");
//        view.findViewById(R.id.view_empty_data).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_start_time) {
            showTimeDialog(true);
            return;
        }
        if (i == R.id.tv_end_time) {
            showTimeDialog(false);
        }
    }
}
