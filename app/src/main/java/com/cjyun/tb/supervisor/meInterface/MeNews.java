package com.cjyun.tb.supervisor.meInterface;

import com.cjyun.tb.supervisor.base.IBaseView;
import com.cjyun.tb.supervisor.bean.MeInfoBean;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public interface MeNews {

    interface SuMeView<SuBaseInfoPresenter> {

        void getInfo(MeInfoBean bean);
    }

    interface SuBaseInfoPresenter {

        void onLoadSuInfo();
    }

    interface SuUpdateFragment extends IBaseView<SuUpdatePresenter> {

        void showIsNewVerCode();

         void onProgress(String v);

         void dismissLoding();
    }


    interface SuUpdatePresenter {

        void updateInfo();
    }

}
