package com.cjyun.tb.patient.ui.homepage;

import com.cjyun.tb.patient.bean.CodeBean;
import com.cjyun.tb.patient.bean.DrugDetailBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.google.gson.Gson;
import com.socks.library.KLog;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 药物详情页面
 */
public class DrugFgtPresenter implements IHomepageContract.IDrugPresenter {
    private final IDataSource mRepository;
    private final IHomepageContract.IDrugView mFragment;
    private final CompositeSubscription mSubscriptions;
    private boolean mFirst = true;

    public DrugFgtPresenter(IDataSource dataRepository, IHomepageContract.IDrugView drugFragment) {
        mRepository = dataRepository;
        mFragment = drugFragment;
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
        Subscription subscription = mRepository.getBeanBySimple(DrugDetailBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DrugDetailBean>() {
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
                    public void onNext(DrugDetailBean bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.updateListData(bean);
                    }
                });
        mSubscriptions.add(subscription);
    }

    public void updateTime(final String time) {
        Subscription subscription = mRepository.updateTime(time+":00")
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
                        mFragment.updateTimeSucceed(time);
                    }
                });
        mSubscriptions.add(subscription);
    }
}
