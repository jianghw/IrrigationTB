package com.cjyun.tb.supervisor.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ViewHolder {

	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
					  int position) {

		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	/**
	 * 判断ViewHolder是new 出来 还是getTag()出来
	 */
	public static ViewHolder getViewHolder(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		// 判断ViewHolder是new出来的还是getTag出来的
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder viewholder = (ViewHolder) convertView.getTag();
			return viewholder;
		}
	}

	/**
	 * 通过viewId获取控件
	 */
	public <T extends View> T getView(int viewId) {// 代表返回值是View的一个子类
		View view = mViews.get(viewId);

		if (null == view) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;

	}

	/**
	 * 给ID为viewId的TextView设置文字text，并返回this
	 * 
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
}
