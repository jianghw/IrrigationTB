package com.cjyun.tb.supervisor.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.util.UIUtils;

/**
 * 描述	      1.提供视图(4种视图类型中的一种)
 * 描述	      2.加载数据
 * 描述	      3.数据和视图的绑定(绑定成功视图)
 */
public abstract class LoadingPager extends FrameLayout {
    public static final int STATE_LOADING = 0;//加载中
    public static final int STATE_SUCCESS = 1;//成功
    public static final int STATE_ERROR = 2;//错误
    public static final int STATE_EMPTY = 3;//空

    public int mCurState = STATE_LOADING;

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;
    private LoadDataTask mLoadDataTask;

    public LoadingPager(Context context) {
        super(context);
        initCommonView();
    }

    /**
     * @des 初始化常规视图(加载中视图, 错误视图, 空视图)
     * @called LoadingPager创建的时候
     */
    private void initCommonView() {
        //加载中视图
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        addView(mLoadingView);

        //错误视图
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        addView(mErrorView);

      /*  mErrorView.findViewById(R.id.pager_loading).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新触发加载数据
                triggerLoadData();
            }
        });*/

        //空视图
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        addView(mEmptyView);

        refreshUIByState();

        triggerLoadData();
    }

    /**
     * @des 根据当前的状态展示具体的ui
     * @called 1.LoadingPager一旦创建的时候
     * @called 2.开始加载数据之前
     * @called 3.数据加载完成的时候
     */
    private void refreshUIByState() {
        //控制加载中视图 显示/隐藏
        mLoadingView.setVisibility((mCurState == STATE_LOADING) ? View.VISIBLE : View.GONE);

        //控制错误视图 显示/隐藏
        mErrorView.setVisibility((mCurState == STATE_ERROR) ? View.VISIBLE : View.GONE);

        //控制空视图 显示/隐藏
        mEmptyView.setVisibility((mCurState == STATE_EMPTY) ? View.VISIBLE : View.GONE);

        if (mCurState == STATE_SUCCESS && mSuccessView == null) {
            //初始化成功视图,并且加入容器
            mSuccessView = initSuccessView();
            addView(mSuccessView);
        }
        //控制成功视图 显示/隐藏
        if (mSuccessView != null) {

            mSuccessView.setVisibility((mCurState == STATE_SUCCESS) ? View.VISIBLE : View.GONE);
        }
    }


    /**
     加载数据
     1.触发加载-->加载中视图
     一进入页面就加载
     点击按钮就加载
     下拉刷新的时候就加载
     上滑时候就加载
     2.异步加载数据-->加载中视图
     3.得到数据
     4.处理数据
     5.处理ui展现
     1.数据加载成功-->展示成功视图
     2.数据加载失败
     出现异常-->展示错误视图
     数据为空-->展示空视图
     */
    /**
     * @des 触发加载数据, 进行数据和视图的绑定
     * @called 外部需要加载数据的时候调用该方法
     */
    public void triggerLoadData() {

        if (mCurState != STATE_SUCCESS && mLoadDataTask == null) {
//            LogUtils.sf("triggerLoadData");
            //异步加载数据之前,重置状态
            mCurState = STATE_LOADING;
            refreshUIByState();

            //异步加载数据
            mLoadDataTask = new LoadDataTask();

            new Thread(mLoadDataTask).start();

            //希望找一个线程池执行任务
            //那就是找线程池代理
            //线程池代理工厂
            // ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(mLoadDataTask);

        }

    }

    class LoadDataTask implements Runnable {
        @Override
        public void run() {
            //真正的加载具体的数据
            LoadedResult tempState = initData();//5s
            /**
             SUCCESS(1)
             ERROR(2)
             EMPTY(3)
             */

            //得到数据,处理数据
            mCurState = tempState.getState();


            UIUtils.postTask(new Runnable() {
                @Override
                public void run() {
                    //刷新ui-->一个最新的状态-->int
                    refreshUIByState();
                }
            });

            //置空任务
            mLoadDataTask = null;
        }
    }

    /**
     * @return
     * @des 在子线程中, 真正的加载数据
     * @des 在LoadingPager里面不知道如何真正的加载数据交给子类
     * @called 外部需要加载数据的时候调用了triggerLoadData
     */
    public abstract LoadedResult initData();

    /**
     * @return
     * @des 初始化成功视图, 而且还要进行数据的绑定
     * @des 在LoadingPager里面不知道如何展示具体的成功视图, 只能交给子类
     * @called 触发加载数据, 数据加载完成, 数据加载成功
     */
    public abstract View initSuccessView();


    public enum LoadedResult {

        SUCCESS(STATE_SUCCESS), ERROR(STATE_ERROR), EMPTY(STATE_EMPTY);
        int state;

        public int getState() {
            return state;
        }

        LoadedResult(int state) {
            this.state = state;
        }
    }
}
