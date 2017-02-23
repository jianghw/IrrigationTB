package com.cjyun.tb.supervisor.bean;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class AllSuPatientBean {

    // TODO 假的数据，只用作展示
    public int id;
    public String name;
    public String time;


    /**
     * email : 11111@11.com
     * created_at : 2016-05-07T14:38:13.791+08:00
     * updated_at : 2016-05-07T14:38:13.791+08:00
     * phone_number :
     * user_type : web_user
     * activate_flag : false
     * freeze_flag : false
     * organization_id : 1
     * id_card_number : 123123123
     */

    private String email;
    private String created_at;
    private String updated_at;
    private String phone_number;
    private String user_type;
    private boolean activate_flag;
    private boolean freeze_flag;
    private int organization_id;
    private String id_card_number;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public boolean isActivate_flag() {
        return activate_flag;
    }

    public void setActivate_flag(boolean activate_flag) {
        this.activate_flag = activate_flag;
    }

    public boolean isFreeze_flag() {
        return freeze_flag;
    }

    public void setFreeze_flag(boolean freeze_flag) {
        this.freeze_flag = freeze_flag;
    }

    public int getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(int organization_id) {
        this.organization_id = organization_id;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

}
