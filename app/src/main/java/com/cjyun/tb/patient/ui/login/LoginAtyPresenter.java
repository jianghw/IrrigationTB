package com.cjyun.tb.patient.ui.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.BindBoxBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.data.IDataSource;
import com.google.gson.Gson;
import com.socks.library.KLog;

import cn.collectcloud.password.scribe.model.Token;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class LoginAtyPresenter implements LoginContract.ILoginPresenter {


    private final CompositeSubscription mSubscriptions;
    private final LoginContract.ILoginView mActivity;
    private final IDataSource mDataRepository;

    public LoginAtyPresenter(@NonNull IDataSource dataRepository,
                             @NonNull LoginContract.ILoginView activity) {
        mDataRepository = dataRepository;
        mActivity = activity;
        mSubscriptions = new CompositeSubscription();
    }

    /**
     * 登陆操作
     */
    @Override
    public void remindToSign() {
        String userName = mActivity.getUserName();
        String userPassword = mActivity.getUserPassword();
        if (TextUtils.isEmpty(userName)) {
            mActivity.showToastMessage(R.string.login_fgt_no_name);
        } else if (TextUtils.isEmpty(userPassword) || userPassword.length() < 6) {
            mActivity.showToastMessage(R.string.login_fgt_no_password);
        } else {
            verifyUserLogin(userName, userPassword);
        }
    }


    @Override
    public void verifyUserLogin(String name, String password) {
        Subscription subscription = mDataRepository.onVerifyUserLogin(name, password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mActivity.loadingDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Token>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.getMessage());
                        mActivity.dismissDialog();
                        //TODO
                        mActivity.showToastMessage(R.string.login_su_error);
                        //                        mActivity.showToastMessage(100);
                    }

                    @Override
                    public void onNext(Token token) {
                        if (TbApp.instance().getCUR_USER().equals(TbGlobal.JString.PATIENT_USER)) {//患者
                            onBindBoxInfo();
                        } else {
                            mActivity.dismissDialog();
                            mActivity.onToNewActivity(TbGlobal.IntHandler.HANDLER_FGT_LOGIN_DIRECTOR);
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onBindBoxInfo() {
        Subscription subscription = mDataRepository.bindBoxInfo(BindBoxBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BindBoxBean>() {
                    @Override
                    public void onCompleted() {
                        mActivity.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //HTTP 400 Bad Request
                        KLog.e(e.getMessage());
                        mActivity.dismissDialog();
                        if (e.getMessage().contains("Bad Request")) {
                            mActivity.showToastMessage(R.string.login_error);
                        } else if (e.getMessage().equals(TbApp.getAppResource().getString(R.string.response_null))) {
                            mActivity.onToNewActivity(TbGlobal.IntHandler.HANDLER_FGT_LOGIN_BOX);
                        } else {
                            mActivity.onToNewActivity(TbGlobal.IntHandler.HANDLER_FGT_LOGIN_PATIENT);
                        }
                    }

                    @Override
                    public void onNext(BindBoxBean bean) {
                        KLog.json(new Gson().toJson(bean));
                        if (null != bean && bean.getDevice_uid() != null) {//跳资料完善
                            mActivity.onToNewActivity(TbGlobal.IntHandler.HANDLER_FGT_LOGIN_PATIENT);
                        } else {//绑定药盒
                            mActivity.onToNewActivity(TbGlobal.IntHandler.HANDLER_FGT_LOGIN_BOX);
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }
}
