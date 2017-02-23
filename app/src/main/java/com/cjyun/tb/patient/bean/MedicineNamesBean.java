package com.cjyun.tb.patient.bean;

import com.google.gson.annotations.Expose;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/5/25 0025</br>
 * description:</br> 母表{@link DrugDetailBean}
 */
public class MedicineNamesBean extends DataSupport {

    @Expose
    private int id;
    @Expose
    private String product_name_zh;
    @Expose
    private String product_name_en;
    @Expose
    private String dosage_form;
    @Expose
    private String production_unit;
    @Expose
    private String name_pinyin;
    @Expose
    private String name_py;
    @Expose
    private int total_user_count;

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getProduct_name_zh() { return product_name_zh;}

    public void setProduct_name_zh(String product_name_zh) { this.product_name_zh = product_name_zh;}

    public String getProduct_name_en() { return product_name_en;}

    public void setProduct_name_en(String product_name_en) { this.product_name_en = product_name_en;}

    public String getDosage_form() { return dosage_form;}

    public void setDosage_form(String dosage_form) { this.dosage_form = dosage_form;}

    public String getProduction_unit() { return production_unit;}

    public void setProduction_unit(String production_unit) { this.production_unit = production_unit;}

    public String getName_pinyin() { return name_pinyin;}

    public void setName_pinyin(String name_pinyin) { this.name_pinyin = name_pinyin;}

    public String getName_py() { return name_py;}

    public void setName_py(String name_py) { this.name_py = name_py;}

    public int getTotal_user_count() { return total_user_count;}

    public void setTotal_user_count(int total_user_count) { this.total_user_count = total_user_count;}
}
