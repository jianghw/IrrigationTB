package com.cjyun.tb.supervisor.Presenter;

import android.support.annotation.NonNull;

import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.activity.SuAllPatientActivity;
import com.cjyun.tb.supervisor.bean.AllSuPatientBean;
import com.cjyun.tb.supervisor.bean.DB_Bean;
import com.cjyun.tb.supervisor.bean.UserEntityBean;
import com.cjyun.tb.supervisor.data.SuDataSource;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class SuAllPresenter implements Contract.SuAllPresenter {

    private final SuDataSource mDataRepository;
    private final SuAllPatientActivity mActivity;
    private ArrayList<AllSuPatientBean> list;

    /**
     * 数据的加载，和视图的持有
     *
     * @param dataRepository
     * @param activity
     */
    public SuAllPresenter(@NonNull SuDataSource dataRepository,
                          @NonNull SuAllPatientActivity activity) {
        mDataRepository = dataRepository;
        mActivity = activity;
    }

    /**
     * 判断是在哪个界面，查询数据
     *
     * @param name
     * @param month
     * @return
     */
    public List getPtNumber(String name, int month) {

        switch (month) {
            case 1:
                return getPtNumber1(name, 1);
            case 3:
                return getPtNumber1(name, 3);
            case 6:
                return getPtNumber1(name, 6);
            case 12:
                return getPtNumber1(name, 12);
            default:

                return getPtNumber1(name, 12);
        }
    }

    public List<AllSuPatientBean> getPtNumber1(String name, int month) {

        list = new ArrayList<>();
        Observable<List<DB_Bean>> observable = Observable.just(mDataRepository.getYearPatient(name, month));
        observable.subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DB_Bean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<DB_Bean> allSuPatientBeans) {

                        mActivity.succeed(allSuPatientBeans);

                        KLog.d(allSuPatientBeans.get(0).getId()+"--------------------------");
                    }
                });

        return null;
    }

}
