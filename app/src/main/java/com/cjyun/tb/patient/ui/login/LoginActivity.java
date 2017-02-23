package com.cjyun.tb.patient.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.base.BaseActivity;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.custom.OsiEditText;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.ui.binding.AddBoxActivity;
import com.cjyun.tb.patient.ui.improve.ImproveInfoActivity;
import com.cjyun.tb.patient.ui.reset.ResetPasswordActivity;

import com.cjyun.tb.patient.util.ToastUtils;
import com.cjyun.tb.supervisor.suImprove.SuImproveInfoActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/21 0021<br/>
 */
public class LoginActivity extends BaseActivity implements LoginContract.ILoginView {

    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLoutActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.oEdit_login_userName)
    OsiEditText mOEditLoginUserName;
    @Bind(R.id.oEdit_login_passWord)
    OsiEditText mOEditLoginPassWord;
    @Bind(R.id.tv_login_forgetPass)
    TextView mTvLoginForgetPass;
    @Bind(R.id.btn_login)
    Button mBtnLogin;

    private LoginContract.ILoginPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pt_login);
        ButterKnife.bind(this);

        if (null == mPresenter) {
            mPresenter = new LoginAtyPresenter(Injection.provideTwoRepository(), this);
        }

        if (TbApp.instance().getCUR_USER().equals(TbGlobal.JString.PATIENT_USER)) {
            //患者
            mTvTitleLeft.setText(getString(R.string.aty_login_patient));
        } else {
            //督导员
            mTvTitleLeft.setText(getString(R.string.aty_login_director));
        }

        mOEditLoginUserName.setTitle(getString(R.string.aty_login_account));
        mOEditLoginPassWord.setTitle(getString(R.string.aty_login_passWord));
        //TODO debug

       /*mOEditLoginUserName.setEtContent("222222222222222222");
        mOEditLoginPassWord.setEtContent("222222222");*/
        mOEditLoginUserName.setEtContent("111111111111111111");
        mOEditLoginPassWord.setEtContent("111111111");

        //初始化眼睛
        mOEditLoginPassWord.initTail();
        mOEditLoginUserName.setContentHint(getString(R.string.aty_login_nameHint));
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
        dismissDialog();
    }

    /**
     * 上方回退键操作
     */
    @OnClick(R.id.ll_actionBar_back)
    public void onBackFragment() {
        finish();
    }

    /**
     * 登陆
     */
    @OnClick(R.id.btn_login)
    public void onUserLogin() {
        mPresenter.remindToSign();
    }

    /**
     * 跳转重设密码页
     */
    @OnClick(R.id.tv_login_forgetPass)
    public void onResetPassword() {
        onJumpToNewActivity(this, ResetPasswordActivity.class);
    }

    @Override
    public String getUserName() {
        return mOEditLoginUserName.getEditText();
    }

    @Override
    public String getUserPassword() {
        return mOEditLoginPassWord.getEditText();
    }

    @Override
    public void loadingDialog() {
        createLoadingDialog();
    }

    @Override
    public void dismissDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void showToastMessage(int id) {
        ToastUtils.showShort(id);
    }

    @Override
    public void onToNewActivity(int i) {
        //TODO 跳转控制器
        switch (i) {
            case TbGlobal.IntHandler.HANDLER_FGT_LOGIN_BOX:
                onJumpToNewActivity(this, AddBoxActivity.class);
                break;
            case TbGlobal.IntHandler.HANDLER_FGT_LOGIN_PATIENT:
                onJumpToNewActivity(this, ImproveInfoActivity.class);
                break;
            case TbGlobal.IntHandler.HANDLER_FGT_LOGIN_DIRECTOR://督导员
                onJumpToNewActivity(this, SuImproveInfoActivity.class);
                break;
            default:
                break;
        }
    }
}
