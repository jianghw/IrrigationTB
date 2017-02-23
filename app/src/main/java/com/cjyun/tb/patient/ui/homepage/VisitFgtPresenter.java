package com.cjyun.tb.patient.ui.homepage;

import com.cjyun.tb.patient.bean.CodeBean;
import com.cjyun.tb.patient.bean.VisitBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 随访控制器
 */
public class VisitFgtPresenter implements IHomepageContract.IVisitEventPresenter {
    private final IDataSource mRepository;
    private final CompositeSubscription mSubscriptions;
    private final IHomepageContract.IVisitEventView mFragment;
    private boolean mFirst = true;

    public VisitFgtPresenter(IDataSource dataRepository,
                             IHomepageContract.IVisitEventView visitEventFragment) {
        mRepository = dataRepository;
        mFragment = visitEventFragment;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void loadingRemoteData(boolean isUpdate) {
        loadingRemoteData(isUpdate, true);
    }

    @Override
    public void onSubscribe() {
        mSubscriptions.clear();
        if (mFirst) loadingRemoteData(false);
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    private void loadingRemoteData(boolean isUpdate, boolean isLoading) {
        if (isLoading) {//加载中...
            mFragment.setLoadingIndicator(true);
        }
        if (isUpdate) {//刷新
            mRepository.refreshTasks();
        }
        Subscription subscription = mRepository.getBeanByList(VisitBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<VisitBean>>() {
                    @Override
                    public void onCompleted() {
                        mFragment.setLoadingIndicator(false);
                        mFirst = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFragment.setLoadingIndicator(false);
                        KLog.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<VisitBean> bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.onSucceedLoadData(bean);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onTelephoneVisitConfirmation(int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        Subscription subscription = mRepository.TelephoneVisit(id,sdf.format(new Date()))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mFragment.loadingDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CodeBean>() {
                    @Override
                    public void onCompleted() {
                        mFragment.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFragment.dismissDialog();
                        KLog.e(e.getMessage());
                    }

                    @Override
                    public void onNext(CodeBean bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.onSucceedTelephone();
                    }
                });
        mSubscriptions.add(subscription);
    }
}
