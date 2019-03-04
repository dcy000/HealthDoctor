package com.gcml.module_mine.api;

import com.gcml.module_mine.bean.MoneyDetailBean;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MineApi {
    @GET("ZZB/api/doctor/{docterId}/amount/info/")
    Observable<HttpResult<List<MoneyDetailBean>>> getMoneyDetails(
            @Path("docterId") String docterId
    );

    @GET("ZZB/api/doctor/{docterId}/amount/")
    Observable<HttpResult<Integer>> getMoneyAmmount(
            @Path("docterId")String docterId
    );
}
