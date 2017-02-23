package com.cjyun.tb.patient.constant;

/**
 * 全局
 */
public class TbGlobal {

    public static final class JString {
        public static final String CUR_USER = "cur_user";
        public static final String PATIENT_USER = "patient_user";//患者
        public static final String DIRECTOR_USER = "director_user";//督导员

        public static final String CUR_MONTH = "cur_month";//本月
        public static final String PREV_MONTH = "prev_month";//上月

        public static final String FA_BUNDLE_TAG = "fa_bundle";//fragment指向activity tag
        public static final String TIME_DRUG_TAG = "time_drug";//fragment指向tag 时间及药物ye
        public static final String VISIT_EVENT_TAG = "visit_event";//访视事件
        public static final String RETURN_VISIT_TAG = "return_visit";//复诊事件
        public static final String MY_DIRECTOR_TAG = "my_director";//我的督导员
        public static final String MY_MESSAGE_TAG = "my_message";//我的消息
        public static final String SUGGESTION_BOX_TAG = "suggestion_box";//意见箱
        public static final String GENERAL_TAG = "general";//通用页
    }

    public static final class SUrl {
        //Basic请求
        public static final String API_KEY = "4bc6ca4e61b61ca247ddd039a9b97e6504a4e875dd158988015e143f10862c08";
        public static final String API_SECRET = "30c9a6ea5c307f0b1859004676eade022fbfac9006c3a3e2e0a4d7c74c6d3a1c";
        public static final String API_SCOPES = "tuberculosis_app_read tuberculosis_app_write";

        //HTTP请求url
        public static final String URL_BASE_CCD = "http://www.caijiyun.cn/";
        public static final String URL_BASE_API = "https://api.ccd12320.com/";
        public static final String URL_BASE_TB = "https://tb.ccd12320.com/";

        //HTTP 请求消息头设计
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER_TOKEN = "Bearer ";
    }

    public static final class IntHandler {
        public static final int HANDLER_BACK_ACTIVITY = 100;//回退当前activity指令

        public static final int HANDLER_FGT_LOGIN_BOX = 200;//登陆成功后绑定药盒指令
        public static final int HANDLER_FGT_LOGIN_PATIENT = 300;//登陆成功后患者信息页指令
        public static final int HANDLER_FGT_LOGIN_DIRECTOR = 400;//登陆成功后督导员信息页指令

        public static final int HANDLER_BACK_FGT = 500;//回退fragment指令

        public static final int HANDLER_TO_GSM = 600;//添加GSM页指令
        public static final int HANDLER_TO_WIFI = 610;//添加GSM页指令
        public static final int HANDLER_TO_BINDING = 700;//添加binding页指令
        public static final int HANDLER_TO_IMPROVE_INFO = 800;//跳转完善资料页指令

        public static final int HANDLER_TO_ATY_CAPTURE = 900;//添跳转扫描页页指令

        public static final int HANDLER_FGT_FONT = 510;//字体页面
        public static final int HANDLER_FGT_ABOUT_US = 520;
        public static final int HANDLER_FGT_DISCLAIMER = 530;
    }
}
