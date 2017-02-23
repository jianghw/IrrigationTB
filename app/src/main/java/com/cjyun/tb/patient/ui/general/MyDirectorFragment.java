package com.cjyun.tb.patient.ui.general;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseFragment;
import com.cjyun.tb.patient.bean.DirectorBean;
import com.cjyun.tb.patient.bean.MyDirectorBean;
import com.cjyun.tb.patient.bean.PhotosBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.adapter.MyDirectorAdapter;
import com.cjyun.tb.patient.data.Injection;
import com.cjyun.tb.patient.util.ToastUtils;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的督导员们
 */
public class MyDirectorFragment extends BaseFragment implements IGeneralContract.IMyDirectorView {


    private static Handler mHandler;
    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.lv_my_director)
    ListView mLvMyDirector;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private Activity mActivity;
    private IGeneralContract.IMyDirectorPresenter mPresent;
    private ArrayList<DirectorBean> mMyDirectorList;//督导员们
    private MyDirectorAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static MyDirectorFragment newInstance(Handler handler) {
        mHandler = handler;
        return new MyDirectorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresent == null) {
            mPresent = new MyDirectorPresenter(Injection.provideTwoRepository(), this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_director, container, false);
        ButterKnife.bind(this, root);

        mSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        mSwipeRefresh.setDistanceToTriggerSync(500);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresent.loadingRemoteData(true);
            }
        });
        mMyDirectorList = new ArrayList<>();
        adapter = new MyDirectorAdapter(mActivity, mMyDirectorList);
        mLvMyDirector.setAdapter(adapter);

        mLvMyDirector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DirectorBean directorBean = mMyDirectorList.get(position);
                List<PhotosBean> newsList = DataSupport
                        .where("director_id = ?", String.valueOf(directorBean.getId()))
                        .find(PhotosBean.class);
                KLog.a(directorBean.getId());
                KLog.a(Arrays.toString(newsList.toArray()));
                if (newsList.size() == 1) {
                    PhotosBean bean = newsList.get(0);
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(R.string.aty_userInfo_age)).append(":").append(directorBean.getId_card_number()).append("\r\n").append("\r\n");
                    sb.append(getString(R.string.aty_userInfo_phone)).append(":").append(bean.getPhone_number()).append("\r\n").append("\r\n");
                    sb.append(getString(R.string.aty_userInfo_guardian1)).append(":").append(bean.getQq()).append("\r\n").append("\r\n");
                    sb.append(getString(R.string.aty_userInfo_guardian2)).append(":").append(bean.getWeixin_id()).append("\r\n").append("\r\n");
                    sb.append(getString(R.string.aty_userInfo_email)).append(":").append(bean.getEmail()).append("\r\n").append("\r\n");

                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

                    builder.setTitle(bean.getFull_name())
                            .setMessage(sb.toString())
                            .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    ToastUtils.showMessage("此督导员没有设置信息~~");
                }
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresent.onSubscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresent.unSubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onClickBack(){
        mHandler.sendEmptyMessage(TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY);
    }

    @Override
    public void setLoadingIndicator(final boolean b) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(b);
            }
        });
    }

    @Override
    public void updateListData(MyDirectorBean data) {
        HashMap<Integer, PhotosBean> array = new HashMap<>();
        List<PhotosBean> photos = data.getPhotos();
        for (PhotosBean bean : photos) {
            KLog.a(bean.getDirector_id());
            array.put(bean.getDirector_id(), bean);
        }
        adapter.setDateForDirector(array);

        if (!mMyDirectorList.isEmpty()) mMyDirectorList.clear();
        mMyDirectorList.addAll(data.getDirector());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showToastMessage(int id) {

    }

    @Override
    public void loadingDialog() {

    }

    @Override
    public void dismissDialog() {

    }
}
