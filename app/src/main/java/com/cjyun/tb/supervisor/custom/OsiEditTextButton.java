package com.cjyun.tb.supervisor.custom;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;


/**
 * 自定义的EditText布局
 * 用于登陆注册等
 * Created by Administrator on 2015/11/26 0026.
 */
public class OsiEditTextButton extends RelativeLayout implements View.OnFocusChangeListener,
        CompoundButton.OnCheckedChangeListener {
    private Context context;
    private TextView tvTitle;
    private EditText etContent;
    private ImageView imgLine;
    private Button btnClock;

    public OsiEditTextButton(Context context) {
        this(context, null, 0);
    }

    public OsiEditTextButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OsiEditTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        initListener();
    }

    /**
     * 初始化布局
     */
    public void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_button_osi, this);
        //        View.inflate(getContext(), R.layout.edit_button_osi, this);
        tvTitle = (TextView) view.findViewById(R.id.tv_btn_osiEdit_title);
        etContent = (EditText) view.findViewById(R.id.edtTxt_btn_osiEdit);
        imgLine = (ImageView) view.findViewById(R.id.imgView_btn_osiEdit_line);
        btnClock = (Button) view.findViewById(R.id.btn_osiEdit_code);
    }

    private void initListener() {
        etContent.setOnFocusChangeListener(this);
    }

    /**
     * 设置Title的值
     *
     * @param title
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    public void setEtContent(String content) {
        etContent.setText(content);
    }

    /**
     * 设置EditText的hint
     *
     * @param hint
     */
    public void setContentHint(String hint) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hint);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        etContent.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
        //        etContent.setHint(hint);
    }

    /**
     * 设置倒计时btnClock按钮点击事件
     *
     * @param listener
     */
    public void setBtnClockListener(OnClickListener listener) {
        btnClock.setOnClickListener(listener);
    }

    /**
     * 设置倒计时btnClock按钮是否可以点击
     *
     * @param state
     */
    public void setBtnClockEnable(boolean state) {
        if (state)
            btnClock.setBackgroundResource(R.drawable.bg_vercode_btn_normal);
        else
            btnClock.setBackgroundResource(R.drawable.bg_vercode_btn_disabled);

        btnClock.setEnabled(state);
    }

    /**
     * 得到EditText输入的文字
     *
     * @return
     */
    public String getEditText() {
        return etContent.getText().toString().trim();
    }

    public EditText getEtContentOnly() {
        return etContent;
    }

    /**
     * 初始化Tail的图标,同时设置输入框为密码隐藏状态
     */
    public void initTail() {
        etContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            imgLine.setBackgroundColor(context.getResources().getColor(R.color.activity_editText_line));
            setLine(1);
        } else {
            imgLine.setBackgroundColor(context.getResources().getColor(R.color.activity_editText_line_gray));
            setLine(1);
        }
    }

    /**
     * 设置底部的线条颜色大小变化
     *
     * @param line
     */
    private void setLine(int line) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgLine.getLayoutParams();
        layoutParams.height = line;
        imgLine.setLayoutParams(layoutParams);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            etContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        } else {
            etContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }


    /**
     * 添加TvTitle隐藏
     */
    public void setTvTitleToGone() {
        tvTitle.setVisibility(View.GONE);
    }

    public ClockTask clockTask;

    /**
     * 倒计时
     */
    public void clock() {
        setBtnClockEnable(false);
        if (clockTask == null) {
            btnClock.setText("60 s");
            clockTask = new ClockTask();
            new Thread(clockTask).start();
        }
    }

    /**
     * 取消倒计时
     */
    public void clockCancel() {
        setBtnClockEnable(true);
        if (clockTask != null) {
            clockTask.cancel();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 > 0) {
                btnClock.setText(msg.arg1 + " s");
            } else {
                if (clockTask != null)
                    clockTask = null;
                btnClock.setText(getContext().getString(R.string.aty_verCode_getCode));
                setBtnClockEnable(true);
            }
        }
    };

    public class ClockTask implements Runnable {
        public Integer time;

        public ClockTask() {
            this.time = 60;
        }

        @Override
        public void run() {
            while (time >= 0) {
                time--;
                Message msg = handler.obtainMessage();
                msg.arg1 = time;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendMessageDelayed(msg, 0);
                clockTask = null;
            }
        }

        public void cancel() {
            time = 0;
        }
    }

}
