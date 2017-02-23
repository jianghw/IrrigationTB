package com.cjyun.tb.supervisor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> list) {

        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {

        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
