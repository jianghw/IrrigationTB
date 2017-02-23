package com.cjyun.tb.supervisor.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.constant.TbGlobal;
import com.socks.library.KLog;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/19 0019<br/>
 */
public class BaseFragment extends Fragment {

    private Activity mA;
    private ProgressDialog mProgressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        KLog.i("TAG", this + "onAttach");
        mA = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i("TAG", this + "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        KLog.i("TAG", this + "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        KLog.i("TAG", this + "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        KLog.i("TAG", this + "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KLog.i("TAG", this + "onDestroyView");
    }

    protected void onJumpToNewActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    /**
     * fragment指定跳转activity
     *
     * @param context
     * @param cls
     * @param tag
     */
    protected void onJumpTargetPage(Context context, Class<?> cls, String tag) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putString(TbGlobal.JString.FA_BUNDLE_TAG, tag);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void createLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mA);
            mProgressDialog.setMessage(getString(R.string.send_net_request));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    protected void dismissLoadingDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
