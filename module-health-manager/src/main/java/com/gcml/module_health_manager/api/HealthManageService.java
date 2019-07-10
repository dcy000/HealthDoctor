package com.gcml.module_health_manager.api;

import com.gcml.module_health_manager.bean.ConstractBean;
import com.gzq.lib_core.http.model.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HealthManageService {
    @GET("ZZB/br/apply/")
    Observable<HttpResult<ConstractBean>> getResidents(
            @Query("doid") int doid,
            @Query("state") int state,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @POST("ZZB/docter/docter_agree_shenqing/")
    Observable<HttpResult<Object>> updateQinaYue(
            @Query("bid") int bid,
            @Query("docter_sign") String docterSign,
            @Query("state") String state);
}
