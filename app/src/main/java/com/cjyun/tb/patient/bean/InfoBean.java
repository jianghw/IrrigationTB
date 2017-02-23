package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/24 0024</br>
 * description:</br> 一对一 {@link PatientBean}
 */
public class InfoBean extends DataSupport {

    @Expose
    private int patient_id;
    @Expose
    private long id;
    @Expose
    private String created_at;
    @Expose
    private String updated_at;
    @Expose
    private String phone_number;
    @Expose
    private String full_name;
    @Expose
    private String address;
    @Expose
    private boolean gender;
    @Expose
    private String weixin_id;
    @Expose
    private String qq;
    @Expose
    private String email;
    @Expose
    private String remarks;
    @Expose
    private String photo;

    public int getPatient_id() { return patient_id;}

    public void setPatient_id(int patient_id) { this.patient_id = patient_id;}

    public long getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getCreated_at() { return created_at;}

    public void setCreated_at(String created_at) { this.created_at = created_at;}

    public String getUpdated_at() { return updated_at;}

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at;}

    public String getPhone_number() { return phone_number;}

    public void setPhone_number(String phone_number) { this.phone_number = phone_number;}

    public String getFull_name() { return full_name;}

    public void setFull_name(String full_name) { this.full_name = full_name;}

    public String getAddress() { return address;}

    public void setAddress(String address) { this.address = address;}

    public boolean isGender() { return gender;}

    public void setGender(boolean gender) { this.gender = gender;}

    public String getWeixin_id() { return weixin_id;}

    public void setWeixin_id(String weixin_id) { this.weixin_id = weixin_id;}

    public String getQq() { return qq;}

    public void setQq(String qq) { this.qq = qq;}

    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}

    public String getRemarks() { return remarks;}

    public void setRemarks(String remarks) { this.remarks = remarks;}

    public String getPhoto() { return photo;}

    public void setPhoto(String photo) { this.photo = photo;}
}
