package com.cjyun.tb.supervisor.suImprove;

import com.cjyun.tb.supervisor.base.BaseViewInterface;
import com.cjyun.tb.supervisor.bean.MeInfoBean;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public interface SuImproveInfo {

    interface SuImptovePresenter {

        // 开始加载
        void onInitStart();

        // 加载中
        void loadingRemoteData();
    }

    interface SuImproveView<SuImptovePresenter> extends BaseViewInterface{

        void getInfo(MeInfoBean bean);
    }
}
