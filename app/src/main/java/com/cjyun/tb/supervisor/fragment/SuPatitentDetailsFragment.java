package com.cjyun.tb.supervisor.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.Presenter.SuPatitentDetailsPresenter;
import com.cjyun.tb.supervisor.activity.SuPatientNewsActivity;
import com.cjyun.tb.supervisor.bean.PatitentDetailsBean;
import com.cjyun.tb.supervisor.data.SuInjection;
import com.socks.library.KLog;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/3 0003.
 * <p/>
 * 患者的详细信息
 */
public class SuPatitentDetailsFragment extends SuBaseFragment implements Contract.BaseNewsView {


    private static final String TAG = "SuPatitentDetailsFragment";
    @Bind(R.id.fm_news_phone)
    TextView fmNewsPhone;
    @Bind(R.id.fm_news_location)
    TextView fmNewsLocation;
    @Bind(R.id.fm_news_id)
    TextView fmNewsId;
    @Bind(R.id.fm_news_identity)
    TextView fmNewsIdentity;
    @Bind(R.id.fm_news_qq)
    TextView fmNewsQq;
    @Bind(R.id.fm_news_weixin)
    TextView fmNewsWeixin;
    @Bind(R.id.fm_news_email)
    TextView fmNewsEmail;
    @Bind(R.id.fm_news_institution_name)
    TextView fmNewsInstitutionName;
    @Bind(R.id.fm_news_institution_coding)
    TextView fmNewsInstitutionCoding;
    @Bind(R.id.fm_news_institution_location)
    TextView fmNewsInstitutionLocation;
    @Bind(R.id.fg_su_ptNews_refresh)
    SwipeRefreshLayout fgSuPtNewsRefresh;

    private SuPatitentDetailsPresenter mPresenter;
    private boolean isLoad = false;
    private int id;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_base_news, container, false);
        ButterKnife.bind(this, view);

        // 刷新数据
        fgSuPtNewsRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        fgSuPtNewsRefresh.setDistanceToTriggerSync(400);
        fgSuPtNewsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                KLog.d("刷新数据--------------");
                mPresenter.onPatitentDetails(id);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.onPatitentDetails(id);
    }

    @Override
    protected void initData() {
        mPresenter = new SuPatitentDetailsPresenter(
                SuInjection.provideTwoRepository(), this);
        id = ((SuPatientNewsActivity) getActivity()).getId();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void setData(PatitentDetailsBean bean) {


        fmNewsPhone.setText(bean.getInfo().getPhone_number() + "");
        fmNewsLocation.setText(bean.getInfo().getAddress() + "");
        KLog.d(bean.getInfo().getAddress() + "------------------------address-----------------------------");
        fmNewsId.setText(bean.getInfo().getPatient_id() + "");


        fmNewsIdentity.setText(bean.getUser().getId_card_number() + "");//  TODO 身份证  ？？  设置真实的数据
        fmNewsQq.setText(bean.getInfo().getQq() + "");
        fmNewsWeixin.setText(bean.getInfo().getWeixin_id() + "");
        fmNewsEmail.setText(String.valueOf(bean.getInfo().getEmail()));
        fmNewsInstitutionName.setText(bean.getOrganization().getName());
        fmNewsInstitutionCoding.setText(bean.getOrganization().getDistrict_id() + "");// 邮编
        fmNewsInstitutionLocation.setText(bean.getOrganization().getAddress());// 地址


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void refresh(final boolean b) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) getView().findViewById(R.id.fg_su_ptNews_refresh);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(b);
            }
        });
    }

}
