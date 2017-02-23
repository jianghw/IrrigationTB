package com.cjyun.tb.patient.ui.general;

import android.support.annotation.NonNull;

import com.cjyun.tb.patient.bean.MyDirectorBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.cjyun.tb.patient.util.NullExUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/5/17 0017</br>
 * description:</br>
 */
public class MyDirectorPresenter implements IGeneralContract.IMyDirectorPresenter {
    private final IDataSource mDataRepository;
    private final IGeneralContract.IMyDirectorView mFragment;
    private final CompositeSubscription mSubscriptions;
    private boolean mFirst = true;

    public MyDirectorPresenter(@NonNull IDataSource dataRepository,
                               @NonNull IGeneralContract.IMyDirectorView myDirectorFragment) {
        this.mDataRepository = NullExUtils.checkNotNull(dataRepository, "Repository cannot be null");
        this.mFragment = NullExUtils.checkNotNull(myDirectorFragment, "View cannot be null!");
        mSubscriptions = new CompositeSubscription();
    }

    private void loadingRemoteData(boolean isUpdate, boolean isLoading) {
        if (isLoading) {//加载中...
            mFragment.setLoadingIndicator(true);
        }
        if (isUpdate) {//刷新
            mDataRepository.refreshTasks();
        }
        Subscription subscription = mDataRepository.getBeanBySimple(MyDirectorBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyDirectorBean>() {
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
                    public void onNext(MyDirectorBean bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.updateListData(bean);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onSubscribe() {
        mSubscriptions.clear();
        if (mFirst) loadingRemoteData(false);
    }

    @Override
    public void loadingRemoteData(boolean isUpdate) {
        loadingRemoteData(isUpdate, true);
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }
}
