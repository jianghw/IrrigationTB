package com.cjyun.tb.patient.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;


/**
 * Created by jianghw on 2016/3/28 0028.
 * Description
 */
public class LoadingDialogFragment extends DialogFragment {

    private static class SingletonHolder {
        private static final LoadingDialogFragment INSTANCE = new LoadingDialogFragment();
    }

    public static LoadingDialogFragment getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("加载中,请等待...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

}
