package com.cjyun.tb.patient.ui.improve;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.custom.UserInfoText;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.ui.PhotoActivity;
import com.cjyun.tb.patient.ui.main.PtMainActivity;
import com.cjyun.tb.patient.util.ToastUtils;
import com.tryking.trykingcitychoose.AddressActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/22 0022<br/>
 */
public class UserInfoFragment extends BaseFragment implements IUserInfoContract.IUserInfoView {


    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.imgView_user_head)
    ImageView mImgViewUserHead;
    @Bind(R.id.userTv_fill_box)
    UserInfoText mUserTvFillBox;
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
    SwipeRefreshLayout mSwipeRefresh;
    private Activity mActivity;
    private static Handler mHandler;

    private IUserInfoContract.IUserInfoPresenter mPresent;
    private String image = "";//等待提交的头像

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static UserInfoFragment newInstance(Handler handler) {
        mHandler = handler;
        return new UserInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new UserInfoPresenter(Injection.provideTwoRepository(), this);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_user_info, container, false);
        ButterKnife.bind(this, root);

        mSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        mSwipeRefresh.setDistanceToTriggerSync(200);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresent.loadingRemoteData(true);
            }
        });
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

    //拍照
    @OnClick(R.id.imgView_user_head)
    public void setUserHead() {
        startActivityForResult(new Intent(mActivity, PhotoActivity.class), 101);
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onClickBace() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && null != data && requestCode == 101) {
            Bundle bundle = data.getExtras();
            image = bundle.getString("image");
            setUserByHead(image);
        } else if (resultCode == Activity.RESULT_OK && null != data && requestCode == 500) {
            Bundle bundle = data.getExtras();
            String address = bundle.getString("result");
            mUserTvFillAddress.setContentTextView(address);
        }
    }

    private void setUserByHead(String image) {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        mImgViewUserHead.setImageBitmap(bitmap);
    }

    //提交资料
    @OnClick(R.id.btn_fill_enter)
    public void onSubmitInformation() {
        mPresent.submitPatientInfo();
    }

    //添加新的信息
    @OnClick({R.id.userTv_phone, R.id.userTv_fill_email, R.id.userTv_fill_guardian1, R.id.userTv_fill_guardian2})
    public void addNewUserInfo(UserInfoText userInfoText) {
        userInfoText.showPopuForEdit(mActivity);
    }

    @OnClick(R.id.userTv_fill_address)
    public void addAddress() {
        startActivityForResult(new Intent(mActivity, AddressActivity.class), 500);
    }

    @Override
    public void setLoadingIndicator(final boolean b) {
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
    public void setBoxId(String id) {
        mUserTvFillBox.setContentTextView(id);
    }

    @Override
    public void setUserBasicInfo(String name, String identityCard, String gender) {
        mUserTvFillUser.setContentTextView(name);
        //        mUserTvFillAge.setContentTextView(identityCard);
        mUserTvFillGender.setContentTextView(gender);
    }

    @Override
    public void setUserPhone(String phone) {
        mUserTvPhone.setContentTextView(phone);
    }

    @Override
    public void setUserEmail(String email) {
        mUserTvFillEmail.setContentTextView(email);
    }

    @Override
    public void setUserQQ(String QQ) {
        mUserTvFillGuardian1.setContentTextView(QQ);
    }

    @Override
    public void setUserWeiXi(String wx) {
        mUserTvFillGuardian2.setContentTextView(wx);
    }

    @Override
    public void setUserAddress(String address) {
        mUserTvFillAddress.setContentTextView(address);
    }

    @Override
    public void setUserIdentityCard(String identityCard) {
        mUserTvFillAge.setContentTextView(identityCard);
    }

    @Override
    public void setUserHead(String s) {
        image = s;
        if (!TextUtils.isEmpty(s)) setUserByHead(s);
    }

    @Override
    public String getUserPhone() {
        return mUserTvPhone.getContentTextView();
    }

    @Override
    public String getUserEmail() {
        return mUserTvFillEmail.getContentTextView();
    }

    @Override
    public String getUserQQ() {
        return mUserTvFillGuardian1.getContentTextView();
    }

    @Override
    public String getUserWeiXi() {
        return mUserTvFillGuardian2.getContentTextView();
    }

    @Override
    public String getUserAddress() {
        return mUserTvFillAddress.getContentTextView();
    }

    @Override
    public String getUserPhoto() {
        return image;
    }

    @Override
    public void submitSucceed() {
        onJumpToNewActivity(mActivity, PtMainActivity.class);
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY);
    }

    @Override
    public void submitError(String message) {
        ToastUtils.showMessage(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("提交更新失败,是否直接进入主页")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        submitSucceed();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void showToastMessage(int id) {

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
