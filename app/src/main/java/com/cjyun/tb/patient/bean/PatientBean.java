package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/17 0017</br>
 * description:</br> 完善信息
 * 一对一 {@link InfoBean} {@link DeviceBean}
 */
public class PatientBean extends DataSupport {


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
    @Expose
    private UserBean user;
    /**
     * patient_id : 3
     * id : 1
     * created_at : 2016-05-07T14:38:13.810+08:00
     * updated_at : 2016-05-07T14:38:13.810+08:00
     * phone_number : 13714418252
     * full_name : 患者——1
     * address : 1
     * gender : true
     * weixin_id : 1
     * qq : 1
     * email : 1
     * remarks : 123123131223
     * photo : 1
     */
    @Expose
    private InfoBean info;
    /**
     * patient_id : 3
     * id : 1
     * device_uid : 1000010
     * device_sn : 11111111
     * bound_activate_flag : true
     * created_at : 2016-05-07T10:02:09.175+08:00
     * updated_at : 2016-05-07T10:02:09.175+08:00
     * last_visit : null
     * first_visit_time : null
     */
    @Expose
    private DeviceBean device;

    public UserBean getUser() { return user;}

    public void setUser(UserBean user) { this.user = user;}

    public InfoBean getInfo() { return info;}

    public void setInfo(InfoBean info) { this.info = info;}

    public DeviceBean getDevice() { return device;}

    public void setDevice(DeviceBean device) { this.device = device;}

}
