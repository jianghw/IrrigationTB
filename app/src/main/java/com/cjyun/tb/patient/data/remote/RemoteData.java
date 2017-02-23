package com.cjyun.tb.patient.data.remote;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.RemoteJson;
import com.cjyun.tb.patient.bean.TokenBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.util.SharedUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.Map;
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
import rx.Subscriber;

/**
 * 网络模块
 */
public class RemoteData {

    private static final long DEFAULT_TIME = 15;
    private OkHttpClient client;

    private RemoteData() {
        //OkHttpClient并设置超时时间
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("LOG", message);
                    }
                });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
                .addInterceptor(mTokenInterceptor)
                .authenticator(mAuthenticator)
                .build();
    }

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

    private RemoteServer getDefaultServer(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(RemoteServer.class);
    }

    private RemoteServer getJhwFactoryServer(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(new JhwConverterFactory<RemoteJson>())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(RemoteServer.class);
    }

    private OAuthService mService = new ServiceBuilder()
            .provider(Jhw12320Api.class)
            .apiKey(TbGlobal.SUrl.API_KEY)
            .apiSecret(TbGlobal.SUrl.API_SECRET)
            //            .callback("https://tb.ccd12320.com/callback")
            .scope(TbGlobal.SUrl.API_SCOPES)
            .build();

    public Observable<RemoteJson> onVersionUpgrade() {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_CCD).getAppUpdateInfo();
    }

    public Observable<Token> onVerifyUserLogin(final String name, final String password) {
        return Observable.create(new Observable.OnSubscribe<Token>() {
            @Override
            public void call(Subscriber<? super Token> subscriber) {
                Token token = null;
                try {
                    token = mService.getAccessToken(name, password);
                } catch (Exception e) {
                    subscriber.onError(new NetworkErrorException(e.getMessage()));
                }
                if (token != null)
                    subscriber.onNext(token);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<RemoteJson> BindBoxInfo() {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).getBindBoxInfo();
    }


    public Observable<RemoteJson> postFirstStep(String boxID, String boxSN) {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).postFirstStep(boxID, boxSN);
    }


    public Observable<RemoteJson> postSecondStep(String boxID, String boxSN) {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).postSecondStep(boxID, boxSN);
    }


    public Observable<RemoteJson> getPatientInfoBean() {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).getPatientInfo();
    }

    public Observable<RemoteJson> submitPatientInfo(Map<String, String> map) {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).putSubmitPatientInfo(map);
    }

    public Observable<RemoteJson> getBoxIsOnline() {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).getBoxIsOnline();
    }

    public Observable<RemoteJson> getDrugDetail() {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).getDrugDetail();
    }

    public Observable<RemoteJson> getVisitBean() {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).getVisitBean();
    }

    public Observable<RemoteJson> getReturnVisit() {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).getReturnVisit();
    }

    public Observable<RemoteJson> postUpdateTime(String time) {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).postUpdateTime(time);
    }

    public Observable<RemoteJson> queryDrugRecord(String start, String end) {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).getDrugRecord(start, end);
    }


    public Observable<RemoteJson> getMyDirectorBean() {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).getDirectorList();
    }

    public Observable<RemoteJson> postSubmitSuggestion(String content, String phone) {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).postSubmitSuggestion(content, phone);
    }

    public Observable<RemoteJson> putNewPassword(String old, String pw) {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).putNewPassword(old, pw, pw, "flb_user");
    }

    public Observable<RemoteJson> TelephoneVisit(int id, String time) {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).TelephoneVisit(id, time);
    }

    public Observable<RemoteJson> removeBinding() {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).removeBinding();
    }

    public Observable<RemoteJson> pushID(String id) {
        return getJhwFactoryServer(TbGlobal.SUrl.URL_BASE_TB).pushID(id, "android");
    }

    private static class SingletonHolder {
        private static final RemoteData INSTANCE = new RemoteData();
    }

    public static RemoteData getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
