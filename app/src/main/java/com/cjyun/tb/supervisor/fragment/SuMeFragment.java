package com.cjyun.tb.supervisor.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.qrcode.capture.CaptureActivity;
import com.cjyun.tb.supervisor.base.ConstantAll;
import com.cjyun.tb.supervisor.bean.MeInfoBean;
import com.cjyun.tb.supervisor.data.SuInjection;
import com.cjyun.tb.supervisor.meInterface.MeControllerActivity;
import com.cjyun.tb.supervisor.meInterface.MeNews;
import com.cjyun.tb.supervisor.meInterface.SuBaseInfoPresenter;
import com.cjyun.tb.supervisor.suImprove.SuImproveInfoActivity;
import com.socks.library.KLog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuMeFragment extends SuBaseFragment implements MeNews.SuMeView {


    private SuBaseInfoPresenter mPresenter;
    private TextView tvMeAccount;
    private TextView tvSuMyId;
    private ImageView imgViewSuMyHead;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_su, container, false);

        tvMeAccount = (TextView) view.findViewById(R.id.tv_me_account);
        tvSuMyId = (TextView) view.findViewById(R.id.tv_su_my_id);
        imgViewSuMyHead = (ImageView) view.findViewById(R.id.imgView_su_my_head);
        mPresenter = new SuBaseInfoPresenter(
                SuInjection.provideTwoRepository(), this);

        ButterKnife.bind(this, view);
        return view;
    }

    public static SuMeFragment getInstance(Bundle bundle) {
        SuMeFragment mFregment = new SuMeFragment();
        mFregment.setArguments(bundle);
        return mFregment;
    }

    @Override
    protected void initData() {
        mPresenter.onLoadSuInfo();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 跳转到对应的Fragment控制器
     *
     * @param view
     */
    /*@OnClick({R.id.rl_su_my_qrCode, R.id.rl_su_my_message, R.id.rl_me_su_my_general})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_su_my_qrCode:
                //  onJumpTargetPage(getActivity(), MeControllerActivity.class, ConstantAll.ConString.SU_MY_BASE_INFO_TAG);
                break;
            case R.id.rl_su_my_message:
                onJumpTargetPage(getActivity(), MeControllerActivity.class, ConstantAll.ConString.MY_MESSAGE_TAG);
                break;
            case R.id.rl_me_su_my_general:
                onJumpTargetPage(getActivity(), MeControllerActivity.class, ConstantAll.ConString.GENERAL_TAG);
                break;
        }
    }*/
    @OnClick({R.id.rl_su_my_qrCode, R.id.rl_me_su_my_general})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_su_my_qrCode:
                //  onJumpTargetPage(getActivity(), MeControllerActivity.class, ConstantAll.ConString.SU_MY_BASE_INFO_TAG);
                Intent openCameraIntent = new Intent(mContext, SuImproveInfoActivity.class);
                startActivity(openCameraIntent);
                break;
            case R.id.rl_me_su_my_general:
                onJumpTargetPage(getActivity(), MeControllerActivity.class, ConstantAll.ConString.GENERAL_TAG);
                break;
        }
    }

    /**
     * 获取数据
     *
     * @param bean
     */
    @Override
    public void getInfo(MeInfoBean bean) {

        String[] src = {bean.getInfo().getFull_name(), bean.getInfo().getAddress()};

        KLog.d(bean.getInfo().getFull_name() + "---姓名---地址------" + bean.getInfo().getAddress());

        tvMeAccount.setText(bean.getInfo().getFull_name());
        tvSuMyId.setText(bean.getUser().getId() + "");

        if (bean.getInfo().getPhoto() != null) {
            byte[] decode = Base64.decode(bean.getInfo().getPhoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            imgViewSuMyHead.setImageBitmap(bitmap);
        }

        SuMeFragment suMeFragment = new SuMeFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("bean", src);
        suMeFragment.setArguments(bundle);


    }


    public void setLoadingIndicator(final boolean b) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_visit);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(b);
            }
        });
    }


    @OnClick(R.id.rl_me_su_my_visit)
    public void onClick() {

        Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
        getActivity().startActivityForResult(openCameraIntent, 100);
        //  startActivity(openCameraIntent);
    }

}
