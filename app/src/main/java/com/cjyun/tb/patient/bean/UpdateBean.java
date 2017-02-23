package com.cjyun.tb.patient.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by jianghw on 2016/4/5 0005.
 * Description app更新信息
 */
public class UpdateBean extends DataSupport{

    /**
     * id : 14
     * name : 药管家
     * version : 17
     * url : http://ccd12320.com/apps/14/download
     * created_at : 2016-01-05T14:39:59.000+08:00
     * updated_at : 2016-04-12T15:02:07.000+08:00
     * file_name : ygj-17-1.3.3.apk
     * size : 7298912
     * description : [修复问题]
     1.修复已知导致程序崩溃的BUG.
     * tag : 1.3.3
     */

    private int id;
    private String name;
    private int version;
    private String url;
    private String created_at;
    private String updated_at;
    private String file_name;
    private int size;
    private String description;
    private String tag;

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public int getVersion() { return version;}

    public void setVersion(int version) { this.version = version;}

    public String getUrl() { return url;}

    public void setUrl(String url) { this.url = url;}

    public String getCreated_at() { return created_at;}

    public void setCreated_at(String created_at) { this.created_at = created_at;}

    public String getUpdated_at() { return updated_at;}

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at;}

    public String getFile_name() { return file_name;}

    public void setFile_name(String file_name) { this.file_name = file_name;}

    public int getSize() { return size;}

    public void setSize(int size) { this.size = size;}

    public String getDescription() { return description;}

    public void setDescription(String description) { this.description = description;}

    public String getTag() { return tag;}

    public void setTag(String tag) { this.tag = tag;}
}
