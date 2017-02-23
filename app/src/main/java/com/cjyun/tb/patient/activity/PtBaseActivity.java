package com.cjyun.tb.patient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.constant.ConstantString;
import com.cjyun.tb.patient.util.PtActivityManager;
import com.cjyun.tb.patient.util.SharedUtils;

/**
 * Created by jianghw on 2016/3/18 0018.
 * Description
 */
public abstract class PtBaseActivity extends FragmentActivity implements ConstantString {

    protected PtBaseActivity mContext;
    protected LinearLayout actionBarBack;
    protected TextView titleLeft;
    protected TextView titleCenter;
    protected TextView titleJump;
    protected LinearLayout actionBarRight;
    protected ImageView imgScan;
    protected TextView titleRight;

    @Override
    public void setContentView(int layoutResID) {
        int mode = SharedUtils.getInteger("Theme_Font", 2);
        if (mode == 1) {
            this.setTheme(R.style.Theme_Small);
        } else if (mode == -1 || mode == 2) {
            this.setTheme(R.style.Theme_Medium);
        } else if (mode == 3) {
            this.setTheme(R.style.Theme_Large);
        }
        super.setContentView(layoutResID);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        PtActivityManager.getAppManager().addActivity(this);

        initView(savedInstanceState);
        initData();
        initListener();
    }

    protected abstract void initView(@Nullable Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void initListener();

    /**
     * @param activity
     * @param left     左侧回退键
     * @param right    右侧图标键
     * @param flag     标记
     */
    protected void initActionBar(final Activity activity, boolean left, boolean right, int flag) {
        actionBarBack = (LinearLayout) findViewById(R.id.ll_actionBar_back);
        titleLeft = (TextView) findViewById(R.id.tv_title_left);
        titleCenter = (TextView) findViewById(R.id.tv_title_center);
        titleJump = (TextView) findViewById(R.id.tv_title_jump);

        actionBarRight = (LinearLayout) findViewById(R.id.rl_actionBar_right);
        imgScan = (ImageView) actionBarRight.findViewById(R.id.imgView_scan);
        titleRight = (TextView) actionBarRight.findViewById(R.id.tv_title_right);

        //点击时返回上一界面
        actionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        if (left) {
            titleCenter.setVisibility(View.GONE);
        } else {//中间大标题时
            titleLeft.setVisibility(View.GONE);
            imgScan.setImageResource(R.drawable.ic_add_popup_main);
        }
        actionBarRight.setVisibility(View.GONE);

        if (right) {
            actionBarRight.setVisibility(View.VISIBLE);
            if (flag == 1) {
                titleRight.setVisibility(View.GONE);
                titleJump.setVisibility(View.VISIBLE);
                titleJump.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//跳转页面至~
                    }
                });
            } else if (flag == 2) {
                actionBarBack.setVisibility(View.GONE);
                titleRight.setVisibility(View.GONE);
            } else if (flag == 3) {
                imgScan.setVisibility(View.GONE);
                titleRight.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onDestroy() {
        PtActivityManager.getAppManager().finishActivity(this);
        super.onDestroy();
        TbApp.getRefWatcher().watch(this);
    }


}
