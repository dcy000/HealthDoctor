package com.gcml.module_mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_mine.api.MineApi;
import com.gcml.module_mine.bean.MoneyDetailBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.data.TimeUtils;
import com.sjtu.yifei.annotation.Route;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Route(path = "/mine/my/money/packet")
public class MyMoneyPacketActivity extends StateBaseActivity {
    /**
     * 余额
     */
    private TextView mTvTitle1;
    /**
     * 99.00
     */
    private TextView mTvMoneyAmount;
    private ConstraintLayout mCl1;
    private LinearLayout mLl1;
    private RecyclerView mRv;
    private UserEntity user;
    private List<MoneyDetailBean> moneyDetailBeans = new ArrayList<>();
    private BaseQuickAdapter<MoneyDetailBean, BaseViewHolder> adapter;
    private int ammount;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_my_money_packet;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        user = Box.getSessionManager().getUser();
        ammount=intentArgument.getIntExtra("money",0);
    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("我的钱包");
        mTvTitle1 = (TextView) findViewById(R.id.tv_title1);
        mTvMoneyAmount = (TextView) findViewById(R.id.tv_money_amount);
        mTvMoneyAmount.setText(ammount + "");
        mCl1 = (ConstraintLayout) findViewById(R.id.cl_1);
        mLl1 = (LinearLayout) findViewById(R.id.ll_1);
        mRv = (RecyclerView) findViewById(R.id.rv);
        initRv();
        getData();
    }

    private void getData() {

        Box.getRetrofit(MineApi.class)
                .getMoneyDetails(user.getDocterid() + "")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<MoneyDetailBean>>() {
                    @Override
                    public void onNext(List<MoneyDetailBean> detailBeans) {
                        moneyDetailBeans.clear();
                        moneyDetailBeans.addAll(detailBeans);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void initRv() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(adapter = new BaseQuickAdapter<MoneyDetailBean, BaseViewHolder>(R.layout.item_layout_my_money_packet, moneyDetailBeans) {
            @Override
            protected void convert(BaseViewHolder helper, MoneyDetailBean item) {
                helper.setText(R.id.tv_title, item.getUserName() + item.getName());
                helper.setText(R.id.tv_time, TimeUtils.milliseconds2String(item.getTime(), new SimpleDateFormat("yyyy.MM.dd HH:mm")));
                helper.setText(R.id.tv_money, "+" + item.getAmount());
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }


}
