package com.gcml.biz.followup.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.biz.followup.R;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.LazyFragment;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * 取消随访
 */
public class FollowUpCancelFragment extends LazyFragment {


    // toolbar
    private ImageView ivToolbarLeft;
    private TextView tvToolbarLeft;
    private TextView tvToolbarRight;
    private TextView tvToolbarTitle;

    private EditText etCancelReason;
    private TextView tvTextCount;

    int maxLen = 100;

    public FollowUpCancelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow_up_cancel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivToolbarLeft = (ImageView) view.findViewById(R.id.ivToolbarLeft);
        tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        tvToolbarRight = (TextView) view.findViewById(R.id.tvToolbarRight);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
        ivToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setVisibility(View.GONE);
        tvToolbarRight.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("取消随访");
//        tvToolbarLeft.setText("取消");
        tvToolbarRight.setText("提交");
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

        etCancelReason = (EditText) view.findViewById(R.id.etCancelReason);
        tvTextCount = (TextView) view.findViewById(R.id.tvTextCount);

        RxTextView.textChanges(etCancelReason)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {
                        String txt = charSequence.toString();
                        Editable editable = etCancelReason.getText();
                        int len = editable.length();

                        if (len > maxLen) {
                            int selEndIndex = Selection.getSelectionEnd(editable);
                            txt = editable.toString();
                            String newStr = txt.substring(0, maxLen);
                            etCancelReason.setText(newStr);
                            txt = newStr;
                            editable = etCancelReason.getText();
                            int newLen = editable.length();
                            if (selEndIndex > newLen) {
                                selEndIndex = editable.length();
                            }
                            Selection.setSelection(editable, selEndIndex);
                        }
                        return txt;
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onNext(String text) {
                        tvTextCount.setText(text.length() + "/100");
                    }
                });

    }


    private void onAction() {

    }

    private void onBack() {

    }
}
