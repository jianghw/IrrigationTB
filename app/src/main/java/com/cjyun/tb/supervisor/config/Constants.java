package com.cjyun.tb.supervisor.config;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public interface Constants {

    // 定义本地存储数据刷新的时间
    public static final long PROTOCOLTIMEOUT = 5 * 60 * 1000;

    public interface SQL {

        public static final String SQL_NAME = "patient_news.db";
        public static final int DATA_VERSION = 1;

        public static final String TAB_NAME = "patient_news";

        public static final String NAME = "name";
        public static final String PHOTo = "photo";
        public static final String ICON = "icon";
        public static final String TIME = "time";

        public static final String ID = "id";


       /* public static final String  ONEMONTH = "onemon";
        public static final String   = "onemon";
        public static final String  ONEMONTH = "onemon";
        public static final String  ONEMONTH = "onemon";
        public static final String  ONEMONTH = "onemon";*/

        public static final String TAB_VISIT_CONTENT = "visit_content";
        public static final String SETTING_DATE = "setting_date";// 设置的访视的时间
        public static final String VERIFY_DATE = "verify_date";// 确认访视的时间
        public static final String VT_TYPE = "vt_type";// 设置的访视类型
        public static final String VT_CONTENT = "vt_content";// 访视的内容


        public static final String PT_BASE_INFO = "pt_base_info"; //患者基本信息
        public static final String PT_DETAILS = "pt_details";// 患者详请信息
        public static final String TACK_PILLS = "tack_pills";// 服药信息
        public static final String SUBSEQUENT = "subsequent";//复诊时间
        public static final String VISIT = "VISIT";//访视时间
        public static final String MI = "mi";//督导员个人信息


    }

    interface Url {

        public static final String BASE_RUL = "http://www.ccd12320.com/";

    }


}
