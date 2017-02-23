package com.cjyun.tb.supervisor.data;

import android.support.annotation.NonNull;

import com.cjyun.tb.supervisor.bean.DB_Bean;
import com.cjyun.tb.supervisor.bean.MeInfoBean;
import com.cjyun.tb.supervisor.bean.PatitentDetailsBean;
import com.cjyun.tb.supervisor.bean.SuCodeBean;
import com.cjyun.tb.supervisor.bean.SubsequentVisitBean;
import com.cjyun.tb.supervisor.bean.TackPillsBean;
import com.cjyun.tb.supervisor.bean.UserEntityBean;
import com.cjyun.tb.supervisor.bean.VisitDetailsBean;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2016/5/24 0024.
 */
public class SuDataRepository implements SuDataSource {

    private static SuDataRepository INSTANCE;

    private SuInitRemoteData mRemoteData;
    private SuInitLocalData mLocalData;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested.
     * 标志着缓存无效,强迫一个更新下次数据请求。
     */
    private boolean mCacheIsDirty = false;

    public SuDataRepository(@NonNull SuInitRemoteData remoteData, @NonNull SuInitLocalData localData) {
        try {
            mRemoteData = remoteData;
            mLocalData = localData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SuDataRepository getInstance(SuInitRemoteData remoteData,//远程数据
                                               SuInitLocalData localData) {//本地数据
        if (INSTANCE == null) {

            INSTANCE = new SuDataRepository(remoteData, localData);
        }
        return INSTANCE;
    }

    /**
     * 极光推送
     *
     * @param jpushId
     * @return
     */
    public Observable<SuCodeBean> onPostJpushID(String jpushId) {

        return mRemoteData.onPostJpushID(jpushId);
    }

    /**
     * 加载所有患者
     */
    public Observable<UserEntityBean> onLoadAllPatient() {

        return mRemoteData.onLoadAllPatient();
    }

    /**
     * 获取患者的详细信息
     *
     * @param id
     */
    public Observable<PatitentDetailsBean> onPatitentDetails(int id) {

        return mRemoteData.onPatitentDetails(id);
    }

    /**
     * 获取患者的服药统计
     *
     * @param id
     */
    public Observable<List<TackPillsBean>> onTackPillsCycle(int id) {

        return mRemoteData.onTackPillsCycle(id);
    }

    /**
     * 复诊时间
     *
     * @param id
     */
    public Observable<List<SubsequentVisitBean>> onSubsequebtVisit(int id) {

        return mRemoteData.onSubsequebtVisit(id);
    }

    /**
     * 获取访问的详情
     *
     * @param id
     */
    public Observable<List<VisitDetailsBean>> onVisitDetails(int id) {

        return mRemoteData.onVisitDetails(id);
    }

    /**
     * 添加访问事件
     *
     * @param id
     * @param date
     * @param type
     * @param content
     */
    public Observable<SuCodeBean> onAddVisit(int id, Date date, String type, String content) {

        return mRemoteData.onAddVisit(id, date, type, content);
    }

    /**
     * 获取本地数据库近一年内的患者操作
     *
     * @param name
     * @return
     */
    public List<DB_Bean> getYearPatient(String name, int month) {

        return mLocalData.getPatient(name, month);
    }

    /**
     * 获取本地数据库，所有患者操作
     *
     * @param name
     * @return
     */
    @Override
    public List<DB_Bean> getAllPt(String name) {

        return mLocalData.getAll(name);
    }

    /**
     * 加载督导员的个人信息
     *
     * @return
     */
    @Override
    public Observable<MeInfoBean> onLoadMeInfo() {
        return mRemoteData.onLoadMeInfo();
    }



    @Override
    public Observable<SuCodeBean> onImproveInfo(String phone, String weixin_id, String qq, String email, String address, String photo) {
        return mRemoteData.onImproveInfo(phone, weixin_id, qq, email, address, photo);
    }

    public Observable<SuCodeBean> onVerifyVisit(int id,int visit_id,Date date) {

        return mRemoteData.onVerifyVisit(id,visit_id, date);
    }

}
