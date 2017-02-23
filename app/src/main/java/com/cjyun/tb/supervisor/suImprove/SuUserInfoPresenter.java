package com.cjyun.tb.supervisor.suImprove;

import android.support.annotation.NonNull;

import com.cjyun.tb.patient.util.SharedUtils;
import com.cjyun.tb.supervisor.bean.MeInfoBean;
import com.cjyun.tb.supervisor.bean.SuCodeBean;
import com.cjyun.tb.supervisor.config.Constants;
import com.cjyun.tb.supervisor.data.SuDataSource;
import com.google.gson.Gson;
import com.socks.library.KLog;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/22 0022<br/>
 */
public class SuUserInfoPresenter implements SuImproveInfo.SuImptovePresenter {
    private final SuDataSource mDataRepository;
    private final SuImproveInfo.SuImproveView mFragment;
    //private boolean mFirstLoad = tue;//第一次加载时
    private boolean mFirstLoad;//第一次加载时

    public SuUserInfoPresenter(@NonNull SuDataSource dataRepository,
                               @NonNull SuImproveInfo.SuImproveView fragment) {
        mDataRepository = dataRepository;
        mFragment = fragment;
    }


   /* private void loadingRemoteData(boolean isUpdate) {
        loadingRemoteData(isUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadingRemoteData(boolean isUpdate, boolean isLoading) {
        if (isLoading) {//加载中...
            //  mFragment.setLoadingIndicator(true);
        }
        if (isUpdate) {
            //  mDataRepository.refreshTasks();
        }
    }*/

    @Override
    public void onInitStart() {

        mFragment.refresh(true);

        Observable<MeInfoBean> observable = mDataRepository.onLoadMeInfo();
        observable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<MeInfoBean>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("数据 加载成功------------");

                        mFragment.refresh(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e + "数据 加载  失败------------");
                        Observable<String> just = Observable.just(com.cjyun.tb.supervisor.util.SharedUtils.getString((Constants.SQL.MI), null));
                        just.subscribeOn(Schedulers.io()).
                                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                KLog.d(e + "-----随访界面----------数据库读取失败--------");

                                mFragment.refresh(false);
                            }

                            @Override
                            public void onNext(String s) {
                                KLog.d("-----随访界面----------数据库读取成功--------");
                                // 解析List数据集的bean
                                MeInfoBean meInfoBean = new Gson().fromJson(s, MeInfoBean.class);
                                mFragment.getInfo(meInfoBean);
                            }
                        });

                        mFragment.refresh(false);
                    }

                    @Override
                    public void onNext(MeInfoBean meInfoBean) {
                        KLog.d("数据 加载成功------------");

                        String s = new Gson().toJson(meInfoBean);
                        mFragment.getInfo(meInfoBean);
                        SharedUtils.setString(Constants.SQL.MI, s);
                    }
                });

    }

    @Override
    public void loadingRemoteData() {

    }

    public void loadingRemoteData(String phone, String Weixin,
                                  String qq, String email, String address,String imager) {

        Observable<SuCodeBean> observable = mDataRepository.onImproveInfo(phone, Weixin, qq, email, address, imager);
        observable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<SuCodeBean>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("数据提交成功-------------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e + "-----------数据提交失败");
                    }

                    @Override
                    public void onNext(SuCodeBean suCodeBean) {
                        KLog.d(suCodeBean.getCode() + "数据提交成功------------");
                    }
                });
    }
}
