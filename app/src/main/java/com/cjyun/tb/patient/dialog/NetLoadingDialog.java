package com.cjyun.tb.patient.dialog;

import android.app.ProgressDialog;
import android.content.Context;

import com.cjyun.tb.R;

/**
 * <b>@Description:</b>下载中。。。框<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/21 0021<br/>
 */
public class NetLoadingDialog {

    private static Context mContext;

    private static class SingletonHolder {
        private static final NetLoadingDialog INSTANCE = new NetLoadingDialog();
    }

    private NetLoadingDialog() {
    }

    private ProgressDialog createDialog() {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage(mContext.getString(R.string.send_net_request));
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static NetLoadingDialog getInstance(Context context) {
        mContext=context;
        return SingletonHolder.INSTANCE;
    }

    public ProgressDialog getOnlyDialog() {
        return createDialog();
    }

}
