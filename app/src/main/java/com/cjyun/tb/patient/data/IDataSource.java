package com.cjyun.tb.patient.data;

import com.cjyun.tb.patient.bean.CodeBean;
import com.cjyun.tb.patient.bean.UpdateBean;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Map;

import cn.collectcloud.password.scribe.model.Token;
import rx.Observable;

/**
 * 数据仓库接口 ~~只要数据
 */
public interface IDataSource {

    Observable<UpdateBean> onVersionUpgrade();

    Observable<Token> onVerifyUserLogin(String name, String password);

    <T extends DataSupport> Observable<T> bindBoxInfo(Class<T> clazz);

    Observable<CodeBean> postFirstStep(String boxID, String boxSN);

    Observable<CodeBean> postSecondStep();

    void refreshTasks();

    <T extends DataSupport> Observable<T> getBeanBySimple(Class<T> clazz);

    <T extends DataSupport> Observable<List<T>> getBeanByList(Class<T> clazz);

    <T extends DataSupport> Observable<T> getBoxIsOnline(Class<T> clazz);

    Observable<CodeBean> submitPatientInfo(Map<String, String> map);

    Observable<CodeBean> updateTime(String time);

    Observable<CodeBean> TelephoneVisit(int id, String time);

    Observable<CodeBean> submitSuggestion(String content, String phone);

    Observable<CodeBean> submitNewPassword(String old, String Pw);

    Observable<CodeBean> removeBinding();

    Observable<CodeBean> pushID(String id);

}
