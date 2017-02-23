package com.cjyun.tb.patient.ui.general;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.ui.main.PtMainActivity;
import com.cjyun.tb.patient.util.SharedUtils;
import com.cjyun.tb.supervisor.activity.SuMainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改字体
 */
public class FontFragment extends BaseFragment {

    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.img_big)
    ImageView mImgBig;
    @Bind(R.id.textView_big)
    RelativeLayout mTextViewBig;
    @Bind(R.id.img_normal)
    ImageView mImgNormal;
    @Bind(R.id.textView_normal)
    RelativeLayout mTextViewNormal;
    @Bind(R.id.img_small)
    ImageView mImgSmall;
    @Bind(R.id.textView_small)
    RelativeLayout mTextViewSmall;
    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static FontFragment newInstance(Handler handler) {
        mHandler = handler;
        return new FontFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_font, container, false);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public void initData() {

        int mode = SharedUtils.getInteger("Theme_Font", 2);
        if (mode == 1) {
            mImgSmall.setVisibility(View.VISIBLE);
        } else if (mode == -1 || mode == 2) {
            mImgNormal.setVisibility(View.VISIBLE);
        } else if (mode == 3) {
            mImgBig.setVisibility(View.VISIBLE);
        }
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

    @OnClick({R.id.textView_big, R.id.textView_normal, R.id.textView_small})
    public void onSubmitSuggestion(RelativeLayout view) {
        switch (view.getId()) {
            case R.id.textView_big:
                SharedUtils.setInteger("Theme_Font", 3);
                repeatLoadActivity();
                break;
            case R.id.textView_normal:
                SharedUtils.setInteger("Theme_Font", 2);
                repeatLoadActivity();
                break;
            case R.id.textView_small:
                SharedUtils.setInteger("Theme_Font", 1);
                repeatLoadActivity();
                break;
            default:
                break;
        }
    }

    private void repeatLoadActivity() {
        if(TbApp.instance().getCUR_USER().equals(TbGlobal.JString.PATIENT_USER)){
            startActivity(new Intent(mActivity, PtMainActivity.class));
        }else{
            startActivity(new Intent(mActivity, SuMainActivity.class));
        }
    }
}
