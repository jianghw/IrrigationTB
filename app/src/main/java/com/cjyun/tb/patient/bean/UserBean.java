package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/27 0027</br>
 * description:</br>
 */
public class UserBean extends DataSupport {

    @Expose
    private long id;
    @Expose
    private int organization_id;
    @Expose
    private String email;
    @Expose
    private String created_at;
    @Expose
    private String updated_at;
    @Expose
    private String name;
    @Expose
    private String phone_number;
    @Expose
    private String user_type;
    @Expose
    private boolean activate_flag;
    @Expose
    private boolean freeze_flag;
    @Expose
    private String id_card_number;

    public long getId() { return id;}

    public void setId(int id) { this.id = id;}

    public int getOrganization_id() { return organization_id;}

    public void setOrganization_id(int organization_id) { this.organization_id = organization_id;}

    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}

    public String getCreated_at() { return created_at;}

    public void setCreated_at(String created_at) { this.created_at = created_at;}

    public String getUpdated_at() { return updated_at;}

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public String getPhone_number() { return phone_number;}

    public void setPhone_number(String phone_number) { this.phone_number = phone_number;}

    public String getUser_type() { return user_type;}

    public void setUser_type(String user_type) { this.user_type = user_type;}

    public boolean isActivate_flag() { return activate_flag;}

    public void setActivate_flag(boolean activate_flag) { this.activate_flag = activate_flag;}

    public boolean isFreeze_flag() { return freeze_flag;}

    public void setFreeze_flag(boolean freeze_flag) { this.freeze_flag = freeze_flag;}

    public String getId_card_number() { return id_card_number;}

    public void setId_card_number(String id_card_number) { this.id_card_number = id_card_number;}
}
