package com.gcml.module_health_manager.api;

import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_resource.bean.ResidentBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HealthManageService {
    @GET("ZZB/br/apply/")
    Observable<HttpResult<List<ResidentBean>>> getResidents(
            @Query("doid") int doid,
            @Query("state") int state,
            @Query("page") int page,
            @Query("limit") int limit
    );
}
