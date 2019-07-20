package com.gcml.module_health_record.others;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.gzq.lib_resource.utils.data.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Time2MouthFormatter implements IAxisValueFormatter {
    private ArrayList<Long> times;

    public Time2MouthFormatter(ArrayList<Long> times) {
        this.times = times;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        if (v == -1) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        if (times.size() == 1) {

            return TimeUtils.milliseconds2String(times.get(0),
                    format);
        }
        if (v >= times.size()) {
            return "";
        }
        return TimeUtils.milliseconds2String(times.get((int) v),
                format);
    }
}
