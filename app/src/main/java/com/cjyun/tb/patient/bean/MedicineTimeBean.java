package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/25 0025</br>
 * description:</br> 母表{@link DrugDetailBean}
 */
public class MedicineTimeBean extends DataSupport {

    @Expose
    private int patient_id;
    @Expose
    private int id;
    @Expose
    private String time;
    @Expose
    private String created_at;
    @Expose
    private String updated_at;

    public int getPatient_id() { return patient_id;}

    public void setPatient_id(int patient_id) { this.patient_id = patient_id;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getTime() { return time;}

    public void setTime(String time) { this.time = time;}

    public String getCreated_at() { return created_at;}

    public void setCreated_at(String created_at) { this.created_at = created_at;}

    public String getUpdated_at() { return updated_at;}

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at;}
}
