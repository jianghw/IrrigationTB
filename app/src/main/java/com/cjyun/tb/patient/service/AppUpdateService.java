package com.cjyun.tb.patient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by jianghw on 2016/4/5 0005.
 * Description
 */
public class AppUpdateService extends Service {

    private OkHttpClient client;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 非首次运行服务时，执行下面操作
        if (client != null) {
            return START_STICKY;
        }
        //TODO 为空时
        String url = intent.getStringExtra("url");
        final String fileFullName = intent.getStringExtra("file");

        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(fileFullName)) {
            // 首次运行时，创建并启动线程
            Request request = new Request.Builder()
                    //下载地址
                    .url(url)
                    .build();
            //发送异步请求
            client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("failure");
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    //将返回结果转化为流，并写入文件
                    writeToFile(response, fileFullName);
                }
            });
        }
        return START_STICKY;
    }

    private void writeToFile(final Response response, final String fileFullName) {
        new Thread() {
            @Override
            public void run() {
                try {
                    int len;
                    byte[] buf = new byte[2048];
                    InputStream inputStream = response.body().byteStream();
                    //可以在这里自定义路径
                    File file1 = new File(fileFullName);
                    FileOutputStream fileOutputStream = new FileOutputStream(file1);

                    while ((len = inputStream.read(buf)) != -1) {
                        fileOutputStream.write(buf, 0, len);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Override
    public void onDestroy() {
        // 终止服务
        super.onDestroy();
    }
}
