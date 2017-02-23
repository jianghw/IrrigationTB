package com.cjyun.tb.supervisor.Presenter;

import android.support.annotation.NonNull;

import com.cjyun.tb.patient.util.SharedUtils;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.activity.SuPatientNewsActivity;
import com.cjyun.tb.supervisor.bean.PatitentDetailsBean;
import com.cjyun.tb.supervisor.config.Constants;
import com.cjyun.tb.supervisor.data.SuDataSource;
import com.google.gson.Gson;
import com.socks.library.KLog;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/11 0011.
 * <p/>
 * 患者详情Activity 的信息
 */
public class SuPatientNewsPresenter implements Contract.PatientNewsPresenter {

    private final SuDataSource mDataRepository;
    private final SuPatientNewsActivity mActicity;


    public SuPatientNewsPresenter(@NonNull SuDataSource dataRepository,
                                  @NonNull SuPatientNewsActivity activity) {
        mDataRepository = dataRepository;
        mActicity = activity;


    }


    @Override
    public void onLoadPtDetails(final int id) {

        Observable<PatitentDetailsBean> observable = mDataRepository.onPatitentDetails(id);
        observable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<PatitentDetailsBean>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("------患者的详情信息获取成功--------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e + "------患者的详情信息获取失败--------");
                        // 网络出错，读取数据库
                        // Observable<String> just = Observable.just(dao_gson.inQuery((Constants.SQL.PT_DETAILS + id)));
                        Observable<String> just = Observable.just(SharedUtils.getString((Constants.SQL.PT_DETAILS + id), null));
                        just.subscribeOn(Schedulers.io()).
                                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                KLog.d(e + "-----基本信息界面----------数据库读取失败--------");
                            }
                            @Override
                            public void onNext(String s) {
                               KLog.d("-----基本信息界面----------数据库读取成功--------");
                                PatitentDetailsBean patitentDetailsBean = new Gson().fromJson(s, PatitentDetailsBean.class);
                                KLog.d(patitentDetailsBean.getInfo().getId() + "---------------");
                                mActicity.succeed(patitentDetailsBean);
                            }
                        });
                    }
                    @Override
                    public void onNext(PatitentDetailsBean patitentDetailsBean) {

                        mActicity.succeed(patitentDetailsBean);
                        //装换成Json
                        String s = new Gson().toJson(patitentDetailsBean);
                      /*  // 存入数据库
                        String s = new Gson().toJson(patitentDetailsBean);
                        boolean add = dao_gson.add((Constants.SQL.PT_DETAILS + id), s);
                        KLog.d(add + "--------数据库存储---------" + Constants.SQL.PT_DETAILS + id);*/

                        SharedUtils.setString((Constants.SQL.PT_DETAILS + id), s);
                    }
                });
    }

}
