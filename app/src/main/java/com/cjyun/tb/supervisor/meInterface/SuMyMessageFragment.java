package com.cjyun.tb.supervisor.meInterface;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.base.BaseFragment;
import com.cjyun.tb.supervisor.base.ConstantAll;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/6 0006.
 * <p/>
 * 我的消息界面
 */
public class SuMyMessageFragment extends BaseFragment {

    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout llActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView tvTitleLeft;

    @Bind(R.id.tv_title_right_redact)
    TextView tvTitleRightRedact;

    @Bind(R.id.lv_su_message)
    ListView lvSuMessage;

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static SuMyMessageFragment newInstance(Handler handler) {
        mHandler = handler;
        return new SuMyMessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_su_message, container, false);
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
    public void onClick() {

        mHandler.sendEmptyMessage(ConstantAll.ConHandlerInt.HANDLER_BACK_ACTIVITY);
    }

    public void initData() {

        // TODO 设置数据
    }

}
