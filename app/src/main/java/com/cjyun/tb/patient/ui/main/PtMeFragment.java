package com.cjyun.tb.patient.ui.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.bean.InfoBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.ui.general.GeneralActivity;

import org.litepal.crud.DataSupport;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianghw on 2016/3/23 0023.
 * Description
 */
public class PtMeFragment extends BaseFragment {

    @Bind(R.id.imgView_me_head)
    ImageView mImgViewMeHead;
    @Bind(R.id.tv_me_account)
    TextView mTvMeAccount;
    @Bind(R.id.tv_me_id)
    TextView mTvMeId;
    @Bind(R.id.imgView_me_qrCode)
    ImageView mImgViewMeQrCode;
    @Bind(R.id.img_instr_right)
    ImageView mImgInstrRight;
    @Bind(R.id.rl_me_qrCode)
    RelativeLayout mRlMeQrCode;
    @Bind(R.id.img_director)
    ImageView mImgDirector;
    @Bind(R.id.tv_me_dir)
    TextView mTvMeDir;
    @Bind(R.id.iv_director_point)
    ImageView mIvDirectorPoint;
    @Bind(R.id.rl_me_director)
    RelativeLayout mRlMeDirector;
    @Bind(R.id.img_box)
    ImageView mImgBox;
    @Bind(R.id.tv_me_message)
    TextView mTvMeMessage;
    @Bind(R.id.iv_message_point)
    ImageView mIvMessagePoint;
    @Bind(R.id.rl_me_message)
    RelativeLayout mRlMeMessage;
    @Bind(R.id.img_feedback)
    ImageView mImgFeedback;
    @Bind(R.id.rl_me_suggestion)
    RelativeLayout mRlMeSuggestion;
    @Bind(R.id.img_general)
    ImageView mImgGeneral;
    @Bind(R.id.tv_me_general)
    TextView mTvMeGeneral;
    @Bind(R.id.iv_me_new)
    ImageView mIvMeNew;
    @Bind(R.id.rl_me_general)
    RelativeLayout mRlMeGeneral;
    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static PtMeFragment newInstance() {
        return new PtMeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_me, container, false);
        ButterKnife.bind(this, root);

        InfoBean info = DataSupport.findLast(InfoBean.class);
        if (info != null) {
            String photo = info.getPhoto();
            if (!TextUtils.isEmpty(photo)) {
                byte[] bytes = Base64.decode(photo, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mImgViewMeHead.setImageBitmap(bitmap);
            }
            mTvMeAccount.setText(info.getFull_name());
            mTvMeId.setText(String.valueOf(info.getPatient_id()));
        }
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

    @OnClick({R.id.rl_me_qrCode, R.id.rl_me_director,
            R.id.rl_me_message, R.id.rl_me_suggestion, R.id.rl_me_general})
    public void onClickEvent(RelativeLayout relativeLayout) {
        switch (relativeLayout.getId()) {
            case R.id.rl_me_qrCode:
                break;
            case R.id.rl_me_director:
                onJumpTargetPage(mActivity, GeneralActivity.class, TbGlobal.JString.MY_DIRECTOR_TAG);
                break;
            case R.id.rl_me_message:
                onJumpTargetPage(mActivity, GeneralActivity.class, TbGlobal.JString.MY_MESSAGE_TAG);
                break;
            case R.id.rl_me_suggestion:
                onJumpTargetPage(mActivity, GeneralActivity.class, TbGlobal.JString.SUGGESTION_BOX_TAG);
                break;
            case R.id.rl_me_general:
                onJumpTargetPage(mActivity, GeneralActivity.class, TbGlobal.JString.GENERAL_TAG);
                break;
            default:
                break;
        }
    }
}
