package com.cjyun.tb.supervisor.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.Contract;
import com.cjyun.tb.supervisor.Presenter.SuAddVisitPresenter;
import com.cjyun.tb.supervisor.data.SuInjection;
import com.cjyun.tb.supervisor.suCustom.DatePickerFragment;
import com.cjyun.tb.supervisor.util.ToastUtils;
import com.socks.library.KLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/5 0005.
 * <p/>
 * 添加访问事件
 */
public class SuAddVisitActivity extends SuBaseActivity implements Contract.SuAddVisitView {


    @Bind(R.id.fm_su_bt_show_come)
    Button fmSuBtShowCome;
    @Bind(R.id.fm_su_bt_show_telephone)
    Button fmSuBtShowTelephone;
    @Bind(R.id.edit_su_visit_content)
    EditText editSuVisitContent;

    @Bind(R.id.btn_su_commit)
    Button btnSuCommit;
    @Bind(R.id.rl_su_addvisit_date)
    RelativeLayout rlSuAddvisitDate;
    @Bind(R.id.tv_su_visit_date)
    TextView tvSuVisitDate;

    private String mContent;

    boolean isVisit = true;
    private SuAddVisitPresenter presenter;
    private int id;
    private Date mDate;

    @Override
    protected void initView() {

        setContentView(R.layout.activity_su_visit);
        initActionBar(this, true, false, 2);
        titleLeft.setText(R.string.suVisitActivity_title);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        id = intent.getIntExtra("suPatutentDetails", 15);

        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);

        tvSuVisitDate.setText((year + "-" + (month + 1) + "-" + date));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        ButterKnife.bind(this);
    }


    @OnClick({R.id.fm_su_bt_show_come, R.id.fm_su_bt_show_telephone, R.id.btn_su_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fm_su_bt_show_come:

                fmSuBtShowCome.setBackground(getResources().getDrawable(R.drawable.bg_tack_pill_btn_left));
                fmSuBtShowTelephone.setBackground(getResources().getDrawable(R.drawable.bg_tack_pills_btn_white));
                fmSuBtShowCome.setTextColor(Color.WHITE);
                fmSuBtShowTelephone.setTextColor(Color.BLACK);
                isVisit = true;
                break;
            case R.id.fm_su_bt_show_telephone:

                fmSuBtShowTelephone.setBackground(getResources().getDrawable(R.drawable.bg_tack_pill_btn));
                fmSuBtShowCome.setBackground(getResources().getDrawable(R.drawable.bg_tack_pills_btn_left_white));
                fmSuBtShowTelephone.setTextColor(Color.WHITE);
                fmSuBtShowCome.setTextColor(Color.BLACK);
                isVisit = false;
                break;
            case R.id.btn_su_commit:

                if (isVisit) {
                    // TODO  保存上门访问
                    String type = "visit";
                    presenter.onAddVisit(type, id, mDate);

                } else {
                    // 保存电话访问
                    String type = "phone";
                    presenter.onAddVisit(type, id, mDate);
                }

                this.finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter = new SuAddVisitPresenter(
                SuInjection.provideTwoRepository(), this);
    }

    @Override
    public String getEdit() {
        String content = editSuVisitContent.getText().toString().trim();
        if (content.isEmpty()) {
            ToastUtils.showMessage("数据不能为空");
            return null;
        }
        return content;
    }

    @Override
    public void geVisittDate(int year, int month, int day) {

        String date = year + "-" + (month + 1) + "-" + day;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {

            mDate = sdf.parse(date);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        KLog.d(mDate + "------时间--------");

        tvSuVisitDate.setText(date);
    }

    @OnClick(R.id.rl_su_addvisit_date)
    public void onClick() {

        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getFragmentManager(), "datePicker");
    }
}
