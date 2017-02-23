package com.cjyun.tb.patient.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.bean.MedicineDatesBean;
import com.cjyun.tb.patient.bean.MedicineNamesBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/25 0025</br>
 * description:</br>
 */
public class DrugDetailAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<MedicineNamesBean> mList;
    private final LayoutInflater inflater;
    private SparseArray<MedicineDatesBean> mDate;

    public DrugDetailAdapter(Context context, ArrayList<MedicineNamesBean> drugList) {
        mContext = context;
        mList = drugList;
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
            convertView = inflater.inflate(R.layout.item_lv_drug_detail, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.mTvOrder.setText(String.valueOf(position+1));

        MedicineNamesBean namesBean = mList.get(position);
        holder.mTvDrug.setText(namesBean.getProduct_name_zh()!=null?namesBean.getProduct_name_zh():"未知名");

        MedicineDatesBean datesBean = mDate.get(namesBean.getId());
        if(datesBean!=null){
            holder.mTvStart.setText(datesBean.getStart_date()!=null?datesBean.getStart_date().substring(2,datesBean.getStart_date().length()):"未设置");
            holder.mTvEnd.setText(datesBean.getEnd_date()!=null?datesBean.getEnd_date().substring(2,datesBean.getEnd_date().length()):"未设置");
        }
        return convertView;
    }

    public void setDateForDrug(SparseArray<MedicineDatesBean> array) {
        mDate = array;
    }

    static class ViewHolder {
        @Bind(R.id.tv_order)
        TextView mTvOrder;
        @Bind(R.id.tv_drug)
        TextView mTvDrug;
        @Bind(R.id.tv_start)
        TextView mTvStart;
        @Bind(R.id.tv_end)
        TextView mTvEnd;

        ViewHolder(View view) {ButterKnife.bind(this, view);}
    }
}
