package com.cjyun.tb.patient.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.bean.DirectorBean;
import com.cjyun.tb.patient.bean.PhotosBean;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/17 0017</br>
 * description:</br>
 */
public class MyDirectorAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater inflater;
    private final ArrayList<DirectorBean> mList;
    private HashMap<Integer, PhotosBean> mDate;

    public MyDirectorAdapter(Context context, ArrayList<DirectorBean> data) {
        mContext = context;
        mList = data;
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
            convertView = inflater.inflate(R.layout.item_listview_my_director, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        DirectorBean bean = mList.get(position);

        PhotosBean photosBean = mDate.get(bean.getId());
        if (photosBean != null && photosBean.getPhoto() != null) {
            String photo = (String) photosBean.getPhoto();
            if (!TextUtils.isEmpty(photo)) {
                byte[] bytes = Base64.decode(photo, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.mIvHead.setImageBitmap(bitmap);
            }
        }
        if (photosBean != null) holder.mTvDirectorName.setText(photosBean.getFull_name());
        return convertView;
    }

    public void setDateForDirector(HashMap<Integer, PhotosBean> array) {
        mDate = array;
    }

    static class ViewHolder {
        @Bind(R.id.iv_head)
        ImageView mIvHead;
        @Bind(R.id.tv_directorName)
        TextView mTvDirectorName;

        ViewHolder(View view) {ButterKnife.bind(this, view);}
    }
}
