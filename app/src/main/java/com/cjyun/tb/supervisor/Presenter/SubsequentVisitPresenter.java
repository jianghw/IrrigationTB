package com.cjyun.tb.supervisor.Presenter;

import android.support.annotation.NonNull;

import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.bean.SubsequentVisitBean;
import com.cjyun.tb.supervisor.config.Constants;
import com.cjyun.tb.supervisor.data.SuDataSource;
import com.cjyun.tb.supervisor.fragment.SubsequentVisitFragment;
import com.cjyun.tb.supervisor.util.SharedUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/11 0011.
 * 复诊时间的Presenter
 */
public class SubsequentVisitPresenter implements Contract.SubsequentVisitPresenter {


    private final SuDataSource mDataRepository;
    private final SubsequentVisitFragment mFragment;

    public SubsequentVisitPresenter(@NonNull SuDataSource dataRepository,
                                    @NonNull SubsequentVisitFragment fragment) {
        mDataRepository = dataRepository;
        mFragment = fragment;

    }


    @Override
    public void onSubsequebtVisit(final int id) {

        mFragment.setLoadingIndicator(true);

        Observable<List<SubsequentVisitBean>> listObservable = mDataRepository.onSubsequebtVisit(id);
        listObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SubsequentVisitBean>>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("------患者的-----访问时间-----信息获取成功--------");
                        mFragment.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e + "------患者的-----访问时间-----信息获取失败--------");

                        Observable<String> just = Observable.just(SharedUtils.getString((Constants.SQL.SUBSEQUENT + id), null));
                        just.subscribeOn(Schedulers.io()).
                                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                KLog.d(e + "-----基本信息界面----------数据库读取失败--------");
                                mFragment.setLoadingIndicator(false);
                            }

                            @Override
                            public void onNext(String s) {
                                KLog.d("-----基本信息界面----------数据库读取成功--------");

                                // 解析List数据集的bean
                                List<SubsequentVisitBean> rs = new ArrayList();
                                Type type = new TypeToken<ArrayList<SubsequentVisitBean>>() {}.getType();
                                rs = new Gson().fromJson(s, type);
                                mFragment.svSucceed(rs);
                            }
                        });

                        mFragment.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(List<SubsequentVisitBean> subsequentVisitBeans) {
                        //  TODO  数据访问成功展示
                        mFragment.svSucceed(subsequentVisitBeans);

                        String s = new Gson().toJson(subsequentVisitBeans);
                        SharedUtils.setString((Constants.SQL.SUBSEQUENT + id), s);
                    }
                });
    }

}
