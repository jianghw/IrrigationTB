package com.cjyun.tb.patient.service;

/**
 * Created by Administrator on 2016/6/12 0012</br>
 * description:</br>
 */
public interface ProgressListener {
    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
