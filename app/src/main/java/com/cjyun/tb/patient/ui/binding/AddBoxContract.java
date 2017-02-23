package com.cjyun.tb.patient.ui.binding;

import android.content.Intent;

import com.cjyun.tb.patient.base.IBasePresenter;
import com.cjyun.tb.patient.base.IBaseView;
import com.cjyun.tb.patient.bean.BindBoxBean;

/**
 * Created by Administrator on 2016/5/6 0006</br>
 * description:</br>
 */
public interface AddBoxContract {

    /***********************************
     * ScanBox 准备扫描fgt
     *************************************************/
    interface IScanBoxView extends IBaseView<IScanBoxPresenter> {

        String getBoxID();

        String getBoxSN();

        /**
         * 显示扫描结果
         *
         * @param scanResult
         */
        void onResultFromQR(String scanResult);

        void onSucceedNextStep();

        /**
         * 解除绑定
         * @param bean
         */
        void showRemoveBinding(BindBoxBean bean);
    }

    interface IScanBoxPresenter extends IBasePresenter {

        void onBindingFirstStep();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onRemoveBinding();
    }

    /***********************************
     * ScanBox 准备扫描fgt
     *************************************************/
    interface IBindFgtView extends IBaseView<IBindFgtPresenter> {

        Thread onCreateWorkThread();

        /**
         * 中断倒计时
         */
        void interruptThread();

        void onEnteredCountdown();

        void onCompleteBinding();

        void showCountdown(long l);

        void onErrorStep();

        void onSucceedStep();
    }

    interface IBindFgtPresenter extends IBasePresenter {
        void onBindingSecondStep();
    }
}
