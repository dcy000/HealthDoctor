package com.gcml.module_guardianship;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.bean.WatchInformationBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.CallPhoneUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

@Route(path = "/fragment/normal/resident")
public class NormalResidentFragment extends StateBaseFragment {
    private NumberChange numberChange;
    private RecyclerView mRv;
    private List<ResidentBean> residentBeans = new ArrayList<>();
    private BaseQuickAdapter<ResidentBean, BaseViewHolder> adapter;

    public void setNumberChange(NumberChange numberChange) {
        this.numberChange = numberChange;
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.fragment_normal_resident;
    }

    @Override
    public void initParams(Bundle bundle) {
    }

    @Override
    public void initView(View view) {
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        initRv();
        getData();
    }

    private void initRv() {
        mRv.setLayoutManager(new LinearLayoutManager(mActivity));
        mRv.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 20));

        adapter = new BaseQuickAdapter<ResidentBean, BaseViewHolder>(R.layout.item_layout_guardianship, residentBeans) {
            @Override
            protected void convert(BaseViewHolder helper, ResidentBean item) {
                helper.setText(R.id.item_name, item.getBname());
                helper.setText(R.id.item_address, TextUtils.isEmpty(item.getDz()) ? "未填" : item.getDz());
                Glide.with(Box.getApp())
                        .load(item.getUserPhoto())
                        .into((ImageView) helper.getView(R.id.item_head));

                helper.getView(R.id.iv_phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWatchInfo(item);
                    }
                });
            }
        };
        mRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Routerfit.register(GuardianshipRouterApi.class).skipResidentDetailActivity(residentBeans.get(position), 2);
            }
        });
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
                .setSupportFM(getFragmentManager())
                .setLayoutId(R.layout.dialog_layout_phone_tips)
                .setWidth(AutoSizeUtils.pt2px(mActivity, 540))
                .setOutCancel(false)
                .setDimAmount(0.5f)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, FDialog dialog) {
                        holder.setText(R.id.tv_title, name + "的电话号码");
                        holder.setText(R.id.tv_message, phone);
                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallPhoneUtils.instance().callPhone(mActivity, phone);
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show();
    }

    private void getData() {
        UserEntity user = Box.getSessionManager().getUser();
        Box.getRetrofit(GuardianshipApi.class)
                .getResidents(user.getDocterid() + "", "2")
                .compose(RxUtils.httpResponseTransformer())
                .subscribe(new CommonObserver<List<ResidentBean>>() {
                    @Override
                    public void onNext(List<ResidentBean> residentBeans) {
                        if (numberChange != null) {
                            numberChange.normalNumberChanged(residentBeans.size());
                        }
                        NormalResidentFragment.this.residentBeans.clear();
                        NormalResidentFragment.this.residentBeans.addAll(residentBeans);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onError(ApiException ex) {

                    }
                });
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

}
