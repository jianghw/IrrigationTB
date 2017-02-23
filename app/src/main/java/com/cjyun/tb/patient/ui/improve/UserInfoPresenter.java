package com.cjyun.tb.patient.ui.improve;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cjyun.tb.patient.bean.CodeBean;
import com.cjyun.tb.patient.bean.InfoBean;
import com.cjyun.tb.patient.bean.PatientBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.cjyun.tb.patient.util.NullExUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.util.LinkedHashMap;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 完善用户资料
 */
public class UserInfoPresenter implements IUserInfoContract.IUserInfoPresenter {


    private final IUserInfoContract.IUserInfoView mFragment;
    private final CompositeSubscription mSubscriptions;
    private final IDataSource mDataRepository;
    private boolean mFirst = true;//是否加载过数据

    public UserInfoPresenter(@NonNull IDataSource dataRepository,
                             @NonNull IUserInfoContract.IUserInfoView fragment) {
        this.mDataRepository = NullExUtils.checkNotNull(dataRepository, "Repository cannot be null");
        this.mFragment = NullExUtils.checkNotNull(fragment, "View cannot be null!");
        mSubscriptions = new CompositeSubscription();
    }


    /*---------------------------生命周期控制------------------------*/
    @Override
    public void onSubscribe() {
        mSubscriptions.clear();
        if (mFirst) loadingRemoteData(false);
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    public void loadingRemoteData(boolean isUpdate) {
        loadingRemoteData(isUpdate, true);
    }

    private void loadingRemoteData(boolean isUpdate, boolean isLoading) {
        if (isLoading) {//加载中...
            mFragment.setLoadingIndicator(true);
        }
        if (isUpdate) {//第一次时
            mDataRepository.refreshTasks();
        }
        Subscription subscription = mDataRepository.getBeanBySimple(PatientBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PatientBean>() {
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
                    public void onNext(PatientBean bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.setBoxId(bean.getDevice() != null ? bean.getDevice().getDevice_uid() : "当前用户没绑定药盒");
                        mFragment.setUserIdentityCard(bean.getUser() != null ? bean.getUser().getId_card_number() : "");
                        InfoBean info = bean.getInfo();
                        if (null != info) {
                            mFragment.setUserBasicInfo(info.getFull_name(), info.getRemarks(), info.isGender() ? "男" : "女");
                            mFragment.setUserPhone(info.getPhone_number() != null ? info.getPhone_number() : "");
                            mFragment.setUserEmail(info.getEmail() != null ? info.getEmail() : "");
                            mFragment.setUserQQ(info.getQq() != null ? info.getQq() : "");
                            mFragment.setUserWeiXi(info.getWeixin_id() != null ? info.getWeixin_id() : "");
                            mFragment.setUserAddress(info.getAddress() != null ? info.getAddress() : "");
                            mFragment.setUserHead(info.getPhoto() != null ? info.getPhoto() : "");
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void submitPatientInfo() {
        final Map<String, String> map = new LinkedHashMap<>();
        map.put("phone_number", mFragment.getUserPhone());
        map.put("weixin_id", mFragment.getUserWeiXi());
        map.put("qq", mFragment.getUserQQ());
        map.put("email", mFragment.getUserEmail());
        map.put("address", mFragment.getUserAddress());
        if (TextUtils.isEmpty(mFragment.getUserPhoto())) {
            InfoBean infoBean = DataSupport.findLast(InfoBean.class);
            map.put("photo", infoBean.getPhoto()!=null?infoBean.getPhoto():"");
        } else {
            map.put("photo", mFragment.getUserPhoto());
        }

        Subscription subscription = mDataRepository.submitPatientInfo(map)
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
                        InfoBean infoBean = DataSupport.findLast(InfoBean.class);
                        infoBean.setPhone_number(map.get("phone_number"));
                        infoBean.setWeixin_id(map.get("weixin_id"));
                        infoBean.setQq(map.get("qq"));
                        infoBean.setEmail(map.get("email"));
                        infoBean.setAddress(map.get("address"));
                        infoBean.setPhoto(map.get("photo"));
                        infoBean.updateAll();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.getMessage());
                        mFragment.dismissDialog();
                        mFragment.submitError(e.getMessage());
                    }

                    @Override
                    public void onNext(CodeBean bean) {
                        mFragment.submitSucceed();
                    }
                });
        mSubscriptions.add(subscription);
    }
}
