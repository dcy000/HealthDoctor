package com.gcml.module_health_record.fragments;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gcml.module_health_record.R;
import com.gcml.module_health_record.RecycleBaseFragment;
import com.gcml.module_health_record.bean.BUA;
import com.gcml.module_health_record.bean.BloodOxygenHistory;
import com.gcml.module_health_record.bean.CholesterolHistory;
import com.gcml.module_health_record.network.HealthRecordRepository;
import com.gcml.module_health_record.others.MyFloatNumFormatter;
import com.gcml.module_health_record.others.MyMarkerView;
import com.gcml.module_health_record.others.TimeFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.gzq.lib_core.base.Box;
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

public class HealthRecordCholesterolFragment extends RecycleBaseFragment implements View.OnClickListener {
    private TextView mColor1;
    private TextView mIndicator1;
    private TextView mColor2;
    private TextView mIndicator2;
    private LinearLayout mLlSecond;
    private LinearLayout mLlIndicator;
    private LineChart mChart;

    private RadioButton mRbKongfu;
    private RadioButton mRbOneHour;
    private RadioButton mRbTwoHour;
    private RadioGroup mRgXuetangTime;
    private int bid;
    private HealthRecordRepository repository;
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private boolean isStart;
    private SimpleDateFormat formatUI = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
    private String startDate = null, endDate = null;
    @Override
    protected int initLayout() {
        return R.layout.health_record_fragment_health_record;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        bid = bundle.getInt("bid", 0);
        repository = new HealthRecordRepository();
        repository.userId = bid + "";
        mRbKongfu = view.findViewById(R.id.rb_kongfu);
        mRbOneHour = view.findViewById(R.id.rb_one_hour);
        mRbTwoHour = view.findViewById(R.id.rb_two_hour);
        mRgXuetangTime = view.findViewById(R.id.rg_xuetang_time);
        mColor1 = view.findViewById(R.id.color_1);
        mIndicator1 = view.findViewById(R.id.indicator_1);
        mColor2 = view.findViewById(R.id.color_2);
        mIndicator2 = view.findViewById(R.id.indicator_2);
        mLlSecond = view.findViewById(R.id.ll_second);
        mLlIndicator = view.findViewById(R.id.ll_indicator);
        mChart = view.findViewById(R.id.chart);
        mTvStartTime = view.findViewById(R.id.tv_start_time);
        mTvEndTime = view.findViewById(R.id.tv_end_time);
        mTvStartTime.setOnClickListener(this);
        mTvEndTime.setOnClickListener(this);
        mRgXuetangTime.setVisibility(View.GONE);
        //指示器的颜色
        mColor1.setBackgroundColor(Color.parseColor("#9CD793"));
        mIndicator1.setText("成人(mmol/L)");
        mColor2.setBackgroundColor(Color.parseColor("#6D80E2"));
        mIndicator2.setText("儿童(mmol/L)");
        mLlSecond.setVisibility(View.VISIBLE);

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
                .getCholesterolHistory(startMillisecond, endMillisecond, "7")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<CholesterolHistory>>() {
                    @Override
                    public void onNext(List<CholesterolHistory> cholesterolHistories) {
                        refreshData(cholesterolHistories,"7");
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
    private void initChart() {
        //x轴右下角文字描述
        mChart.getDescription().setEnabled(false);
        // enable touch gestures 启用触
        mChart.setTouchEnabled(true);

        mChart.setDragEnabled(true);
        //启用缩放
        mChart.setScaleEnabled(true);
        //禁止y轴缩放
        mChart.setScaleYEnabled(false);
        mChart.setExtraLeftOffset(40);
        mChart.setExtraRightOffset(80);
        mChart.setNoDataText("");

        XAxis xAxis = mChart.getXAxis();
        //绘制底部的X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //启用X轴的网格虚线
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);
        xAxis.setTextSize(20f);
        xAxis.setLabelCount(4);


        LimitLine ll1 = new LimitLine(2.9f, "2.9mmol/L");
        ll1.setLineWidth(2f);
        ll1.setLineColor(Color.parseColor("#9CD793"));
        ll1.enableDashedLine(10.0f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setTextSize(18f);


        LimitLine ll2 = new LimitLine(6.0f, "6.0mmol/L");
        ll2.setLineWidth(2f);
        ll2.setLineColor(Color.parseColor("#9CD793"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(18f);

        LimitLine ll3 = new LimitLine(3.1f, "3.1mmol/L");
        ll3.setLineWidth(2f);
        ll3.setLineColor(Color.parseColor("#6D80E2"));
        ll3.enableDashedLine(10.0f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll3.setTextSize(18f);


        LimitLine ll4 = new LimitLine(5.2f, "5.2mmol/L");
        ll4.setLineWidth(2f);
        ll4.setLineColor(Color.parseColor("#6D80E2"));
        ll4.enableDashedLine(10f, 10f, 0f);
        ll4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll4.setTextSize(18f);

        //Y轴设置
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setGranularity(0.01f);
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.addLimitLine(ll3);
        leftAxis.addLimitLine(ll4);

        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();

        //网格线
        leftAxis.setDrawGridLines(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        //启用零线
        leftAxis.setDrawZeroLine(false);

        //绘制警戒线在绘制数据之后
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setTextSize(20f);

        //禁用右边的Y轴
        mChart.getAxisRight().setEnabled(false);
        mChart.animateX(2500);
    }

    public void refreshData(List<CholesterolHistory> response, String temp) {
        initChart();
        ArrayList<Entry> value = new ArrayList<Entry>();
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < response.size(); i++) {
            if (response.get(i).cholesterol < 2.9 || response.get(i).cholesterol > 6.0) {
                colors.add(Color.RED);
            } else {
//                colors.add(getResources().getColor(R.color.health_record_node_text_color));//正常字体的颜色
                colors.add(ContextCompat.getColor(Box.getApp(), R.color.health_record_node_text_color));//正常字体的颜色
            }
            value.add(new Entry(i, response.get(i).cholesterol));
            times.add(response.get(i).time);
        }
        if (times.size() != 0) {

            mChart.getXAxis().setValueFormatter(new TimeFormatter(times));
            if (isAdded()) {
                MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view, temp, times);
                mv.setChartView(mChart);
                mChart.setMarker(mv);
            }


            LineDataSet set1;
            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(value);
                if (value.size() <= 3) {
                    set1.setMode(LineDataSet.Mode.LINEAR);
                }
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                set1 = new LineDataSet(value, "");
                set1.setDrawIcons(false);
                //设置选中指示线的样式
                set1.setHighLightColor(Color.rgb(244, 117, 117));


                //走势线的样式
//                set1.setColor(getResources().getColor(R.color.health_record_line_color));
                set1.setColor(ContextCompat.getColor(Box.getApp(), R.color.health_record_line_color));
//                set1.setCircleColor(getResources().getColor(R.color.health_record_node_color));
                set1.setCircleColor(ContextCompat.getColor(Box.getApp(), R.color.health_record_node_color));


                set1.setValueTextColors(colors);
                //走势线的粗细
                set1.setValueFormatter(new MyFloatNumFormatter(temp));
                set1.setLineWidth(6f);
                //封顶圆圈的直径
                set1.setCircleRadius(8f);
                //是否镂空
                set1.setDrawCircleHole(true);
                set1.setCircleHoleRadius(4f);
                set1.setValueTextSize(18f);

                //左下角指示器样式
                set1.setFormLineWidth(0f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
                set1.setFormSize(0f);
//
                //曲线区域颜色填充
                set1.setDrawFilled(false);
                if (Utils.getSDKInt() >= 18) {
                    Drawable drawable = ContextCompat.getDrawable(
                            Box.getApp(), R.drawable.fade_tiwen);
                    set1.setFillDrawable(drawable);
                } else {
                    set1.setFillColor(Color.parseColor("#B3DCE2F3"));
                }
                if (value.size() <= 3) {
                    set1.setMode(LineDataSet.Mode.LINEAR);
                } else {
                    set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                }
                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1);
                LineData data = new LineData(dataSets);
                mChart.setData(data);
            }
        }
    }

    public void refreshErrorData(String message) {
        ToastUtils.showShort(message);
        if (mChart != null && isAdded()) {
            mChart.setNoDataText(getResources().getString(R.string.noData));
            mChart.setData(null);
            mChart.invalidate();
//            mTvEmptyDataTips.setText("啊哦!你还没有测量数据");
//            view.findViewById(R.id.view_empty_data).setVisibility(View.VISIBLE);
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
                    .getCholesterolHistory(startDate, endDate, "7")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<List<CholesterolHistory>>() {
                        @Override
                        public void onNext(List<CholesterolHistory> cholesterolHistories) {
                            refreshData(cholesterolHistories,"7");
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
