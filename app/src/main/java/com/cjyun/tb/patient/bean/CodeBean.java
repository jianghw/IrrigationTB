package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by jianghw on 2016/3/24 0024.
 * Description 网络请求，200返回码
 */
public class CodeBean extends DataSupport{


    /**
     * status : success
     * code : 1
     */
    @Expose
    private String status;
    @Expose
    private int code;

    public String getStatus() { return status;}

    public void setStatus(String status) { this.status = status;}

    public int getCode() { return code;}

    public void setCode(int code) { this.code = code;}
}
