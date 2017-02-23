package com.cjyun.tb.patient.data.remote;

import com.cjyun.tb.patient.bean.RemoteJson;
import com.cjyun.tb.patient.bean.ReturnVisitBean;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jianghw on 2016/3/24 0024.
 * Description 此为患者网络操作请求接口
 */
public interface RemoteServer {
    //GET、POST、PUT、DELETE和HEAD

    //检查是否有新版本发布 https://www.ccd12320.com/api/1/apps/14
    @GET("api/1/apps/18")
    Observable<RemoteJson> getAppUpdateInfo();


    //判断是否绑定药盒
    @GET("v1/app_bound_device/has_bound_device")
    Observable<RemoteJson> getBindBoxInfo();

    //修改密码
    @FormUrlEncoded
    @PUT("password")
    Observable<RemoteJson> putNewPassword(@Field("current_password") String old,
                                          @Field("password") String pw,
                                          @Field("password_confirmation") String rpw,
                                          @Field("user_type") String type);

    //绑定药盒第一步
    @FormUrlEncoded
    @POST("v1/app_bound_device")
    Observable<RemoteJson> postFirstStep(@Field("device_uid") String device_uid, @Field("device_sn") String device_sn);

    //绑定药盒第二步
    @FormUrlEncoded
    @POST("v1/app_bound_device/app_bound_device")
    Observable<RemoteJson> postSecondStep(@Field("device_uid") String device_uid, @Field("device_sn") String device_sn);

    //获取患者个人信息
    @GET("v1/patient/info")
    Observable<RemoteJson> getPatientInfo();

    //修改患者个人信息
    @FormUrlEncoded
    @PUT("v1/patient/info")
    Observable<RemoteJson> putSubmitPatientInfo(@FieldMap Map<String, String> map);

    //我的督导员们
    @GET("v1/patient/directors")
    Observable<RemoteJson> getDirectorList();

    //获取药物详情
    @GET("v1/patient/medicine")
    Observable<RemoteJson> getDrugDetail();

    //修改吃药时间
    @FormUrlEncoded
    @POST("v1/patient/medicine/change_time")
    Observable<RemoteJson> postUpdateTime(@Field("time") String time);

    //获取服药记录 按月份
    @GET("v1/patient/medicine/medicine_log")
    Observable<RemoteJson> getDrugRecord(@Query("begin_date") String start, @Query("end_date") String end);

    //获取复诊事件
    @GET("v1/patient/medicine/medicine_log")
    Observable<List<ReturnVisitBean>> geReturnVisit();

    //提交意见
    @FormUrlEncoded
    @POST("v1/patient/feedback")
    Observable<RemoteJson> postSubmitSuggestion(@Field("content") String content, @Field("contact_information") String phone);

    //判断设备是否在线
    @GET("v1/patient/device")
    Observable<RemoteJson> getBoxIsOnline();

    //随访详情
    @GET("v1/patient/visit")
    Observable<RemoteJson> getVisitBean();

    //复诊事件
    @GET("v1/patient/subsequent_visit")
    Observable<RemoteJson> getReturnVisit();

    //确认电话访视事件
    @FormUrlEncoded
    @POST("v1/patient/visit")
    Observable<RemoteJson> TelephoneVisit(@Field("id") int id, @Field("actually_time") String time);

    //解除绑定
    @POST("v1/app_bound_device/remove")
    Observable<RemoteJson> removeBinding();

    @GET("apps/18/download")
    Call<ResponseBody> getFile();

    //push id
    @FormUrlEncoded
    @POST("v1/patient/app/jpush_id")
    Observable<RemoteJson> pushID(@Field("push_id")String id,
                                  @Field("device_type")String android);
}
