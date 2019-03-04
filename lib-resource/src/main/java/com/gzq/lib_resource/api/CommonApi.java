package com.gzq.lib_resource.api;

import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_resource.bean.UserEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CommonApi {
    @GET("ZZB/api/doctor/{docterId}/")
    Observable<HttpResult<UserEntity>> getProfile(
            @Path("docterId") String userId
    );
}
