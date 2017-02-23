package com.cjyun.tb.patient.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cjyun.tb.patient.bean.ProgressBean;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/6/12 0012</br>
 * description:</br>
 */
public abstract class ProgressHandler {

    protected abstract void sendMessage(ProgressBean progressBean);

    protected abstract void handleMessage(Message message);

    protected abstract void onProgress(long progress, long total, boolean done);

    protected static class ResponseHandler extends Handler {

        WeakReference<ProgressHandler> weakReference;

        public ResponseHandler(ProgressHandler mProgressHandler, Looper looper) {
            super(looper);
            weakReference = new WeakReference<>(mProgressHandler);
        }

        @Override
        public void handleMessage(Message msg) {
            ProgressHandler mProgressHandler = weakReference.get();
            if (mProgressHandler != null) mProgressHandler.handleMessage(msg);
        }
    }

}
