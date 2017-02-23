package com.cjyun.tb.supervisor.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.Presenter.SuVisitPresenter;
import com.cjyun.tb.supervisor.activity.SuPatientNewsActivity;
import com.cjyun.tb.supervisor.base.BaseFragment;
import com.cjyun.tb.supervisor.bean.VisitDetailsBean;
import com.cjyun.tb.supervisor.custom.SuCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 访视事件
 */
public class SuVisitEventFgt extends BaseFragment implements Contract.SuVisitView {

    private static Handler mHandler;
    @Bind(R.id.relay_btn_prev)
    RelativeLayout mRelayBtnPrev;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.relay_btn_cur)
    RelativeLayout mRelayBtnCur;
    @Bind(R.id.cv_su_calendar)
    SuCalendarView mCvCalendar;
    @Bind(R.id.swipe_refresh_visit)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.tv_type)
    TextView mTvType;
    @Bind(R.id.tv_content)
    TextView mTvContent;
    @Bind(R.id.ll_show_popup)
    LinearLayout mLlShowPopup;
    @Bind(R.id.lv_su_visit_events_show)
    ListView lvSuVisitEventsShow;
    private Activity mActivity;
    private SuVisitPresenter mPresent;

    private ArrayMap<String, VisitDetailsBean> map = new ArrayMap<>();
    private String curDayByClick;//当前点击的日期
    private int curID = 0;//访视计划的id
    private int id;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static SuVisitEventFgt newInstance(Handler handler) {
        mHandler = handler;
        return new SuVisitEventFgt();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
          //  mPresent = new SuVisitPresenter(SuInjection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_su_visit_event, container, false);
        ButterKnife.bind(this, root);

        id = ((SuPatientNewsActivity) mActivity).getId();
        mSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        mSwipeRefresh.setDistanceToTriggerSync(500);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresent.onVisitDetails(id);
            }
        });
        // 时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        curDayByClick = sdf.format(new Date());


        setTitleDate();
        mRelayBtnPrev.setEnabled(true);
        mRelayBtnCur.setEnabled(false);
        mCvCalendar.setOnItemListener(new SuCalendarView.OnItemByClick() {
            @Override
            public void onItemClick(int day) {
                int year = mCvCalendar.getYear();
                int month = mCvCalendar.getMonth();
                StringBuilder sb = new StringBuilder();
                sb.append(year).append("-").append(standardDateFormat(month)).append("-").append(standardDateFormat(day));
                curDayByClick = sb.toString();
                if (map.containsKey(curDayByClick)) showVisitDetail(map.get(curDayByClick));
            }
        });
        return root;
    }

    private String standardDateFormat(int day) {
        StringBuilder sb = new StringBuilder();
        if (day < 10) {
            sb.append("0").append(day);
        } else {
            sb.append(day);
        }
        return sb.toString();
    }

    private void showVisitDetail(VisitDetailsBean bean) {
        mTvType.setText(bean.getVisit_type().equals("visit") ? "上门访视" : "电话访视");
        mTvContent.setText(bean.getContent() != null ? bean.getContent() : "无设置内容");
        curID = bean.getId();
    }

    private void setTitleDate() {
        if (!isAdded()) return;
        Calendar calendar = mCvCalendar.getCalendar();
        calendar.get(Calendar.YEAR);
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR)).append("年").append(calendar.get(Calendar.MONTH) + 1).append("月");
        mTvDate.setText(sb.toString());
    }

    @OnClick(R.id.relay_btn_prev)
    public void onClickPrevMonth() {
        mRelayBtnPrev.setEnabled(false);
        mRelayBtnCur.setEnabled(true);
        mCvCalendar.refreshPrevMonth();
        setTitleDate();
    }

    @OnClick(R.id.relay_btn_cur)
    public void onClickNextMonth() {
        mRelayBtnPrev.setEnabled(true);
        mRelayBtnCur.setEnabled(false);
        mCvCalendar.refreshNextMonth();
        setTitleDate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresent.onVisitDetails(id);
    }

    @Override
    public void onPause() {
        super.onPause();
        // mPresent.unSubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (map != null && !map.isEmpty()) map.clear();
        ButterKnife.unbind(this);

    }

    @Override
    public void setLoadingIndicator(final boolean b) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_visit);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(b);
            }
        });
    }


    @Override
    public void onSucceedLoadData(List<VisitDetailsBean> list) {

        if (map != null && !map.isEmpty()) {
            map.clear();
        }
        for (VisitDetailsBean bean : list) {
            if (bean != null && bean.getSetting_date() == null || bean == null) {
                break;
            }
            map.put(bean.getSetting_date(), bean);
        }
        if (map != null && map.containsKey(curDayByClick)) {
            showVisitDetail(map.get(curDayByClick));
        } else {
            mTvType.setText("无访视");
            mTvContent.setText("无设置内容");
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
