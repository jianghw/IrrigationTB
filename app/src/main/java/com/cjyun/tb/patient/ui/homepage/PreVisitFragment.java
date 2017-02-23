package com.cjyun.tb.patient.ui.homepage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.bean.CurRecordBean;
import com.cjyun.tb.patient.bean.PrevRecordBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.custom.MonthGridView;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.ui.record.IRecordContract;
import com.cjyun.tb.patient.ui.record.RecordFgtPresenter;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/25 0025<br/>
 */
public class PreVisitFragment extends BaseFragment implements IRecordContract.IRecordView {

    @Bind(R.id.gv_month)
    MonthGridView mGvMonth;

    private Activity mActivity;
    private IRecordContract.IRecordPresenter mPresent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static PreVisitFragment newInstance() {
        return new PreVisitFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new RecordFgtPresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_month, container, false);
        ButterKnife.bind(this, root);
        //初始化控件
        mGvMonth.setMonthData(mActivity, TbGlobal.JString.CUR_MONTH);
        //传递异常数据
        HashMap<Integer, Boolean> m = new HashMap<>();
        m.put(26, true);
        m.put(1, true);
        mGvMonth.setAbnormalState(m);
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
      /*  RecordFragment fragment = (RecordFragment) getParentFragment();
        fragment.setLoadingIndicator(b);*/
    }

 /*   @Override
    public String getMonthStartAndEnd() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, 1);
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        String startDay = simpleFormate.format(calendar.getTime());
        // 本月的最后一天
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        String ultimateDay = simpleFormate.format(calendar.getTime());
        return startDay + "/" + ultimateDay;
    }*/


    @Override
    public void onSucceedLoadPrev(List<PrevRecordBean> list) {
        //传递异常数据
        HashMap<Integer, Boolean> hashMap = new HashMap<>();
        for (PrevRecordBean bean : list) {
            String[] days = bean.getCreated_at().split("T");
            String day = days[0].split("-")[2];
            if (day.startsWith("0")) day = day.substring(1, day.length());

            if (!bean.isTaken()) {
                hashMap.put(Integer.valueOf(day), true);
            }
            if (bean.isTaken() && hashMap.containsKey(Integer.valueOf(day))) {
                hashMap.remove(Integer.valueOf(day));
            }
        }
        mGvMonth.setAbnormalState(hashMap);
    }

    @Override
    public void onSucceedLoadCur(List<CurRecordBean> list) {

    }
}
