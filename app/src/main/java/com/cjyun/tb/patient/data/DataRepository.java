package com.cjyun.tb.patient.data;

import android.accounts.NetworkErrorException;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.CodeBean;
import com.cjyun.tb.patient.bean.RemoteJson;
import com.cjyun.tb.patient.bean.TokenBean;
import com.cjyun.tb.patient.bean.UpdateBean;
import com.cjyun.tb.patient.data.local.LocalData;
import com.cjyun.tb.patient.data.remote.RemoteData;
import com.cjyun.tb.patient.util.AppUtils;
import com.cjyun.tb.patient.util.SharedUtils;
import com.cjyun.tb.patient.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.collectcloud.password.scribe.model.Token;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 数据控制台资源库
 */
public class DataRepository implements IDataSource {

    private static DataRepository INSTANCE = null;
    private final RemoteData mRemoteData;
    private final LocalData mLocalData;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested.
     */
    private boolean mCacheIsDirty = false;

    public DataRepository(@NonNull RemoteData remoteData, @NonNull LocalData localData) {
        mRemoteData = remoteData;
        mLocalData = localData;
    }

    public static DataRepository getInstance(RemoteData remoteData,//远程数据
                                             LocalData localData) {//本地数据
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(remoteData, localData);
        }
        return INSTANCE;
    }

    /**
     * 过滤数据 为object对象
     *
     * @param observable
     * @param clazz
     * @param isSave
     * @param <T>
     * @return
     */
    private <T extends DataSupport> Observable<T> extractData(Observable<RemoteJson> observable, final Class<T> clazz, final boolean isSave) {
        return observable
                .flatMap(new Func1<RemoteJson, Observable<T>>() {
                    @Override
                    public Observable<T> call(RemoteJson remoteJson) {
                        if (null == remoteJson) {
                            return Observable.error(new NullPointerException());
                        } else if (remoteJson.getStatus() == 1) {
                            Gson gson = new GsonBuilder()
                                    .serializeNulls()
                                    .excludeFieldsWithoutExposeAnnotation()
                                    .create();
                            T bean = gson.fromJson(remoteJson.getData(), clazz);
                            KLog.json(gson.toJson(bean));
                            if (isSave)
                                mCacheIsDirty = (!mLocalData.beforeInsert(gson.fromJson(remoteJson.getData(), clazz)));
                            AppUtils.getLruCache().put(clazz.getSimpleName(), remoteJson.getData());
                            return Observable.just(bean);
                        } else {//有网出错时
                            return Observable.error(new NetworkErrorException(remoteJson.getMessage()));
                        }
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
                    @Override
                    public Observable<? extends T> call(final Throwable throwable) {
                        new Thread() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                ToastUtils.showMessage(throwable.getMessage());
                                Looper.loop();
                            }
                        }.start();
                        return Observable.just(mLocalData.querySimpleByBean(clazz));
                    }
                });
    }

    /**
     * 过滤数据 为arraylist
     *
     * @param observable
     * @param clazz
     * @param isSave
     * @param <T>
     * @return
     */
    private <T extends DataSupport> Observable<List<T>> extractListData(Observable<RemoteJson> observable, final Class<T> clazz, final boolean isSave) {
        return observable
                .flatMap(new Func1<RemoteJson, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(RemoteJson remoteJson) {
                        if (null == remoteJson) {
                            return Observable.error(new NetworkErrorException(TbApp.getAppResource().getString(R.string.response_null)));
                        } else if (remoteJson.getStatus() == 1) {
                            Gson gson = new GsonBuilder()
                                    .serializeNulls()
                                    .excludeFieldsWithoutExposeAnnotation()
                                    .create();
                            JsonArray array = new JsonParser().parse(remoteJson.getData()).getAsJsonArray();
                            List<T> mList=new ArrayList<>();
                            List<T> list=new ArrayList<>();
                            for(JsonElement jsonElement : array){
                                mList.add(gson.fromJson(jsonElement, clazz));
                                list.add(gson.fromJson(jsonElement, clazz));
                            }
                            KLog.json(mList.toString());
                            if (isSave) mCacheIsDirty = (!mLocalData.insertListBean(mList));
                            AppUtils.getLruCache().put(clazz.getSimpleName(), remoteJson.getData());
                            return Observable.just(list);
                        } else {//有网出错时
                            return Observable.error(new NetworkErrorException(remoteJson.getMessage()));
                        }
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<T>>>() {
                    @Override
                    public Observable<? extends List<T>> call(final Throwable throwable) {
                        new Thread() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                ToastUtils.showMessage(throwable.getMessage());
                                Looper.loop();
                            }
                        }.start();
                        return Observable.just(mLocalData.queryListByBean(clazz));
                    }
                });
    }


    private <T extends DataSupport> Observable<T> extractDataNoError(Observable<RemoteJson> observable, final Class<T> clazz) {
        return observable
                .flatMap(new Func1<RemoteJson, Observable<T>>() {
                    @Override
                    public Observable<T> call(RemoteJson remoteJson) {
                        KLog.json(new Gson().toJson(remoteJson));
                        if (null == remoteJson) {
                            return Observable.error(new NetworkErrorException(TbApp.getAppResource().getString(R.string.response_null)));
                        } else if (remoteJson.getStatus() == 1) {
                            T bean = new Gson().fromJson(remoteJson.getData(), clazz);
                            return Observable.just(bean);
                        } else {//有网出错时
                            return Observable.error(new NetworkErrorException(remoteJson.getMessage()));
                        }
                    }
                });
    }

    /**
     * 标记强迫更新数据
     */
    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    /**
     * 更新app
     *
     * @return UpdateBean
     */
    @Override
    public Observable<UpdateBean> onVersionUpgrade() {
        return extractDataNoError(mRemoteData.onVersionUpgrade(), UpdateBean.class);
    }

    /**
     * 登陆
     *
     * @param name
     * @param password
     * @return Token
     */
    @Override
    public Observable<Token> onVerifyUserLogin(String name, String password) {
        Observable<Token> remoteData = mRemoteData.onVerifyUserLogin(name, password)
                .doOnNext(new Action1<Token>() {
                    @Override
                    public void call(Token token) {
                        //TODO Local
                        KLog.json(new Gson().toJson(token));
                        TokenBean tokenBean = new Gson().fromJson(token.getRawResponse(), TokenBean.class);
                        mLocalData.saveToken(tokenBean);
                    }
                });
        return remoteData;
    }

    /**
     * 药盒信息
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T extends DataSupport> Observable<T> bindBoxInfo(Class<T> clazz) {
        return extractDataNoError(mRemoteData.BindBoxInfo(), clazz);
    }

    /**
     * 是否在线
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T extends DataSupport> Observable<T> getBoxIsOnline(Class<T> clazz) {
        return extractDataNoError(mRemoteData.getBoxIsOnline(), clazz);
    }

    /**
     * 绑定
     *
     * @param boxID
     * @param boxSN
     * @return
     */
    @Override
    public Observable<CodeBean> postFirstStep(final String boxID, final String boxSN) {
        //TODO Local
        SharedUtils.setString("ID", boxID);
        SharedUtils.setString("SN", boxSN);
        return extractDataNoError(mRemoteData.postFirstStep(boxID, boxSN), CodeBean.class);
    }

    @Override
    public Observable<CodeBean> postSecondStep() {
        return extractDataNoError(mRemoteData.postSecondStep(SharedUtils.getString("ID", ""), SharedUtils.getString("SN", "")),CodeBean.class);
    }

    /**
     * 获取结果对象为 object的
     *
     * @return Bean
     */
    @Override
    public <T extends DataSupport> Observable<T> getBeanBySimple(final Class<T> clazz) {
        if (mCacheIsDirty) {//true
            AppUtils.getLruCache().remove(clazz.getSimpleName());
        }
        if (null != AppUtils.getLruCache().get(clazz.getSimpleName())) {
            return Observable.create(new Observable.OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    KLog.a("开始拿取缓存中的");
                    T cache = new Gson().fromJson(AppUtils.getLruCache().get(clazz.getSimpleName()), clazz);
                    subscriber.onNext(cache);
                    subscriber.onCompleted();
                }
            });
        }
        switch (clazz.getSimpleName()) {
            case "PatientBean":
                return extractData(mRemoteData.getPatientInfoBean(), clazz, true);
            case "DrugDetailBean":
                return extractData(mRemoteData.getDrugDetail(), clazz, true);
            case "MyDirectorBean":
                return extractData(mRemoteData.getMyDirectorBean(), clazz, true);
            default:
                return Observable.error(new NullPointerException());
        }
    }

    @Override
    public <T extends DataSupport> Observable<List<T>> getBeanByList(final Class<T> clazz) {
        if (mCacheIsDirty) {//true
            AppUtils.getLruCache().remove(clazz.getSimpleName());
        }
        if (null != AppUtils.getLruCache().get(clazz.getSimpleName())) {
            return Observable.create(new Observable.OnSubscribe<List<T>>() {
                @Override
                public void call(Subscriber<? super List<T>> subscriber) {
                    KLog.a("开始拿取缓存中的");
                    String json = AppUtils.getLruCache().get(clazz.getSimpleName());
                    Gson gson = new GsonBuilder()
                            .serializeNulls()
                            .excludeFieldsWithoutExposeAnnotation()
                            .create();
                    JsonArray array = new JsonParser().parse(json).getAsJsonArray();
                    List<T> mList=new ArrayList<>();
                    for(JsonElement jsonElement : array){
                        mList.add(gson.fromJson(jsonElement, clazz));
                    }
                    subscriber.onNext(mList);
                    subscriber.onCompleted();
                }
            });
        }
        switch (clazz.getSimpleName()) {
            case "VisitBean":
                return extractListData(mRemoteData.getVisitBean(), clazz, true);
            case "ReturnVisitBean":
                return extractListData(mRemoteData.getReturnVisit(), clazz, true);
            case "CurRecordBean":
                Calendar calendar = new GregorianCalendar();
                calendar.set(Calendar.DATE, 1);
                SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
                String startDay = simpleFormate.format(calendar.getTime());
                // 本月的最后一天
                calendar.roll(Calendar.DATE, -1);
                String ultimateDay = simpleFormate.format(calendar.getTime());
                return extractListData(mRemoteData.queryDrugRecord(startDay, ultimateDay), clazz, true);
            case "PrevRecordBean":
                Calendar c = new GregorianCalendar();
                c.add(Calendar.MONTH, -1);
                c.set(Calendar.DAY_OF_MONTH, 1);
                SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
                String start = sfd.format(c.getTime());
                // 本月的最后一天
                c.roll(Calendar.DAY_OF_MONTH, -1);
                String end = sfd.format(c.getTime());
                return extractListData(mRemoteData.queryDrugRecord(start, end), clazz, true);
            default:
                return Observable.error(new NullPointerException());
        }
    }

    /**
     * 修改患者信息
     *
     * @param map
     * @return
     */
    @Override
    public Observable<CodeBean> submitPatientInfo(Map<String, String> map) {
        return extractDataNoError(mRemoteData.submitPatientInfo(map), CodeBean.class);
    }

    @Override
    public Observable<CodeBean> updateTime(String time) {
        return extractDataNoError(mRemoteData.postUpdateTime(time), CodeBean.class);
    }

    @Override
    public Observable<CodeBean> TelephoneVisit(int id, String time) {
        return extractDataNoError(mRemoteData.TelephoneVisit(id,time), CodeBean.class);
    }


    @Override
    public Observable<CodeBean> submitSuggestion(String content, String phone) {
        return extractDataNoError(mRemoteData.postSubmitSuggestion(content, phone),CodeBean.class);
    }

    @Override
    public Observable<CodeBean> submitNewPassword(String old, String Pw) {
        return extractDataNoError(mRemoteData.putNewPassword(old, Pw),CodeBean.class);
    }

    @Override
    public Observable<CodeBean> removeBinding() {
        return extractDataNoError(mRemoteData.removeBinding(),CodeBean.class);
    }

    @Override
    public Observable<CodeBean> pushID(String id) {
        return extractDataNoError(mRemoteData.pushID(id),CodeBean.class);
    }
}
