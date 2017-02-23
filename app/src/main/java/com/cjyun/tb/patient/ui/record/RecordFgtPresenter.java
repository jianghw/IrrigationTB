package com.cjyun.tb.patient.ui.record;

import android.support.annotation.NonNull;

import com.cjyun.tb.patient.bean.CurRecordBean;
import com.cjyun.tb.patient.bean.PrevRecordBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 记录页
 */
public class RecordFgtPresenter implements IRecordContract.IRecordPresenter {
    private final IDataSource mRepository;
    private final IRecordContract.IRecordView mFragment;
    private final CompositeSubscription mSubscriptions;
    private boolean mFirst = true;

    public RecordFgtPresenter(@NonNull IDataSource dataRepository,
                              @NonNull IRecordContract.IRecordView recordFragment) {
        mRepository = dataRepository;
        mFragment = recordFragment;
        mSubscriptions = new CompositeSubscription();
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

    @Override
    public void loadingRemoteData(boolean isUpdate) {
        loadingRemoteData(isUpdate, true);
    }

    private void loadingRemoteData(boolean isUpdate, boolean isLoading) {
        if (isLoading) {//加载中...
            mFragment.setLoadingIndicator(true);
        }
        if (isUpdate) {//刷新
            mRepository.refreshTasks();
        }
        Subscription subscription = mRepository.getBeanByList(CurRecordBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CurRecordBean>>() {
                    @Override
                    public void onCompleted() {
                        mFragment.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.getMessage());
                        mFragment.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(List<CurRecordBean> bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.onSucceedLoadCur(bean);
                    }
                });
        Subscription subscription2 = mRepository.getBeanByList(PrevRecordBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PrevRecordBean>>() {
                    @Override
                    public void onCompleted() {
                        mFragment.setLoadingIndicator(false);
                        mFirst=false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFragment.setLoadingIndicator(false);
                        KLog.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<PrevRecordBean> bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.onSucceedLoadPrev(bean);
                    }
                });
        mSubscriptions.add(subscription);
        mSubscriptions.add(subscription2);
    }
}
