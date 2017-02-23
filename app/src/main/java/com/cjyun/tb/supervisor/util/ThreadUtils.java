package com.cjyun.tb.supervisor.util;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/28 0028<br/>
 */
public class ThreadUtils<T> {

    public void onCreatCallable() {
        Callable<T> callable = new Callable<T>() {
            @Override
            public T call() throws Exception {
                return null;
            }
        };

        FutureTask<T> task = new FutureTask<>(callable);
        Thread t = new Thread(task);
        t.start();
        task.cancel(true);
    }


}
