package com.cjyun.tb.patient.ui.reset;

import com.cjyun.tb.patient.base.IBasePresenter;
import com.cjyun.tb.patient.base.IBaseView;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/19 0019<br/>
 */
public interface IResetPwContract {


    /***********************************
     * ResetPasswordActivity
     *************************************************/

    interface IResetFgtView extends IBaseView<IResetFgtPresenter>{

        String getUserPhone();

        String getUserPassword();

        /**
         * 注册短信监听器，接收短信
         */
        void onRegistrationMessage();

        /**
         * 注销监听器
         */
        void onUnRegistrationMessage();

    }

    interface IResetFgtPresenter extends IBasePresenter{

        void onVerifyFillContent();

        /**
         * 提交新密码
         * @param oldPw
         * @param newPw
         */
        void onSubmitNewPassword(String oldPw, String newPw);
    }

}
