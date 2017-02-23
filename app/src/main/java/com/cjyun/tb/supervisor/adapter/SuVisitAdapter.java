package com.cjyun.tb.supervisor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.bean.VisitBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/4 0004</br>
 * description:</br>
 */
public class SuVisitAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<VisitBean> mList;
    private final LayoutInflater inflater;

    public SuVisitAdapter(Context context, ArrayList<VisitBean> list) {
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
            convertView = inflater.inflate(R.layout.item_lv_visit, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        VisitBean bean = mList.get(position);
        //        bean.getVisit_type()
        holder.mTvType.setText(bean.getVisit_type().equals("visit") ? "上门访视" : "电话访视");
        holder.mTvContent.setText(bean.getContent());
        String ac_time = bean.getActually_time();
        String time = "";
        if (ac_time != null) time = ac_time.split("T")[0];
        if (bean.getActually_time() != null && bean.getActually_time().equals(time)) {
            holder.mTvType.setBackgroundResource(R.color.activity_controls_normal_green);
        } else if (bean.getActually_time() != null && !bean.getActually_time().equals(time)) {
            holder.mTvType.setBackgroundResource(R.color.viewfinder_laser);
        } else {
            holder.mTvType.setBackgroundResource(R.color.viewfinder_laser);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_type)
        TextView mTvType;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.ll_show_popup)
        LinearLayout mLlShowPopup;

        ViewHolder(View view) {ButterKnife.bind(this, view);}
    }
}
