package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17 0017</br>
 * description:</br>
 */
public class MyDirectorBean extends DataSupport{

    /**
     * id : 14
     * email : ddy@163.com
     * created_at : 2016-05-27T09:10:24.000+08:00
     * updated_at : 2016-05-27T09:10:24.000+08:00
     * name :
     * phone_number :
     * user_type : web_user
     * activate_flag : false
     * freeze_flag : false
     * organization_id : 1
     * id_card_number : 111111111111111111
     */
    @Expose
    private List<DirectorBean> director;
    /**
     * id : 2
     * full_name : ddy001
     * phone_number : 15353535533
     * address : null
     * created_at : 2016-05-27T09:10:24.000+08:00
     * updated_at : 2016-05-27T09:10:24.000+08:00
     * director_id : 14
     * gender : true
     * weixin_id : nima
     * qq : 1314520
     * email : null
     * remarks : 不许动~
     * photo : null
     */
    @Expose
    private List<PhotosBean> photos;

    public List<DirectorBean> getDirector() { return director;}

    public void setDirector(List<DirectorBean> director) { this.director = director;}

    public List<PhotosBean> getPhotos() { return photos;}

    public void setPhotos(List<PhotosBean> photos) { this.photos = photos;}
}
