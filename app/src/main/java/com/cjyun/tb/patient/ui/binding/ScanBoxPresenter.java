package com.cjyun.tb.patient.ui.binding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.BindBoxBean;
import com.cjyun.tb.patient.bean.CodeBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.google.gson.Gson;
import com.socks.library.KLog;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class ScanBoxPresenter implements AddBoxContract.IScanBoxPresenter {

    private final AddBoxContract.IScanBoxView mFragment;
    private final CompositeSubscription mSubscriptions;
    private final IDataSource mDataRepository;


    public ScanBoxPresenter(@NonNull IDataSource dataRepository,
                            @NonNull AddBoxContract.IScanBoxView fragment) {
        mDataRepository = dataRepository;
        mFragment = fragment;
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
    public void onBindingFirstStep() {
        String boxID = mFragment.getBoxID();
        String boxSN = mFragment.getBoxSN();
        if (TextUtils.isEmpty(boxID)) {
            mFragment.showToastMessage(R.string.bind_fgt_no_id);
        } else if (TextUtils.isEmpty(boxSN)) {
            mFragment.showToastMessage(R.string.bind_fgt_no_sn);
        } else {
            onFirstStepBindBox(boxID, boxSN);
        }
    }

    private void onFirstStepBindBox(String boxID, String boxSN) {
        Subscription subscription = mDataRepository.postFirstStep(boxID, boxSN)
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
                        String error = e.getMessage();
                        KLog.e(error);
                        if (error.contains("is not correct")) {
                            mFragment.showToastMessage(R.string.bind_fgt_first_error);
                        }
                    }

                    @Override
                    public void onNext(CodeBean bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.onSucceedNextStep();
                    }
                });
        mSubscriptions.add(subscription);
    }

    public void onBindBoxInfo() {
        Subscription subscription = mDataRepository.bindBoxInfo(BindBoxBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BindBoxBean>() {
                    @Override
                    public void onCompleted() {
                        mFragment.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //HTTP 400 Bad Request
                        KLog.e(e.getMessage());
                        if (e.getMessage().equals(TbApp.getAppResource().getString(R.string.response_null))) {
                            onBindingFirstStep();
                        } else {
                            mFragment.dismissDialog();
                        }
                    }

                    @Override
                    public void onNext(BindBoxBean bean) {
                        KLog.json(new Gson().toJson(bean));
                        if (null != bean && bean.getDevice_uid() != null) {
                            mFragment.showRemoveBinding(bean);
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && data != null) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result", "ID:扫描出错~,SN:请重新扫描");
            mFragment.onResultFromQR(scanResult);
        }
    }

    @Override
    public void onRemoveBinding() {
        Subscription subscription = mDataRepository.removeBinding()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CodeBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //HTTP 400 Bad Request
                        KLog.e(e.getMessage());
                        mFragment.showToastMessage(R.string.remove_error);
                    }

                    @Override
                    public void onNext(CodeBean bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.showToastMessage(R.string.remove_bing);
                    }
                });
        mSubscriptions.add(subscription);
    }
}
