package com.cjyun.tb.supervisor.data;

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
 * Created by Administrator on 2016/5/16 0016.
 * <p/>
 * 数据库仓库接口
 */

public interface SuDataSource {


    Observable<SuCodeBean> onPostJpushID(String jpushId);

    Observable<UserEntityBean> onLoadAllPatient();

    Observable<PatitentDetailsBean> onPatitentDetails(int id);

    Observable<List<TackPillsBean>> onTackPillsCycle(int id);

    Observable<List<SubsequentVisitBean>> onSubsequebtVisit(int id);

    Observable<List<VisitDetailsBean>> onVisitDetails(int id);

    Observable<SuCodeBean> onAddVisit(int id, Date date, String type, String content);

    List<DB_Bean> getYearPatient(String name, int month);

    List<DB_Bean> getAllPt(String name);

    Observable<MeInfoBean> onLoadMeInfo();

    Observable<SuCodeBean> onImproveInfo(String phone, String weixin_id, String qq, String email, String address
            , String photo // TODO  照片
    );

    Observable<SuCodeBean> onVerifyVisit(int id, int visit_id, Date date);
}
