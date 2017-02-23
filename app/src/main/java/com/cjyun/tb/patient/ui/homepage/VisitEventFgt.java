package com.cjyun.tb.patient.ui.homepage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.adapter.VisitAdapter;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.bean.VisitBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.custom.CalendarView;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.util.PtCreatQrCodeTool;
import com.cjyun.tb.patient.util.ToastUtils;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
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
public class VisitEventFgt extends BaseFragment implements IHomepageContract.IVisitEventView {

    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.relay_btn_prev)
    RelativeLayout mRelayBtnPrev;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.relay_btn_cur)
    RelativeLayout mRelayBtnCur;
    @Bind(R.id.cv_calendar)
    CalendarView mCvCalendar;
    @Bind(R.id.lv_content)
    ListView mLvContent;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private Activity mActivity;
    private IHomepageContract.IVisitEventPresenter mPresent;


    private String curDayByClick;//当前点击的日期2019-06-12
    private ArrayList<VisitBean> mList;//某天对应的数据
    private ArrayMap<Integer, Boolean> prevMap = new ArrayMap<>();
    private ArrayMap<Integer, Boolean> curMap = new ArrayMap<>();
    private VisitAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static VisitEventFgt newInstance(Handler handler) {
        mHandler = handler;
        return new VisitEventFgt();
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
        View root = inflater.inflate(R.layout.fragment_visit_event, container, false);
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        curDayByClick = sdf.format(new Date());

        setTitleDate();
        mRelayBtnPrev.setEnabled(true);
        mRelayBtnCur.setEnabled(false);

        mList = new ArrayList<>();
        adapter = new VisitAdapter(mActivity, mList);
        mLvContent.setAdapter(adapter);

        mCvCalendar.setOnItemListener(new CalendarView.OnItemByClick() {
            @Override
            public void onItemClick(int day) {
                int year = mCvCalendar.getYear();
                int month = mCvCalendar.getMonth();
                StringBuilder sb = new StringBuilder();
                sb.append(year).append("-").append(standardDateFormat(month)).append("-").append(standardDateFormat(day));
                curDayByClick = sb.toString();
                onQueryInformationDaily(curDayByClick);
            }
        });

        mLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
                if (mList != null && !mList.isEmpty()) {
                    VisitBean bean = mList.get(position);
                    if (bean == null) return;
                    String setTime = bean.getSetting_date();
                    long set_time = 0;
                    try {
                        set_time = sdf.parse(setTime).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //设置时间大约当前时间
                    if (set_time > new Date().getTime()) {
                        ToastUtils.showMessage("时间还没到,不要作弊哦~");
                        return;
                    }
                    String ac_time =bean.getActually_time();
                    String time = "";
                    if (ac_time != null) time = ac_time.split("T")[0];
                    if (time.equals(setTime)) {
                        ToastUtils.showMessage("本次你已经访视过的~");
                        return;
                    }

                    if (bean.getVisit_type().equals("phone")) {
                        onTelephoneVisitConfirmation(bean);
                    } else {
                        onComeToVisit(bean);
                    }
                }
            }
        });
        return root;
    }

    /**
     * 上门访视
     *
     * @param bean
     */
    private void onComeToVisit(VisitBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        StringBuilder sb = new StringBuilder();
        sb.append("id:").append(bean.getId()).append(",").append("patient_id:").append(bean.getPatient_id());

        Bitmap bitmap = PtCreatQrCodeTool.prevQrcode(sb.toString(), 500);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.alert_dialog_img, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_qr_code);
        imageView.setImageBitmap(bitmap);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
  /*      WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        try {
            Field field = dialog.getClass().getDeclaredField("mAlert");
            field.setAccessible(true);
            Object object = field.get(dialog);
            Field tvField = object.getClass().getDeclaredField("mTitleView");
            tvField.setAccessible(true);
            TextView tv = (TextView) tvField.get(object);

            LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(tv.getLayoutParams());
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(params.width/2-tv.getWidth()/2,
                    layoutParams.topMargin,params.width/2-tv.getWidth()/2+tv.getWidth(),layoutParams.bottomMargin);
            tv.setLayoutParams(layoutParams);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 电话访视
     *
     * @param bean
     */
    private void onTelephoneVisitConfirmation(final VisitBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("电话访视")
                .setMessage("督导员已进行电话访视")
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresent.onTelephoneVisitConfirmation(bean.getId());
                    }
                }).show();
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        curDayByClick = sdf.format(calendar.getTime());
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
        mCvCalendar.setErrorData(curMap);
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onActionBarBack() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY);
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
        if (curMap != null && !curMap.isEmpty()) curMap.clear();
        if (prevMap != null && !prevMap.isEmpty()) prevMap.clear();
        dismissDialog();
        ButterKnife.unbind(this);
    }

    @Override
    public void setLoadingIndicator(final boolean b) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(b);
            }
        });
    }

    @Override
    public void onSucceedLoadData(List<VisitBean> list) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.SIMPLIFIED_CHINESE);
        String curMonth = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String prevMonth = sdf.format(calendar.getTime());

        if (curMap != null && curMap.isEmpty()) curMap.clear();
        if (prevMap != null && prevMap.isEmpty()) prevMap.clear();

        for (VisitBean bean : list) {
            if (bean != null && bean.getSetting_date() == null || bean == null) break;
            String day = bean.getSetting_date().substring(0, 7);

            String d = bean.getSetting_date().substring(8, 10);
            if (d.startsWith("0")) d = d.substring(1, d.length());

            //点击当天对应的数据
            if (day.equals(curMonth)) {
                curMap.put(Integer.valueOf(d), true);
            } else if (day.equals(prevMonth)) {
                prevMap.put(Integer.valueOf(d), true);
            }
        }

        if (curDayByClick.substring(0, 7).equals(curMonth)) {
            mCvCalendar.setErrorData(curMap);
        } else if (curDayByClick.substring(0, 7).equals(prevMonth)) {
            mCvCalendar.setErrorData(prevMap);
        }
        //底部对应显示框
        onQueryInformationDaily(curDayByClick);
    }

    /**
     * 手机访视成功后
     */
    @Override
    public void onSucceedTelephone() {
        mPresent.loadingRemoteData(true);
    }

    private void onQueryInformationDaily(String curDayByClick) {
        List<VisitBean> visitBeanList = DataSupport.where("setting_date = ?", curDayByClick).find(VisitBean.class);
        if (mList != null && !mList.isEmpty()) mList.clear();
        if (mList != null) mList.addAll(visitBeanList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showToastMessage(int id) {

    }

    @Override
    public void loadingDialog() {
        createLoadingDialog();
    }

    @Override
    public void dismissDialog() {
        dismissLoadingDialog();
    }
}
