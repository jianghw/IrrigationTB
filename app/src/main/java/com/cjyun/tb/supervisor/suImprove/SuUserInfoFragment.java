package com.cjyun.tb.supervisor.suImprove;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.ui.PhotoActivity;
import com.cjyun.tb.supervisor.activity.SuMainActivity;
import com.cjyun.tb.supervisor.base.BaseFragment;
import com.cjyun.tb.supervisor.base.ConstantAll;
import com.cjyun.tb.supervisor.bean.MeInfoBean;
import com.cjyun.tb.supervisor.custom.UserInfoText;
import com.cjyun.tb.supervisor.data.SuInjection;
import com.socks.library.KLog;
import com.tryking.trykingcitychoose.AddressActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/22 0022<br/>
 * <p/>
 * 资料完善
 */
public class SuUserInfoFragment extends BaseFragment implements SuImproveInfo.SuImproveView {

    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.imgView_user_head)
    ImageView mImgViewUserHead;

    @Bind(R.id.userTv_fill_user)
    UserInfoText mUserTvFillUser;
    @Bind(R.id.userTv_fill_age)
    UserInfoText mUserTvFillAge;
    @Bind(R.id.userTv_fill_gender)
    UserInfoText mUserTvFillGender;
    @Bind(R.id.userTv_phone)
    UserInfoText mUserTvPhone;
    @Bind(R.id.userTv_fill_email)
    UserInfoText mUserTvFillEmail;
    @Bind(R.id.userTv_fill_guardian1)
    UserInfoText mUserTvFillGuardian1;
    @Bind(R.id.userTv_fill_guardian2)
    UserInfoText mUserTvFillGuardian2;
    @Bind(R.id.userTv_fill_address)
    UserInfoText mUserTvFillAddress;
    @Bind(R.id.btn_fill_enter)
    Button mBtnFillEnter;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
   /* @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;*/

    private Activity mActivity;
    private static Handler mHandler;
    private SuUserInfoPresenter mPresent;
    private String image;
    private String address;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static SuUserInfoFragment newInstance(Handler handler) {
        mHandler = handler;
        return new SuUserInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_su_user_info, container, false);
        ButterKnife.bind(this, root);

        // 设置进度条
        swipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        swipeRefresh.setDistanceToTriggerSync(400);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);

                // isRefreshing(); //检查是否处于刷新状态
                //   mPresent.onInitStart();
            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresent == null) {
            mPresent = new SuUserInfoPresenter(
                    SuInjection.provideTwoRepository(), this);
        }
        mPresent.onInitStart();
    }


    //拍照
    @OnClick(R.id.imgView_user_head)
    public void setUserHead() {

        startActivityForResult(new Intent(mActivity, PhotoActivity.class), 101);
        //  Toast.makeText(getActivity(), "拍照", Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置图片   TODO
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && null != data && requestCode == 101) {
            Bundle bundle = data.getExtras();
            String image = bundle.getString("image");
            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mImgViewUserHead.setImageBitmap(bitmap);
        }
    }*/
    // 返回图片和地址
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && null != data && requestCode == 101) {
            Bundle bundle = data.getExtras();
            image = bundle.getString("image");
            setUserByHead(image);
        } else if (resultCode == Activity.RESULT_OK && null != data && requestCode == 500) {
            Bundle bundle = data.getExtras();
            address = bundle.getString("result");

            mUserTvFillAddress.setContentTextView(address);

            KLog.d(address + "-----地址-------");

        }
    }

    private void setUserByHead(String image) {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        mImgViewUserHead.setImageBitmap(bitmap);
    }

    //提交资料，提交后退出当前界面
    @OnClick(R.id.btn_fill_enter)
    public void onSubmitInformation() {

        // TODO 跳转  editText点击未响应

        mPresent.loadingRemoteData(mUserTvPhone.getContentTextView(), mUserTvFillEmail.getContentTextView(),
                mUserTvFillGuardian1.getContentTextView(), mUserTvFillGuardian2.getContentTextView(),
                mUserTvFillAddress.getContentTextView(), image
        );
        onJumpToNewActivity(getActivity(), SuMainActivity.class);
        mHandler.sendEmptyMessage(ConstantAll.ConHandlerInt.HANDLER_BACK_ACTIVITY);
    }


    @OnClick(R.id.ll_actionBar_back)
    public void onClick() {
        getActivity().finish();
    }


    @OnClick({R.id.userTv_phone, R.id.userTv_fill_email, R.id.userTv_fill_guardian1,
            R.id.userTv_fill_guardian2, R.id.userTv_fill_address, R.id.imgView_user_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userTv_phone:
                mUserTvPhone.showPopuForEdit(mActivity);
                break;
            case R.id.userTv_fill_email:
                mUserTvFillEmail.showPopuForEdit(mActivity);
                break;
            case R.id.userTv_fill_guardian1:
                mUserTvFillGuardian1.showPopuForEdit(mActivity);
                break;
            case R.id.userTv_fill_guardian2:
                mUserTvFillGuardian2.showPopuForEdit(mActivity);
                break;
            case R.id.userTv_fill_address:
                // mUserTvFillAddress.showPopuForEdit(mActivity);

                startActivityForResult(new Intent(mActivity, AddressActivity.class), 500);
                break;
            case R.id.imgView_user_head:
                // TODO 传输图片  等待上传

                break;
        }
    }

    @Override
    public void getInfo(MeInfoBean bean) {


        mUserTvFillUser.setContentTextView(bean.getInfo().getFull_name());
        mUserTvFillAge.setContentTextView(bean.getInfo().getRemarks());
        mUserTvFillGender.setContentTextView(bean.getInfo().isGender() ? "男" : "女");

        MeInfoBean.InfoEntity info = bean.getInfo();

        mUserTvPhone.setContentTextView(bean.getInfo().getPhone_number() != null ? info.getPhone_number() : "");
        mUserTvFillEmail.setContentTextView(info.getEmail() != null ? info.getEmail() : "");
        mUserTvFillGuardian1.setContentTextView(info.getQq() != null ? info.getQq() : "");
        mUserTvFillGuardian2.setContentTextView(info.getWeixin_id() != null ? info.getWeixin_id() : "");

        if (!info.getAddress().isEmpty()) {
            mUserTvFillAddress.setContentTextView(info.getAddress());
        }
        if (info.getPhoto() != null) {
            byte[] bytes = Base64.decode(info.getPhoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mImgViewUserHead.setImageBitmap(bitmap);
        }

        if (address != null)
            mUserTvFillAddress.setContentTextView(address);
    }

    @Override
    public void refresh(final boolean b) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(b);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
