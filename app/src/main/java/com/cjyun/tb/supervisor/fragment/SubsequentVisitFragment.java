package com.cjyun.tb.supervisor.fragment;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.Presenter.SubsequentVisitPresenter;
import com.cjyun.tb.supervisor.activity.SuPatientNewsActivity;
import com.cjyun.tb.supervisor.adapter.SubsequentVisitAdapter;
import com.cjyun.tb.supervisor.bean.SubsequentVisitBean;
import com.cjyun.tb.supervisor.data.SuInjection;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by Administrator on 2016/5/3 0003.
 * <p/>
 * 患者信息的复诊情况
 */
public class SubsequentVisitFragment extends SuBaseFragment implements Contract.SubsequentVisitView {

    private ListView mListView;
    private List<SubsequentVisitBean> mDatas;
    private SubsequentVisitPresenter mPresenter;
    private SwipeRefreshLayout mSwipeRefresh;
    private int id;
    private Activity mActivity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_subsequent_visit, container, false);

        mListView = (ListView) view.findViewById(R.id.lv_su_subsequent_visit);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

        mSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        mSwipeRefresh.setDistanceToTriggerSync(400);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                KLog.d("刷新数据--------------");
                mPresenter.onSubsequebtVisit(id);
            }
        });
        return view;
    }

    @Override
    protected void initData() {

        id = ((SuPatientNewsActivity) mActivity).getId();

        mPresenter = new SubsequentVisitPresenter(
                SuInjection.provideTwoRepository(), this);

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.onSubsequebtVisit(id);
    }

    @Override
    public void svSucceed(List list) {

        mListView.setAdapter(new SubsequentVisitAdapter(getActivity(), list));
    }

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
}
