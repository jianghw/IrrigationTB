package com.cjyun.tb.patient.ui.homepage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brithpicker.BrithPopupWindow;
import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.bean.DrugDetailBean;
import com.cjyun.tb.patient.bean.MedicineDatesBean;
import com.cjyun.tb.patient.bean.MedicineNamesBean;
import com.cjyun.tb.patient.bean.MedicineTimeBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.adapter.DrugDetailAdapter;
import com.cjyun.tb.patient.data.Injection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 时间及药物fragment
 */
public class DrugFragment extends BaseFragment implements IHomepageContract.IDrugView {
    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.relay_drug_time)
    RelativeLayout mRelayDrugTime;
    @Bind(R.id.lv_drug_list)
    ListView mLvDrugList;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    private Activity mActivity;
    private DrugFgtPresenter mPresent;

    //时间选择器相关
    private BrithPopupWindow mTimePopupWindow;
    private ArrayList<String> time1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> time2Items = new ArrayList<>();
    private ArrayList<MedicineNamesBean> mDrugList;
    private DrugDetailAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static DrugFragment newInstance(Handler handler) {
        mHandler = handler;
        return new DrugFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new DrugFgtPresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_time_drug, container, false);
        ButterKnife.bind(this, root);

        mTimePopupWindow = new BrithPopupWindow(mActivity);
        initTimeOfData();

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

        mDrugList = new ArrayList<>();
        adapter = new DrugDetailAdapter(mActivity, mDrugList);
        mLvDrugList.setAdapter(adapter);

        return root;
    }

    private void initTimeOfData() {
        //分钟
        ArrayList<String> items_01 = new ArrayList<>();
        ArrayList<String> items_02 = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                items_01.add("0" + String.valueOf(i));
                items_02.add("0" + String.valueOf(i));
            } else {
                items_01.add(String.valueOf(i));
                items_02.add(String.valueOf(i));
            }
        }
        time2Items.add(items_01);
        time2Items.add(items_02);

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                time1Items.add("0" + String.valueOf(i));
            } else {
                time1Items.add(String.valueOf(i));
            }
        }
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
        dismissDialog();
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onActionBarBack() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY);
    }

    @OnClick(R.id.relay_drug_time)
    public void onPopuTime() {
        mTimePopupWindow.showAtLocation(mRelayDrugTime, Gravity.BOTTOM, 0, 0);
        chooseTimeData();
    }

    private void chooseTimeData() {
        //初始化pwBrith的信息,false关闭联动
        mTimePopupWindow.setPicker(time1Items, time2Items, false);
        //开启循环
        mTimePopupWindow.setCyclic(false);
        //设置初始位置
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mTimePopupWindow.setSelectOptions(hour, minute);
        mTimePopupWindow.setOnoptionsSelectListener(new BrithPopupWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2) {
                String time = time1Items.get(options1) + ":" + time2Items.get(0).get(option2);
                mPresent.updateTime(time);
            }
        });
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

    /**
     * 药物更新
     *
     * @param detailBean
     */
    @Override
    public void updateListData(DrugDetailBean detailBean) {
        MedicineTimeBean timeBean = detailBean.getMedicine_time();
        if (timeBean != null){
            String time = timeBean.getTime().substring(0, 5);
            mTvTime.setText(time);
        }

        List<MedicineDatesBean> dates = detailBean.getMedicine_dates();
        SparseArray<MedicineDatesBean> array = new SparseArray<>();
        for (MedicineDatesBean bean : dates) {
            array.put(bean.getMedicine_id(), bean);
        }
        adapter.setDateForDrug(array);
        List<MedicineNamesBean> namesBeanList = detailBean.getMedicine_names();
        if (!mDrugList.isEmpty()) mDrugList.clear();
        mDrugList.addAll(namesBeanList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateTimeSucceed(String time) {
        mTvTime.setText(time);
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
