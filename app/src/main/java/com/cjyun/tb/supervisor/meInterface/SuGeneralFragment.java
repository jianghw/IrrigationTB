package com.cjyun.tb.supervisor.meInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.ui.splash.SplashActivity;
import com.cjyun.tb.patient.util.AppUtils;
import com.cjyun.tb.patient.util.PtActivityManager;
import com.cjyun.tb.supervisor.base.BaseFragment;
import com.cjyun.tb.supervisor.base.ConstantAll;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/6 0006.
 * <p>
 * 通用设置
 */
public class SuGeneralFragment extends BaseFragment implements MeNews.SuUpdateFragment {

    private static Handler mHandler;
   /* @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.relay_font_size)
    RelativeLayout relayFontSize;
    @Bind(R.id.relay_update)
    RelativeLayout relayUpdate;
    @Bind(R.id.relay_about_us)
    RelativeLayout relayAboutUs;
    @Bind(R.id.relay_quite_login)
    RelativeLayout relayQuiteLogin;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    */


    private Activity mActivity;
    private SuUpdatePresenter mPresent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static SuGeneralFragment newInstance(Handler handler) {
        mHandler = handler;
        return new SuGeneralFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new SuUpdatePresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_general, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onBackActivity() {

        mHandler.sendEmptyMessage(ConstantAll.ConHandlerInt.HANDLER_BACK_ACTIVITY);
    }

    @OnClick({R.id.relay_font_size, R.id.relay_update, R.id.relay_about_us, R.id.relay_quite_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relay_font_size:
                mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_FGT_FONT);
                break;
            case R.id.relay_update:
                updateApp();

                break;
            case R.id.relay_about_us:
                mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_FGT_ABOUT_US);
                break;
            case R.id.relay_quite_login:

                if (TbApp.instance().getCUR_USER().equals(TbGlobal.JString.PATIENT_USER)) {
                    AppUtils.getLruCache().evictAll();
                } else {

                }
                onJumpToNewActivity(mActivity, SplashActivity.class);
                PtActivityManager.getAppManager().finishAllException(SplashActivity.class);
                break;
        }
    }

    private AlertDialog mDialog;
    private TextView textView;

    private void updateApp() {

        View view = View.inflate(mActivity, R.layout.alert_dialog_update, null);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("正在获取最新版本信息~");
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view);
        mDialog = builder.create();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
        mPresent.updateInfo();
    }


    @Override
    public void onProgress(String v) {
        textView.setText("正在下载中~" + v);
    }

    @Override
    public void dismissLoding() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }


    @Override
    public void showIsNewVerCode() {

    }

    @Override
    public void showToastMessage(int id) {

    }

    @Override
    public void loadingDialog() {

    }

    @Override
    public void dismissDialog() {

    }
}
