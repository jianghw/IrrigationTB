package com.cjyun.tb.patient.ui.general;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.ui.splash.SplashActivity;
import com.cjyun.tb.patient.util.AppUtils;
import com.cjyun.tb.patient.util.PtActivityManager;
import com.cjyun.tb.patient.util.SharedUtils;
import com.cjyun.tb.patient.util.ToastUtils;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/23 0023<br/>
 */
public class GeneralFragment extends BaseFragment implements IGeneralContract.IUpdateView {

    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLoutActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.relay_font_size)
    RelativeLayout mRelayFontSize;
    @Bind(R.id.relay_update)
    RelativeLayout mRelayUpdate;
    @Bind(R.id.relay_about_us)
    RelativeLayout mRelayAboutUs;
    @Bind(R.id.relay_quite_login)
    RelativeLayout mRelayQuiteLogin;
    private Activity mActivity;
    private IGeneralContract.IUpdatePresenter mPresent;
    private WeakReference<TextView> weak;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static GeneralFragment newInstance(Handler handler) {
        mHandler = handler;
        return new GeneralFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new UpdatePresenter(Injection.provideTwoRepository(), this);
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
        mPresent.onSubscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresent.unSubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissLoding();
        mPresent.stopThread();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onClickBackEvent() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY);
    }

    @OnClick({R.id.relay_font_size, R.id.relay_update, R.id.relay_about_us})
    public void onClickPageEvent(RelativeLayout view) {
        switch (view.getId()) {
            case R.id.relay_font_size:
                mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_FGT_FONT);
                break;
            case R.id.relay_about_us:
                mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_FGT_ABOUT_US);
                break;
            case R.id.relay_update:
                updateApp();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.relay_quite_login)
    public void onClickExitEvent() {
        if (TbApp.instance().getCUR_USER().equals(TbGlobal.JString.PATIENT_USER)) {
            AppUtils.getLruCache().evictAll();
        } else {
            //TODO 督导员

        }
        SharedUtils.remove(TbGlobal.JString.CUR_USER);
        SharedUtils.remove("expires_in_" + TbApp.instance().getCUR_USER());
        onJumpToNewActivity(mActivity, SplashActivity.class);
        PtActivityManager.getAppManager().finishAllException(SplashActivity.class);
    }

    private AlertDialog mDialog;

    private void updateApp() {
        View view = View.inflate(mActivity, R.layout.alert_dialog_update, null);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("正在获取最新版本信息~");
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view);
        mDialog = builder.create();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);

        weak = new WeakReference<>(textView);
        mPresent.updateInfo();
    }

    @Override
    public void showToastMessage(int id) {
        ToastUtils.showShort(id);
    }

    @Override
    public void loadingDialog() {

    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public void showIsNewVerCode() {
        TextView textView = weak.get();
        if (textView != null) textView.setText("当前已是最新版本,亲~");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (mDialog != null) mDialog.dismiss();
        }
    }

    @Override
    public void onProgress(String v) {
        TextView textView = weak.get();
        if (textView != null) textView.setText("正在下载中~" + v );
    }

    @Override
    public void dismissLoding() {
        if (mDialog != null) mDialog.dismiss();
    }
}
