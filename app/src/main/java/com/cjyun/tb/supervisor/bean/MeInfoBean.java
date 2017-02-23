package com.cjyun.tb.supervisor.bean;

/**
 * Created by Administrator on 2016/5/27 0027.
 * <p/>
 * 我的个人信息bean
 */
public class MeInfoBean {


    /**
     * id : 2
     * organization_id : 1
     * email : 123123@aa.com
     * created_at : 2016-05-12T09:56:35.604+08:00
     * updated_at : 2016-05-25T17:34:38.211+08:00
     * name :
     * phone_number :
     * user_type : web_user
     * activate_flag : false
     * freeze_flag : false
     * id_card_number : 123123
     */

    private UserEntity user;
    /**
     * director_id : 2
     * id : 1
     * full_name : 督导员——1
     * phone_number : 123123
     * address : null
     * created_at : 2016-05-12T09:56:35.631+08:00
     * updated_at : 2016-05-12T09:56:35.631+08:00
     * gender : true
     * weixin_id : 12312312
     * qq : 3123123
     * email : null
     * remarks : ffffff
     * photo : null
     */

    private InfoEntity info;
    /**
     * id : 1
     * district_id : 584
     * name : 机构名称——1
     * number : 1
     * address : 详细地址
     * created_at : 2016-05-12T09:55:02.600+08:00
     * updated_at : 2016-05-12T09:55:02.600+08:00
     */

    private OrganizationEntity organization;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public static class UserEntity {
        private int id;
        private int organization_id;
        private String email;
        private String created_at;
        private String updated_at;
        private String name;
        private String phone_number;
        private String user_type;
        private boolean activate_flag;
        private boolean freeze_flag;
        private String id_card_number;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrganization_id() {
            return organization_id;
        }

        public void setOrganization_id(int organization_id) {
            this.organization_id = organization_id;
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

        public String getId_card_number() {
            return id_card_number;
        }

        public void setId_card_number(String id_card_number) {
            this.id_card_number = id_card_number;
        }
    }

    public static class InfoEntity {
        private int director_id;
        private int id;
        private String full_name;
        private String phone_number;
        private String address;
        private String created_at;
        private String updated_at;
        private boolean gender;
        private String weixin_id;
        private String qq;
        private String email;
        private String remarks;
        private String  photo;

        public int getDirector_id() {
            return director_id;
        }

        public void setDirector_id(int director_id) {
            this.director_id = director_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

    public static class OrganizationEntity {
        private int id;
        private int district_id;
        private String name;
        private String number;
        private String address;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDistrict_id() {
            return district_id;
        }

        public void setDistrict_id(int district_id) {
            this.district_id = district_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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
    }
}
