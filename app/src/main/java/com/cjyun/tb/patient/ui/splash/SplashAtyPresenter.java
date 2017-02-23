package com.cjyun.tb.patient.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.UpdateBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.data.IDataSource;
import com.cjyun.tb.patient.service.AppUpdateService;
import com.cjyun.tb.patient.util.AppUtils;
import com.cjyun.tb.patient.util.NetUtils;
import com.cjyun.tb.patient.util.SDCardTool;
import com.cjyun.tb.patient.util.SharedUtils;
import com.socks.library.KLog;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 欢迎页面
 */
public class SplashAtyPresenter implements SplashContract.ISplashPresenter {


    private final SplashContract.ISplashView mActivity;
    private final CompositeSubscription mSubscriptions;
    private final IDataSource mDataRepository;

    private boolean mFirstLoad = true;//初始化时网路加载

    public SplashAtyPresenter(@NonNull IDataSource dataRepository,
                              @NonNull SplashContract.ISplashView activity) {
        mDataRepository = dataRepository;
        mActivity = activity;
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void onSubscribe() {
        mSubscriptions.clear();
        onVersionUpdate(false);
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void onVersionUpdate(boolean forceUpdate) {
        if (forceUpdate || mFirstLoad) {
            Subscription subscription = mDataRepository.onVersionUpgrade()
                    .subscribeOn(Schedulers.io())
                    .retry(4)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UpdateBean>() {
                        @Override
                        public void onCompleted() {
                            mFirstLoad = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            KLog.e(e.getMessage());
                            if (e.getMessage().contains("No address associated with hostname")) {//无网络
                                mActivity.showToastMessage(100);
                            }
                        }

                        @Override
                        public void onNext(UpdateBean bean) {
                            try {
                                isToUpgrade(bean);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            mSubscriptions.add(subscription);
        }
    }

    @Override
    public void isToUpgrade(UpdateBean response) throws Exception {
        if (AppUtils.getVerCode() < (response != null ? response.getVersion() : 0)
                && NetUtils.isNetworkAvailable()) {//显示更新框
            String fileFullName;
            //先看外面存储卡是否可用，有空间，有文件时~
            if (SDCardTool.isSDCardEnable()) {
                fileFullName = SDCardTool.getSDCardPath() +
                        (response != null ? response.getFile_name() : null);
                onWriteAPKFile(response, fileFullName);
            } else {//写到手机文件空间
                fileFullName = SDCardTool.getRootDirectoryPath() +
                        (response != null ? response.getFile_name() : null);
                onWriteAPKFile(response, fileFullName);
            }
        } else {
            // 是否自动登陆 隐藏选择按钮
            if (isAutoSignUi()) mActivity.setSelectorBtnGone(true);
        }
    }

    @Override
    public boolean isAutoSignUi() {
        String cur_user = SharedUtils.getString(TbGlobal.JString.CUR_USER, TbGlobal.JString.CUR_USER);
        boolean isGoneUi;
        if (cur_user.equals(TbGlobal.JString.CUR_USER)) {
            isGoneUi = false;
        } else {
            int expires_in = SharedUtils.getInteger("expires_in_" + cur_user, 604801);
            if (cur_user.equals(TbGlobal.JString.PATIENT_USER) && expires_in != 604801) {//患者
                isGoneUi = true;
                onAutomaticLogin(TbGlobal.JString.PATIENT_USER);
            } else if (cur_user.equals(TbGlobal.JString.DIRECTOR_USER) && expires_in != 604801) {
                isGoneUi = true;
                onAutomaticLogin(TbGlobal.JString.DIRECTOR_USER);
            } else {
                isGoneUi = false;
            }
        }
        return isGoneUi;
    }

    /**
     * 自动登陆
     *
     * @param user Can be {@link TbGlobal.JString#PATIENT_USER},
     */
    @Override
    public void onAutomaticLogin(String user) {
        //TODO
        TbApp.instance().setCUR_USER(user);
        TbApp.instance().setCUR_TOKEN(TbApp.instance().getCUR_TOKEN());
       mActivity.showToastMessage(100);
    }

    @Override
    public void onWriteAPKFile(UpdateBean updateBean, String fileFullName) throws Exception {
        if (SDCardTool.isFileExist(fileFullName) &&
                SDCardTool.getFileSize(fileFullName) == updateBean.getSize()) {
            //显示提示框
            mActivity.showDialogToUpdate(updateBean, fileFullName);
        } else {//文件不存在或大小不等时
            SDCardTool.deleteFile(fileFullName);
            Intent mServiceIntent = new Intent(TbApp.instance(), AppUpdateService.class);
            mServiceIntent.setAction("com.cjyun.tb.AppUpdateService");
            Bundle bundle = new Bundle();
            bundle.putString("url", updateBean.getUrl());
            bundle.putString("file", fileFullName);
            mServiceIntent.putExtras(bundle);
            TbApp.instance().startService(mServiceIntent);
        }
    }
}
