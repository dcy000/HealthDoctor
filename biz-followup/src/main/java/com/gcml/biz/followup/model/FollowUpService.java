package com.gcml.biz.followup.model;

import com.gcml.biz.followup.model.entity.DoctorList;
import com.gcml.biz.followup.model.entity.FollowUpBody;
import com.gcml.biz.followup.model.entity.FollowUpList;
import com.gcml.biz.followup.model.entity.FollowUpUpdateBody;
import com.gcml.biz.followup.model.entity.HealthTagEntity;
import com.gcml.biz.followup.model.entity.ResidentList;
import com.gcml.biz.followup.model.entity.ResidentListBody;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FollowUpService {


    /**
     * 获取随访信息列表
     *
     * @param doctorId 医生 id
     * @param status   随访状态
     * @param page     页码
     * @param limit    页数
     * @return 随访信息列表
     */
    @GET("ZZB/api/web/follow/getFollowsNew")
    Observable<HttpResult<FollowUpList>> followUpList(
            @Query("planDoctorId") String doctorId,
            @Query("followStatus") String status,
            @Query("page") int page,
            @Query("limit") int limit
    );

    /**
     * 获取居民健康状态标签列表
     *
     * @param code 健康状态标签分类码
     *             ｛
     *             职业病管理：professional_user_type，
     *             慢性病： chronic_user_type,
     *             <p>
     *             随访模版：follow_up_plan，
     *             随访结果模版：follow_up_tamplate，
     *             <p>
     *             随访方式：follow_up_type， 面对面
     *             ｝
     * @return 健康状态标签列表
     */
    @POST("ZZB/api/type/getItemByCode")
    Observable<HttpResult<List<HealthTagEntity>>> healthTagList(
            @Query("code") String code
    );

    /**
     * 搜索居民信息 可按名字模糊搜索
     *
     * @param residentListBody
     * @return 居民信息
     */
    @POST("ZZB/br/searchPatientAndFollow")
    Observable<HttpResult<ResidentList>> residentList(
            @Body ResidentListBody residentListBody
    );

    /**
     * 搜索医生信息 可按名字模糊搜索
     *
     * @return 医生信息列表
     */
    @POST("ZZB/docter/docters_all")
    Observable<HttpResult<DoctorList>> doctorList(
            @Query("doname") String name,
            @Query("page") int page,
            @Query("limit") int limit
    );

    /**
     * 批量添加随访
     *
     * @return
     */
    @POST("ZZB/api/web/follow/fastAdd/")
    Observable<HttpResult<DoctorList>> addFollowUpList(
            @Body List<FollowUpBody> body
    );


    /**
     * 修改随访状态： 已取消， 已随访
     *
     * @return
     */
    @POST("ZZB/api/web/follow/updateFollowById")
    Observable<HttpResult<Object>> updateFollowUp(
            @Body FollowUpUpdateBody body
    );

}
