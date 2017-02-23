package com.cjyun.tb.supervisor.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.bean.PieItemBean;
import com.cjyun.tb.supervisor.bean.TackPillsBean;
import com.cjyun.tb.supervisor.custom.PicChartView;
import com.cjyun.tb.supervisor.util.TimeUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4 0004.
 */
public class TpCycleAdapter extends CommonAdapter<TackPillsBean> {

    private Context myContext;
    private List<TackPillsBean> mData;
    private int yet;
    private int not;
    private TackPillsBean bean;

    public TpCycleAdapter(Context context, List<TackPillsBean> datas) {
        super(context, datas);
        myContext = context;
        mData = datas;
    }

    public void setData(int yet, int not) {
        this.yet = yet;
        this.not = not;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        bean = mData.get(position);

        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent,
                R.layout.item_su_cycle, position);

        ((TextView) viewHolder.getView(R.id.item_su_tv_serial_number)).setText(position + 1 + "");
        ((TextView) viewHolder.getView(R.id.item_su_tv_drug_name)).setText(bean.getName());
        ((TextView) viewHolder.getView(R.id.item_su_tv_start_time)).setText(TimeUtils.getDate(bean.getCreated_at()) + "");
        ((TextView) viewHolder.getView(R.id.item_su_tv_end_time)).setText(TimeUtils.getDate(bean.getEnd_date()) + "");
        ((ImageView) viewHolder.getView(R.id.item_su_iv_lookup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();
            }
        });


        return viewHolder.getConvertView();
    }

    private void showDialog() {

        PieItemBean[] src = {new PieItemBean("未服药", not),
                new PieItemBean("已服药", yet)};

        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_cycle, null);
        PicChartView picSu = (PicChartView) contentView.findViewById(R.id.pie_su);
        TextView mStartTime = (TextView) contentView.findViewById(R.id.dialog_start_time);
        TextView mPtbox = (TextView) contentView.findViewById(R.id.dialog_ptbox_id);
        TextView mEndTime = (TextView) contentView.findViewById(R.id.dialog_end_time);

        mStartTime.setText(TimeUtils.getDate(bean.getCreated_at()));
        mEndTime.setText(TimeUtils.getDate(bean.getEnd_date()));
        mPtbox.setText("药盒ID " + bean.getMedicine_id());

        picSu.setPieItems(src);
        //  把设定好的窗口布局放到dialog中
        dialog.setContentView(contentView);
        // 点击窗口空白处取消对话
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }
}
