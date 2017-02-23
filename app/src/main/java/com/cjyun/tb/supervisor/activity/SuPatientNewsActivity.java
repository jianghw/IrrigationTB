package com.cjyun.tb.supervisor.activity;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.Presenter.SuPatientNewsPresenter;
import com.cjyun.tb.supervisor.adapter.MyViewPagerAdapter;
import com.cjyun.tb.supervisor.bean.PatitentDetailsBean;
import com.cjyun.tb.supervisor.data.SuInjection;
import com.cjyun.tb.supervisor.fragment.SuPatitentDetailsFragment;
import com.cjyun.tb.supervisor.fragment.SuVisitEventFgt_Cp;
import com.cjyun.tb.supervisor.fragment.SubsequentVisitFragment;
import com.cjyun.tb.supervisor.fragment.TackPillsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/29 0029.
 * <p/>
 * 患者的基本详情
 */
public class SuPatientNewsActivity extends SuBaseActivity implements Contract.PatientNewsView {

    @Bind(R.id.iv_su_news_head)
    ImageView ivSuNewsHead;
    @Bind(R.id.tv_su_news_name)
    TextView tvSuNewsName;
    @Bind(R.id.tv_su_news_sex)
    TextView tvSuNewsSex;
    @Bind(R.id.tv_su_news_phone)
    TextView tvSuNewsPhone;
    @Bind(R.id.tv_su_news_identity_card)
    TextView tvSuNewsIdentityCard;
    private ViewPager mviewPager;
    private TextView mVisit;
    private TextView mSubsequentVisit;
    private TextView mTackPills;
    private TextView mBaseNews;

    private List<Fragment> mDataFragment;
    private ImageView mIndicator;
    private int mCurrentPageIndex = 0;
    private int width;
    private SuPatientNewsPresenter mPresenter;
    private int id;
    private PatitentDetailsBean bean;
    private SuPatitentDetailsFragment mBaseNewsFragment;


    @Override
    protected void initView() {

        setContentView(R.layout.activity_su_patient_news);
        initActionBar(this, true, true, 3);
        titleLeft.setText(R.string.suPatientNews_title);
        mDataFragment = new ArrayList<>();

        mBaseNews = (TextView) findViewById(R.id.suPatientNews_indicator_baseNews);
        mTackPills = (TextView) findViewById(R.id.suPatientNews_indicator_tackPills);
        mSubsequentVisit = (TextView) findViewById(R.id.suPatientNews_indicator_subsequentVisit);
        mVisit = (TextView) findViewById(R.id.suPatientNews_indicator_visit);
        mIndicator = (ImageView) findViewById(R.id.iv_su_patient_news_indicator);
        mviewPager = (ViewPager) findViewById(R.id.vp_su_patient_news);

        mBaseNewsFragment = new SuPatitentDetailsFragment();

        TackPillsFragment tackPillsFragment = new TackPillsFragment();
        SubsequentVisitFragment subsequentVisitFragment = new SubsequentVisitFragment();
        // SuVisitEventFgt visitFragment = new SuVisitEventFgt();
        SuVisitEventFgt_Cp visitFragment = new SuVisitEventFgt_Cp();


        mDataFragment.add(mBaseNewsFragment);
        mDataFragment.add(tackPillsFragment);
        mDataFragment.add(subsequentVisitFragment);
        mDataFragment.add(visitFragment);

        mviewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), mDataFragment));

        // 设置三个缓存界面
        mviewPager.setOffscreenPageLimit(3);

        getWidth();
    }

    @Override
    public void initData() {

        Intent intent = getIntent();
        id = intent.getIntExtra("suPatutentDetails", 1);

        mPresenter = new SuPatientNewsPresenter(
                SuInjection.provideTwoRepository(), this);

        mPresenter.onLoadPtDetails(id); // TODO  加载数据
    }

    @Override
    protected void initListener() {

        mBaseNews.setOnClickListener(new MyOnClickListener(0));
        mTackPills.setOnClickListener(new MyOnClickListener(1));
        mSubsequentVisit.setOnClickListener(new MyOnClickListener(2));
        mVisit.setOnClickListener(new MyOnClickListener(3));

        mviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                // 左右滑动时 指示器的变化
                if (mCurrentPageIndex == 0 && position == 0) {
                    lp.leftMargin = (int) (mCurrentPageIndex * width + positionOffset * width);
                } else if (mCurrentPageIndex == 1 && position == 0) {
                    lp.leftMargin = (int) (mCurrentPageIndex * width + (positionOffset - 1) * width);
                } else if (mCurrentPageIndex == 1 && position == 1) {
                    lp.leftMargin = (int) (mCurrentPageIndex * width + positionOffset * width);
                } else if (mCurrentPageIndex == 2 && position == 1) {
                    lp.leftMargin = (int) (mCurrentPageIndex * width + (positionOffset - 1) * width);
                } else if (mCurrentPageIndex == 2 && position == 2) {
                    lp.leftMargin = (int) (mCurrentPageIndex * width + positionOffset * width);
                } else if (mCurrentPageIndex == 3 && position == 2) {
                    lp.leftMargin = (int) (mCurrentPageIndex * width + (positionOffset - 1) * width);
                }
                mIndicator.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        mBaseNews.setTextColor(Color.WHITE);
                        // 设置右边显示
                        titleRight.setVisibility(View.GONE);
                        break;
                    case 1:
                        mTackPills.setTextColor(Color.WHITE);
                        titleRight.setVisibility(View.VISIBLE);
                        titleRight.setText("添加访视");

                        break;
                    case 2:
                        mSubsequentVisit.setTextColor(Color.WHITE);
                        titleRight.setVisibility(View.VISIBLE);
                        titleRight.setText("添加访视");
                        break;
                    case 3:
                        mVisit.setTextColor(Color.WHITE);
                        titleRight.setVisibility(View.VISIBLE);
                        titleRight.setText("添加访视");
                        break;
                }
                mCurrentPageIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        titleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO  设置添加访问事件的点击事件

                Intent intent = new Intent(getBaseContext(), SuAddVisitActivity.class);
                intent.putExtra("SuAddVisitActivity", id);
                startActivity(intent);
            }
        });
    }

    private void resetTextView() {
        mBaseNews.setTextColor(Color.BLACK);
        mTackPills.setTextColor(Color.BLACK);
        mSubsequentVisit.setTextColor(Color.BLACK);
        mVisit.setTextColor(Color.BLACK);

    }

    /**
     * 获取屏幕的宽度
     */
    public void getWidth() {
/*
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();*/

        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        width = displayMetrics.widthPixels / 4;
        ViewGroup.LayoutParams layoutParams = mIndicator.getLayoutParams();

        layoutParams.width = width;
        mIndicator.setLayoutParams(layoutParams);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        ButterKnife.bind(this);
    }

    @Override
    public void succeed(PatitentDetailsBean bean) {
        // ivSuNewsHead   头像  TODO 还没有数据
        // this.bean = bean;
        //  mBaseNewsFragment.setData(bean);

        tvSuNewsName.setText(bean.getInfo().getFull_name());
        tvSuNewsSex.setText(bean.getInfo().isGender() ? "男" : "女");
        tvSuNewsPhone.setText(bean.getInfo().getPhone_number() + "");
        tvSuNewsIdentityCard.setText(bean.getUser().getId_card_number() + "");

        if (bean.getInfo().getPhoto() != null) {
            byte[] bytes = Base64.decode(bean.getInfo().getPhoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ivSuNewsHead.setImageBitmap(bitmap);
        }

    }

    /**
     * 设置点击事件
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mviewPager.setCurrentItem(index);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public int getId() {

        return id;
    }

    public PatitentDetailsBean getBean() {

        return bean;
    }
}
