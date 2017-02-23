package com.cjyun.tb.supervisor.meInterface;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.cjyun.tb.patient.util.SharedUtils;
import com.cjyun.tb.supervisor.bean.MeInfoBean;
import com.cjyun.tb.supervisor.bean.SuCodeBean;
import com.cjyun.tb.supervisor.config.Constants;
import com.cjyun.tb.supervisor.data.SuDataSource;
import com.cjyun.tb.supervisor.fragment.SuMeFragment;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/11 0011.
 * <p>
 * 我的个人信息界面
 */
public class SuBaseInfoPresenter implements MeNews.SuBaseInfoPresenter {


    private final SuDataSource mDataRepository;
    private final SuMeFragment mFragment;

    public SuBaseInfoPresenter(@NonNull SuDataSource dataRepository,
                               @NonNull SuMeFragment fragment) {
        mDataRepository = dataRepository;
        mFragment = fragment;
    }


    @Override
    public void onLoadSuInfo() {

        mFragment.setLoadingIndicator(true);
        Observable<MeInfoBean> observable = mDataRepository.onLoadMeInfo();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MeInfoBean>() {
                    @Override
                    public void onCompleted() {
                        mFragment.setLoadingIndicator(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e + "----督导员个人信息---加载错误--------");
                        String string = SharedUtils.getString(Constants.SQL.MI, null);
                        MeInfoBean meInfoBean = new Gson().fromJson(string, MeInfoBean.class);

                        mFragment.getInfo(meInfoBean);
                        mFragment.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(MeInfoBean meInfoBean) {

                        mFragment.getInfo(meInfoBean);
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Date verifyDate = new Date(System.currentTimeMillis());

        // TODO 上传对应的数据操作  二维码操作
        //  onVerifyVisit( id, visit_id,  verifyDate);
    }

    public void onVerifyVisit(int id, int visit_id, Date verifyDate) {
        Observable<SuCodeBean> observable = mDataRepository.onVerifyVisit(id, visit_id, verifyDate);
        observable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<SuCodeBean>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("访视确认成功" + "----------------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d("访视确认失败" + "----------------");
                    }

                    @Override
                    public void onNext(SuCodeBean suCodeBean) {
                        KLog.d("访视确认成功" + "----------------");
                    }
                });
    }
}
