package com.cjyun.tb.patient.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/9 0009</br>
 * description:</br> 响应json 为200时的错误处理
 */
public class RemoteJson extends DataSupport {

    private int status;  // 返回状态：0 失败   1 成功
    private String message;  // 返回信息
    private String data;  // 包装的对象

    public RemoteJson() {
    }

    public RemoteJson(int status, String message, String data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

