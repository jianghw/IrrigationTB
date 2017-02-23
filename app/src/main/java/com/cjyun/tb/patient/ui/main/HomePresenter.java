package com.cjyun.tb.patient.ui.main;

import android.support.annotation.NonNull;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.BindBoxBean;
import com.cjyun.tb.patient.bean.CodeBean;
import com.cjyun.tb.patient.bean.DrugDetailBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.cjyun.tb.patient.ui.homepage.IHomepageContract;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 *
 */
public class HomePresenter implements IHomepageContract.IHomepagePresenter {

    private final IDataSource mRepository;
    private final IHomepageContract.IHomepageView mFragment;
    private final CompositeSubscription mSubscriptions;
    private boolean mFirstLoad = true;

    public HomePresenter(@NonNull IDataSource dataRepository,
                         @NonNull IHomepageContract.IHomepageView homeMedicFragment) {
        mRepository = dataRepository;
        mFragment = homeMedicFragment;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void onSubscribe() {
        mSubscriptions.clear();
        if (mFirstLoad) loadingRemoteData(false);
        if (mFirstLoad) pushID();
        BoxIsOnline();
        countdownTime();
    }

    private void pushID() {
        Subscription subscription = mRepository.pushID(mFragment.getPushId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CodeBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.getMessage());
                    }

                    @Override
                    public void onNext(CodeBean bean) {
                        KLog.json(new Gson().toJson(bean));
                    }
                });
        mSubscriptions.add(subscription);
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFragment.setLoadingIndicator(false);
                        KLog.e(e.getMessage());
                    }

                    @Override
                    public void onNext(DrugDetailBean bean) {
                        KLog.json(new Gson().toJson(bean));
                        mFragment.updateListData(bean.getMedicine_names());
                        mFragment.updateDrugTime(bean.getMedicine_time());
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void BoxIsOnline() {
        Subscription subscription = Observable.interval(10, 5 * 60 * 1000, TimeUnit.MILLISECONDS)
                .flatMap(new Func1<Long, Observable<BindBoxBean>>() {
                    @Override
                    public Observable<BindBoxBean> call(Long aLong) {
                        return mRepository.getBoxIsOnline(BindBoxBean.class);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BindBoxBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BindBoxBean bean) {
                        mFragment.updateOnline(bean);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void countdownTime() {
        Subscription subscription = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Long, Observable<String>>() {
                    @Override
                    public Observable<String> call(Long aLong) {
                        String s = mFragment.getDrugTime();//07:10
                        if (!s.equals(TbApp.getAppResource().getString(R.string.no_time))) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd/HH:mm/ss", Locale.SIMPLIFIED_CHINESE);
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DATE, 1);
                            String oldTomorrow = sdf.format(calendar.getTime());
                            String[] t = oldTomorrow.split("/");
                            String newTomorrow = t[0] + "/" + s + "/" + "00";
                            long chart = 0;

                            try {
                                String now = sdf.format(new Date());
                                long nowTime = sdf.parse(now).getTime();
                                String[] nowT = now.split("/");
                                String setT = nowT[0] + "/" + s + "/" + "00";
                                long setTime = sdf.parse(setT).getTime();
                                if (nowTime <= setTime) {
                                    chart = setTime - nowTime;
                                } else {
                                    long tomorrow = sdf.parse(newTomorrow).getTime();
                                    long today = new Date().getTime();
                                    chart = tomorrow - today;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            long day = ((chart / 1000) / (3600 * 24));
                            long hour = ((chart / 1000) - day * 86400) / 3600;
                            long minutes = ((chart / 1000) - day * 86400 - hour * 3600) / 60;
                            long seconds = (chart / 1000) - day * 86400 - hour * 3600 - minutes * 60;
                            String sHour = changLongToString(hour);
                            String sMinutes = changLongToString(minutes);
                            String sSeconds = changLongToString(seconds);
                            return Observable.just(sHour + ":" + sMinutes + ":" + sSeconds);
                        }
                        return Observable.just(TbApp.getAppResource().getString(R.string.no_down_time));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mFragment.updateDownTime(s);
                    }

                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        mSubscriptions.add(subscription);
    }

    private String changLongToString(long hour) {
        String sHour;
        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }
        return sHour;
    }
}
