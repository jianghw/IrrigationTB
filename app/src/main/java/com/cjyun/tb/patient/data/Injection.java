package com.cjyun.tb.patient.data;

import com.cjyun.tb.patient.data.local.LocalData;
import com.cjyun.tb.patient.data.remote.RemoteData;

/**
 * 数据层传递过来
 */
public class Injection {

    public static DataRepository provideTwoRepository() {
        return DataRepository.getInstance(RemoteData.getInstance(), LocalData.getInstance());
    }
}
