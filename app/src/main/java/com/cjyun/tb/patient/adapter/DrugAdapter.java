package com.cjyun.tb.patient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.bean.MedicineNamesBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/18 0018</br>
 * description:</br>
 */
public class DrugAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<MedicineNamesBean> mList;
    private final LayoutInflater inflater;

    public DrugAdapter(Context context, ArrayList<MedicineNamesBean> drugList) {
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
            convertView = inflater.inflate(R.layout.item_listview_drug, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.mTvItemDrug.setText(mList.get(position).getProduct_name_zh());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_item_drug)
        TextView mTvItemDrug;

        ViewHolder(View view) {ButterKnife.bind(this, view);}
    }
}
