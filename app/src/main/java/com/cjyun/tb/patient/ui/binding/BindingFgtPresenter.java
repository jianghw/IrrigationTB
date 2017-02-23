package com.cjyun.tb.patient.ui.binding;

import android.support.annotation.NonNull;

import com.cjyun.tb.patient.bean.CodeBean;
import com.cjyun.tb.patient.bean.PatientBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.cjyun.tb.patient.util.AppUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/21 0021<br/>
 */
public class BindingFgtPresenter implements AddBoxContract.IBindFgtPresenter {


    private final IDataSource mDataRepository;
    private final AddBoxContract.IBindFgtView mFragment;
    private final CompositeSubscription mSubscriptions;

    public BindingFgtPresenter(@NonNull IDataSource dataRepository,
                               @NonNull AddBoxContract.IBindFgtView bindingFragment) {
        mDataRepository = dataRepository;
        mFragment = bindingFragment;
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void onSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void onBindingSecondStep() {
        startCountdown();
        onBindingPCS();
    }

    private void startCountdown() {
        Subscription subCount = Observable.interval(10, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mFragment.showCountdown(60 - aLong);
                    }
                });
        mSubscriptions.add(subCount);
    }

    private void onBindingPCS() {
        Subscription subscription = mDataRepository.postSecondStep()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mFragment.loadingDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .take(10)
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return Observable.interval(5, TimeUnit.SECONDS);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CodeBean>() {
                    @Override
                    public void onCompleted() {
                        KLog.a("onCompleted");
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
                        AppUtils.getLruCache().remove(PatientBean.class.getSimpleName());
                        mFragment.onSucceedStep();
                    }
                });
        mSubscriptions.add(subscription);
    }
}
