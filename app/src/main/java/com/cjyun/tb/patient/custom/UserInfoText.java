package com.cjyun.tb.patient.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
public class UserInfoText extends RelativeLayout {

    TextView mTvUserInfoSign;
    TextView mTvUserInfoTitle;
    TextView mTvUserInfoContent;
    ImageView mImgViewUserInfoRight;
    ImageView mImgViewUserInfoContact;
    private Context context;

    public UserInfoText(Context context) {
        this(context, null);
    }

    public UserInfoText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserInfoText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        initView(layoutInflater);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.userInfoText, defStyleAttr, 0);
        initControlState(typedArray);
        initListener();
    }

    /**
     * 初始化布局
     *
     * @param layoutInflater
     */
    public void initView(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.text_user_info, this);
        mTvUserInfoSign = (TextView) view.findViewById(R.id.tv_userInfo_sign);
        mTvUserInfoTitle = (TextView) view.findViewById(R.id.tv_userInfo_title);
        mTvUserInfoContent = (TextView) view.findViewById(R.id.tv_userInfo_content);
        mImgViewUserInfoRight = (ImageView) view.findViewById(R.id.imgView_userInfo_right);
        mImgViewUserInfoContact = (ImageView) view.findViewById(R.id.imgView_userInfo_contact);
    }

    private void initControlState(TypedArray typedArray) {
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.userInfoText_tvSign:
                    boolean isSign = typedArray.getBoolean(attr, true);
                    if (!isSign) {
                        mTvUserInfoSign.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.styleable.userInfoText_tvTitle:
                    String title = typedArray.getString(attr);
                    mTvUserInfoTitle.setText(title);
                    break;
                case R.styleable.userInfoText_tvContent:
                    String content = typedArray.getString(attr);
                    setContentHint(content);
                    break;
                case R.styleable.userInfoText_endState:
                    int value = typedArray.getInt(attr, 1);
                    if (value == 1) {
                        mImgViewUserInfoRight.setVisibility(View.GONE);
                        mImgViewUserInfoContact.setVisibility(View.GONE);
                    } else if (value == 2) {
                        mImgViewUserInfoContact.setVisibility(View.GONE);
                    } else if (value == 3) {
                        mImgViewUserInfoRight.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
    }

    private void initListener() {
        if (mImgViewUserInfoContact.getVisibility() == View.VISIBLE) {
            mImgViewUserInfoContact.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = (Activity) context;
                    activity.startActivityForResult(
                            new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), getRequestCode());
                }
            });
        }
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
        mTvUserInfoContent.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
        //        etContent.setHint(hint);
    }

    /**
     * 得到TitleText输入的文字
     *
     * @return
     */
    public String getTitleTextView() {
        return mTvUserInfoTitle.getText().toString().trim();
    }

    /**
     * 得到EditText输入的文字
     *
     * @return
     */
    public String getContentTextView() {
        return mTvUserInfoContent.getText().toString().trim();
    }

    public void setContentTextView(String name) {
        mTvUserInfoContent.setText(name);
    }

    /**
     * 设置请求码
     *
     * @return 100~300
     */
    private int getRequestCode() {
        int code = 0;
        String title = getTitleTextView();
        if (title.equals(context.getString(R.string.aty_userInfo_user))) {
            code = 100;
        } else if (title.equals(context.getString(R.string.aty_userInfo_guardian1))) {
            code = 200;
        } else if (title.equals(context.getString(R.string.aty_userInfo_guardian2))) {
            code = 300;
        }
        return code;
    }

    public void showPopuForEdit(Context context) {
        View view = View.inflate(context, R.layout.dialog_info_edit, null);
        final EditText editText = (EditText) view.findViewById(R.id.edit_dialog);
        editText.setText(getContentTextView());
        AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle(getTitleTextView())
                .setView(view)
                .setNegativeButton(getResources().getString(R.string.dialog_no_initFgt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setContentTextView(editText.getText().toString().trim());
                        dialog.dismiss();
                    }
                }).show();
    }

}
