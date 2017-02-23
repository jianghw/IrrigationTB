package com.cjyun.tb.supervisor.data;


import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2016/5/11 0011.
 * 获取对象的实例 注入
 */
public class SuInjection {

    public static SuDataRepository provideTwoRepository() {

       /* return SuDataRepository.getInstance(
                SuInitRemoteData.getInstance(),
                SuInitLocalData.getInstance(context));*/

        return SuDataRepository.getInstance(SuInitRemoteData.getInstance(),
                SuInitLocalData.getInstance());
    }
}
