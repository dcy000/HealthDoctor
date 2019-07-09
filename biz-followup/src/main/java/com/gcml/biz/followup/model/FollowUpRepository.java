package com.gcml.biz.followup.model;

import com.gcml.biz.followup.model.entity.FollowUpList;
import com.gcml.biz.followup.model.entity.HealthTagEntity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
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

}
