package com.cjyun.tb.supervisor.meInterface;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.base.BaseFragment;
import com.cjyun.tb.supervisor.base.ConstantAll;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/6 0006.
 * <p/>
 * 我的个人信息
 */
public class SuBaseInfoFragment extends BaseFragment {

    @Bind(R.id.ll_actionBar_back)
    LinearLayout llActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView tvTitleLeft;
    @Bind(R.id.fm_news_location)
    TextView fmNewsLocation;
    @Bind(R.id.fm_news_id)
    TextView fmNewsId;
    @Bind(R.id.fm_news_cellphone)
    TextView fmNewsCellphone;
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
    @Bind(R.id.fm_news_name)
    TextView fmNewsName;
    private Activity mActivity;
    private static Handler mHandler;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static SuBaseInfoFragment newInstance(Handler handler) {
        mHandler = handler;
        return new SuBaseInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_su_me_base_info, container, false);


        initView();
        ButterKnife.bind(this, root);
        return root;
    }

    private void initView() {


        // TODO 传递数据
       /* String[] beans = getArguments().getStringArray("bean");

        KLog.d(beans[0]+"--------------");*/

        // SuMeFragment.getInstance();
        // new SuMainActivity().
        //  KLog.d(bean.getInfo().getFull_name());

        //fmNewsName.setText(bean.getInfo().getFull_name());
       /* fmNewsId.setText(bean.getInfo().getDirector_id());
        fmNewsCellphone.setText(bean.getUser().getId_card_number());
        fmNewsQq.setText(bean.getInfo().getQq());
        fmNewsEmail.setText(bean.getUser().getEmail());
        fmNewsWeixin.setText(bean.getInfo().getWeixin_id());
        fmNewsInstitutionName.setText(bean.getOrganization().getName());
        fmNewsInstitutionLocation.setText(bean.getOrganization().getAddress());*/
        // fmNewsInstitutionCoding.setText(bean.getOrganization().get);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_actionBar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_actionBar_back:
                //通过Handler来发送消息退出
                mHandler.sendEmptyMessage(ConstantAll.ConHandlerInt.HANDLER_BACK_ACTIVITY);
                break;
            case R.id.btn_submit:
                break;
        }
    }


}
