package com.cjyun.tb.supervisor.Presenter;

import android.support.annotation.NonNull;

import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.activity.SuMainActivity;
import com.cjyun.tb.supervisor.bean.SuCodeBean;
import com.cjyun.tb.supervisor.data.SuDataSource;
import com.socks.library.KLog;

import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/4 0004.
 */
public class SuCapturePresenter implements Contract.SuCapturePresenter {

    private final SuMainActivity mActivity;
    private final SuDataSource mDataRepository;

    public SuCapturePresenter(@NonNull SuDataSource dataRepository,
                              @NonNull SuMainActivity fragment) {
        mDataRepository = dataRepository;
        mActivity = fragment;
    }

    @Override
    public void onActivityResult(int id, int pt_id) {

        KLog.d(id + "-------------" + pt_id);
        Date date = new Date(System.currentTimeMillis());

        onVerifyVisit(pt_id, id, date);
    }


    public void onVerifyVisit(int id, int visit_id, Date verifyDate) {


        KLog.d(id + "--------打印id-----" + visit_id);

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
                        KLog.d(e + "访视确认失败" + "----------------");
                    }

                    @Override
                    public void onNext(SuCodeBean suCodeBean) {
                        KLog.d("访视确认成功" + "----------------");
                    }
                });
    }


}
