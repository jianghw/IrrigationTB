package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/18 0018</br>
 * description:</br> 复诊事件
 */
public class ReturnVisitBean extends DataSupport {


    /**
     * patient_id : 15
     * id : 2
     * director_id : 14
     * setting_date : 2016-05-27
     * actually_time : null
     * created_at : 2016-05-27T09:14:35.000+08:00
     * updated_at : 2016-05-27T09:14:35.000+08:00
     * take_phlegm : not_take
     */

    @Expose
    private int patient_id;
    @Expose
    private int id;
    @Expose
    private int director_id;
    @Expose
    private String setting_date;
    @Expose
    private Object actually_time;
    @Expose
    private String created_at;
    @Expose
    private String updated_at;
    @Expose
    private String take_phlegm;

    public int getPatient_id() { return patient_id;}

    public void setPatient_id(int patient_id) { this.patient_id = patient_id;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public int getDirector_id() { return director_id;}

    public void setDirector_id(int director_id) { this.director_id = director_id;}

    public String getSetting_date() { return setting_date;}

    public void setSetting_date(String setting_date) { this.setting_date = setting_date;}

    public Object getActually_time() { return actually_time;}

    public void setActually_time(Object actually_time) { this.actually_time = actually_time;}

    public String getCreated_at() { return created_at;}

    public void setCreated_at(String created_at) { this.created_at = created_at;}

    public String getUpdated_at() { return updated_at;}

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at;}

    public String getTake_phlegm() { return take_phlegm;}

    public void setTake_phlegm(String take_phlegm) { this.take_phlegm = take_phlegm;}
}
