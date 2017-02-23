package com.cjyun.tb.patient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.constant.TbGlobal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/26 0026<br/>
 */
public class MonthGridAdapter extends BaseAdapter {
    private final ArrayList<Integer> mList;
    private final LayoutInflater inflater;
    private final Context mContext;
    private final String mDay;
    private HashMap<Integer, Boolean> mStateMap = new HashMap<>();
    private int selectPosition = -1;

    public MonthGridAdapter(Context context, ArrayList<Integer> mDateList, String day) {
        mContext = context;
        mDay = day;
        mList = mDateList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.item_gridview_month, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        convertView.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                parent.getMeasuredHeight() / 6));
        //日期设置
        if (mList.get(position) != 0) {
            holder.mTvItemDate.setText(String.valueOf(mList.get(position)));
            //字体颜色设置
            Calendar calendar = new GregorianCalendar();
            if (mDay.equals(TbGlobal.JString.CUR_MONTH)) {//本月
                calendar.add(Calendar.MONTH, 0);
            } else {
                calendar.add(Calendar.MONTH, -1);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
            calendar.set(Calendar.DAY_OF_MONTH, 1);//本月第一天
            calendar.add(Calendar.DATE, mList.get(position) - 1);//今天为...

            int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (week == 0 || week == 6) {
                holder.mTvItemDate.setTextColor(mContext.getResources().getColor(R.color.activity_hint_gray));
            }
            if (sdf.format(calendar.getTime()).equals(sdf.format(new Date()))) {
                holder.mLrlayItemDate.setBackgroundResource(R.drawable.ic_today_noral);
            }

            //标记异常状态的数值
            if (!mStateMap.isEmpty()) {
                if (mStateMap.containsKey(mList.get(position))) {//包含时，即为异常的一天
                    if (sdf.format(calendar.getTime()).equals(sdf.format(new Date()))) {
                        holder.mLrlayItemDate.setBackgroundResource(R.drawable.ic_today_red);
                    } else {
                        holder.mIvItemRedPoint.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.mIvItemRedPoint.setVisibility(View.GONE);
                }
            }
            //单选时
            if (-1 != selectPosition && selectPosition == position) {
                holder.mLrlayItemDate.setBackgroundResource(R.color.green);
            }

        }
        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.tv_item_date)
        TextView mTvItemDate;
        @Bind(R.id.iv_item_red_point)
        ImageView mIvItemRedPoint;
        @Bind(R.id.lrlay_item_date)
        LinearLayout mLrlayItemDate;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    /**
     * 异常状态数据
     *
     * @param hashMap
     */
    public void setStateMapBy(HashMap<Integer, Boolean> hashMap) {
        mStateMap = hashMap;
    }

    public void setSelectPosition(int position) {
        selectPosition = position;
    }
}
