package com.cjyun.tb.supervisor.bean;

/**
 * Created by Administrator on 2016/5/17 0017.
 */
public class VisitDetailsBean {


    /**
     * patient_id : 3
     * id : 1
     * director_id : 2
     * setting_date : 2016-05-10
     * actually_time : 2016-05-10T00:00:00.000+08:00
     * visit_type : 1
     * content : dsdfsdfsd
     * created_at : 2001
     * updated_at : 2001
     */

    private int patient_id;
    private int id;
    private int director_id;
    private String setting_date; // 预定访问时间
    private String actually_time;// 实际访问时间
    private String visit_type; // 访问类型
    private String content;// 访问内容
    private String created_at;
    private String updated_at;

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDirector_id() {
        return director_id;
    }

    public void setDirector_id(int director_id) {
        this.director_id = director_id;
    }

    public String getSetting_date() {
        return setting_date;
    }

    public void setSetting_date(String setting_date) {
        this.setting_date = setting_date;
    }

    public String getActually_time() {
        return actually_time;
    }

    public void setActually_time(String actually_time) {
        this.actually_time = actually_time;
    }

    public String getVisit_type() {
        return visit_type;
    }

    public void setVisit_type(String visit_type) {
        this.visit_type = visit_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
