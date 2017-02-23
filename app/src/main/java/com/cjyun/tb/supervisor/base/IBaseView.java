package com.cjyun.tb.supervisor.base;


public interface IBaseView<T> {

    void showToastMessage(int id);

    void loadingDialog();

    void dismissDialog();
}
