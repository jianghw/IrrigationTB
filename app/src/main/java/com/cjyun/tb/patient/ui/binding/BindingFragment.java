package com.cjyun.tb.patient.ui.binding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/21 0021<br/>
 */
public class BindingFragment extends BaseFragment implements AddBoxContract.IBindFgtView {
    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLoutActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.include_actionBar)
    RelativeLayout mIncludeActionBar;
    @Bind(R.id.ll_imagView)
    LinearLayout mLlImagView;
    @Bind(R.id.tv_prompt)
    TextView mTvPrompt;
    @Bind(R.id.btn_addFlb)
    Button mBtnAddFlb;

    private Activity mActivity;
    private BindingFgtPresenter mPresenter;

    private Thread t;//倒计时线程
    private int count = 30;
    private boolean isSucceed = false;
    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static BindingFragment newInstance(Handler handler) {
        mHandler = handler;
        return new BindingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter == null) {
            mPresenter = new BindingFgtPresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_binding_box, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onSubscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        onErrorStep();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (t != null && t.isAlive()) t.interrupt();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        dismissDialog();
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onActionBarBack() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_FGT);
    }

    @OnClick(R.id.btn_addFlb)
    public void onNextStep(Button button) {
        if (button.isEnabled()) mPresenter.onBindingSecondStep();

        button.setEnabled(false);
    }

    @Override
    public Thread onCreateWorkThread() {
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!interrupted() && count > 0) {
                        mPresenter.onBindingSecondStep();
                        onEnteredCountdown();
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    onCompleteBinding();
                }
            }
        };
        return t;
    }

    /**
     * 倒计时
     */
    @Override
    public void onEnteredCountdown() {
        if (isAdded()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBtnAddFlb.setText(getString(R.string.btn_bind_box_binding) + count + "s");
                    count--;
                }
            });
        }
    }

    /**
     * 倒计时完成后
     */
    @Override
    public void onCompleteBinding() {
        if (isAdded()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBtnAddFlb.setEnabled(true);
                    count = 30;
                    if (!isSucceed) {
                        mBtnAddFlb.setText(getString(R.string.btn_bind_box_retry));
                    } else {
                        mBtnAddFlb.setText(getString(R.string.btn_bind_box_success));
                        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_TO_IMPROVE_INFO);
                    }
                }
            });
        }
    }

    @Override
    public void onSucceedStep() {
        mPresenter.unSubscribe();
        mBtnAddFlb.setText(getString(R.string.btn_bind_box_success));
        mBtnAddFlb.setEnabled(true);
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_TO_IMPROVE_INFO);
    }

    @Override
    public void showCountdown(long l) {
        mBtnAddFlb.setText(getString(R.string.btn_bind_box_binding) + String.valueOf(l) + "秒");
        if(l==0)onErrorStep();
    }

    @Override
    public void onErrorStep() {
        mPresenter.unSubscribe();
        mBtnAddFlb.setText(getString(R.string.btn_bind_box_retry));
        mBtnAddFlb.setEnabled(true);
        dismissDialog();
    }

    @Override
    public void interruptThread() {
        isSucceed = true;
        if (t != null && t.isAlive()) t.interrupt();
    }

    @Override
    public void showToastMessage(int id) {
        ToastUtils.showLong(id);
    }

    @Override
    public void loadingDialog() {
        createLoadingDialog();
    }

    @Override
    public void dismissDialog() {
        dismissLoadingDialog();
    }
}
