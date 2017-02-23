package com.cjyun.tb.supervisor.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24 0024.
 * <p/>
 * 所有患者的信息
 */
public class UserEntityBean {


    /**
     * id : 15
     * email : hz@163.com
     * created_at : 2016-05-27T09:12:19.000+08:00
     * updated_at : 2016-05-27T09:12:19.000+08:00
     * name :
     * phone_number :
     * user_type : web_user
     * activate_flag : false
     * freeze_flag : false
     * organization_id : 1
     * id_card_number : 222222222222222222
     */

    private List<PatientsEntity> patients;

    /**
     * id : 2
     * patient_id : 15
     * created_at : 2016-05-27T09:12:19.000+08:00
     * updated_at : 2016-05-27T09:12:19.000+08:00
     * phone_number : 15454545544
     * full_name : hz001
     * address :
     * gender : false
     * weixin_id : cnma
     * qq : 141325046566
     * email : jha@163.com
     * remarks : 凑啥~~
     * photo :
     */
    private List<PhotosEntity> photos;

    public List<PatientsEntity> getPatients() {
        return patients;
    }

    public void setPatients(List<PatientsEntity> patients) {
        this.patients = patients;
    }

    public List<PhotosEntity> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotosEntity> photos) {
        this.photos = photos;
    }

    public static class PatientsEntity {
        private int id;
        private String email;
        private String created_at;
        private String updated_at;
        private String name;
        private String phone_number;
        private String user_type;
        private boolean activate_flag;
        private boolean freeze_flag;
        private int organization_id;
        private String id_card_number;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

    public static class PhotosEntity {
        private int id;
        private int patient_id;
        private String created_at;
        private String updated_at;
        private String phone_number;
        private String full_name;
        private String address;
        private boolean gender;
        private String weixin_id;
        private String qq;
        private String email;
        private String remarks;
        private String photo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPatient_id() {
            return patient_id;
        }

        public void setPatient_id(int patient_id) {
            this.patient_id = patient_id;
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

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        public String getWeixin_id() {
            return weixin_id;
        }

        public void setWeixin_id(String weixin_id) {
            this.weixin_id = weixin_id;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}

