package com.tryking.trykingcitychoose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cjyun.tb.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/20.
 */
public class AddressCityListAdapter extends BaseAdapter {
    private List<CityItemBean> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;
    private ViewHolder holder;

    public AddressCityListAdapter(Context context, List<CityItemBean> datas) {
        mDatas = datas;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
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
            convertView = mInflater.inflate(R.layout.address_city_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.mTvCity.setText(mDatas.get(position).getName());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_city)
        TextView mTvCity;

        ViewHolder(View view) {ButterKnife.bind(this, view);}
    }
}
