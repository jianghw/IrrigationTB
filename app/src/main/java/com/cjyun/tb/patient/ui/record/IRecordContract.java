package com.cjyun.tb.patient.ui.record;

import com.cjyun.tb.patient.base.IBasePresenter;
import com.cjyun.tb.patient.base.IBaseView;
import com.cjyun.tb.patient.bean.CurRecordBean;
import com.cjyun.tb.patient.bean.PrevRecordBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/6 0006</br>
 * description:</br>
 */
public interface IRecordContract {

    /***********************************
     * 服药记录
     *************************************************/

    interface IRecordView extends IBaseView<IRecordPresenter> {
        /**
         * 设置父类的指示器 走起~~
         *
         * @param b
         */
        void setLoadingIndicator(boolean b);

        /**
         * 加载数据成功
         *
         * @param list
         */
        void onSucceedLoadPrev(List<PrevRecordBean> list);
        void onSucceedLoadCur(List<CurRecordBean> list);
    }

    interface IRecordPresenter extends IBasePresenter {
        /**
         * 刷新数据
         *
         * @param isUpdate
         */
        void loadingRemoteData(boolean isUpdate);
    }
}
