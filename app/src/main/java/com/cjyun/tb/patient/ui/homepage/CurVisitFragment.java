package com.cjyun.tb.patient.ui.homepage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.bean.VisitBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.custom.MonthGridView;
import com.cjyun.tb.patient.data.Injection;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 访视事件 当前月
 */
public class CurVisitFragment extends BaseFragment implements IHomepageContract.IVisitEventView {

    @Bind(R.id.gv_month)
    MonthGridView mGvMonth;

    private Activity mActivity;
    private IHomepageContract.IVisitEventPresenter mPresent;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static CurVisitFragment newInstance() {
        return new CurVisitFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new VisitFgtPresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cur_visit, container, false);
        ButterKnife.bind(this, root);
        //初始化控件
        mGvMonth.setMonthData(mActivity, TbGlobal.JString.CUR_MONTH);

        //item监控回调
        mGvMonth.setOnItemCallBackByDate(new MonthGridView.DateCallBackable() {
            @Override
            public void setDataByCallItem(String date) {

            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresent.onSubscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresent.unSubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void showToastMessage(int id) {

    }

    @Override
    public void loadingDialog() {

    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public void setLoadingIndicator(boolean b) {
       /* RecordFragment fragment = (RecordFragment) getParentFragment();
        fragment.setLoadingIndicator(b);*/
    }

    @Override
    public void onSucceedLoadData(List<VisitBean> list) {
        //传递异常数据
        HashMap<Integer, Boolean> hashMap = new HashMap<>();

        mGvMonth.setAbnormalState(hashMap);
    }

    @Override
    public void onSucceedTelephone() {

    }
}
