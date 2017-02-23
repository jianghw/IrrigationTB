package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/24 0024</br>
 * description:</br> 一对一 {@link PatientBean}
 */
public class DeviceBean extends DataSupport {

    @Expose
    private int patient_id;
    @Expose
    private int id;
    @Expose
    private String device_uid;
    @Expose
    private String device_sn;
    @Expose
    private boolean bound_activate_flag;
    @Expose
    private String created_at;
    @Expose
    private String updated_at;
    @Expose
    private Object last_visit;
    @Expose
    private Object first_visit_time;

    public int getPatient_id() { return patient_id;}

    public void setPatient_id(int patient_id) { this.patient_id = patient_id;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getDevice_uid() { return device_uid;}

    public void setDevice_uid(String device_uid) { this.device_uid = device_uid;}

    public String getDevice_sn() { return device_sn;}

    public void setDevice_sn(String device_sn) { this.device_sn = device_sn;}

    public boolean isBound_activate_flag() { return bound_activate_flag;}

    public void setBound_activate_flag(boolean bound_activate_flag) { this.bound_activate_flag = bound_activate_flag;}

    public String getCreated_at() { return created_at;}

    public void setCreated_at(String created_at) { this.created_at = created_at;}

    public String getUpdated_at() { return updated_at;}

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at;}

    public Object getLast_visit() { return last_visit;}

    public void setLast_visit(Object last_visit) { this.last_visit = last_visit;}

    public Object getFirst_visit_time() { return first_visit_time;}

    public void setFirst_visit_time(Object first_visit_time) { this.first_visit_time = first_visit_time;}
}
