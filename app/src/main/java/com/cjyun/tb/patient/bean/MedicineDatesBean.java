package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/25 0025</br>
 * description:</br> 母表{@link DrugDetailBean}
 */
public class MedicineDatesBean extends DataSupport {

    @Expose
    private int patient_id;
    @Expose
    private int id;
    @Expose
    private int medicine_id;
    @Expose
    private String start_date;
    @Expose
    private String end_date;
    @Expose
    private String created_at;
    @Expose
    private String updated_at;

    public int getPatient_id() { return patient_id;}

    public void setPatient_id(int patient_id) { this.patient_id = patient_id;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public int getMedicine_id() { return medicine_id;}

    public void setMedicine_id(int medicine_id) { this.medicine_id = medicine_id;}

    public String getStart_date() { return start_date;}

    public void setStart_date(String start_date) { this.start_date = start_date;}

    public String getEnd_date() { return end_date;}

    public void setEnd_date(String end_date) { this.end_date = end_date;}

    public String getCreated_at() { return created_at;}

    public void setCreated_at(String created_at) { this.created_at = created_at;}

    public String getUpdated_at() { return updated_at;}

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at;}
}
