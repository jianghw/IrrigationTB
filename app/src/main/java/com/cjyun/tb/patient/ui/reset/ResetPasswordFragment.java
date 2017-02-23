package com.cjyun.tb.patient.ui.reset;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.custom.OsiEditText;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.util.ToastUtils;
import com.socks.library.KLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/23 0023<br/>
 */
public class ResetPasswordFragment extends BaseFragment implements IResetPwContract.IResetFgtView {

    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.oEdit_pwd_userName)
    OsiEditText mOEditPwdUserName;
    @Bind(R.id.oEdit_pwd_passWord)
    OsiEditText mOEditPwdPassWord;
    @Bind(R.id.btn_login)
    Button mBtnLogin;

    private Activity mActivity;
    private static Handler mHandler;
    private ResetPasswordPresenter mPresent;

    private SmsObserver mObserver;//短信监听器
    private ContentResolver resolver;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static ResetPasswordFragment newInstance(Handler handler) {
        mHandler = handler;
        return new ResetPasswordFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new ResetPasswordPresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_reset_password, container, false);
        ButterKnife.bind(this, root);

        mOEditPwdUserName.initTail();
        mOEditPwdUserName.setTitle(getString(R.string.aty_pw_old));
        mOEditPwdUserName.setContentHint(getString(R.string.aty_pRegister_passHint));

        mOEditPwdPassWord.initTail();
        mOEditPwdPassWord.setTitle(getString(R.string.aty_pw_new));
        mOEditPwdPassWord.setContentHint(getString(R.string.aty_pRegister_passHint));

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

    @OnClick(R.id.btn_login)
    public void onSubmitNewPassword() {
        mPresent.onVerifyFillContent();
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onBackActivity() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY);
    }

    @Override
    public String getUserPhone() {
        return mOEditPwdUserName.getEditText();
    }

    @Override
    public String getUserPassword() {
        return mOEditPwdPassWord.getEditText();
    }

    @Override
    public void onRegistrationMessage() {
        if (!isAdded()) return;
        resolver = mActivity.getContentResolver();
        mObserver = new SmsObserver(resolver);
        resolver.registerContentObserver(Uri.parse("context://sms"), true, mObserver);
    }

    @Override
    public void onUnRegistrationMessage() {
        if (resolver != null)
            resolver.unregisterContentObserver(mObserver);
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

    private class SmsObserver extends ContentObserver {
        private final ContentResolver resolver;

        public SmsObserver(ContentResolver resolver) {
            super(new Handler());
            this.resolver = resolver;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"),
                    new String[]{"_id", "address", "read", "body", "thread_id"},
                    "read=0", null, "date desc");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int _inIndex = cursor.getColumnIndex("_id");
                    String _smsInfo_id = cursor.getString(_inIndex);

                    int address = cursor.getColumnIndex("address");
                    String _smsInfo_address = cursor.getString(address);

                    int body = cursor.getColumnIndex("body");
                    String _smsInfo_body = cursor.getString(body);

                    int read = cursor.getColumnIndex("read");
                    String _smsInfo_read = cursor.getString(read);

                    KLog.i(_smsInfo_id);
                    KLog.i(cursor.getColumnCount());
                    KLog.i(_smsInfo_address);//电话号
                    KLog.i(_smsInfo_body);//信息
                    KLog.i(_smsInfo_read);
                    if ("0".equals(_smsInfo_read)) {
                        Pattern p = Pattern.compile("(\\d+)");
                        Matcher matcher = p.matcher(_smsInfo_body);
                        StringBuilder sb = new StringBuilder();
                        while (matcher.find()) {
                            String find = matcher.group(1);
                            sb.append(find);
                        }
                        break;
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }
}
