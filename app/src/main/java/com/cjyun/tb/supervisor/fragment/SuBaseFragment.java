package com.cjyun.tb.supervisor.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjyun.tb.TbApp;
import com.cjyun.tb.supervisor.base.ConstantAll;
import com.socks.library.KLog;

/**
 * Created by jianghw on 2016/3/23 0023.
 * Description
 */
public abstract class SuBaseFragment extends Fragment {

    protected Activity mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container);
        initData();
        initListener();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TbApp.getRefWatcher().watch(this);
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initData();

    protected abstract void initListener();

    /**
     * Activity之间的跳转
     *
     * @param context
     * @param cls
     */
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
        bundle.putString(ConstantAll.ConString.FA_BUNDLE_TAG, tag);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onResume() {

        super.onResume();
        KLog.i("TAG", this + "onResume");
    }
}
