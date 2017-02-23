package com.cjyun.tb.patient.ui.reset;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.bean.CodeBean;
import com.cjyun.tb.patient.data.IDataSource;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/23 0023<br/>
 */
public class ResetPasswordPresenter implements IResetPwContract.IResetFgtPresenter {

    private final IDataSource mDataRepository;
    private final CompositeSubscription mSubscriptions;
    private final IResetPwContract.IResetFgtView mFragment;

    public ResetPasswordPresenter(@NonNull IDataSource dataRepository,
                                  @NonNull IResetPwContract.IResetFgtView fragment) {
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
    public void onVerifyFillContent() {
        String oldPw = mFragment.getUserPhone();
        String newPw = mFragment.getUserPassword();
        if (TextUtils.isEmpty(oldPw) || oldPw.length() < 6) {
            mFragment.showToastMessage(R.string.reset_fgt_no_name);
        } else if (TextUtils.isEmpty(newPw) || newPw.length() < 6) {
            mFragment.showToastMessage(R.string.reset_fgt_no_name);
        } else {
            onSubmitNewPassword(oldPw, newPw);
        }
    }

    @Override
    public void onSubmitNewPassword(String oldPw, String newPw) {
        Subscription subscription = mDataRepository.submitNewPassword(oldPw, newPw)
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
                        if (e.getMessage().contains("error")) {
                            mFragment.showToastMessage(R.string.password_error);
                        }else{
                            mFragment.showToastMessage(R.string.no_know_error);
                        }
                    }

                    @Override
                    public void onNext(CodeBean codeBean) {
                        mFragment.showToastMessage(R.string.submit_ok);
                    }
                });
        mSubscriptions.add(subscription);
    }
}
