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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.bean.VisitBean;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.Presenter.SuVisitPresenter;
import com.cjyun.tb.supervisor.activity.SuPatientNewsActivity;
import com.cjyun.tb.supervisor.adapter.SuVisitAdapter;
import com.cjyun.tb.supervisor.base.BaseFragment;
import com.cjyun.tb.supervisor.bean.VisitDetailsBean;
import com.cjyun.tb.supervisor.custom.SuCalendarView;
import com.cjyun.tb.supervisor.data.SuInjection;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class SuVisitEventFgt_Cp extends BaseFragment implements Contract.SuVisitView {

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
    /* @Bind(R.id.tv_type)
     TextView mTvType;
     @Bind(R.id.tv_content)
     TextView mTvContent;
     @Bind(R.id.ll_show_popup)
     LinearLayout mLlShowPopup;*/
    @Bind(R.id.lv_su_visit_events_show)
    ListView lvSuVisitEventsShow;
    private Activity mActivity;
    private SuVisitPresenter mPresent;

    // private ArrayMap<String, VisitDetailsBean> map = new ArrayMap<>();
    private ArrayMap<Integer, Boolean> curMap = new ArrayMap<>();
    private ArrayMap<Integer, Boolean> prevMap = new ArrayMap<>();
    private ArrayList<VisitBean> mList;//某天对应的数据
    private String curDayByClick;//当前点击的日期
    //  private int curID = 0;//访视计划的id
    private int id;
    private SuVisitAdapter adapter;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static SuVisitEventFgt_Cp newInstance(Handler handler) {
        mHandler = handler;
        return new SuVisitEventFgt_Cp();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new SuVisitPresenter(SuInjection.provideTwoRepository(), this);
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

        mList = new ArrayList<>();
        adapter = new SuVisitAdapter(mActivity, mList);
        lvSuVisitEventsShow.setAdapter(adapter);
        //  setListViewHeightBasedOnChildren(lvSuVisitEventsShow);

        mCvCalendar.setOnItemListener(new SuCalendarView.OnItemByClick() {
            @Override
            public void onItemClick(int day) {
                int year = mCvCalendar.getYear();
                int month = mCvCalendar.getMonth();
                StringBuilder sb = new StringBuilder();
                sb.append(year).append("-").append(standardDateFormat(month)).append("-").append(standardDateFormat(day));
                curDayByClick = sb.toString();

                KLog.d(curDayByClick + "-------对应的时间----");
                // TODO  获取到对应点击的时间  (根据点击的时间设置对应的数据)
                onQueryInformationDaily(curDayByClick);

                //onQueryInformationDaily(curDayByClick);
                //if (map.containsKey(curDayByClick)) showVisitDetail(map.get(curDayByClick));
            }
        });

        listViewSlide();
        return root;
    }

    // 设置ListView的 滑动
    private void listViewSlide() {
        lvSuVisitEventsShow.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (lvSuVisitEventsShow != null && lvSuVisitEventsShow.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = lvSuVisitEventsShow.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = lvSuVisitEventsShow.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefresh.setEnabled(enable);
            }
        });

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

    private void showVisitDetail(VisitBean bean) {

        //  mTvType.setText(bean.getVisit_type().equals("visit") ? "上门访视" : "电话访视");
        // mTvContent.setText(bean.getContent() != null ? bean.getContent() : "无设置内容");
        // curID = bean.getId();
    }

    /**
     * 设置当前的年和月
     */
    private void setTitleDate() {
        if (!isAdded()) return;
        Calendar calendar = mCvCalendar.getCalendar();
        calendar.get(Calendar.YEAR);
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR)).append("年").append(calendar.get(Calendar.MONTH) + 1).append("月");
        mTvDate.setText(sb.toString());
    }

    /**
     * 切换月份
     */
    @OnClick(R.id.relay_btn_prev)
    public void onClickPrevMonth() {
        mRelayBtnPrev.setEnabled(false);
        mRelayBtnCur.setEnabled(true);
        mCvCalendar.refreshPrevMonth();
        setTitleDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        curDayByClick = sdf.format(calendar.getTime());
        //  onQueryInformationDaily(curDayByClick);
        onQueryInformationDaily(curDayByClick);

        mCvCalendar.setErrorData(prevMap);


    }

    @OnClick(R.id.relay_btn_cur)
    public void onClickNextMonth() {
        mRelayBtnPrev.setEnabled(true);
        mRelayBtnCur.setEnabled(false);
        mCvCalendar.refreshNextMonth();
        setTitleDate();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        curDayByClick = sdf.format(new Date());

        onQueryInformationDaily(curDayByClick);
        //  onQueryInformationDaily(curDayByClick);
        mCvCalendar.setErrorData(curMap);
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
        mPresent.onTruncate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (curMap != null && !curMap.isEmpty()) curMap.clear();
        if (prevMap != null && !prevMap.isEmpty()) prevMap.clear();

        ButterKnife.unbind(this);

    }

    /**
     * 刷新显示
     *
     * @param b
     */
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.SIMPLIFIED_CHINESE);
        String curMonth = sdf.format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String prevMonth = sdf.format(calendar.getTime());

        if (curMap != null && curMap.isEmpty()) curMap.clear();
        if (prevMap != null && prevMap.isEmpty()) prevMap.clear();

        for (VisitDetailsBean bean : list) {
            if (bean != null && bean.getSetting_date() == null || bean == null) break;
            String day = bean.getSetting_date().substring(0, 7);
            String d = bean.getSetting_date().substring(8, 10);

            if (d.startsWith("0")) d = d.substring(1, d.length());

            //点击当天对应的数据
            if (day.equals(curMonth)) {
                curMap.put(Integer.valueOf(d), true);

                // TODO 将对应的数据进行存储
                mPresent.addSQL((bean.getSetting_date().substring(0, 10)),
                        (bean.getActually_time() == null ? null : bean.getActually_time().substring(0, 10)),
                        bean.getVisit_type(), bean.getContent());

                KLog.d(bean.getContent() + "--cur------对应的内容-----" + (bean.getSetting_date().substring(0, 10)));

            } else if (day.equals(prevMonth)) {


                mPresent.addSQL((bean.getSetting_date().substring(0, 10)),
                        (bean.getActually_time() == null ? null : bean.getActually_time().substring(0, 10)),

                        bean.getVisit_type(), bean.getContent());
                KLog.d(bean.getContent() + "---pre-----对应的内容-----" + d);
                prevMap.put(Integer.valueOf(d), true);
            }
        }

        //画对应的日期设置标识
        if (curDayByClick.substring(0, 7).equals(curMonth)) {
            mCvCalendar.setErrorData(curMap);
        } else if (curDayByClick.substring(0, 7).equals(prevMonth)) {
            mCvCalendar.setErrorData(prevMap);
        }
        //底部对应显示框
        onQueryInformationDaily(curDayByClick);
    }

    // 查询数据库设置对应的数据
    private void onQueryInformationDaily(String curDayByClick) {
        // List<VisitBean> visitBeanList = DataSupport.where("setting_date = ?", curDayByClick).find(VisitBean.class);

        List<VisitBean> list = mPresent.querySQL(curDayByClick);
        if (mList != null && !mList.isEmpty()) mList.clear();
        mList.addAll(list);
        adapter.notifyDataSetChanged();
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
