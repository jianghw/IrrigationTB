package com.cjyun.tb.supervisor.Presenter;

import android.support.annotation.NonNull;

import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.VisitBean;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.bean.VisitDetailsBean;
import com.cjyun.tb.supervisor.config.Constants;
import com.cjyun.tb.supervisor.data.SuDataSource;
import com.cjyun.tb.supervisor.data.local.SuPatientNewsDao_Vt_Contetnt;
import com.cjyun.tb.supervisor.fragment.SuVisitEventFgt_Cp;
import com.cjyun.tb.supervisor.util.SharedUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/11 0011.
 * <p/>
 * 访问事件
 */
public class SuVisitPresenter implements Contract.SuVisitPresenter {


    private final SuDataSource mDataRepository;
    private final SuVisitEventFgt_Cp mFragment;
    private SuPatientNewsDao_Vt_Contetnt dao;


    /**
     * 数据的加载，和视图的持有
     *
     * @param dataRepository
     * @param
     */
    public SuVisitPresenter(@NonNull SuDataSource dataRepository,
                            @NonNull SuVisitEventFgt_Cp fragment) {
        mDataRepository = dataRepository;
        mFragment = fragment;
    }

    @Override
    public void onVisitDetails(final int id) {

        mFragment.setLoadingIndicator(true);

        Observable<List<VisitDetailsBean>> listObservable = mDataRepository.onVisitDetails(id);
        listObservable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<List<VisitDetailsBean>>() {
                    @Override
                    public void onCompleted() {
                        mFragment.setLoadingIndicator(false);
                        KLog.d("------随访--加载成功-----");
                    }

                    @Override
                    public void onError(Throwable e) {

                        Observable<String> just = Observable.just(SharedUtils.getString((Constants.SQL.VISIT + id), null));
                        just.subscribeOn(Schedulers.io()).
                                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                KLog.d(e + "-----随访界面----------数据库读取失败--------");
                            }

                            @Override
                            public void onNext(String s) {
                                KLog.d("-----随访界面----------数据库读取成功--------");

                                // 解析List数据集的bean
                                List<VisitDetailsBean> rs = new ArrayList();
                                Type type = new TypeToken<ArrayList<VisitDetailsBean>>() {
                                }.getType();
                                rs = new Gson().fromJson(s, type);
                                mFragment.onSucceedLoadData(rs);
                            }
                        });

                        mFragment.setLoadingIndicator(false);
                        KLog.d(e + "------随访--加失败-----");
                    }

                    @Override
                    public void onNext(List<VisitDetailsBean> visitDetailsBeans) {
                        //KLog.json(new Gson().toJson(visitDetailsBeans));
                        mFragment.onSucceedLoadData(visitDetailsBeans);

                        String s = new Gson().toJson(visitDetailsBeans);
                        SharedUtils.setString((Constants.SQL.VISIT + id), s);
                    }
                });
    }

    /**
     * 判断有没有访问内容
     *
     * @return
     */
    @Override
    public boolean getVisitContent() {

        return false;
    }

    public void addSQL(String setting_date,String verify,String type, String content) {
        if (dao == null)
            dao = new SuPatientNewsDao_Vt_Contetnt(TbApp.getmContext());
        dao.add(setting_date,verify, type, content);
    }

    public List<VisitBean> querySQL(String setting_date) {

        if (dao == null)
            dao = new SuPatientNewsDao_Vt_Contetnt(TbApp.getmContext());
        List<VisitBean> list = dao.inQuery(setting_date);
        //  dao.truncate();
        return list;
    }

    public void onTruncate() {
        if (dao == null)
            dao = new SuPatientNewsDao_Vt_Contetnt(TbApp.getmContext());
        dao.truncate();
    }
}
