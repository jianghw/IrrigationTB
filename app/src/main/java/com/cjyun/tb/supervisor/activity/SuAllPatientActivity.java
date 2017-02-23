package com.cjyun.tb.supervisor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.Presenter.SuAllPresenter;
import com.cjyun.tb.supervisor.adapter.AllSuAdapter;
import com.cjyun.tb.supervisor.bean.DB_Bean;
import com.cjyun.tb.supervisor.custom.SearchEditText;
import com.cjyun.tb.supervisor.data.SuInjection;
import com.socks.library.KLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class SuAllPatientActivity extends SuBaseActivity implements Contract.SuAllView<Contract.SuAllPresenter> {


    @Bind(R.id.edit_su_all_query)
    SearchEditText editSuAllQuery;
    @Bind(R.id.tv_su_all_heading)
    TextView tvSuAllHeading;
    private ListView mListView;

    private List<DB_Bean> mData;
    private Contract.SuAllPresenter mPresenter;
    public String pName;
    public int isMonth = 0;

    @Override
    protected void initView() {
        activitycontroller();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {

        editSuAllQuery.setOnInitClickListener(new SearchEditText.OnInitClickListener() {

            @Override
            public void onClick(String name, LinearLayout linearLayout) {
                //  设置一个值来记录是点击了谁
                pName = name;

                mPresenter.getPtNumber(name, isMonth);
                if (mData != null) {
                    mListView.setAdapter(new AllSuAdapter(getBaseContext(), mData));
                }
                // 键盘隐藏
                linearLayout.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //  TODO 这里需要上传id 加载对应的数据  触发加载
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), SuPatientNewsActivity.class);
                intent.putExtra("suPatutentDetails", mData.get(i).getId());
                KLog.d(mData.get(i).getId() + "--------------id---------");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        ButterKnife.bind(this);
    }

    /**
     * 设置跳转
     */
    private void activitycontroller() {

        Intent intent = getIntent();

        int suAllActivity = intent.getIntExtra("suAllActivity", 1);
        String name = intent.getStringExtra("suAllString");

        switch (suAllActivity) {
            // TODO  根据不同的页面加载数据
            case 1:
                setContentView(R.layout.activity_su_all_patient);
                initActionBar(this, true, false, 3);
                titleLeft.setText(R.string.allSuActivity_title);
                ButterKnife.bind(this);
                tvSuAllHeading.setText(R.string.suAllActivity_all);
                initviews();
                mPresenter.getPtNumber(name, 0);
                isMonth = 0;
                break;
            case 2:

                setContentView(R.layout.activity_su_all_patient);
                initActionBar(this, true, false, 3);
                titleLeft.setText(R.string.suAllActivity_one_month);
                ButterKnife.bind(this);
                tvSuAllHeading.setText(R.string.suAllActivity_one_month);
                initviews();
                mPresenter.getPtNumber(null, 1);
                isMonth = 1;
                break;
            case 3:
                setContentView(R.layout.activity_su_all_patient);
                initActionBar(this, true, false, 3);
                titleLeft.setText(R.string.suAllActivity_three_month);
                ButterKnife.bind(this);
                tvSuAllHeading.setText(R.string.suAllActivity_three_month);
                initviews();
                mPresenter.getPtNumber(null, 3);
                isMonth = 3;
                break;
            case 4:
                setContentView(R.layout.activity_su_all_patient);
                initActionBar(this, true, false, 3);
                titleLeft.setText(R.string.suAllActivity_six_month);
                ButterKnife.bind(this);
                tvSuAllHeading.setText(R.string.suAllActivity_six_month);
                initviews();
                mPresenter.getPtNumber(null, 6);
                isMonth = 6;
                break;
            case 5:
                setContentView(R.layout.activity_su_all_patient);
                initActionBar(this, true, false, 3);
                titleLeft.setText(R.string.suAllActivity_year);
                ButterKnife.bind(this);
                tvSuAllHeading.setText(R.string.suAllActivity_year);
                initviews();
                mPresenter.getPtNumber(null, 12);
                isMonth = 12;
                break;
        }
    }

    public void initviews() {

        mListView = (ListView) findViewById(R.id.lv_su_all_patient);
        mPresenter = new SuAllPresenter(
                SuInjection.provideTwoRepository(), this);
    }

    @Override
    public void succeed(List<DB_Bean> bean) {

        mData = bean;
        initDatas();
    }

    @Override
    public void error() {

        Toast.makeText(getBaseContext(), "查询数据出错", Toast.LENGTH_SHORT).show();
    }

    private void initDatas() {
        if (mData != null) {
            mListView.setAdapter(new AllSuAdapter(this, mData));
        } else {
            Toast.makeText(getBaseContext(), "暂无数据", Toast.LENGTH_LONG).show();
        }
    }
}
