package com.cjyun.tb.supervisor.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
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
public class OsiEditText extends RelativeLayout implements View.OnFocusChangeListener, CompoundButton
        .OnCheckedChangeListener {

    private Context context;
    private TextView tvTitle;
    private EditText etContent;
    private ImageView imgLine;
    private CheckBox cbTail;
    private ImageView imgSearch;

    public OsiEditText(Context context) {
        this(context, null);
    }

    public OsiEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OsiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initView();
        initListener();
    }

    /**
     * 初始化布局
     */
    public void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_text_osi, this);
        //        View.inflate(context, R.layout.edit_text_osi, this);
        tvTitle = (TextView) view.findViewById(R.id.tv_osiEdit_title);
        etContent = (EditText) view.findViewById(R.id.edtTxt_osiEdit_title);
        cbTail = (CheckBox) view.findViewById(R.id.cBox_osiEdit_eye);
        imgLine = (ImageView) view.findViewById(R.id.iv_oet_line);
        imgSearch = (ImageView) view.findViewById(R.id.imgView_osiEdit_title);

        cbTail.setVisibility(View.GONE);
    }

    private void initListener() {
        etContent.setOnFocusChangeListener(this);
        cbTail.setOnCheckedChangeListener(this);
    }

    public void setTitleBitmap(Bitmap bitmap) {
        setTvTitleToGone();
        etContent.setGravity(Gravity.CENTER);
        imgSearch.setVisibility(View.VISIBLE);
        imgSearch.setImageBitmap(bitmap);
    }

    /**
     * 设置Title的值
     *
     * @param title
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setImagViewVisib() {
        imgSearch.setVisibility(View.VISIBLE);
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
        cbTail.setVisibility(View.VISIBLE);
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
}
