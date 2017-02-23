package com.cjyun.tb.supervisor.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.Presenter.SuTackPillsPresenter;
import com.cjyun.tb.supervisor.activity.SuPatientNewsActivity;
import com.cjyun.tb.supervisor.adapter.TpCycleAdapter;
import com.cjyun.tb.supervisor.bean.PieItemBean;
import com.cjyun.tb.supervisor.bean.TackPillsBean;
import com.cjyun.tb.supervisor.bean.TackPillsCycleBean;
import com.cjyun.tb.supervisor.config.Constants;
import com.cjyun.tb.supervisor.custom.PicChartView;
import com.cjyun.tb.supervisor.data.SuInjection;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/3 0003.
 * <p/>
 * 服药展示
 */
public class TackPillsFragment extends SuBaseFragment implements Contract.TackPillsView {

    @Bind(R.id.pie_su)
    PicChartView pieSu;
    @Bind(R.id.fm_su_bt_show_statistics)
    Button fmSuBtShowStatistics;
    @Bind(R.id.fm_su_bt_show_cycle)
    Button fmSuBtShowCycle;
    @Bind(R.id.tv_su_cjy_website)
    TextView tvSuCjyWebsite;
    @Bind(R.id.tv_su_tp_pill_case)
    TextView tvSuTpPillCase;
    @Bind(R.id.swipe_refresh_tcak)
    SwipeRefreshLayout swipeRefreshTcak;
    private ListView mListView;
    private LinearLayout m_Ll_Cycle;
    private List<TackPillsCycleBean> mDatas;
    private RelativeLayout m_ll_statistics;
    private PieItemBean[] src = null;

    private SuTackPillsPresenter mPresenter;
    private int id;
    private Activity mActivity;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_take_pills_statistics, container, false);

        m_ll_statistics = (RelativeLayout) view.findViewById(R.id.fm_su_ll_statistics);
        m_Ll_Cycle = (LinearLayout) view.findViewById(R.id.fm_include_history_tack_pills);
        mListView = (ListView) m_Ll_Cycle.findViewById(R.id.fm_su_lv_cycle);
        ButterKnife.bind(this, view);
        mPresenter = new SuTackPillsPresenter(
                SuInjection.provideTwoRepository(), this);

        swipeRefreshTcak.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        swipeRefreshTcak.setDistanceToTriggerSync(500);
        swipeRefreshTcak.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onTackPillsCycle(id);
            }
        });

        return view;
    }

    @Override
    protected void initData() {

        mDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TackPillsCycleBean bean = new TackPillsCycleBean();
            bean.pillsInfo = "药品" + i;
            bean.endTime = 4 + i + "";
            bean.startTime = 1 + i + "";
            mDatas.add(bean);
        }
        id = ((SuPatientNewsActivity) getActivity()).getId();

    }
    @Override
    protected void initListener() {

    }

    /**
     * 设置饼的参数
     */
    private void showPie(int yet, int not) {

        // TODO  设置饼图的数据
        src = new PieItemBean[]{
                new PieItemBean("未服药", not),
                new PieItemBean("已服药", yet)};

        if (pieSu != null)
            pieSu.setPieItems(src);
    }

    /**
     * 服药周期 和服药统计的切换
     *
     * @param view
     */
    @OnClick({R.id.fm_su_bt_show_statistics, R.id.fm_su_bt_show_cycle})
    public void onClick(View view) {
        switch (view.getId()) {
            // 服药统计
            case R.id.fm_su_bt_show_statistics:

                m_ll_statistics.setVisibility(View.VISIBLE);
                m_Ll_Cycle.setVisibility(View.GONE);
                fmSuBtShowStatistics.setBackground(getResources().getDrawable(R.drawable.bg_tack_pill_btn_left));
                fmSuBtShowCycle.setBackground(getResources().getDrawable(R.drawable.bg_tack_pills_btn_white));
                fmSuBtShowStatistics.setTextColor(Color.WHITE);
                fmSuBtShowCycle.setTextColor(Color.BLACK);
                break;
//            服药周期
            case R.id.fm_su_bt_show_cycle:

                m_ll_statistics.setVisibility(View.GONE);
                m_Ll_Cycle.setVisibility(View.VISIBLE);
                fmSuBtShowCycle.setBackground(getResources().getDrawable(R.drawable.bg_tack_pill_btn));
                fmSuBtShowStatistics.setBackground(getResources().getDrawable(R.drawable.bg_tack_pills_btn_left_white));
                fmSuBtShowCycle.setTextColor(Color.WHITE);
                fmSuBtShowStatistics.setTextColor(Color.BLACK);
                break;
        }
    }

    /**
     * 加载成功 设置数据
     */
    @Override
    public void tackPillsSucceed(List<TackPillsBean> list) {
        int not = 0; //未服药
        int yet = 0; //已服药
        TackPillsBean tackPillsBean = null;
        for (int i = 0; i < list.size(); i++) {

            tackPillsBean = list.get(i);
            // TODO  数据解析  ---需要处理
            if (tackPillsBean.isTaken()) {
                yet++;
            } else {
                not++;
            }
        }
        showPie(yet, not);
        // TextView mPillCase = (TextView) (m_ll_statistics.findViewById(R.id.tv_su_tp_pill_case));

        tvSuTpPillCase.setText("药盒ID " + tackPillsBean.getMedicine_id() + "");
        TpCycleAdapter tpCycleAdapter = new TpCycleAdapter(getActivity(), list);
        tpCycleAdapter.setData(yet, not);
        mListView.setAdapter(tpCycleAdapter);
    }

    @OnClick(R.id.tv_su_cjy_website)
    public void onClick() {

        Intent intent = new Intent();
        intent.setData(Uri.parse(Constants.Url.BASE_RUL));
        intent.setAction(Intent.ACTION_VIEW);
        this.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onTackPillsCycle(id);
    }

    @Override
    public void refresh(final boolean b) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_tcak);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(b);
            }
        });
    }

}
