package com.cjyun.tb.patient.base;


public interface IBaseView<T> {

    void showToastMessage(int id);

    void loadingDialog();

    void dismissDialog();
}
