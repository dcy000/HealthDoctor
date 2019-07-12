package com.gcml.biz.followup.model;

import com.gcml.biz.followup.model.entity.DoctorList;
import com.gcml.biz.followup.model.entity.FollowUpBody;
import com.gcml.biz.followup.model.entity.FollowUpList;
import com.gcml.biz.followup.model.entity.HealthTagEntity;
import com.gcml.biz.followup.model.entity.ResidentList;
import com.gcml.biz.followup.model.entity.ResidentListBody;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.UserEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

public class FollowUpRepository {

    private FollowUpService service = Box.getRetrofit(FollowUpService.class);

    public Observable<FollowUpList> followUpList(
            String doctorId,
            String status,
            int page,
            int limit) {
        return service.followUpList(doctorId, status, page, limit)
                .compose(RxUtils.httpResponseTransformer());
    }

    public Observable<List<HealthTagEntity>> healthTagList(
            String code) {
        return service.healthTagList(code)
                .compose(RxUtils.httpResponseTransformer());
    }

    public Observable<List<Object>> healthTagList() {
        HealthTagEntity item = new HealthTagEntity();
        item.setText("正常居民");
        item.setValue("正常居民");
        //健康居民
        Observable<List<HealthTagEntity>> rxTags0 =
                Observable.just(item).toList().toObservable();
        //职业病
        Observable<List<HealthTagEntity>> rxTags1 =
                healthTagList("professional_user_type").subscribeOn(Schedulers.io());
        //慢性病
        Observable<List<HealthTagEntity>> rxTags2 =
                healthTagList("chronic_user_type").subscribeOn(Schedulers.io());

        Observable<List<Object>> rxObjs = Observable.zip(
                rxTags0, rxTags1, rxTags2,
                new Function3<
                        List<HealthTagEntity>,
                        List<HealthTagEntity>,
                        List<HealthTagEntity>,
                        List<Object>>() {
                    @Override
                    public List<Object> apply(
                            List<HealthTagEntity> healthTagEntities1,
                            List<HealthTagEntity> healthTagEntities2,
                            List<HealthTagEntity> healthTagEntities3) throws Exception {
                        ArrayList<Object> objects = new ArrayList<>();
                        objects.add("健康居民");
                        objects.addAll(healthTagEntities1);
                        objects.add("职业病");
                        objects.addAll(healthTagEntities2);
                        objects.add("慢性病");
                        objects.addAll(healthTagEntities3);
                        return objects;
                    }
                });
        return rxObjs;
    }

    public Observable<List<HealthTagEntity>> followUpTempletes() {
        return healthTagList("follow_up_plan");
    }

    public Observable<List<HealthTagEntity>> followUpResultTempletes() {
        return healthTagList("follow_up_tamplate");
    }

    public Observable<List<ResidentBean>> residentList(
            int doctorId,
            String existUserType,
            String patientName,
            int followCountType,
            int page,
            int limit) {

        ResidentListBody body = new ResidentListBody();
        body.setDoctorId(doctorId);
        body.setExistUserType(existUserType);
        body.setPatientName(patientName);
        body.setFollowCountType(followCountType);
        body.setPage(1);
        body.setLimit(1000);

        return service.residentList(body)
                .compose(RxUtils.httpResponseTransformer())
                .map(new Function<ResidentList, List<ResidentBean>>() {
                    @Override
                    public List<ResidentBean> apply(ResidentList residentList) throws Exception {
                        if (residentList.getData() == null) {
                            return new ArrayList<>();
                        }
                        return residentList.getData();
                    }
                });
    }

    public Observable<List<UserEntity>> doctorList(
            String doctorName,
            int page,
            int limit) {
        return service.doctorList(doctorName, 1, 1000)
                .compose(RxUtils.httpResponseTransformer())
                .map(new Function<DoctorList, List<UserEntity>>() {
                    @Override
                    public List<UserEntity> apply(DoctorList doctorList) throws Exception {
                        if (doctorList.getData() == null) {
                            return new ArrayList<>();
                        }
                        return doctorList.getData();
                    }
                });
    }

    public Observable<Object> addFollowUpList(List<FollowUpBody> followUpBodyList) {
        return service.addFollowUpList(followUpBodyList)
                .compose(RxUtils.httpResponseTransformer());
    }
}
