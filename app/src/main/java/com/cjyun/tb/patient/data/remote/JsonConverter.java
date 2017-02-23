package com.cjyun.tb.patient.data.remote;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.RemoteJson;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.socks.library.KLog;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2016/5/21 0021</br>
 * description:</br>
 */
public class JsonConverter<T> implements Converter<ResponseBody, T> {
    @Override
    public T convert(ResponseBody value) throws IOException {
        if (null == value) return null;
        String json = value.string();
        KLog.json(json);

        RemoteJson result = new RemoteJson();
        JsonElement jsonElement = new JsonParser().parse(json);

        if (jsonElement.isJsonObject() && null != jsonElement.getAsJsonObject()) {
            KLog.a("isJsonObject");
            if (jsonElement.getAsJsonObject().has("response")) {
                //由response开头
                KLog.i("response");
                JsonElement responseEle = jsonElement.getAsJsonObject().get("response");
                onSucceedButError(json, result, responseEle);
            } else if (jsonElement.getAsJsonObject().has("status")) {
                KLog.i("status");
                analyStatus(result, jsonElement);
            } else if (jsonElement.getAsJsonObject().toString().equals("{}")) {
                KLog.i("{}");
                result.setStatus(0);
                result.setMessage(TbApp.getAppResource().getString(R.string.response_null));
            } else {
                KLog.i("[xxxxxxxxxx]");
                result.setStatus(1);
                result.setData(json);
            }
        } else if (jsonElement.isJsonArray() && null != jsonElement.getAsJsonArray()) {
            KLog.a("isJsonArray");
            if (jsonElement.getAsJsonArray().size()==0) {
                result.setStatus(0);
                result.setMessage(TbApp.getAppResource().getString(R.string.response_null));
            } else {
                result.setStatus(1);
                result.setData(json);
            }
        } else if (jsonElement.isJsonNull()) {
            KLog.a("isJsonNull");
            result.setStatus(0);
            result.setMessage(TbApp.getAppResource().getString(R.string.response_null));
        } else {
            KLog.a("elsexxxxxxxxxxxxx");
            return null;
        }
        return (T) result;
    }

    private void onSucceedButError(String json, RemoteJson result, JsonElement responseEle) {
        if (null != responseEle && responseEle.isJsonObject()) {//200出错时
            if (responseEle.getAsJsonObject().has("status")) {
                analyStatus(result, responseEle);
            } else {
                result.setStatus(1);
                String j = new Gson().toJson(responseEle);
                result.setData(j);
            }
        } else if (null != responseEle) {
            result.setStatus(1);
            result.setData(json);
        }
    }

    private void analyStatus(RemoteJson result, JsonElement jsonElement) {

        if (jsonElement.getAsJsonObject().get("status").getAsString().equals("failure")) {
            result.setStatus(0);
            if (jsonElement.getAsJsonObject().has("failure_index"))
                result.setMessage(jsonElement.getAsJsonObject().get("failure_index").getAsString());
        } else {
            result.setData(new Gson().toJson(jsonElement));
            result.setStatus(1);
        }
    }
}
