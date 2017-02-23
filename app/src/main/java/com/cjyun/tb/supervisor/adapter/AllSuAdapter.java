package com.cjyun.tb.supervisor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.bean.DB_Bean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class AllSuAdapter extends CommonAdapter<DB_Bean> {


    public AllSuAdapter(Context context, List<DB_Bean> datas) {

        super(context, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {


        DB_Bean patientsEntity = mDatas.get(position);

        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent,
                R.layout.item_su_all_patient, position);

        // 设置样本信息栏
        //  ((TextView) viewHolder.getView(R.id.tv_item_name)).setText(reportInfo.getName());
        ((TextView) viewHolder.getView(R.id.tv_item_name)).setText(patientsEntity.getName());

        if (patientsEntity.getPhoto() != null) {
            byte[] bytes = Base64.decode(patientsEntity.getPhoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ((ImageView) viewHolder.getView(R.id.itme_iv_all_patient)).setImageBitmap(bitmap);
        }

        // TODO：设置点击事件跳转到对应的 页面
        return viewHolder.getConvertView();
    }
}
