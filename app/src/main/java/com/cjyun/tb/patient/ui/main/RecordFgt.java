package com.cjyun.tb.patient.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.bean.CurRecordBean;
import com.cjyun.tb.patient.bean.PrevRecordBean;
import com.cjyun.tb.patient.custom.CalendarView;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.ui.record.IRecordContract;
import com.cjyun.tb.patient.ui.record.RecordFgtPresenter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianghw on 2016/3/23 0023.
 * Description 记录页
 */
public class RecordFgt extends BaseFragment implements IRecordContract.IRecordView {

    @Bind(R.id.relay_btn_prev)
    RelativeLayout mRelayBtnPrev;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.relay_btn_cur)
    RelativeLayout mRelayBtnCur;
    @Bind(R.id.cv_calendar)
    CalendarView mCvCalendar;
    @Bind(R.id.tv_day_count)
    TextView mTvDayCount;
    @Bind(R.id.lr_web)
    LinearLayout mLrWeb;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private Activity mActivity;
    private IRecordContract.IRecordPresenter mPresent;

    private ArrayMap<Integer, Boolean> prevMap;
    private ArrayMap<Integer, Boolean> curMap;
    private int prevCount = 0;
    private int curCount = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static RecordFgt newInstance() {
        return new RecordFgt();
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
        View root = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, root);

        mSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        mSwipeRefresh.setDistanceToTriggerSync(500);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresent.loadingRemoteData(true);
            }
        });
        setTitleDate();
        mRelayBtnPrev.setEnabled(true);
        mRelayBtnCur.setEnabled(false);
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
        if (curMap != null && !curMap.isEmpty()) {
            curMap.clear();
            curMap = null;
        }
        if (prevMap != null && !prevMap.isEmpty()) {
            prevMap.clear();
            prevMap = null;
        }
    }

    @OnClick(R.id.lr_web)
    public void onOpenWebView() {
      /*  Intent intent = new Intent(Intent.ACTION_VIEW);
        intent .setData(Uri.parse(url));
        startActivity(intent);*/
    }

    /**
     * 上月
     */
    @OnClick(R.id.relay_btn_prev)
    public void onMoveToPrev() {
        mRelayBtnPrev.setEnabled(false);
        mRelayBtnCur.setEnabled(true);
        mCvCalendar.refreshPrevMonth();
        mCvCalendar.setErrorData(prevMap);
        setTitleDate();
        onAbnormalOfDays(prevCount);
    }

    /**
     * 本月
     */
    @OnClick(R.id.relay_btn_cur)
    public void onMoveToCur() {
        mRelayBtnPrev.setEnabled(true);
        mRelayBtnCur.setEnabled(false);
        mCvCalendar.refreshNextMonth();
        mCvCalendar.setErrorData(curMap);
        setTitleDate();
        onAbnormalOfDays(curCount);
    }

    private void setTitleDate() {
        if (!isAdded()) return;
        Calendar calendar = mCvCalendar.getCalendar();
        calendar.get(Calendar.YEAR);
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR)).append("年").append(calendar.get(Calendar.MONTH) + 1).append("月");
        mTvDate.setText(sb.toString());
    }

    public void setLoadingIndicator(final boolean b) {
        if (getView() == null) return;
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(b);
            }
        });
    }

    /**
     * 上月数据
     *
     * @param list
     */
    @Override
    public void onSucceedLoadPrev(List<PrevRecordBean> list) {
        ArrayMap<Integer, Boolean> hashMap = new ArrayMap<>();
        for (PrevRecordBean bean : list) {
            String[] days = bean.getActualize_time().split("T");//2016-10-12
            String day = days[0].split("-")[2];
            if (day.startsWith("0")) day = day.substring(1, day.length());

            if (!bean.isTaken()) {
                hashMap.put(Integer.valueOf(day), true);
            }
            if (bean.isTaken() && hashMap.containsKey(Integer.valueOf(day))) {
                hashMap.remove(Integer.valueOf(day));
            }
        }
        if (prevMap != null && !prevMap.isEmpty()) prevMap.clear();
        prevCount = hashMap.size();
        prevMap = hashMap;
    }

    /**
     * 本月数据
     *
     * @param list
     */
    @Override
    public void onSucceedLoadCur(List<CurRecordBean> list) {
        //传递异常数据
        ArrayMap<Integer, Boolean> hashMap = new ArrayMap<>();
        addErrorData(list, hashMap);
        mCvCalendar.setErrorData(hashMap);
        if (curMap != null && !curMap.isEmpty()) curMap.clear();
        curCount = hashMap.size();
        curMap = hashMap;
        onAbnormalOfDays(curCount);
    }

    public void onAbnormalOfDays(int count) {
        mTvDayCount.setText(String.valueOf(count));
    }

    private void addErrorData(List<CurRecordBean> list, ArrayMap<Integer, Boolean> hashMap) {
        for (CurRecordBean bean : list) {
            String[] days = bean.getActualize_time().split("T");//2016-10-12
            String day = days[0].split("-")[2];
            if (day.startsWith("0")) day = day.substring(1, day.length());

            if (!bean.isTaken()) {
                hashMap.put(Integer.valueOf(day), true);
            }
            if (bean.isTaken() && hashMap.containsKey(Integer.valueOf(day))) {
                hashMap.remove(Integer.valueOf(day));
            }
        }
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
}
