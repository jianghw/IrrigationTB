package com.cjyun.tb.patient.ui.homepage;

import com.cjyun.tb.patient.bean.ReturnVisitBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.cjyun.tb.patient.ui.record.IRecordContract;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/5/18 0018</br>
 * description:</br>
 */
public class ReturnVisitPresenter implements IRecordContract.IRecordPresenter {
    private final IDataSource mRepository;
    private final CompositeSubscription mSubscriptions;
    private final IHomepageContract.IReturnVisitView mFragment;
    private boolean mFirst = true;

    ReturnVisitPresenter(IDataSource dataRepository,
                         IHomepageContract.IReturnVisitView returnVisitFgt) {
        mRepository = dataRepository;
        mFragment = returnVisitFgt;
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
        Subscription subscription = mRepository.getBeanByList(ReturnVisitBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ReturnVisitBean>>() {
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
                    public void onNext(List<ReturnVisitBean> bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.updateListData(bean);
                    }
                });
        mSubscriptions.add(subscription);
    }
}
