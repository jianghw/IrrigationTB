package com.cjyun.tb.supervisor;

import com.cjyun.tb.supervisor.base.BaseViewInterface;
import com.cjyun.tb.supervisor.base.IBaseView;
import com.cjyun.tb.supervisor.bean.DB_Bean;
import com.cjyun.tb.supervisor.bean.PatitentDetailsBean;
import com.cjyun.tb.supervisor.bean.SubsequentVisitBean;
import com.cjyun.tb.supervisor.bean.TackPillsBean;
import com.cjyun.tb.supervisor.bean.VisitDetailsBean;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10 0010.
 */
public interface Contract {


    interface SuCapturePresenter {


        void onActivityResult(int id, int pt_id);

        // void onVerifyVisit(int id, int visit_id, Date VerifyDate);
    }

    /**
     * Search view对应的操作
     */
    interface SearchView<SearchPresenter> {

        void setLoadingIndicator(boolean b); // 加载中的回调

        void allPatient(List list);// 所有患者

        void oneMonth(List list); // 一个月的患者

        void trimester(List list); // 三个月的患者

        void sixMonth(List list); // 半年的患者

        void year(List list);// 一年的患者


    }

    /**
     * Presenter 对应的逻辑操作
     */
    interface SearchPresenter {

        // void onLoadAllPatient();

        void onPostJpushID(String jpush);

        void onReadSQL(String name, int month);

    }

    /**
     * 所有患者
     */
    interface SuAllView<SuAllPresenter> {

        void initviews();

        void succeed(List<DB_Bean> bean);

        void error();
    }

    interface SuAllPresenter {

        /**
         * 读取数据库
         */
        List getPtNumber(String name, int month);

    }

    /**
     * 患者基本信息
     */
    interface BaseNewsView<BaseNewsPresenter> extends BaseViewInterface {

        void setData(PatitentDetailsBean bean);
    }

    interface BaseNewsPresenter {

        /**
         * 从网上加载
         */
        void onPatitentDetails(int id);

    }


    /**
     * 服药统计页面
     */
    interface TackPillsView extends BaseViewInterface<TackPillsPresenter> {

        /**
         * 设置数据
         */
        void tackPillsSucceed(List<TackPillsBean> tackPillsBeans);
    }

    interface TackPillsPresenter {
        /**
         * 网上加载
         */
        void onTackPillsCycle(int id);

    }

    /**
     * 患者信息
     */
    interface PatientNewsView<PatientNewsPresenter> {

        void succeed(PatitentDetailsBean bean);

    }

    interface PatientNewsPresenter {

        void onLoadPtDetails(int id);
    }


    /**
     * 患者复诊详情
     */
    interface SubsequentVisitView<SubsequentVisitPresenter> {

        void svSucceed(List<SubsequentVisitBean> subsequentVisitBeans);

    }

    interface SubsequentVisitPresenter {

        /**
         * 加载数据
         */
        void onSubsequebtVisit(int id);

    }

    /**
     * 患者访问事件
     */
    interface SuVisitView extends IBaseView<SuVisitPresenter> {


        void setLoadingIndicator(boolean b);

        void onSucceedLoadData(List<VisitDetailsBean> list);

    }

    interface SuVisitPresenter {

        /**
         * 访问事件
         */
        void onVisitDetails(int id);

        boolean getVisitContent();
    }

    /**
     * 添加访问事件
     */
    interface SuAddVisitView<SuVisitDetailsPresenter> {

        String getEdit();

        void geVisittDate(int year, int month, int day);

    }

    interface SuVisitDetailsPresenter {

        void onAddVisit(String type, int id, Date date);

    }
}
