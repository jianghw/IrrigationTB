package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/18 0018</br>
 * description:</br> 上月服药记录
 */
public class PrevRecordBean extends DataSupport {

    /**
     * patient_id : 3
     * id : 1
     * device_id : null
     * medicine_id : 1
     * number : null
     * taken : true
     * setting_time : 09:30
     * eat_medicine_time : null
     * name : null
     * unit : null
     * quantity : null
     * created_at : 2016-05-10T17:44:41.960+08:00
     * updated_at : 2016-05-10T17:44:41.960+08:00
     * actualize_time : 2016-05-10T09:30:00+00:00
     * start_date : 2016-05-07
     * end_date : 2016-06-07
     */

    @Expose
    private int patient_id;
    @Expose
    private int id;
    @Expose
    private Object device_id;
    @Expose
    private int medicine_id;
    @Expose
    private Object number;
    @Expose
    private boolean taken;
    @Expose
    private String setting_time;
    @Expose
    private Object eat_medicine_time;
    @Expose
    private Object name;
    @Expose
    private Object unit;
    @Expose
    private Object quantity;
    @Expose
    private String created_at;
    @Expose
    private String updated_at;
    @Expose
    private String actualize_time;
    @Expose
    private String start_date;
    @Expose
    private String end_date;

    public int getPatient_id() { return patient_id;}

    public void setPatient_id(int patient_id) { this.patient_id = patient_id;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public Object getDevice_id() { return device_id;}

    public void setDevice_id(Object device_id) { this.device_id = device_id;}

    public int getMedicine_id() { return medicine_id;}

    public void setMedicine_id(int medicine_id) { this.medicine_id = medicine_id;}

    public Object getNumber() { return number;}

    public void setNumber(Object number) { this.number = number;}

    public boolean isTaken() { return taken;}

    public void setTaken(boolean taken) { this.taken = taken;}

    public String getSetting_time() { return setting_time;}

    public void setSetting_time(String setting_time) { this.setting_time = setting_time;}

    public Object getEat_medicine_time() { return eat_medicine_time;}

    public void setEat_medicine_time(Object eat_medicine_time) { this.eat_medicine_time = eat_medicine_time;}

    public Object getName() { return name;}

    public void setName(Object name) { this.name = name;}

    public Object getUnit() { return unit;}

    public void setUnit(Object unit) { this.unit = unit;}

    public Object getQuantity() { return quantity;}

    public void setQuantity(Object quantity) { this.quantity = quantity;}

    public String getCreated_at() { return created_at;}

    public void setCreated_at(String created_at) { this.created_at = created_at;}

    public String getUpdated_at() { return updated_at;}

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at;}

    public String getActualize_time() { return actualize_time;}

    public void setActualize_time(String actualize_time) { this.actualize_time = actualize_time;}

    public String getStart_date() { return start_date;}

    public void setStart_date(String start_date) { this.start_date = start_date;}

    public String getEnd_date() { return end_date;}

    public void setEnd_date(String end_date) { this.end_date = end_date;}
}
