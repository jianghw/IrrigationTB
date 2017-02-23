package com.cjyun.tb.patient.ui.improve;

import com.cjyun.tb.patient.base.IBasePresenter;
import com.cjyun.tb.patient.base.IBaseView;

/**
 * Created by Administrator on 2016/5/6 0006</br>
 * description:</br>
 */
public interface IUserInfoContract {

    /***********************************
     * ImproveInfoActivity 完善用户资料
     *************************************************/

    interface IUserInfoView extends IBaseView<IUserInfoPresenter> {
        /**
         * 加载指示器
         *
         * @param b true 显示加载图标
         */
        void setLoadingIndicator(boolean b);

        void setBoxId(String id);

        void setUserBasicInfo(String name, String identityCard, String gender);

        void setUserPhone(String phone);

        void setUserEmail(String email);

        void setUserQQ(String QQ);

        void setUserWeiXi(String wx);

        void setUserAddress(String address);

        String getUserPhone();

        String getUserEmail();

        String getUserQQ();

        String getUserWeiXi();

        String getUserAddress();

        String getUserPhoto();

        void setUserHead(String s);

        void submitSucceed();

        void submitError(String message);

        void setUserIdentityCard(String s);
    }

    interface IUserInfoPresenter extends IBasePresenter {
        /**
         * 下拉刷新
         *
         * @param isUpdate
         */
        void loadingRemoteData(boolean isUpdate);

        /**
         * 修改资料
         */
        void submitPatientInfo();
    }
}
