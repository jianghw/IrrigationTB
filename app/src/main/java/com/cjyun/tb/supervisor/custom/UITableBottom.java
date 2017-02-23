package com.cjyun.tb.supervisor.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;

import java.util.HashMap;

/**
 * 项目名称：
 * 类描述：主界面底部标签栏
 * 创建人：jianghw
 * 修改备注：
 */
public class UITableBottom extends LinearLayout implements View.OnClickListener {
    private Context context;
    private int colorClick;
    private int colorUnclick;
    //子控件
    private UITableItem table_0;
    private UITableItem table_1;
    private UITableItem table_2;
    private UITableItem table_3;
    private HashMap<Integer, UITableItem> itemHashMap;

    private int index = 5;
    private int currentPosition = 0;
    private Activity mActivity;// 弱引用Activity, 防止内存泄露

    private OnUITabChangListener changeListener; //ui Tab 改变监听器

    public UITableBottom(Context context) {
        this(context, null, 0);
    }

    public UITableBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UITableBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //        init();
    }

    private void init() {
       /* colorClick = ContextCompat.getColor(context, R.color.uiBottom_green_press);
        colorUnclick =ContextCompat.getColor(context, R.color.uiBottom_gray_normal);
        colorClick = getResources().getColor(R.color.uiBottom_green_press);
        colorUnclick = getResources().getColor(R.color.uiBottom_gray_normal);
        *//**拿控件的高度*//*
        int tableBottomHeight = ViewGroup.LayoutParams.MATCH_PARENT;
        //设置父类控件的方向
        setOrientation(LinearLayout.HORIZONTAL);
        *//**创建子控件，并标记*//*
        table_0 = getChileItem(tableBottomHeight, 0, hashMap);
        table_1 = getChileItem(tableBottomHeight, 1, hashMap);
        table_2 = getChileItem(tableBottomHeight, 2, hashMap);
        table_3 = getChileItem(tableBottomHeight, 3, hashMap);

        selectTab(currentPosition);*/
    }

    /**
     * 建立布局子控件Item
     *
     * @param tableBottomHeight 父类控件高度
     * @param i                 子控件位号
     * @param hashMap
     * @return zi控件对象
     */
   /* private UITableItem getChileItem(int tableBottomHeight, int i)
    {

        UITableItem table = newChildItem(i);
        //用于建立控件，所用参数
        LayoutParams layoutParams = new LayoutParams(0, tableBottomHeight);
        layoutParams.weight = 1;

        if (i == currentPosition) {
            table.labelView.setTextColor(colorClick);
        } else {
            table.labelView.setTextColor(colorUnclick);
        }
        switch (i) {
            case 0:
                table.labelView.setText(context.getString(R.string.aty_ptMain_medicine));
                table.iconView.initBitmap(R.drawable.ic_uibottom_medicine_pre, R.drawable.ic_uibottom_medicine_nor);
                break;
            case 1:
                table.labelView.setText(context.getString(R.string.aty_ptMain_record));
                table.iconView.initBitmap(R.drawable.ic_uibottom_record_pre, R.drawable.ic_uibottom_record_nor);
                break;
            case 2:
                table.labelView.setText(context.getString(R.string.aty_ptMain_knowledge));
                table.iconView.initBitmap(R.drawable.ic_uibottom_know_pre, R.drawable.ic_uibottom_know_nor);
                break;
            case 3:
                table.labelView.setText(context.getString(R.string.aty_ptMain_me));
                table.iconView.initBitmap(R.drawable.ic_uibottom_me_pre, R.drawable.ic_uibottom_me_nor);
                break;
            default:
                break;
        }
        //加入到父控件中
        addView(table.parent, layoutParams);
        return table;
    }*/
    private UITableItem getChileItem(int tableBottomHeight, int i, HashMap<Integer, Integer[]> hashMap) {

        UITableItem table = newChildItem(i);
        //用于建立控件，所用参数
        LayoutParams layoutParams = new LayoutParams(0, tableBottomHeight);
        layoutParams.weight = 1;

        if (i == currentPosition) {
            table.labelView.setTextColor(colorClick);
        } else {
            table.labelView.setTextColor(colorUnclick);
        }
        table.labelView.setText(context.getString(hashMap.get(i)[2]));
        table.iconView.initBitmap(hashMap.get(i)[0], hashMap.get(i)[1]);

        /**
         *加入到父控件中
         */
        addView(table.parent, layoutParams);
        return table;
    }

    /**
     * Button控件 子item包含组件
     *
     * @param i 标记位号
     * @return
     */
    private UITableItem newChildItem(int i) {
        UITableItem tableItem = new UITableItem();
        tableItem.parent = LayoutInflater.from(context).inflate(R.layout.ptmain_uibottom_item, null);
        tableItem.iconView = (TableImageView) tableItem.parent.findViewById(R.id.imgView_uiBottom_picture);
        tableItem.labelView = (TextView) tableItem.parent.findViewById(R.id.tv_uiBottom_text);

        tableItem.parent.setTag(i);
        tableItem.parent.setOnClickListener(this);
        return tableItem;
    }

    private class UITableItem {
        View parent; //父控件
        TableImageView iconView; //图片
        TextView labelView; //标签
        View tipView; //红点
    }

    @Override
    public void onClick(View v) {
        int i = (Integer) v.getTag();
        //跳转到ViewPager的指定页面
       /* if (mActivity instanceof PtMainActivity) {
            ((PtMainActivity) mActivity).setCurrentItem(i, false);
        } else if (mActivity instanceof SuMainActivity) {

            ((SuMainActivity) mActivity).setCurrentItem(i, false);
        }*/
        selectTab(i);
    }

    /**
     * OnPagerChangListener 时会被调用
     *
     * @param i
     */
    public void selectTab(int i) {
        if (index == i) {
            return;
        }
        index = i;
        if (changeListener != null) {
            changeListener.onTabChang(index);
        }

        for (int j = 0; j < itemHashMap.size(); j++) {
            itemHashMap.get(j).iconView.setmAlpha(0);
            itemHashMap.get(j).labelView.setTextColor(colorUnclick);
        }
        itemHashMap.get(i).iconView.setmAlpha(255);
        itemHashMap.get(i).labelView.setTextColor(colorClick);

        //mIndex表示处于mIndex到mIndex+1页面之间
        /*switch (index) {
            case 0:
                table_0.iconView.setmAlpha(255);
                table_1.iconView.setmAlpha(0);
                table_2.iconView.setmAlpha(0);
                table_3.iconView.setmAlpha(0);
                table_0.labelView.setTextColor(colorClick);
                table_1.labelView.setTextColor(colorUnclick);
                table_2.labelView.setTextColor(colorUnclick);
                table_3.labelView.setTextColor(colorUnclick);
                break;
            case 1:
                table_0.iconView.setmAlpha(0);
                table_1.iconView.setmAlpha(255);
                table_2.iconView.setmAlpha(0);
                table_3.iconView.setmAlpha(0);
                table_0.labelView.setTextColor(colorUnclick);
                table_1.labelView.setTextColor(colorClick);
                table_2.labelView.setTextColor(colorUnclick);
                table_3.labelView.setTextColor(colorUnclick);
                break;
            case 2:
                table_0.iconView.setmAlpha(0);
                table_1.iconView.setmAlpha(0);
                table_2.iconView.setmAlpha(255);
                table_3.iconView.setmAlpha(0);
                table_0.labelView.setTextColor(colorUnclick);
                table_1.labelView.setTextColor(colorUnclick);
                table_2.labelView.setTextColor(colorClick);
                table_3.labelView.setTextColor(colorUnclick);
                break;
            case 3:
                table_0.iconView.setmAlpha(0);
                table_1.iconView.setmAlpha(0);
                table_2.iconView.setmAlpha(0);
                table_3.iconView.setmAlpha(255);
                table_0.labelView.setTextColor(colorUnclick);
                table_1.labelView.setTextColor(colorUnclick);
                table_2.labelView.setTextColor(colorUnclick);
                table_3.labelView.setTextColor(colorClick);
                break;
            default:
                break;
        }*/
    }

    /**
     * UITab 改变监听器
     */
    public static interface OnUITabChangListener {
        void onTabChang(int index);
    }

    public OnUITabChangListener getChangeListener() {
        return changeListener;
    }

    public void setChangeListener(OnUITabChangListener changeListener) {
        this.changeListener = changeListener;
    }

    public void setmViewPager(Activity activity, int currentPosition, HashMap<Integer, Integer[]> hashMap) {
        mActivity = activity;
        this.currentPosition = currentPosition;

        init(hashMap);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void init(HashMap<Integer, Integer[]> hashMap) {

        colorClick = getResources().getColor(R.color.uiBottom_green_press);
        colorUnclick = getResources().getColor(R.color.uiBottom_gray_normal);
        /**拿控件的高度*/
        int tableBottomHeight = ViewGroup.LayoutParams.MATCH_PARENT;
        //设置父类控件的方向
        setOrientation(LinearLayout.HORIZONTAL);
        /**创建子控件，并标记*/
        itemHashMap = new HashMap<>();
        for (int i = 0; i < hashMap.size(); i++) {
            UITableItem item = getChileItem(tableBottomHeight, i, hashMap);
            itemHashMap.put(i, item);
        }
        selectTab(currentPosition);
    }
}
