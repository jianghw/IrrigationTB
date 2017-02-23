package com.cjyun.tb.patient.ui.login;

import com.cjyun.tb.patient.base.IBasePresenter;
import com.cjyun.tb.patient.base.IBaseView;


public interface LoginContract {

    /***********************************
     * LoginActivity
     *************************************************/
    interface ILoginView extends IBaseView<ILoginPresenter>{

        String getUserName();

        String getUserPassword();

        void onToNewActivity(int i);

    }

    interface ILoginPresenter extends IBasePresenter{

        void remindToSign();

        /**
         * 登陆验证
         *
         * @param name
         * @param password
         */
        void verifyUserLogin(String name, String password);

        /**
         * 获取是否绑定药盒
         */
        void onBindBoxInfo();
    }
}
