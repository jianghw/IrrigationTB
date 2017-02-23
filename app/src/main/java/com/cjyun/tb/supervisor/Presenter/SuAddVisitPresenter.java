package com.cjyun.tb.supervisor.Presenter;

import android.support.annotation.NonNull;

import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.activity.SuAddVisitActivity;
import com.cjyun.tb.supervisor.bean.SuCodeBean;
import com.cjyun.tb.supervisor.data.SuDataSource;
import com.socks.library.KLog;

import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class SuAddVisitPresenter implements Contract.SuVisitDetailsPresenter {


    private final SuDataSource mDataRepository;
    private final SuAddVisitActivity mActivity;

    /**
     * 数据的加载，和视图的持有
     *
     * @param dataRepository
     * @param activity
     */
    public SuAddVisitPresenter(@NonNull SuDataSource dataRepository,
                               @NonNull SuAddVisitActivity activity) {
        mDataRepository = dataRepository;
        mActivity = activity;
    }

    @Override
    public void onAddVisit(String type, int id, Date date) {
        id = 15;
        // TODO  需要传入对应的ID
        if (mActivity.getEdit() == null) {
            return;
        }
        Observable<SuCodeBean> suCodeBeanObservable =
                mDataRepository.onAddVisit(id, date, type, mActivity.getEdit());
        suCodeBeanObservable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<SuCodeBean>() {
                    @Override
                    public void onCompleted() {

                        KLog.d("---------提交成功--------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e + "---------提交失败--------");
                    }

                    @Override
                    public void onNext(SuCodeBean suCodeBean) {

                        KLog.d(suCodeBean.getStatus() + "---------提交成功--------");
                    }
                });
    }
}
