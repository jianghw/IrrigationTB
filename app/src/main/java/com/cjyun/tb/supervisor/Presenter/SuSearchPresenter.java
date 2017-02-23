package com.cjyun.tb.supervisor.Presenter;

import android.support.annotation.NonNull;

import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.bean.DB_Bean;
import com.cjyun.tb.supervisor.bean.SuCodeBean;
import com.cjyun.tb.supervisor.bean.UserEntityBean;
import com.cjyun.tb.supervisor.data.SuDataSource;
import com.cjyun.tb.supervisor.data.local.SuPatientNewsDao;
import com.cjyun.tb.supervisor.fragment.SearchFragment;
import com.cjyun.tb.supervisor.util.ToastUtils;
import com.socks.library.KLog;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/10 0010.
 */
public class SuSearchPresenter implements Contract.SearchPresenter {


    private final SuDataSource mDataRepository;
    private final SearchFragment mFragment;
    private SuPatientNewsDao dao;
    // private Boolean isLoad = true;
    private boolean mFirst = true;


    public SuSearchPresenter(@NonNull SuDataSource dataRepository,
                             @NonNull SearchFragment fragment) {
        mDataRepository = dataRepository;
        mFragment = fragment;
        //Observable 中持有Content 对象容易发生泄漏

    }

    public void onSubscribe() {
        if (mFirst) loadingRemoteData(false);
    }

    public void loadingRemoteData(boolean isUpdate) {
        onLoadAllPatient(true, isUpdate);
    }

    // 获取所有患者的数据
    public void onLoadAllPatient(boolean isLoading, boolean isUpdate) {

        if (isLoading) {//加载中...
            mFragment.setLoadingIndicator(true);
        }

        if (isUpdate) {//刷新
            return;
        }
        // 加载不成功，或者第一次加载的时候
        Observable<UserEntityBean> listObservable = mDataRepository.onLoadAllPatient();
        listObservable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<UserEntityBean>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("----加载成功-----");
                        //  isLoad = false;
                        mFragment.setLoadingIndicator(false);
                        mFirst = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e + "----数据加载失败-------");
                        ToastUtils.showMessage("网络数据加载失败，为您加载本地数据");

                        onReasSQLAllPt(null);
                        onReadSQL(null, 1);
                        onReadSQL(null, 3);
                        onReadSQL(null, 6);
                        onReadSQL(null, 12);
                        mFragment.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(UserEntityBean userEntityBeans) {

                        saveData(userEntityBeans);
                    }
                });
    }

    @Override
    public void onPostJpushID(String jpush) {

        KLog.d(jpush+"------提交的id-------");

        Observable<SuCodeBean> observable = mDataRepository.onPostJpushID(jpush);
        observable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<SuCodeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e + "----id--上传失败--------");
                    }

                    @Override
                    public void onNext(SuCodeBean suCodeBean) {
                        KLog.d( "--JPush--id--上传----成功----");
                    }
                });
    }

    // TODO 保存所有患者的数据
    public void saveData(UserEntityBean bean) {

        if (bean == null) {
            return;
        }
        List<UserEntityBean.PhotosEntity> photos = bean.getPhotos();

        if (dao == null)
            dao = new SuPatientNewsDao(mFragment.getActivity());
        dao.truncate();


        for (int i = 0; i < photos.size(); i++) {
            dao.add(photos.get(i).getPatient_id(), photos.get(i).getFull_name(), photos.get(i).getUpdated_at(), photos.get(i).getPhoto());
        }

        onReasSQLAllPt(null);
        onReadSQL(null, 1);
        onReadSQL(null, 3);
        onReadSQL(null, 6);
        onReadSQL(null, 12);
    }


    /**
     * 加载一年以内的
     *
     * @param name
     * @param month
     */
    public void onReadSQL(String name, final int month) {


        Observable<List<DB_Bean>> observable = Observable.just(mDataRepository.getYearPatient(name, month));
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DB_Bean>>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("-------数据库读取成功--------");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<DB_Bean> allSuPatientBeans) {

                        switch (month) {
                            case 1:
                                mFragment.oneMonth(allSuPatientBeans);
                                break;
                            case 3:
                                mFragment.trimester(allSuPatientBeans);
                            case 6:
                                mFragment.sixMonth(allSuPatientBeans);
                            case 12:
                                mFragment.year(allSuPatientBeans);
                        }
                    }
                });
    }


    /**
     * 加载所有的
     *
     * @param name
     */
    public void onReasSQLAllPt(String name) {

        Observable<List<DB_Bean>> observable = Observable.just(mDataRepository.getAllPt(name));
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DB_Bean>>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("-------所有的数据库读取成功--------");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<DB_Bean> allSuPatientBeans) {

                        mFragment.allPatient(allSuPatientBeans);
                    }
                });
    }
}
