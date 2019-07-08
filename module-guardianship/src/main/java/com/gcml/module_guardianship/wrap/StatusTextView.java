package com.gcml.module_guardianship.wrap;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gcml.module_guardianship.R;

public class StatusTextView extends FrameLayout {

    private View view;
    private TextView tvStatusName;

    public StatusTextView(Context context) {
        super(context);
        init();
    }

    public StatusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.health_status_view, null, false);
        tvStatusName = view.findViewById(R.id.tvStatusName);
        addView(view);
    }

    public void setStatus(String name) {
        if (TextUtils.equals("正常居民", name)) {
            tvStatusName.setBackgroundResource(R.drawable.shape_bg_green);
        } else {
            tvStatusName.setBackgroundResource(R.drawable.shape_bg_orange);
        }
        tvStatusName.setText(name);
    }


}
