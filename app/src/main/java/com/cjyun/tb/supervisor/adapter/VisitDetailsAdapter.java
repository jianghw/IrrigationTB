package com.cjyun.tb.supervisor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.bean.VisitDetailsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/20 0020.
 * <p/>
 * 访问事件详情的Adapter
 */
public class VisitDetailsAdapter extends CommonAdapter<VisitDetailsBean> {

    private Context myContext;
    private List<VisitDetailsBean> mData;

    public VisitDetailsAdapter(Context context, List<VisitDetailsBean> datas) {
        super(context, datas);
        myContext = context;
        mData = datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VisitDetailsBean bean = mDatas.get(position);

        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent,
                R.layout.item_su_visit_details, position);

       // ((TextView) viewHolder.getView(R.id.tv_su_visit_type)).setText(bean.getVisit_type()==1?"电话访问":"上门访问");
        ((TextView) viewHolder.getView(R.id.tv_su_visit_content)).setText(bean.getContent());
       // ((TextView) viewHolder.getView(R.id.tv_su_visit_content)).setText(bean.pillsInfo);


        return viewHolder.getConvertView();
    }
}
