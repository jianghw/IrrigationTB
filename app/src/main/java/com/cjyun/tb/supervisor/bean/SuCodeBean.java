package com.cjyun.tb.supervisor.bean;

/**
 * Created by Administrator on 2016/5/24 0024.
 * <p/>
 * post 请求的代码返回值
 * <p/>
 * 网络请求，200返回码
 */
public class SuCodeBean {


    private String status;
    private int code;  // -1

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
