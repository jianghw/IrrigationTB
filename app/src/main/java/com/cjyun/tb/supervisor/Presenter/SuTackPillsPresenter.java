package com.cjyun.tb.supervisor.Presenter;

import android.support.annotation.NonNull;

import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.bean.TackPillsBean;
import com.cjyun.tb.supervisor.config.Constants;
import com.cjyun.tb.supervisor.data.SuDataSource;
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
 */
public class SuTackPillsPresenter implements Contract.TackPillsPresenter {

    private final SuDataSource mDataRepository;
    private final Contract.TackPillsView mFragment;
    // TODO


    public SuTackPillsPresenter(@NonNull SuDataSource dataRepository,
                                @NonNull Contract.TackPillsView fragment) {
        mDataRepository = dataRepository;
        mFragment = fragment;
    }

    /**
     * 加载数据
     */
    @Override
    public void onTackPillsCycle(final int id) {

        mFragment.refresh(true);
        KLog.d(id + "----------服药统计id----------");

        Observable<List<TackPillsBean>> listObservable = mDataRepository.onTackPillsCycle(id);
        listObservable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<List<TackPillsBean>>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("------患者的----服药----信息获取成功--------");
                        mFragment.refresh(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e + "------患者的-----服药-----信息获取失败--------");

                        Observable<String> just = Observable.just(SharedUtils.getString((Constants.SQL.TACK_PILLS + id), null));
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

                                // 解析List数据集的bean
                                List<TackPillsBean> rs = new ArrayList();
                                Type type = new TypeToken<ArrayList<TackPillsBean>>() {
                                }.getType();
                                rs = new Gson().fromJson(s, type);
                                mFragment.tackPillsSucceed(rs);
                            }
                        });
                        mFragment.refresh(false);
                    }

                    @Override
                    public void onNext(List<TackPillsBean> tackPillsBeans) {

                        mFragment.tackPillsSucceed(tackPillsBeans);
                        //  mFragment.tackPillsSucceed(tackPillsBeans);
                        KLog.d("------患者的----服药----信息获取成功--------");

                        String s = new Gson().toJson(tackPillsBeans);
                        SharedUtils.setString((Constants.SQL.TACK_PILLS + id), s);

                    }
                });
    }

}
