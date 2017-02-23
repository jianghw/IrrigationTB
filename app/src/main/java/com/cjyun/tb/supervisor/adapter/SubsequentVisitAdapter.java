package com.cjyun.tb.supervisor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.bean.SubsequentVisitBean;
import com.cjyun.tb.supervisor.util.TimeUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class SubsequentVisitAdapter extends CommonAdapter<SubsequentVisitBean> {

    List mData;

    public SubsequentVisitAdapter(Context context, List datas) {
        super(context, datas);
        mDatas = datas;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //  long time = 1000 * 60 * 60 * 24;
        SubsequentVisitBean bean = mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent,
                R.layout.item_subsequent_visit, position);
        ((TextView) viewHolder.getView(R.id.item_sus_serial_number)).setText(position + 1 + "");
        ((TextView) viewHolder.getView(R.id.item_sus_reserve_time)).setText(bean.getSetting_date());

       /* if ((TimeUtils.getDateLong(bean.getSetting_date()) - TimeUtils.getCurTime()) < (time * 2.5)) {
            if ((TimeUtils.getDateLong(bean.getActually_time()) - TimeUtils.getCurTime()) < time)
        }*/
        ((TextView) viewHolder.getView(R.id.item_sus_realty_time)).setText(TimeUtils.getDate(bean.getSetting_date()));

        return viewHolder.getConvertView();
    }
}
