package com.cjyun.tb.supervisor.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/17 0017.
 * 添加访问事件的Bean
 */
public class AddVisitBean implements Serializable {

    private int id; // 访问患者的ID
    private long time;// 访问的 时间
    private String visit_type;// 访问的类型
    private String content;// 访问患者的内容
}
