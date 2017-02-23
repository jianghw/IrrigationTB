package com.cjyun.tb.patient.ui.general;

import com.cjyun.tb.patient.base.IBasePresenter;
import com.cjyun.tb.patient.base.IBaseView;
import com.cjyun.tb.patient.bean.MyDirectorBean;

/**
 * Created by Administrator on 2016/5/6 0006</br>
 * description:</br>
 */
public interface IGeneralContract {

    /***********************************
     * IMyDirector 我的督导员
     *************************************************/

    interface IMyDirectorView extends IBaseView<IMyDirectorPresenter> {
        /**
         * 加载指示器
         *
         * @param b true 显示加载图标
         */
        void setLoadingIndicator(boolean b);

        /**
         * 更新数据列表
         *
         * @param data
         */
        void updateListData(MyDirectorBean data);
    }

    interface IMyDirectorPresenter extends IBasePresenter {
        /**
         * 下拉刷新
         *
         * @param isUpdate
         */
        void loadingRemoteData(boolean isUpdate);
    }

    /***********************************
     * suggestion 意见箱
     *************************************************/

    interface ISuggestionView extends IBaseView<ISuggestionPresenter> {
        String getContent();
        String getPhone();
    }

    interface ISuggestionPresenter extends IBasePresenter {
        /**
         * 提交意见信息
         */
        void submitSuggestion();
    }

    interface IUpdateView extends IBaseView<IUpdatePresenter> {

        void showIsNewVerCode();

        void onProgress(String v);

        void dismissLoding();
    }

    interface IUpdatePresenter extends IBasePresenter {

        void updateInfo();

        void stopThread();
    }


}
