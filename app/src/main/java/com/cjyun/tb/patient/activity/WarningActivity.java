package com.cjyun.tb.patient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseActivity;
import com.cjyun.tb.patient.ui.splash.SplashActivity;
import com.cjyun.tb.patient.util.SharedUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/29 0029.
 */
public class WarningActivity extends BaseActivity {
    @Bind(R.id.tv_ok)
    ImageView mTvOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean mode = SharedUtils.getBoolean("first", true);
        if(!mode){
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }
        setContentView(R.layout.activity_warning);
        ButterKnife.bind(this);

        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedUtils.setBoolean("first", false);
                startActivity(new Intent(WarningActivity.this, WarningActivity1.class));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
