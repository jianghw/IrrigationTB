package com.cjyun.tb.patient.constant;

public interface ConstantString {
    //指向患者或督导员
    String To_USER = "user";
    String PATIENT_USER = "patient";
    String DIRECTOR_USER = "director";
    String PHONE_USER="phone";

    //注册请求字段
    String USER_TYPE_PT = "sz_flb_user";
    String USER_TYPE_DR = "pre_tuberculosis_director";
    String KEY_TYPE_PT = "phone_number";
    String ITEM_NAME_PT = "medicine_box_tuberculosis";
    //HTTP请求url
    String URL_BASE_CCD = "https://ccd12320.com/";
    String URL_BASE_API = "https://api.ccd12320.com/";
    //Basic请求
    String API_KEY="43fc6d6db459ccf7883c0968005e3f318cb933a8c31d60e6336314b9dba06217";
    String API_SECRET="827b98cd6e5a209a52e0b27f722030cf9ddedfe3817d26839c50465535c9f96f";
    String API_SCOPES="tuberculosis_app_read tuberculosis_app_write";

/*    String API_KEY = "457bde1b0b7902781483a6c445280a971e11f9eb08067d095be86ecd0f8aee83";
    String API_SECRET = "90a8cdf44921910ff90636ddf3b670bd34b4272918a3312e688bfb5819595c98";
    String API_SCOPES = "flb_app_read flb_app_write";*/

    String AUTHORIZATION = "Authorization";
    String BASIC_TOKEN="Basic "+
            "NDNmYzZkNmRiNDU5Y2NmNzg4M2MwOTY4MDA1ZTNmMzE4Y2I5MzNhOGMzMWQ2MGU2MzM2MzE0YjlkYmEwNjIxNzo4MjdiOThjZDZlNWEy" +
            "MDlhNTJlMGIyN2Y3MjIwMzBjZjlkZGVkZmUzODE3ZDI2ODM5YzUwNDY1NTM1YzlmOTZm";

    String BEARER_TOKEN ="Bearer ";
}
