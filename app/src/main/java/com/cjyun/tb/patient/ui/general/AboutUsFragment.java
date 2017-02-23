package com.cjyun.tb.patient.ui.general;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.util.AppUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改字体
 */
public class AboutUsFragment extends BaseFragment {

    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.tv_versionName)
    TextView mTvVersionName;
    @Bind(R.id.textView)
    TextView mTextView;
    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static AboutUsFragment newInstance(Handler handler) {
        mHandler = handler;
        return new AboutUsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this, root);

        mTvVersionName.setText(AppUtils.getVerName(TbApp.mContext));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onBackActivity() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_FGT);
    }

    @OnClick(R.id.textView)
    public void onDisclaimer() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_FGT_DISCLAIMER);
    }
}
