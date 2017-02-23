package com.cjyun.tb.patient.ui.general;

import android.text.TextUtils;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.bean.CodeBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.cjyun.tb.patient.util.NullExUtils;
import com.socks.library.KLog;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/5/19 0019</br>
 * description:</br>
 */
public class SuggestionPresenter implements IGeneralContract.ISuggestionPresenter {
    private final IDataSource mDataRepository;
    private final IGeneralContract.ISuggestionView mFragment;
    private final CompositeSubscription mSubscriptions;

    public SuggestionPresenter(IDataSource dataRepository,
                               IGeneralContract.ISuggestionView fragment) {
        this.mDataRepository = NullExUtils.checkNotNull(dataRepository, "Repository cannot be null");
        this.mFragment = NullExUtils.checkNotNull(fragment, "View cannot be null!");
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
    public void submitSuggestion() {
        String content = mFragment.getContent();
        String phone = mFragment.getPhone();
        if (TextUtils.isEmpty(content)) {
            mFragment.showToastMessage(R.string.suggestion_toast_content);
        } else if (TextUtils.isEmpty(phone)) {
            mFragment.showToastMessage(R.string.suggestion_toast_phone);
        } else {
            submitSuggestion(content, phone);
        }

    }

    private void submitSuggestion(String content, String phone) {
        Subscription subscription = mDataRepository.submitSuggestion(content, phone)
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
                        KLog.e(e.getMessage());
                        mFragment.dismissDialog();
                        mFragment.showToastMessage(R.string.submit_error);
                    }

                    @Override
                    public void onNext(CodeBean bean) {
                        mFragment.showToastMessage(R.string.submit_ok);
                    }
                });
        mSubscriptions.add(subscription);
    }
}
