package com.cjyun.tb.patient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseActivity;
import com.cjyun.tb.patient.ui.splash.SplashActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/29 0029.
 */
public class WarningActivity2 extends BaseActivity {
    @Bind(R.id.tv_ok)
    ImageView mTvOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning2);
        ButterKnife.bind(this);

        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WarningActivity2.this, SplashActivity.class));

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
