package com.cjyun.tb.patient.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.cjyun.tb.patient.activity.PtBaseActivity;


/**
 * Created by jianghw on 2016/3/26 0026.
 * Description
 */
public class BaseDialogFragment extends DialogFragment {

    private static final java.lang.String EXTRA_DIALOG_ID_KEY = "extra_dialog_id";
    private static final java.lang.String EXTRA_DIALOG_TITLE_KEY = "extra_dialog_title_key";
    private static final java.lang.String EXTRA_DIALOG_CANCELABLE_KEY = "extra_dialog_cancelable";
    private static final java.lang.String EXTRA_DIALOG_IS_CUSTOM_KEY = "extra_dialog_is_custom";
    private PtBaseActivity mBaseActivity;
    //每个对话框的ID及其特性
    private int mDialog;
    private String mTitle;

    //是否为自定义view的dialog
    private boolean mIsCustomDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getActivity() instanceof PtBaseActivity) {
            mBaseActivity = (PtBaseActivity) getActivity();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        onParseBundleArgs(bundle);
    }

    private void onParseBundleArgs(Bundle bundle) {
        mDialog = bundle.getInt(EXTRA_DIALOG_ID_KEY);
        mTitle = bundle.getString(EXTRA_DIALOG_TITLE_KEY);
        mIsCustomDialog = bundle.getBoolean(EXTRA_DIALOG_IS_CUSTOM_KEY);
        //Control whether the shown Dialog is cancelable.
        boolean mIsCancelable = bundle.getBoolean(EXTRA_DIALOG_CANCELABLE_KEY);
        setCancelable(mIsCancelable);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
