package com.cjyun.tb.supervisor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.ui.reset.ResetPasswordActivity;
import com.cjyun.tb.patient.util.SuCreatQrCodeTool;
import com.cjyun.tb.qrcode.capture.CaptureActivity;
import com.cjyun.tb.supervisor.Presenter.SuCapturePresenter;
import com.cjyun.tb.supervisor.custom.UITableBottom;
import com.cjyun.tb.supervisor.data.SuInjection;
import com.cjyun.tb.supervisor.fragment.SearchFragment;
import com.cjyun.tb.supervisor.fragment.SuMeFragment;
import com.cjyun.tb.supervisor.suImprove.SuImproveInfoActivity;
import com.socks.library.KLog;

import java.lang.reflect.Field;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SuMainActivity extends SuBaseActivity {


    @Bind(R.id.tabBottom_ptMain)
    UITableBottom tabBottom_ptMain;

    private int currentPosition = 0;//当前页面fragment
    private View ppw;
    private PopupWindow popupWindow;
    private SuCapturePresenter mPresenter;
    private String jpush_id;
    private SearchFragment searchFragment;
    private SuMeFragment suMeFragment;


    /**
     * 设置ActionBar的显示和隐藏
     */
    protected void initView() {

        setContentView(R.layout.activity_su_main);

        tabBottom_ptMain = (UITableBottom) findViewById(R.id.tabBottom_ptMain);

        ButterKnife.bind(this);

        mPresenter = new SuCapturePresenter(
                SuInjection.provideTwoRepository(), this);

        initActionBar(this, true, true, 2);
        titleLeft.setText(R.string.suMainActivity_title);
        imgScan.setImageResource(R.drawable.ic_add_popup_main);

        // String jpush_id = JPushInterface.getRegistrationID(this);

        // 创建底部状态栏
        HashMap<Integer, Integer[]> hashMap = new GetBottonButtonData().invoke();
        tabBottom_ptMain.setmViewPager(this, currentPosition, hashMap);
    }

    protected void initData() {

        KLog.d("打印几次--------------");

        searchFragment = new SearchFragment();
        suMeFragment = new SuMeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_ptMain_fragment, searchFragment);
        ft.add(R.id.fl_ptMain_fragment, suMeFragment);
        ft.hide(suMeFragment).show(searchFragment).commit();

        // TODO 还需要进行判断   这里需要 post 一个 id 来进行推送
        // informDialog();
    }

    protected void initListener() {
        if (imgScan != null)

            imgScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openDropDownBox();
                }
            });

        tabBottom_ptMain.setChangeListener(new UITableBottom.OnUITabChangListener() {
            @Override
            public void onTabChang(int index) {

                KLog.d(index + "------对应选中的-Fragment-----");
                if (index == 0) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.hide(suMeFragment).show(searchFragment).commit();
                } else if (index == 1) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.hide(searchFragment).show(suMeFragment).commit();

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        ButterKnife.bind(this);
    }

    private class GetBottonButtonData {
        /**
         * 1、按下时图片 2、未按时图片 3、文字资源
         *
         * @return
         */
        public HashMap<Integer, Integer[]> invoke() {
            HashMap<Integer, Integer[]> hashMap = new HashMap<>();
            hashMap.put(0, new Integer[]{R.drawable.ic_uibottom_record_pre, R.drawable.ic_uibottom_record_nor, R
                    .string.suMainActivity_bottombar_mypatients});
            hashMap.put(1, new Integer[]{R.drawable.ic_uibottom_me_pre, R.drawable.ic_uibottom_me_nor, R.string
                    .suMainActivity_bottombar_my});

            return hashMap;
        }
    }

    /**
     * 底部栏滑动监听加载
     *
     * @param i
     * @param b
     */
    public void setCurrentItem(int i, boolean b) {

        KLog.d(i + "--底部栏滑动监听加载----" + b);

       /* if (i != currentPosition) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.commit();
        }*/
    }

    /**
     * 打开下拉框
     */
    private void openDropDownBox() {

        ppw = getLayoutInflater().inflate(R.layout.activity_su_add, null);
        // 设置宽高
        popupWindow = new PopupWindow(ppw, LinearLayout.LayoutParams.WRAP_CONTENT,

                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        LinearLayout boxLy = (LinearLayout) ppw.findViewById(R.id.ly_ppw_main_box);
        LinearLayout visit = (LinearLayout) ppw.findViewById(R.id.ly_ppw_main_visit);
        final LinearLayout password = (LinearLayout) ppw.findViewById(R.id.ly_ppw_main_password);

        // TODO 调用二维码扫描
        boxLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(getBaseContext(), CaptureActivity.class);
                startActivityForResult(openCameraIntent, 100);
                //隐藏popuwindow
                popupWindow.dismiss();
            }
        });
        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SuImproveInfoActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO  这里调用的是患者的接口
                Intent intent = new Intent(getBaseContext(), ResetPasswordActivity.class);
                startActivity(intent);

                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_ppw_main_add_border));
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(imgScan, Gravity.RIGHT | Gravity.TOP, Dp2Px(this, 8f), getStatusBarHeight() +
                getActionBarHeight() - 25);
    }

    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    private int getStatusBarHeight() {
        int sbHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            sbHeight = getResources().getDimensionPixelSize(x);
            //xhdpi--50px
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return sbHeight;
    }

    private int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue value = new TypedValue();
        if (getTheme().resolveAttribute(
                android.R.attr.actionBarSize, value, true)) {// 如果资源是存在的、有效的
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    value.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    // dialog 的显示
    private void informDialog() {

       /* final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View contentView = LayoutInflater.from(this).inflate(R.layout.infrom_su_dialog, null);
        //  把设定好的窗口布局放到dialog中
        dialog.setContentView(contentView);
        //  设定点击窗口空白处取消会话
        dialog.setCanceledOnTouchOutside(true);
        Button mBtDialog = (Button) contentView.findViewById(R.id.bt_dialog);
        dialog.show();

        mBtDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });*/
    }

    // 处理二维码返回的数据 TODO
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100 && data != null) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result", "ID:扫描出错~,Date:请重新扫描");
            try {
                String decrypt = SuCreatQrCodeTool.decrypt(scanResult);
                String[] split = decrypt.split(",");
                String[] split1 = split[0].split(":");
                String[] split2 = split[1].split(":");
                int id = Integer.parseInt(split1[1]);
                int pt_id = Integer.parseInt(split2[1]);
                KLog.d(id + "------二维码访问内容------" + pt_id);
                mPresenter.onActivityResult(id, pt_id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}