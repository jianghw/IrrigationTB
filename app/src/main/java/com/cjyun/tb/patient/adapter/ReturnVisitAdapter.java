package com.cjyun.tb.patient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.bean.ReturnVisitBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/18 0018</br>
 * description:</br> 复诊
 */
public class ReturnVisitAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<ReturnVisitBean> mList;
    private final LayoutInflater inflater;

    public ReturnVisitAdapter(Context context, List<ReturnVisitBean> list) {
        mContext = context;
        mList = list;
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
            convertView = inflater.inflate(R.layout.item_lv_return_visit, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.mTvNumber.setText(String.valueOf(position + 1));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        ReturnVisitBean bean = mList.get(position);
        holder.mTvScheduled.setText(bean.getSetting_date());

        try {
            long setTime = sdf.parse(bean.getSetting_date()).getTime();
            long today = sdf.parse(sdf.format(new Date())).getTime();//今天
            if (null != bean.getActually_time() && bean.getActually_time().toString().contains("T")) {//有记录时
                String[] days = bean.getActually_time().toString().split("T");
                holder.mTvActual.setText(days[0]);
            } else {//无记录时 ~未来  ~今天 ~昨日
                if (setTime > today) {
                    long day = ((setTime - today / 1000) / (3600 * 24));
                    holder.mTvActual.setText("距离复诊还有" + day + "天");
                } else {
                    holder.mTvActual.setText(mContext.getResources().getString(R.string.no_return_visit));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_number)
        TextView mTvNumber;
        @Bind(R.id.tv_scheduled)
        TextView mTvScheduled;
        @Bind(R.id.tv_actual)
        TextView mTvActual;

        ViewHolder(View view) {ButterKnife.bind(this, view);}
    }
}
