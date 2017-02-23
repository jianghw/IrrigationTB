package com.cjyun.tb.supervisor.suRetrofit;

import com.cjyun.tb.supervisor.bean.MeInfoBean;
import com.cjyun.tb.supervisor.bean.PatitentDetailsBean;
import com.cjyun.tb.supervisor.bean.SuCodeBean;
import com.cjyun.tb.supervisor.bean.SubsequentVisitBean;
import com.cjyun.tb.supervisor.bean.TackPillsBean;
import com.cjyun.tb.supervisor.bean.UserEntityBean;
import com.cjyun.tb.supervisor.bean.VisitDetailsBean;

import java.util.Date;
import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/5/17 0017.
 * <p>
 * retrofit的请求方式
 */
public interface SuRequestService {
    //GET、POST、PUT、DELETE和HEAD


    //所有患者信息的请求
    @GET("v1/director/patients")
    Observable<UserEntityBean> getAllPatientInfo();


    // 极光推送
    @POST("v1/director/app/jpush_id")
    Observable<SuCodeBean> postJpushInfo(@Query("push_id") String jpush,
                                         @Query("device_type") String Android);


    // 患者对应的详情信息
    @GET("v1/director/patients/{id}")
    Observable<PatitentDetailsBean> getPatientDetailsInfo(@Path("id") int patientId);

    // 服药记录
    @GET("v1/director/patients/{id}/medicine_log")
    Observable<List<TackPillsBean>> getTackPillsInfo(@Path("id") int patientId);

    // 复诊患者
    @GET("v1/director/patients/{id}/subsequent_visits")
    Observable<List<SubsequentVisitBean>> getSubsequentVisitInfo(@Path("id") int patientId);

    // 随访记录
    @GET("v1/director/patients/{id}/visits")
    Observable<List<VisitDetailsBean>> getVisitInfo(@Path("id") int patientId);

    // 添加随访记录
    @POST("v1/director/patients/{id}/visits")
    Observable<SuCodeBean> postAddVisitInfo(@Path("id") int patientId,
                                            @Query("setting_date") Date date,
                                            @Query("visit_typpe") String type,
                                            @Query("content") String content);

    // 请求督导员信息
    @GET("v1/director/info")
    Observable<MeInfoBean> getLoadMeInfo();

    // 修改密码
    @PUT("users")
    Observable<SuCodeBean> postResetPassword(@Query("current_password") String currentPassword,
                                             @Query("password") String password,
                                             @Query("password_confirmation") String passwordConfirmation,
                                             @Query("user_type") String flb_user);// 用户的类型

    // 修改详细信息
    @FormUrlEncoded
    @PUT("v1/director/info")
    Observable<SuCodeBean> postImproveInfo(@Field("phone_number") String phone_number,
                                           @Field("weixin_id") String weixin_id,
                                           @Field("qq") String qq,
                                           @Field("email") String email,
                                           @Field("address") String address,
                                           @Field("photo") String photo);//头像

    //确认随访
    @FormUrlEncoded
    @PUT("v1/director/patients/{id}/visits")
    Observable<SuCodeBean> postVerifyVisit(@Path("id") int id,
                                           @Field("visit_id") int visit_id,
                                           @Field("actually_time") Date verifyDate);
}
