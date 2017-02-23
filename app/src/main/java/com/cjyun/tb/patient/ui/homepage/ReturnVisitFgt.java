package com.cjyun.tb.patient.ui.homepage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.adapter.ReturnVisitAdapter;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.bean.ReturnVisitBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.ui.record.IRecordContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 复诊事件
 */
public class ReturnVisitFgt extends BaseFragment implements IHomepageContract.IReturnVisitView {
    private static Handler mHandler;
    @Bind(R.id.lv_return_visit)
    ListView mLvReturnVisit;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    private Activity mActivity;
    private IRecordContract.IRecordPresenter mPresent;
    private List<ReturnVisitBean> mList;
    private ReturnVisitAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static ReturnVisitFgt newInstance(Handler handler) {
        mHandler = handler;
        return new ReturnVisitFgt();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new ReturnVisitPresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_return_visit, container, false);
        ButterKnife.bind(this, root);

        mSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        mSwipeRefresh.setDistanceToTriggerSync(400);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresent.loadingRemoteData(true);
            }
        });

        mList = new ArrayList<>();
        adapter = new ReturnVisitAdapter(mActivity, mList);
        mLvReturnVisit.setAdapter(adapter);
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
    public void setLoadingIndicator(final boolean b) {
        if (getView() == null) return;
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
    public void updateListData(List<ReturnVisitBean> been) {
        if (mList != null && !mList.isEmpty()) mList.clear();

        Collections.sort(been, new Comparator<ReturnVisitBean>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);

            @Override
            public int compare(ReturnVisitBean lhs, ReturnVisitBean rhs) {
                long time1 = 0;
                long time2 = 0;
                try {
                    time1 = sdf.parse(lhs.getSetting_date()).getTime();
                    time2 = sdf.parse(rhs.getSetting_date()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return time1 == time2 ? 0 : time1 > time2 ? -1 : 11;
            }
        });


        if (mList != null) {
            mList.addAll(been);
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onActionBarBack() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY);
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
