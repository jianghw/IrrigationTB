package com.cjyun.tb.supervisor.meInterface;

import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.UpdateBean;
import com.cjyun.tb.patient.data.IDataSource;
import com.cjyun.tb.patient.data.remote.RemoteServer;
import com.cjyun.tb.patient.service.DownloadProgressHandler;
import com.cjyun.tb.patient.service.ProgressHelper;
import com.cjyun.tb.patient.util.AppUtils;
import com.cjyun.tb.patient.util.NullExUtils;
import com.cjyun.tb.patient.util.SDCardTool;
import com.cjyun.tb.patient.util.ToastUtils;
import com.socks.library.KLog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/6/12 0012</br>
 * description:</br>
 */
public class SuUpdatePresenter implements MeNews.SuUpdatePresenter {
    private final CompositeSubscription mSubscriptions;
    private final MeNews.SuUpdateFragment mFragment;
    private final IDataSource mDataRepository;

    public SuUpdatePresenter(IDataSource repository, MeNews.SuUpdateFragment fragment) {
        this.mDataRepository = NullExUtils.checkNotNull(repository, "Repository cannot be null");
        this.mFragment = NullExUtils.checkNotNull(fragment, "View cannot be null!");


        mSubscriptions = new CompositeSubscription();
    }

  /*  @Override
    public void onSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }*/

    @Override
    public void updateInfo() {
        Subscription subscription = mDataRepository.onVersionUpgrade()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.getMessage());
                        if (e.getMessage().contains("No address associated with hostname")) {//无网络
                            mFragment.showToastMessage(100);
                        }
                    }

                    @Override
                    public void onNext(UpdateBean bean) {
                        try {
                            isToUpgrade(bean);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    public void isToUpgrade(UpdateBean response) throws Exception {
        if (AppUtils.getVerCode() < (response != null ? response.getVersion() : 0)) {//显示更新框
            downloadAppFile(response);
        } else {
            // 是否自动登陆 隐藏选择按钮
            mFragment.showIsNewVerCode();
        }
    }

    private void downloadAppFile(final UpdateBean response) {

        Retrofit.Builder retrofitBuilder = new Retrofit
                .Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://ccd12320.com/");
        OkHttpClient.Builder builder = ProgressHelper.addProgress(null);
        RemoteServer retrofit = retrofitBuilder
                .client(builder.build())
                .build().create(RemoteServer.class);
        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                //                Log.e("是否在主线程中运行", String.valueOf(Looper.getMainLooper() == Looper.myLooper()));
                //                Log.e("onProgress", String.format("%d%% done\n", (100 * bytesRead) / contentLength));
                //                dialog.setMax((int) (contentLength / 1024));
                //                dialog.setProgress((int) (bytesRead / 1024));
                String s = String.format("%d%%\n", (100 * bytesRead) / contentLength);
                mFragment.onProgress(s.substring(1, s.length()));
                if (done) mFragment.dismissLoding();

            }
        });

        Call<ResponseBody> call = retrofit.getFile();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> callback) {
                try {
                    InputStream is = callback.body().byteStream();
                    File file = new File(SDCardTool.getSDCardPath(), response.getFile_name());
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mFragment.dismissLoding();
                    AppUtils.installApk(TbApp.mContext, SDCardTool.getSDCardPath() + response.getFile_name());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtils.showMessage(t.getMessage());
            }
        });
    }
}
