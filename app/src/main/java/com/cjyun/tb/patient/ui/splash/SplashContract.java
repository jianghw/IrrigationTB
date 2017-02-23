package com.cjyun.tb.patient.ui.splash;

import com.cjyun.tb.patient.base.IBasePresenter;
import com.cjyun.tb.patient.base.IBaseView;
import com.cjyun.tb.patient.bean.UpdateBean;
import com.cjyun.tb.patient.constant.TbGlobal;


public interface SplashContract {

    /***********************************
     * SplashActivity 启动
     *************************************************/
    interface ISplashView extends IBaseView<ISplashPresenter>{

        /**
         * 控制选择按钮是否出现
         *
         * @param b true 不出现gone
         */
        void setSelectorBtnGone(boolean b);

        /**
         * 显示是否更新app提示框
         *
         * @param appUpdateBean
         * @param fileFullName  文件全名
         */
        void showDialogToUpdate(UpdateBean appUpdateBean, String fileFullName);

    }

    interface ISplashPresenter extends IBasePresenter{

        /**
         * app详情获取
         *
         * @param b
         */
        void onVersionUpdate(boolean b);

        /**
         * 是否需要要自动登陆
         *
         * @return
         */
        boolean isAutoSignUi();

        /**
         * 自动登陆设置
         *
         * @param patientUser Can be {@link TbGlobal.JString#PATIENT_USER},
         *                    {@link TbGlobal.JString#DIRECTOR_USER}
         */
        void onAutomaticLogin(String patientUser);

        /**
         * 更新文件的地址
         *
         * @param response 请求响应体
         * @throws Exception
         */
        void isToUpgrade(UpdateBean response) throws Exception;

        /**
         * 开启写文件服务或提示更新
         *
         * @param appUpdateBean app详情
         * @param fileFullName  文件路径
         * @throws Exception
         */
        void onWriteAPKFile(UpdateBean appUpdateBean, String fileFullName) throws Exception;
    }
}
