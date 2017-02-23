package com.cjyun.tb.supervisor.data;

import com.cjyun.tb.TbApp;
import com.cjyun.tb.supervisor.bean.DB_Bean;
import com.cjyun.tb.supervisor.bean.UserEntityBean;
import com.cjyun.tb.supervisor.data.local.SuPatientNewsDao;
import com.cjyun.tb.supervisor.util.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/11 0011.
 * <p/>
 * 本地数据的加载
 */
public class SuInitLocalData {

    private static SuInitLocalData INSTANCE;
    private List<UserEntityBean> list;



    public static SuInitLocalData getInstance() {

      //  mContext = context;
        if (INSTANCE == null) {
            INSTANCE = new SuInitLocalData();
        }
        return INSTANCE;
    }

    /**
     * 查询所有
     *
     * @param name
     * @return
     */
    public List<DB_Bean> getAll(String name) {

        SuPatientNewsDao dao = new SuPatientNewsDao(TbApp.getmContext());
        List<DB_Bean> lists = dao.inQuery(name);

        return lists;
    }

    /**
     * 查询近一年的数据
     *
     * @param name
     * @param month
     * @return
     */
    public List<DB_Bean> getPatient(String name, int month) {

        List<DB_Bean> mData = new ArrayList();

        String date;
        int id;
        SuPatientNewsDao dao = new SuPatientNewsDao(TbApp.getmContext());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);

        List<DB_Bean> lists = dao.inQuery(name);
        for (int i = 0; i < lists.size(); i++) {

            DB_Bean db_bean = lists.get(i);
            date = db_bean.getUpdated_at();
            id = db_bean.getId();

            Date parse = null;
            try {
                parse = sdf.parse(date);
                long storeTime = parse.getTime();

                if ((TimeUtils.getCurTime() - storeTime) <=
                        (TimeUtils.getCurTime() - TimeUtils.getTrimester(month))) {

                    DB_Bean bean = new DB_Bean();
                    bean.setId(id);
                    bean.setName(db_bean.getName());
                    bean.setUpdated_at(date);
                    bean.setPhoto(db_bean.getPhoto());

                    mData.add(bean);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mData;
    }

}