package com.gcml.module_guardianship.api;

import com.gcml.module_guardianship.bean.FamilyBean;
import com.gcml.module_guardianship.bean.HandRingHealthDataBena;
import com.gcml.module_guardianship.bean.LatLonBean;
import com.gcml.module_guardianship.bean.MeasureDataBean;
import com.gcml.module_guardianship.bean.UpdateHealthStatusBean;
import com.gcml.module_guardianship.bean.UserTypeBean;
import com.gzq.lib_resource.bean.WatchInformationBean;
import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_resource.bean.ResidentBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by gzq on 19-2-6.
 */

public interface GuardianshipApi {

    /**
     * 根据手环码查用户信息
     *
     * @param watchCode
     * @return
     */
    @GET("ZZB/api/user/watch/")
    Observable<HttpResult<WatchInformationBean>> getWatchInfo(
            @Query("watchCode") String watchCode
    );

    /**
     * 添加监护人
     *
     * @return
     */
    @POST("ZZB/api/guardian/user/{userId}/")
    Observable<HttpResult<Object>> addResident(
            @Path("userId") String userId,
            @Query("mobileNum") String mobileNum
    );

    /**
     * 获取居民的监护圈子
     *
     * @return
     */
    @GET("ZZB/api/guardian/user/{userId}/guardians/")
    Observable<HttpResult<List<FamilyBean>>> getResidentRelationships(
            @Path("userId") String userId
    );

    /**
     * 紧急呼叫的处理结果
     *
     * @return
     */
    @POST("ZZB/api/guardian/warning/{warningId}/")
    Observable<HttpResult<Object>> postSOSDealResult(
            @Path("warningId") String warningId,
            @Query("docterId") String guardianId,
            @Query("content") String content
    );

    /**
     * 获取手环的经纬度信息
     *
     * @param userId
     * @return
     */
    @GET("ZZB/api/user/watch/address/")
    Observable<HttpResult<LatLonBean>> getLatLon(
            @Query("userId") String userId
    );

    /**
     * 获取手环上的测量数据
     *
     * @param userId
     * @return
     */
    @GET("ZZB/api/user/watch/detection/")
    Observable<HttpResult<HandRingHealthDataBena>> getHandRingHealthData(
            @Query("userId") String userId
    );

    /**
     * @param docterId
     * @param payStatus 0:签约用户 1：已付费 2：未付费
     * @return
     */
    @GET("ZZB/api/doctor/{docterId}/users/")
    Observable<HttpResult<List<ResidentBean>>> getResidents(
            @Path("docterId") String docterId,
            @Query("payStatus") String payStatus
    );

    /**
     * @param imei 手环码
     * @param type 类型：1->血压；2->心率
     * @return
     */
    @POST("ZZB/api/wristband/Zh/startMeasure")
    Observable<HttpResult<Object>> measureByHandRing(
            @Query("imei") String imei,
            @Query("type") String type
    );

    @GET("ZZB/api/user/watch/detection/")
    Observable<HttpResult<MeasureDataBean>> getMeasureData(
            @Query("userId") String userId
    );

    @POST("ZZB/api/type/getItemByCode/")
    Observable<HttpResult<List<UserTypeBean>>> getItemByCode(
            @Query("code") String code
    );


    @POST("ZZB/br/updateOneUser")
    Observable<HttpResult<Object>> updateOneUser(@Body UpdateHealthStatusBean data);
}
