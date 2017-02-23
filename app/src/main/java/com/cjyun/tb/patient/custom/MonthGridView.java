package com.cjyun.tb.patient.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cjyun.tb.patient.adapter.MonthGridAdapter;
import com.cjyun.tb.patient.constant.TbGlobal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * 简单的显示月份
 */
public class MonthGridView extends GridView {
    private MonthGridAdapter adapter;

    public MonthGridView(Context context) {
        this(context, null, 0);
    }

    public MonthGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param context
     * @param day
     */
    public void setMonthData(Context context, String day) {
        //当前日期
        Calendar calendar = new GregorianCalendar();
        if (day.equals(TbGlobal.JString.CUR_MONTH)) {//本月
            calendar.add(Calendar.MONTH, 0);
        } else {
            calendar.add(Calendar.MONTH, -1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,此时当前日期既为本月第一天
        //本月的最大天数
        int maximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        ArrayList<Integer> mDateList = new ArrayList<>();
        for (int i = 1; i <= week + maximum; i++) {
            if (i <= week) {
                mDateList.add(0);
            } else {
                mDateList.add(i - week);
            }
        }
        adapter = new MonthGridAdapter(context, mDateList, day);
        setAdapter(adapter);
    }

    public void setAbnormalState(HashMap<Integer, Boolean> hashMap) {
        adapter.setStateMapBy(hashMap);
        adapter.notifyDataSetChanged();
    }


    public void setOnItemCallBackByDate(final DateCallBackable dateCallBackable) {
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectPosition(position);
                adapter.notifyDataSetChanged();
                dateCallBackable.setDataByCallItem("xxx");
            }
        });
    }

    public interface DateCallBackable {
        void setDataByCallItem(String date);
    }
}
