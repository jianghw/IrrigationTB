package com.cjyun.tb.supervisor.base;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/19 0019<br/>
 */
public final class ConstantAll {

    public static final class ConString {
        public static final String CUR_USER = "CUR_USER";
        public static final String PATIENT_USER = "PATIENT_USER";//患者
        public static final String DIRECTOR_USER = "DIRECTOR_USER";//督导员

        public static final String CUR_MONTH = "CUR_MONTH";//本月
        public static final String PREV_MONTH = "PREV_MONTH";//上月

        public static final String FA_BUNDLE_TAG = "FA_BUNDLE";//fragment指向activity tag
        public static final String TIME_DRUG_TAG = "TIME_DRUG";//fragment指向tag 时间及药物ye
        public static final String VISIT_EVENT_TAG = "VISIT_EVENT";//访视事件
        public static final String RETURN_VISIT_TAG = "RETURN_VISIT";//复诊事件
        public static final String MY_DIRECTOR_TAG = "MY_DIRECTOR";//我的督导员
        public static final String MY_MESSAGE_TAG = "MY_MESSAGE";//我的消息
        public static final String SUGGESTION_BOX_TAG = "SUGGESTION_BOX";//意见箱
        public static final String GENERAL_TAG = "GENERAL";//通用页

        // 督导员的个人信息
        public static final String SU_MY_BASE_INFO_TAG = "SU_MY_BASE_INFO_TAG";//督导员的个人信息

    }

    public static final class ConUrl {
        //Basic请求
        public static final String API_KEY = "43fc6d6db459ccf7883c0968005e3f318cb933a8c31d60e6336314b9dba06217";
        public static final String API_SECRET = "827b98cd6e5a209a52e0b27f722030cf9ddedfe3817d26839c50465535c9f96f";
        public static final String API_SCOPES = "tuberculosis_app_read tuberculosis_app_write";
        //HTTP请求url
        public static final String URL_BASE_TB = "https://tb.ccd12320.com/";
        public static final String URL_BASE_CCD = "https://ccd12320.com/";


        public static final String URL_BASE_API = "https://api.ccd12320.com/";
        //HTTP 请求消息头设计
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER_TOKEN = "Bearer ";


        public static final String DEVICE_TYPE = "android ";

    }

    public static final class ConHandlerInt {
        public static final int HANDLER_BACK_ACTIVITY = 100;//回退当前activity指令

        public static final int HANDLER_INIT_FGT_LOGIN_BOX = 200;//登陆成功后绑定药盒指令
        public static final int HANDLER_INIT_FGT_LOGIN_PATIENT = 300;//登陆成功后患者主页指令
        public static final int HANDLER_INIT_FGT_LOGIN_DIRECTOR = 400;//登陆成功后督导员主页指令

        public static final int HANDLER_BACK_FGT = 500;//回退fragment指令
        public static final int HANDLER_TO_GSM = 600;//添加GSM页指令
        public static final int HANDLER_TO_BINDING = 700;//添加binding页指令
        public static final int HANDLER_TO_IMPROVE_INFO = 800;//跳转完善资料页指令

        public static final int HANDLER_TO_ATY_CAPTURE = 900;//添跳转扫描页页指令


    }
}
