package com.cjyun.tb.patient.ui.general;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
 * 服务意见箱
 */
public class SuggestionFragment extends BaseFragment implements IGeneralContract.ISuggestionView {

    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.edit_content)
    EditText mEditContent;
    @Bind(R.id.ll_edit)
    LinearLayout mLlEdit;
    @Bind(R.id.edit_email_number)
    EditText mEditEmailNumber;
    @Bind(R.id.btn_submit)
    Button mBtnSubmit;
    private Activity mActivity;
    private IGeneralContract.ISuggestionPresenter mPresent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static SuggestionFragment newInstance(Handler handler) {
        mHandler = handler;
        return new SuggestionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new SuggestionPresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_suggestion, container, false);
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
        ButterKnife.unbind(this);
        dismissDialog();
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onBackActivity() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY);
    }

    @OnClick(R.id.btn_submit)
    public void onSubmitSuggestion() {
        mPresent.submitSuggestion();
    }

    @Override
    public void showToastMessage(int id) {
        ToastUtils.showShort(id);
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
    public String getContent() {
        return mEditContent.getText().toString().trim();
    }

    @Override
    public String getPhone() {
        return mEditEmailNumber.getText().toString().trim();
    }
}
