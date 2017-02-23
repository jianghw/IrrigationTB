package com.cjyun.tb.patient.ui.binding;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.bean.BindBoxBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.custom.OsiEditText;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.qrcode.capture.CaptureActivity;
import com.cjyun.tb.patient.util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/21 0021<br/>
 */
public class ScanBoxFragment extends BaseFragment implements AddBoxContract.IScanBoxView {

    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.tv_title_jump)
    TextView mTvTitleJump;
    @Bind(R.id.imgView_separator)
    ImageView mImgViewSeparator;
    @Bind(R.id.imgView_scan)
    ImageView mImgViewScan;
    @Bind(R.id.rl_actionBar_right)
    LinearLayout mRlActionBarRight;
    @Bind(R.id.oEdit_bindBox_ID)
    OsiEditText mOEditBindBoxID;
    @Bind(R.id.oEdit_bindBox_SN)
    OsiEditText mOEditBindBoxSN;
    @Bind(R.id.tv_bindBox_prompt)
    TextView mTvBindBoxPrompt;
    @Bind(R.id.btn_bindBox_next)
    Button mBtnBindBoxNext;
    private Activity mActivity;
    private ScanBoxPresenter mPresent;
    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new ScanBoxPresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_scan_box, container, false);
        ButterKnife.bind(this, root);

        mOEditBindBoxID.setTitle("ID");
        mOEditBindBoxSN.setTitle("SN");

        mOEditBindBoxID.setEtContent("10002222");
        mOEditBindBoxSN.setEtContent("222222");
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

    public static ScanBoxFragment newInstance(Handler handler) {
        mHandler = handler;
        return new ScanBoxFragment();
    }

    //回退
    @OnClick(R.id.ll_actionBar_back)
    public void onBackActivity() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY);
    }

    //跳过至完善
    @OnClick(R.id.tv_title_jump)
    public void onJumpToUserInfo() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_TO_IMPROVE_INFO);
    }

    //下一步
    @OnClick(R.id.btn_bindBox_next)
    public void onFirstBindBox() {
        mPresent.onBindBoxInfo();
    }

    @OnClick(R.id.rl_actionBar_right)
    public void onQRCode() {
        Intent openCameraIntent = new Intent(mActivity, CaptureActivity.class);
        this.startActivityForResult(openCameraIntent, 100);
    }


    @Override
    public String getBoxID() {
        return mOEditBindBoxID.getEditText();
    }

    @Override
    public String getBoxSN() {
        return mOEditBindBoxSN.getEditText();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresent.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResultFromQR(String scanResult) {
        //ID:扫描出错~,SN:请重新扫描
        String[] strings = scanResult.split(",");
        try {
            if (strings.length > 1) {
                String[] id = strings[0].split(":");
                String[] sn = strings[1].split(":");
                mOEditBindBoxID.setEtContent(id[1]);
                mOEditBindBoxSN.setEtContent(sn[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下一步成功时,添加GSM页
    @Override
    public void onSucceedNextStep() {
        if (getBoxID().startsWith("1")) {
            mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_TO_GSM);
        } else {
            mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_TO_WIFI);
        }
    }

    @Override
    public void showRemoveBinding(BindBoxBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        StringBuilder sb = new StringBuilder();
        sb.append("当前账号已绑定药盒ID:").append(bean.getDevice_uid()).append("\r\n")
                .append("本账户只能绑定一个药盒,是否解绑药盒:").append(bean.getDevice_uid()).append("\r\n");
        builder.setTitle("是否解绑")
                .setMessage(sb.toString())
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("解绑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresent.onRemoveBinding();
                    }
                })
                .show();
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
}
