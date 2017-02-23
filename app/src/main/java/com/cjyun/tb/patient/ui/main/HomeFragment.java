package com.cjyun.tb.patient.ui.main;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.adapter.DrugAdapter;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.bean.BindBoxBean;
import com.cjyun.tb.patient.bean.MedicineNamesBean;
import com.cjyun.tb.patient.bean.MedicineTimeBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.ui.homepage.HomePageActivity;
import com.cjyun.tb.patient.ui.homepage.IHomepageContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by jianghw on 2016/3/23 0023.
 * Description
 */
public class HomeFragment extends BaseFragment implements IHomepageContract.IHomepageView {
    @Bind(R.id.tv_medic_date)
    TextView mTvMedicDate;
    /*    @Bind(R.id.tv_medic_week)
        TextView mTvMedicWeek;*/
    @Bind(R.id.tv_medic_name)
    TextView mTvMedicName;
    @Bind(R.id.imgView_medic_online)
    ImageView mImgViewMedicOnline;
    @Bind(R.id.tv_medic_online)
    TextView mTvMedicOnline;
    @Bind(R.id.imgView_medic_time)
    ImageView mImgViewMedicTime;
    @Bind(R.id.tv_medic_time)
    TextView mTvMedicTime;
    @Bind(R.id.tv_medic_prompt)
    TextView mTvMedicPrompt;
    @Bind(R.id.lv_medic_drug)
    ListView mLvMedicDrug;
    @Bind(R.id.tv_medic_next)
    TextView mTvMedicNext;
    @Bind(R.id.ll_medic_follow)
    LinearLayout mLlMedicFollow;
    @Bind(R.id.ll_medic_visit)
    LinearLayout mLlMedicVisit;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.ll_set_drug)
    LinearLayout mLrlaySetDrug;

    private Activity mActivity;
    private IHomepageContract.IHomepagePresenter mPresent;
    private ArrayList<MedicineNamesBean> mDrugList;
    private DrugAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new HomePresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_medicine, container, false);
        ButterKnife.bind(this, root);

        onTimeDisplay();
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
        adapter = new DrugAdapter(mActivity, mDrugList);
        mLvMedicDrug.setAdapter(adapter);
        return root;
    }

    @Override
    public String getPushId(){
        String id = JPushInterface.getRegistrationID(mActivity);
        return id;
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

    /**
     * 设置药物时间
     */
    @OnClick(R.id.ll_set_drug)
    public void onSetDrugFragment() {
        onJumpTargetPage(mActivity, HomePageActivity.class, TbGlobal.JString.TIME_DRUG_TAG);
    }

    /**
     * 访视事件
     */
    @OnClick(R.id.ll_medic_follow)
    public void onVisitEvent() {
        onJumpTargetPage(mActivity, HomePageActivity.class, TbGlobal.JString.VISIT_EVENT_TAG);
    }

    /**
     * 复诊事件
     */
    @OnClick(R.id.ll_medic_visit)
    public void onReturnVisit() {
        onJumpTargetPage(mActivity, HomePageActivity.class, TbGlobal.JString.RETURN_VISIT_TAG);
    }

    /**
     * 日期显示
     */
    private void onTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.SIMPLIFIED_CHINESE);
        mTvMedicDate.setText(sdf.format(new Date()));
    }

    /**
     * 加载图标
     *
     * @param flag true 加载中...
     */
    @Override
    public void setLoadingIndicator(final boolean flag) {
        if (getView() == null) return;
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(flag);
            }
        });
    }

    @Override
    public void updateListData(List<MedicineNamesBean> medicine_names) {
        if (!medicine_names.isEmpty()) {
            mLvMedicDrug.setVisibility(View.VISIBLE);
            if (!mDrugList.isEmpty()) mDrugList.clear();
            mDrugList.addAll(medicine_names);
            adapter.notifyDataSetChanged();
            mTvMedicPrompt.setVisibility(View.GONE);
            setListViewHeight(mLvMedicDrug);
        } else {
            mLvMedicDrug.setVisibility(View.GONE);
            mTvMedicPrompt.setVisibility(View.VISIBLE);
        }
    }

    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void updateOnline(BindBoxBean bean) {
        String visit = null;
        if (bean != null) visit = (String) bean.getLast_visit();
        if (visit != null) {
            try {
                String day = visit.substring(0, 10);
                String time = visit.substring(11, 19);
                StringBuilder sb = new StringBuilder();
                sb.append(day).append("/").append(time);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.SIMPLIFIED_CHINESE);

                long today = new Date().getTime();
                long t = simpleDateFormat.parse(sb.toString()).getTime();
                if (today - t >= 0 && today - t <= 60 * 1000 * 15) {
                    mImgViewMedicOnline.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_medic_online));
                    mTvMedicOnline.setText(R.string.box_online);
                } else {
                    mImgViewMedicOnline.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_medic_unline));
                    mTvMedicOnline.setText(R.string.box_unline);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateDrugTime(MedicineTimeBean medicine_time) {
        if (null != medicine_time) {
            String time = medicine_time.getTime().substring(0, 5);
            mTvMedicTime.setText(time);
        }
    }

    @Override
    public String getDrugTime() {
        return mTvMedicTime.getText().toString().trim();
    }

    @Override
    public void updateDownTime(String s) {
        mTvMedicNext.setText(s);
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
