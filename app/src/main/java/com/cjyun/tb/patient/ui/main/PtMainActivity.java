package com.cjyun.tb.patient.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseActivity;
import com.cjyun.tb.patient.custom.UITableBottom;
import com.cjyun.tb.patient.service.AlarmClockService;
import com.cjyun.tb.patient.ui.binding.AddBoxActivity;
import com.cjyun.tb.patient.ui.improve.ImproveInfoActivity;
import com.cjyun.tb.patient.ui.knowledge.KnowledgeFragment;
import com.cjyun.tb.patient.ui.reset.ResetPasswordActivity;
import com.cjyun.tb.patient.util.ActivityTool;
import com.cjyun.tb.patient.util.AppUtils;
import com.cjyun.tb.patient.util.PtActivityManager;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class PtMainActivity extends BaseActivity {

    @Bind(R.id.tv_title_center)
    TextView mTvTitleCenter;
    @Bind(R.id.imgView_separator)
    ImageView mImgViewSeparator;
    @Bind(R.id.imgView_scan)
    ImageView mImgViewScan;
    @Bind(R.id.rl_actionBar_right)
    LinearLayout mRlActionBarRight;
    @Bind(R.id.rl_actionBar)
    RelativeLayout mRlayActionBar;
    @Bind(R.id.flay_content_fragment)
    FrameLayout mFlayContentFragment;
    @Bind(R.id.tabBottom_pt_main)
    UITableBottom mTabBottomPtMain;

    private int currentPosition = 0;//当前页面fragment

    private HomeFragment mHomeFragment;
    private RecordFgt mRecordFragment;
    private KnowledgeFragment mKnowledgeFragment;
    private PtMeFragment meFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        onCreatBottomBar();
        String id = JPushInterface.getRegistrationID(this);
        Log.i("push", "==================" + id);

        if (savedInstanceState == null) {
            createCurrentFragment();
        } else {
            onSolveOverlapByTag();
        }

        Intent intent = new Intent(getApplicationContext(), AlarmClockService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);

        PtActivityManager.getAppManager().finishAllException(PtMainActivity.this.getClass());
    }

    /**
     * 解决fragment重叠问题
     */
    private void onSolveOverlapByTag() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragments(transaction);
        switch (currentPosition) {
            case 0:
                mHomeFragment = (HomeFragment)
                        getSupportFragmentManager().findFragmentByTag(String.valueOf(currentPosition));
                transaction.show(mHomeFragment);
                break;
            case 1:
                mRecordFragment = (RecordFgt)
                        getSupportFragmentManager().findFragmentByTag(String.valueOf(currentPosition));
                transaction.show(mRecordFragment);
                break;
//            case 2:
//                mKnowledgeFragment = (KnowledgeFragment)
//                        getSupportFragmentManager().findFragmentByTag(String.valueOf(currentPosition));
//                transaction.show(mKnowledgeFragment);
//                break;
            case 2:
                meFragment = (PtMeFragment)
                        getSupportFragmentManager().findFragmentByTag(String.valueOf(currentPosition));
                transaction.show(meFragment);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 创建底部栏
     */
    private void onCreatBottomBar() {
        HashMap<Integer, Integer[]> hashMap = new HashMap<>();
        hashMap.put(0, new Integer[]{R.drawable.ic_uibottom_medicine_pre,
                R.drawable.ic_uibottom_medicine_nor, R.string.aty_ptMain_medicine});
        hashMap.put(1, new Integer[]{R.drawable.ic_uibottom_record_pre,
                R.drawable.ic_uibottom_record_nor, R.string.aty_ptMain_record});
        // TODO　设置知识的展示
//        hashMap.put(2, new Integer[]{R.drawable.ic_uibottom_know_pre,
//                R.drawable.ic_uibottom_know_nor, R.string.aty_ptMain_knowledge});
        hashMap.put(2, new Integer[]{R.drawable.ic_uibottom_me_pre,
                R.drawable.ic_uibottom_me_nor, R.string.aty_ptMain_me});
        mTabBottomPtMain.setmViewPager(this, currentPosition, hashMap);
    }

    /**
     * 创建当前fragment
     */
    private void createCurrentFragment() {
        ActivityTool.addFragmentOnly(
                getSupportFragmentManager(),
                getItem(currentPosition),
                R.id.flay_content_fragment,
                String.valueOf(currentPosition));
    }

    public Fragment getItem(int position) {
        setTitleContent(String.valueOf(position));
        switch (position) {
            case 0:
                if (mHomeFragment == null)
                    mHomeFragment = HomeFragment.newInstance();
                return mHomeFragment;
            case 1:
                if (mRecordFragment == null)
                    mRecordFragment = RecordFgt.newInstance();
                return mRecordFragment;
//            case 2:
//                if (mKnowledgeFragment == null) {
//                    mKnowledgeFragment = KnowledgeFragment.newInstance();
//                }
//                return mKnowledgeFragment;
            case 2:
                if (meFragment == null) {
                    meFragment = PtMeFragment.newInstance();
                }
                return meFragment;
            default:
                return null;
        }
    }

    /**
     * 底部栏滑动监听加载
     *
     * @param i
     * @param b
     */
    public void setCurrentItem(int i, boolean b) {
        if (i != currentPosition) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            hideAllFragments(fragmentTransaction);
            switch (i) {
                case 0:
                    addOrShowFragment(i, fragmentTransaction, mHomeFragment, String.valueOf(i));
                    break;
                case 1:
                    addOrShowFragment(i, fragmentTransaction, mRecordFragment, String.valueOf(i));
                    break;
//                case 2:
//                    addOrShowFragment(i, fragmentTransaction, mKnowledgeFragment, String.valueOf(i));
//                    break;
                case 2:
                    addOrShowFragment(i, fragmentTransaction, meFragment, String.valueOf(i));
                    break;
                default:
                    break;
            }
            fragmentTransaction.commit();
            currentPosition = i;
        }
    }

    private void addOrShowFragment(int i, FragmentTransaction fragmentTransaction, Fragment fragment, String tag) {
        if (fragment == null) {
            fragmentTransaction.add(R.id.flay_content_fragment, getItem(i), tag);
        } else {
            fragmentTransaction.show(fragment);
            setTitleContent(tag);
        }
    }

    /**
     * 设置标题栏 标题
     *
     * @param tag 指标
     */
    private void setTitleContent(String tag) {
        switch (tag) {
            case "0":
                mTvTitleCenter.setText(getString(R.string.aty_ptMain_medicine));
                break;
            case "1":
                mTvTitleCenter.setText(getString(R.string.aty_ptMain_record));
                break;
//            case "2":
//                mTvTitleCenter.setText(getString(R.string.aty_ptMain_knowledge));
//                break;
            case "2":
                mTvTitleCenter.setText(getString(R.string.aty_ptMain_me));
                break;
            default:
                break;
        }
    }

    private void hideAllFragments(FragmentTransaction fragmentTransaction) {
        if (mHomeFragment != null)
            fragmentTransaction.hide(mHomeFragment);
        if (mRecordFragment != null)
            fragmentTransaction.hide(mRecordFragment);
        if (mKnowledgeFragment != null)
            fragmentTransaction.hide(mKnowledgeFragment);
        if (meFragment != null)
            fragmentTransaction.hide(meFragment);
    }

    /**
     * 弹出框
     */
    @OnClick(R.id.imgView_scan)
    public void onPopUpOverflow() {
        popUpMyOverflow();
    }

    private void popUpMyOverflow() {
        View ppw = getLayoutInflater().inflate(R.layout.ppw_pt_main_add, null);
        final PopupWindow popupWindow = new PopupWindow(ppw, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        LinearLayout boxLy = (LinearLayout) ppw.findViewById(R.id.ly_ppw_main_box);
        LinearLayout infoLy = (LinearLayout) ppw.findViewById(R.id.ly_ppw_main_info);
        LinearLayout dirLy = (LinearLayout) ppw.findViewById(R.id.ly_ppw_main_dir);

        boxLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJumpToNewActivity(PtMainActivity.this, AddBoxActivity.class);
                popupWindow.dismiss();
            }
        });
        infoLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJumpToNewActivity(PtMainActivity.this, ImproveInfoActivity.class);
                popupWindow.dismiss();
            }
        });
        dirLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJumpToNewActivity(PtMainActivity.this, ResetPasswordActivity.class);
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_ppw_main_add_border));
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(mImgViewScan, Gravity.RIGHT | Gravity.TOP,
                AppUtils.Dp2Px(this, 8f),
                AppUtils.getStatusBarHeight(this) +
                        AppUtils.getActionBarHeight(this) - 25);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
