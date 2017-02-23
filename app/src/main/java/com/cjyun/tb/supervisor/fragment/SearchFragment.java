package com.cjyun.tb.supervisor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.Presenter.SuSearchPresenter;
import com.cjyun.tb.supervisor.activity.SuAllPatientActivity;
import com.cjyun.tb.supervisor.activity.SuPatientNewsActivity;
import com.cjyun.tb.supervisor.adapter.AllSuAdapter;
import com.cjyun.tb.supervisor.bean.DB_Bean;
import com.cjyun.tb.supervisor.custom.SearchEditText;
import com.cjyun.tb.supervisor.data.SuInjection;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;


public class SearchFragment extends SuBaseFragment implements Contract.SearchView {
    @Bind(R.id.include_fragment_su_list_item_1)
    LinearLayout include_fragment_su_list_item_1;
    @Bind(R.id.include_fragment_su_list_item_2)
    LinearLayout include_fragment_su_list_item_2;
    @Bind(R.id.include_fragment_su_list_item_3)
    LinearLayout include_fragment_su_list_item_3;
    @Bind(R.id.include_fragment_su_list_item_4)
    LinearLayout include_fragment_su_list_item_4;
    @Bind(R.id.include_fragment_su_list_item_5)
    LinearLayout include_fragment_su_list_item_5;
    @Bind(R.id.sedit_su_main_seek)
    SearchEditText seditSuMainSeek;
    @Bind(R.id.lv_su_search_listview)
    ListView lvSuSearchListview;
    @Bind(R.id.ll_su_patient_time)
    LinearLayout llSuPatientTime;
    @Bind(R.id.search_su_refresh)
    SwipeRefreshLayout searchSuRefresh;

    private SuSearchPresenter mPresent;
    private String jpush_id;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        jpush_id = JPushInterface.getRegistrationID(mContext);

        if (mPresent == null) {
            mPresent = new SuSearchPresenter(
                    SuInjection.provideTwoRepository(), this);
        }
        //   mPresent.addPt();
        // mPresent.onLoadAllPatient();

        // TODO 设置人数
        //  mPresent.onReasSQLAllPt(null);// 触发加载
        ((TextView) include_fragment_su_list_item_1.findViewById(R.id.tv_fragment_su_list_item_name)).setText("所有患者");
        // mPresent.onReadSQL(null, 1);
        ((TextView) include_fragment_su_list_item_2.findViewById(R.id.tv_fragment_su_list_item_name)).setText("近一月");
        // mPresent.onReadSQL(null, 3);
        ((TextView) include_fragment_su_list_item_3.findViewById(R.id.tv_fragment_su_list_item_name)).setText("近三月");
        // mPresent.onReadSQL(null, 6);
        ((TextView) include_fragment_su_list_item_4.findViewById(R.id.tv_fragment_su_list_item_name)).setText("近半年");
        //  mPresent.onReadSQL(null, 12);
        ((TextView) include_fragment_su_list_item_5.findViewById(R.id.tv_fragment_su_list_item_name)).setText("近一年");


        // 刷新数据
        searchSuRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        searchSuRefresh.setDistanceToTriggerSync(400);
        searchSuRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // mPresent.isLoad(true);
                //  mPresent.onLoadAllPatient();
                // mPresent.loadingRemoteData(true);
                mPresent.loadingRemoteData(false);
               // mPresent.onSubscribe();

            }
        });

        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {

        include_fragment_su_list_item_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(1,null);
            }
        });
        include_fragment_su_list_item_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(2,null);
            }
        });
        include_fragment_su_list_item_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(3,null);
            }
        });
        include_fragment_su_list_item_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(4,null);
            }
        });
        include_fragment_su_list_item_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(5,null);
            }
        });

        seditSuMainSeek.setOnInitClickListener(new SearchEditText.OnInitClickListener() {

            @Override
            public void onClick(String name, LinearLayout linearLayout) {
                // TODO 设置一个值来记录是点击了谁
               // mPresent.onReasSQLAllPt(name);
                myStartActivity(1,name);



               /* lvSuSearchListview.setVisibility(View.VISIBLE);
                llSuPatientTime.setVisibility(View.GONE);

                linearLayout.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/
            }
        });
    }

    public void myStartActivity(int value,String name) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SuAllPatientActivity.class);
        intent.putExtra("suAllActivity", value);
        intent.putExtra("suAllString",name);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresent.loadingRemoteData(false);
        mPresent.onPostJpushID(jpush_id);
    }

    /**
     * 设置对应的数据
     *
     * @param list
     */
    @Override
    public void allPatient(final List list) {


        ((TextView) include_fragment_su_list_item_1.findViewById(R.id.tv_fragment_su_list_item_size)).
                setText("(" + (list.size() != 0 ? list.size() : 0) + "人)");

        if (list != null) {
            lvSuSearchListview.setAdapter(new AllSuAdapter(getActivity(), list));
        }

        // 设置listView的点击事件 并传递id
        lvSuSearchListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), SuPatientNewsActivity.class);
                intent.putExtra("suPatutentDetails", ((DB_Bean) list.get(i)).getId());
                startActivity(intent);
            }
        });
    }

    public void setLoadingIndicator(final boolean b) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) getView().findViewById(R.id.search_su_refresh);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(b);
            }
        });
    }

    @Override
    public void oneMonth(List list) {

        ((TextView) include_fragment_su_list_item_2.findViewById(R.id.tv_fragment_su_list_item_size)).
                setText("(" + (list.size() != 0 ? list.size() : 0) + "人)");
    }

    @Override
    public void trimester(List list) {
        ((TextView) include_fragment_su_list_item_3.findViewById(R.id.tv_fragment_su_list_item_size)).
                setText("(" + (list.size() != 0 ? list.size() : 0) + "人)");
    }

    @Override
    public void sixMonth(List list) {

        ((TextView) include_fragment_su_list_item_4.findViewById(R.id.tv_fragment_su_list_item_size)).
                setText("(" + (list.size() != 0 ? list.size() : 0) + "人)");
    }

    @Override
    public void year(List list) {
        ((TextView) include_fragment_su_list_item_5.findViewById(R.id.tv_fragment_su_list_item_size)).
                setText("(" + (list.size() != 0 ? list.size() : 0) + "人)");
    }
}
