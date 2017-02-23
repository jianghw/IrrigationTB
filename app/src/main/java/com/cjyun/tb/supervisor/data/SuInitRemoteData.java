package com.cjyun.tb.supervisor.data;

import android.util.Log;

import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.TokenBean;
import com.cjyun.tb.patient.data.remote.Jhw12320Api;
import com.cjyun.tb.supervisor.base.ConstantAll;
import com.cjyun.tb.supervisor.bean.MeInfoBean;
import com.cjyun.tb.supervisor.bean.PatitentDetailsBean;
import com.cjyun.tb.supervisor.bean.SuCodeBean;
import com.cjyun.tb.supervisor.bean.SubsequentVisitBean;
import com.cjyun.tb.supervisor.bean.TackPillsBean;
import com.cjyun.tb.supervisor.bean.UserEntityBean;
import com.cjyun.tb.supervisor.bean.VisitDetailsBean;
import com.cjyun.tb.supervisor.constant.TbGlobal;
import com.cjyun.tb.supervisor.suRetrofit.SuRequestService;
import com.cjyun.tb.supervisor.util.SharedUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.collectcloud.password.scribe.builder.ServiceBuilder;
import cn.collectcloud.password.scribe.model.Token;
import cn.collectcloud.password.scribe.model.Verifier;
import cn.collectcloud.password.scribe.oauth.OAuthService;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Administrator on 2016/5/11 0011.
 * <p/>
 * 网络数据的加载
 */
public class SuInitRemoteData {

    private final OkHttpClient client;
    private static SuInitRemoteData INSTANCE;
    private static final long DEFAULT_TIME = 15;


    public SuInitRemoteData() {

        //OkHttpClient并设置超时时间
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("LOG", message); // 打印请求的Log
                    }
                });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // 拦截
                .retryOnConnectionFailure(true)// 失败重新连接
                .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS) // 连接的时间
                .addInterceptor(mTokenInterceptor)
                .authenticator(mAuthenticator) // 认证
                .build();
    }


    /**
     * 设置自动登录，Token 里面进行封装
     */
  /*  private OAuthService mService = new ServiceBuilder()
            .provider(Cdc12320Api.class)
            .apiKey(TbGlobal.SUrl.API_KEY)
            .apiSecret(TbGlobal.SUrl.API_SECRET)
            .scope(TbGlobal.SUrl.API_SCOPES)
            .build();*/

    /**
     * 设置重新连接
     */
    Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            KLog.d("mTokenInterceptor==" + TbApp.instance().getCUR_TOKEN());
            if (TbApp.instance().getCUR_TOKEN() == null) {
                return chain.proceed(originalRequest);
            }
            Request authorised = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + TbApp.instance().getCUR_TOKEN())
                    .build();
            return chain.proceed(authorised);
        }
    };

    /**
     * 自动登录封装
     */
    Authenticator mAuthenticator = new Authenticator() {
        @Override
        public Request authenticate(Route route, Response response)
                throws IOException {
            Verifier refreshTokenVer = new Verifier(SharedUtils.getString("refresh_token_" + TbApp.instance().getCUR_USER(), ""));
            Token token = null;
            try {
                token = mService.refreshAccessToken(null, refreshTokenVer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (token != null) {
                TokenBean tokenBean = new Gson().fromJson(token.getRawResponse(), TokenBean.class);
                KLog.d("mAuthenticator==" + token.getToken());
                TbApp.instance().setCUR_TOKEN(token.getToken());
                SharedUtils.setString("access_token_" + TbApp.instance().getCUR_USER(), token.getToken());
                SharedUtils.setString("refresh_token_" + TbApp.instance().getCUR_USER(), tokenBean.getRefresh_token());
            }
            return response.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + TbApp.instance().getCUR_TOKEN())
                    .build();
        }
    };
    /*-------------------------------------------------------*/

    private OAuthService mService = new ServiceBuilder()
            .provider(Jhw12320Api.class)
            .apiKey(TbGlobal.SUrl.API_KEY)
            .apiSecret(TbGlobal.SUrl.API_SECRET)
                    //.callback("https://tb.ccd12320.com/callback")
            .scope(TbGlobal.SUrl.API_SCOPES)
            .build();
/*-----------------------------------------------------------*/

    /**
     * 对请求进行封装
     *
     * @return
     */
    public SuRequestService getDefaultServer(String url) {

        //手动创建一个OkHttpClient并设置超时时间
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(ConstantAll.ConUrl.URL_BASE_CCD)// Base Url
                .baseUrl(url)// Base Url
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(SuRequestService.class);

    }

    /**
     * 创建实例对象
     *
     * @return
     */
    public static SuInitRemoteData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SuInitRemoteData();
        }
        return INSTANCE;
    }

    /**
     * 极光推送
     * post 激光推送的Key
     *
     * @param jpushId
     */
    public Observable<SuCodeBean> onPostJpushID(String jpushId) {


        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).
                postJpushInfo(jpushId,ConstantAll.ConUrl.DEVICE_TYPE);
    }

    /**
     * 获取所有的患者信息
     *
     * @return
     */
    public Observable<UserEntityBean> onLoadAllPatient() {

        KLog.d("开始读取-----------------");


        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).getAllPatientInfo();
    }

    /**
     * 获取患者的详细信息
     *
     * @param id
     */
    public Observable<PatitentDetailsBean> onPatitentDetails(int id) {

        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).getPatientDetailsInfo(id);
    }

    /**
     * 获取患者的服药统计
     *
     * @param id
     */
    public Observable<List<TackPillsBean>> onTackPillsCycle(int id) {

        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).getTackPillsInfo(id);
    }

    /**
     * 复诊时间
     *
     * @param id
     */
    public Observable<List<SubsequentVisitBean>> onSubsequebtVisit(int id) {

        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).getSubsequentVisitInfo(id);
    }

    /**
     * 获取复诊的详情
     *
     * @param id
     */
    public Observable<List<VisitDetailsBean>> onVisitDetails(int id) {

        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).getVisitInfo(id);
    }

    /**
     * 添加访问事件
     *
     * @param id
     */
    public Observable<SuCodeBean> onAddVisit(int id, Date date, String type, String content) {

        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).postAddVisitInfo(id, date, type, content);

    }

    /**
     * 督导员的个人信息
     *
     * @return
     */
    public Observable<MeInfoBean> onLoadMeInfo() {
        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).getLoadMeInfo();
    }

    /**
     * 修改密码
     *
     * @param current_password
     * @param password
     * @param password_confirmation
     * @param user_type
     * @return
     */
    public Observable<SuCodeBean> onResetPassword(String current_password, String password,
                                                  String password_confirmation, String user_type) {

        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).postResetPassword(current_password, password,
                password_confirmation, user_type);
    }

    /**
     * 修改详细资料
     *
     * @param phone_number
     * @param weixin_id
     * @param qq
     * @param email
     * @param address
     * @param photo
     * @return
     */
    public Observable<SuCodeBean> onImproveInfo(String phone_number, String weixin_id, String qq,
                                                String email, String address, String photo) {

        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).postImproveInfo(phone_number, weixin_id,
                qq, email, address, photo);
    }

    public Observable<SuCodeBean> onVerifyVisit(int id, int visit_id, Date VerifyDate) {

        return getDefaultServer(ConstantAll.ConUrl.URL_BASE_TB).postVerifyVisit(id, visit_id, VerifyDate);
    }
}
