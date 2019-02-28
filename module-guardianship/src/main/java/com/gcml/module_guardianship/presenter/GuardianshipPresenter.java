package com.gcml.module_guardianship.presenter;

import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by gzq on 19-2-6.
 */

public class GuardianshipPresenter extends BasePresenter {
    public GuardianshipPresenter(IView view) {
        super(view);
    }

    private boolean isFirst = true;

    @Override
    public void preData(Object... objects) {
        UserEntity user = Box.getSessionManager().getUser();

        Observable<List<ResidentBean>> vip = Box.getRetrofit(GuardianshipApi.class)
                .getResidents(user.getDocterid() + "", "1")
                .compose(RxUtils.httpResponseTransformer());
        Observable<List<ResidentBean>> normal = Box.getRetrofit(GuardianshipApi.class)
                .getResidents(user.getDocterid() + "", "0")
                .compose(RxUtils.httpResponseTransformer());
        Observable.concat(vip, normal)
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<List<ResidentBean>>() {
                    @Override
                    public void onNext(List<ResidentBean> residentBeans) {
                        if (isFirst) {
                            isFirst = false;
                            ((IGuardianshipView) mView).loadVipSuccess(residentBeans);
                        } else {
                            ((IGuardianshipView) mView).loadNorSuccess(residentBeans);
                            isFirst = true;
                        }
                    }
                });
    }

    @Override
    public void refreshData(Object... objects) {

    }

    @Override
    public void loadMoreData(Object... objects) {

    }
}
