package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18 0018</br>
 * description:</br> 药物详情
 */
public class DrugDetailBean extends DataSupport{

    /**
     * patient_id : 3
     * id : 1
     * time : 09:30
     * created_at : 2016-05-07T14:41:21.065+08:00
     * updated_at : 2016-05-07T14:41:21.065+08:00
     */
    @Expose
    private MedicineTimeBean medicine_time;
    /**
     * id : 1
     * product_name_zh : 复方氨维胶囊
     * product_name_en : Compound Amino Acid and Vitamin Capsules
     * dosage_form : 胶囊剂
     * production_unit : 乐普药业股份有限公司
     * name_pinyin : fufanganweijiaonang
     * name_py : ffawjn
     * total_user_count : 814
     */
    @Expose
    private List<MedicineNamesBean> medicine_names;
    /**
     * patient_id : 3
     * id : 1
     * medicine_id : 1
     * start_date : 2016-05-07
     * end_date : 2016-06-07
     * created_at : 2016-05-07T14:41:31.403+08:00
     * updated_at : 2016-05-07T14:41:31.403+08:00
     */
    @Expose
    private List<MedicineDatesBean> medicine_dates;

    public MedicineTimeBean getMedicine_time() { return medicine_time;}

    public void setMedicine_time(MedicineTimeBean medicine_time) { this.medicine_time = medicine_time;}

    public List<MedicineNamesBean> getMedicine_names() { return medicine_names;}

    public void setMedicine_names(List<MedicineNamesBean> medicine_names) { this.medicine_names = medicine_names;}

    public List<MedicineDatesBean> getMedicine_dates() { return medicine_dates;}

    public void setMedicine_dates(List<MedicineDatesBean> medicine_dates) { this.medicine_dates = medicine_dates;}

}
