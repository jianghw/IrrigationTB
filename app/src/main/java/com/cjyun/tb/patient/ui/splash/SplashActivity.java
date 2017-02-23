package com.cjyun.tb.patient.ui.splash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.base.BaseActivity;
import com.cjyun.tb.patient.bean.UpdateBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.ui.login.LoginActivity;
import com.cjyun.tb.patient.ui.main.PtMainActivity;
import com.cjyun.tb.patient.util.AppUtils;
import com.cjyun.tb.patient.util.SharedUtils;
import com.cjyun.tb.supervisor.activity.SuMainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <b>@Description:</b>欢迎界面<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/21 0021<br/>
 */
public class SplashActivity extends BaseActivity implements SplashContract.ISplashView {

    @Bind(R.id.iv_welcome_logo)
    ImageView mImgViewWelcomeLogo;
    @Bind(R.id.rl_patient)
    RelativeLayout mRlPatient;
    @Bind(R.id.img_line)
    ImageView mImgLine;
    @Bind(R.id.rl_director)
    RelativeLayout mRlDirector;
    @Bind(R.id.rl_welcome_selector)
    RelativeLayout mRlWelcomeSelector;

    private SplashContract.ISplashPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (null == mPresenter) {
            mPresenter = new SplashAtyPresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onSubscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 选择框
     *
     * @param rlay 控件
     */
    @OnClick({R.id.rl_patient, R.id.rl_director})
    public void toLoginStep(RelativeLayout rlay) {
        switch (rlay.getId()) {
            case R.id.rl_patient:
                TbApp.instance().setCUR_USER(TbGlobal.JString.PATIENT_USER);
                break;
            case R.id.rl_director:
                TbApp.instance().setCUR_USER(TbGlobal.JString.DIRECTOR_USER);
                break;
            default:
                TbApp.instance().setCUR_USER(TbGlobal.JString.PATIENT_USER);
                break;
        }
        onJumpToNewActivity(this, LoginActivity.class);
    }

    @Override
    public void setSelectorBtnGone(boolean b) {
        mRlWelcomeSelector.setVisibility(b ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showDialogToUpdate(UpdateBean appInfo, final String fileFullName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        StringBuilder sb = new StringBuilder();
        sb.append(appInfo.getName()).append("有最新版本，请更新!").append("\r\n").append("\r\n");
        String size = String.valueOf(appInfo.getSize() / 1000D / 1024D).substring(0, 3);
        sb.append("软件大小：").append(size).append("M").append("\r\n");
        sb.append("版本号：").append(appInfo.getTag()).append("\r\n");
        sb.append("更新描述：").append(appInfo.getDescription());

        builder.setTitle(R.string.dialog_title_initFgt)
                .setMessage(sb.toString())
                .setNegativeButton(R.string.dialog_no_initFgt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.dialog_yes_initFgt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AppUtils.installApk(getApplicationContext(), fileFullName);
                    }
                }).setCancelable(false);
        builder.create().show();
    }


    @Override
    public void showToastMessage(int id) {
        switch (id) {
            case 100:
                //TODO 无网络时，更新缓存判断当前用户
                String cur_user = SharedUtils.getString(TbGlobal.JString.CUR_USER, TbGlobal.JString.CUR_USER);
                if (cur_user.equals(TbGlobal.JString.PATIENT_USER)) {
                    //TODO 跳转患者主页
                    onJumpToNewActivity(this, PtMainActivity.class);
                } else if (cur_user.equals(TbGlobal.JString.DIRECTOR_USER)) {
                    //TODO 跳转督导主页
                    onJumpToNewActivity(this, SuMainActivity.class);
                }
                break;
        }
    }

    @Override
    public void loadingDialog() {

    }

    @Override
    public void dismissDialog() {

    }
}
