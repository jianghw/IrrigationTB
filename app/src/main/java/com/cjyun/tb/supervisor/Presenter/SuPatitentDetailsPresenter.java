package com.cjyun.tb.supervisor.Presenter;

import android.support.annotation.NonNull;

import com.cjyun.tb.patient.util.SharedUtils;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.bean.PatitentDetailsBean;
import com.cjyun.tb.supervisor.config.Constants;
import com.cjyun.tb.supervisor.data.SuDataSource;
import com.cjyun.tb.supervisor.fragment.SuPatitentDetailsFragment;
import com.google.gson.Gson;
import com.socks.library.KLog;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/11 0011.
 * <p/>
 * 患者详情的presenter
 */
public class SuPatitentDetailsPresenter implements Contract.BaseNewsPresenter {


    private final SuDataSource mDataRepository;
    private final SuPatitentDetailsFragment mFragment;

    /**
     * @param dataRepository 做数据请求，和本地缓存
     * @param fragment
     */
    public SuPatitentDetailsPresenter(@NonNull SuDataSource dataRepository,
                                      @NonNull SuPatitentDetailsFragment fragment) {

        mDataRepository = dataRepository;
        mFragment = fragment;
    }

    @Override
    public void onPatitentDetails(final int id) {

        mFragment.refresh(true);

        // TODO
        Observable<PatitentDetailsBean> patitentDetailsBeanObservable = mDataRepository.onPatitentDetails(id);
        patitentDetailsBeanObservable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<PatitentDetailsBean>() {
                    @Override
                    public void onCompleted() {
                        mFragment.refresh(false);
                        KLog.d("加载数据-------成功----");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 网络加载失败，本地加载
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
                                mFragment.setData(patitentDetailsBean);
                            }
                        });
                        mFragment.refresh(false);
                        KLog.d("加载数据------加载失败-----");
                    }

                    @Override
                    public void onNext(PatitentDetailsBean patitentDetailsBean) {

                        mFragment.setData(patitentDetailsBean);
                    }
                });
    }


}
