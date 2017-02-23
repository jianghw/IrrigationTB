package com.cjyun.tb.patient.ui.homepage;

import com.cjyun.tb.patient.base.IBasePresenter;
import com.cjyun.tb.patient.base.IBaseView;
import com.cjyun.tb.patient.bean.BindBoxBean;
import com.cjyun.tb.patient.bean.DrugDetailBean;
import com.cjyun.tb.patient.bean.MedicineNamesBean;
import com.cjyun.tb.patient.bean.MedicineTimeBean;
import com.cjyun.tb.patient.bean.ReturnVisitBean;
import com.cjyun.tb.patient.bean.VisitBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/6 0006</br>
 * description:</br>
 */
public interface IHomepageContract {

    /***********************************
     * 患者主页
     *************************************************/

    interface IHomepageView extends IBaseView<IHomepagePresenter> {

        void setLoadingIndicator(boolean b);

        /**
         * 更新list显示数据
         *
         * @param medicine_names
         */
        void updateListData(List<MedicineNamesBean> medicine_names);

        /**
         * 更新吃药时间
         *
         * @param medicine_time
         */
        void updateDrugTime(MedicineTimeBean medicine_time);

        /**
         * 更新是否在线图标
         *
         * @param bean
         */
        void updateOnline(BindBoxBean bean);

        /**
         * 获取显示的吃药时间
         *
         * @return
         */
        String getDrugTime();

        /**
         * 更新倒计时
         *
         * @param s
         */
        void updateDownTime(String s);

        String getPushId();
    }

    interface IHomepagePresenter extends IBasePresenter {
        /**
         * 刷新数据
         *
         * @param isUpdate
         */
        void loadingRemoteData(boolean isUpdate);
    }

    /***********************************
     * 药物显示页
     *************************************************/
    interface IDrugView extends IBaseView<IDrugPresenter> {

        void setLoadingIndicator(boolean b);

        /**
         * 更新list显示数据
         *
         * @param medicine_names
         */
        void updateListData(DrugDetailBean medicine_names);

        /**
         * 网络修改时间成功后 ~改
         *
         * @param time
         */
        void updateTimeSucceed(String time);
    }

    interface IDrugPresenter extends IBasePresenter {
        /**
         * 刷新数据
         *
         * @param isUpdate
         */
        void loadingRemoteData(boolean isUpdate);
    }

    /***********************************
     * 复诊事件页
     *************************************************/
    interface IReturnVisitView extends IBaseView<IReturnVisitPresenter> {

        void setLoadingIndicator(boolean b);

        /**
         * 更新list显示数据
         *
         * @param been
         */
        void updateListData(List<ReturnVisitBean> been);
    }

    interface IReturnVisitPresenter extends IBasePresenter {
        /**
         * 刷新数据
         *
         * @param isUpdate
         */
        void loadingRemoteData(boolean isUpdate);
    }

    /***********************************
     * 访视事件页
     *************************************************/
    interface IVisitEventView extends IBaseView<IVisitEventPresenter> {

        void setLoadingIndicator(boolean b);

        void onSucceedLoadData(List<VisitBean> list);

        void onSucceedTelephone();
    }

    interface IVisitEventPresenter extends IBasePresenter {
        /**
         * 刷新数据
         *
         * @param isUpdate
         */
        void loadingRemoteData(boolean isUpdate);

        /**
         * 电话访视确认
         *
         * @param id
         */
        void onTelephoneVisitConfirmation(int id);
    }


}
