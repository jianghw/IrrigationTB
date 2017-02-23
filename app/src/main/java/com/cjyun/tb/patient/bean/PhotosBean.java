package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/30 0030</br>
 * description:</br> 患者的相片
 */
public class PhotosBean extends DataSupport {
    @Expose
    private int id;
    @Expose
    private String full_name;
    @Expose
    private String phone_number;
    @Expose
    private Object address;
    @Expose
    private String created_at;
    @Expose
    private String updated_at;
    @Expose
    private int director_id;
    @Expose
    private boolean gender;
    @Expose
    private String weixin_id;
    @Expose
    private String qq;
    @Expose
    private Object email;
    @Expose
    private String remarks;
    @Expose
    private Object photo;

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getFull_name() { return full_name;}

    public void setFull_name(String full_name) { this.full_name = full_name;}

    public String getPhone_number() { return phone_number;}

    public void setPhone_number(String phone_number) { this.phone_number = phone_number;}

    public Object getAddress() { return address;}

    public void setAddress(Object address) { this.address = address;}

    public String getCreated_at() { return created_at;}

    public void setCreated_at(String created_at) { this.created_at = created_at;}

    public String getUpdated_at() { return updated_at;}

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at;}

    public int getDirector_id() { return director_id;}

    public void setDirector_id(int director_id) { this.director_id = director_id;}

    public boolean isGender() { return gender;}

    public void setGender(boolean gender) { this.gender = gender;}

    public String getWeixin_id() { return weixin_id;}

    public void setWeixin_id(String weixin_id) { this.weixin_id = weixin_id;}

    public String getQq() { return qq;}

    public void setQq(String qq) { this.qq = qq;}

    public Object getEmail() { return email;}

    public void setEmail(Object email) { this.email = email;}

    public String getRemarks() { return remarks;}

    public void setRemarks(String remarks) { this.remarks = remarks;}

    public Object getPhoto() { return photo;}

    public void setPhoto(Object photo) { this.photo = photo;}
}