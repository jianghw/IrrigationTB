package com.cjyun.tb.supervisor.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/26 0026.
 */
public class SearchEditText extends LinearLayout {
    private Context context;
    @Bind(R.id.et_input)
    public EditText et_input;
    @Bind(R.id.ll_hint)
    public LinearLayout ll_hint;
    @Bind(R.id.tv_show)
    public TextView tv_show;


    public SearchEditText(Context context) {
        this(context, null, 0);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        initListener();
    }

    /**
     * 初始化布局
     */
    public void initView() {
        View view = View.inflate(context, R.layout.edit_text_search, this);
        ButterKnife.bind(this, view);
    }




    private void initListener() {
        ll_hint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnInitClickListener!= null){
                    mOnInitClickListener.onClick(et_input.getText().toString().trim(),ll_hint);


                }

            }
        });


        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_input.getText().toString().trim().length() > 0) {
                    ll_hint.setVisibility(View.VISIBLE);
                    tv_show.setText(et_input.getText().toString().trim());

                } else {
                    ll_hint.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    OnInitClickListener mOnInitClickListener;

    /**
     * 提示框点击事件
     * @param mOnInitClickListener
     */
    public void setOnInitClickListener(OnInitClickListener mOnInitClickListener){
        this.mOnInitClickListener = mOnInitClickListener ;
    }

    public interface OnInitClickListener {

        public void onClick(String name,LinearLayout linearLayout);


    }
}
