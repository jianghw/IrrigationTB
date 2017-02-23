package com.cjyun.tb.patient.ui.binding;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.cjyun.tb.patient.custom.OsiEditText;
import com.cjyun.tb.patient.util.ToastUtils;
import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * wifi版连接操作
 */
public class WiFiFragment extends BaseFragment {

    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_wifi)
    TextView mTvWifi;
    @Bind(R.id.img_line)
    ImageView mImgLine;
    @Bind(R.id.oEdit_pwd)
    OsiEditText mOEditPwd;
    @Bind(R.id.btn_next)
    Button mBtnNext;

    private Activity mActivity;
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private int seconds = 60;//倒计时秒数
    private Thread timeThread;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static WiFiFragment newInstance(Handler handler) {
        mHandler = handler;
        return new WiFiFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pt_wifi, container, false);
        ButterKnife.bind(this, root);

        mOEditPwd.initTail();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_wifi_password);
        mOEditPwd.setTitleBitmap(bitmap);

        mWifiManager = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager.isWifiEnabled()) {
            mWifiInfo = mWifiManager.getConnectionInfo();
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifiNetworkInfo.isConnected()) {
                String mSSID = mWifiInfo.getSSID();
                if (mSSID.startsWith("\"") && mSSID.endsWith("\""))
                    mSSID = mSSID.substring(1, mSSID.length() - 1);
                mTvWifi.setText((CharSequence) mSSID);
            }
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (timeThread != null) {
            timeThread.interrupt();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public String getWifiPassword() {
        return mOEditPwd.getEditText();
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onActionBarBack() {
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_FGT);
    }

    @OnClick(R.id.btn_next)
    public void onNextStep() {
        String password = getWifiPassword();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort(R.string.login_fgt_no_password);
        } else {
            showEspTouchToCon();
            mBtnNext.setEnabled(false);
        }

        //        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_TO_BINDING);
    }

    private void showEspTouchToCon() {
        new EsptouchAsyncTask3().execute(mTvWifi.getText().toString(),
                getConnectionInfo(), getWifiPassword(), "NO", String.valueOf(1));
    }

    private String getConnectionInfo() {
        return mWifiInfo.getBSSID();
    }

    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {

        private IEsptouchTask mEsptouchTask;
        private final Object mLock = new Object();

        @Override
        protected void onPreExecute() {
            timeThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted() && seconds > 0) {
                            handler.sendEmptyMessage(200);
                            sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        handler.sendEmptyMessage(100);
                    }
                }
            };
            timeThread.start();
        }

        @Override
        protected List<IEsptouchResult> doInBackground(String... params) {
            int taskResultCount = 0;
            synchronized (mLock) {
                String apSsid = params[0];
                String apBssid = params[1];
                String apPassword = params[2];
                String isSsidHiddenStr = params[3];
                String taskResultCountStr = params[4];
                boolean isSsidHidden = false;
                if (isSsidHiddenStr.equals("YES")) {
                    isSsidHidden = true;
                }
                taskResultCount = Integer.parseInt(taskResultCountStr);
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, isSsidHidden, mActivity);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            for (IEsptouchResult iEsptouchResult : resultList) {
                KLog.json("xxxi" + new Gson().toJson(iEsptouchResult));
            }
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            IEsptouchResult firstResult = result.get(0);
            if (!firstResult.isCancelled()) {
                int count = 0;
                final int maxDisplayCount = 5;
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("Esptouch success, bssid = " + resultInList.getBssid() + ",InetAddress = " + resultInList.getInetAddress().getHostAddress() + "\n");
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's " + (result.size() - count) + " more result(s) without showing\n");
                    }
                    handler.sendEmptyMessage(300);
                } else {
                    handler.sendEmptyMessage(100);
                }
            }
        }
    }

    private final MyHandler handler = new MyHandler(this);

    static class MyHandler extends Handler {
        WeakReference<WiFiFragment> weakReference;

        public MyHandler(WiFiFragment activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WiFiFragment activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 100:
                        activity.endForOpenWifi();
                        break;
                    case 200:
                        activity.countdownDisplay();
                        break;
                    case 300:
                        activity.conSucceed();
                        break;
                    default:
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    private void endForOpenWifi() {
        if (getView() == null) return;
        mBtnNext.setEnabled(true);
        seconds = 60;
        mBtnNext.setText(getString(R.string.btn_bind_box_retry));
    }

    private void countdownDisplay() {
        mBtnNext.setText(getString(R.string.btn_bind_box_binding) + seconds + "s");
        seconds--;
        if (seconds % 5 == 0) {
            //            onBoxVisitTime();
        }
    }

    private void conSucceed() {
        mBtnNext.setEnabled(true);
        seconds = 60;
        mBtnNext.setText(getString(R.string.submit_ok));
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_TO_BINDING);
    }
}
