package com.cjyun.tb.supervisor.suCustom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import com.cjyun.tb.supervisor.Contract;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/6/2 0002.
 * <p>
 * 调用系统时间的页面
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private  Contract.SuAddVisitView mActivity;

    public void setmActivity( Contract.SuAddVisitView activity){


        mActivity = activity;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

      //  KLog.d("OnDateSet", "select year:" + year + ";month:" + month + ";day:" + day);

        mActivity.geVisittDate(year, month, day);
    }

}
