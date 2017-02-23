package com.cjyun.tb.supervisor.bean;

/**
 * Created by Administrator on 2016/5/17 0017.
 *
 * 服药统计的bean
 */
public class TackPillsBean {
    /**
     * patient_id : 3
     * id : 2
     * device_id : null
     * medicine_id : 1
     * number : null
     * taken : true
     * setting_time : 09:30
     * eat_medicine_time : null
     * name : 复方氨维胶囊
     * unit : 胶囊剂
     * quantity : null
     * created_at : 2016-05-11T12:48:33.625+08:00
     * updated_at : 2016-05-11T12:48:33.625+08:00
     * actualize_time : 2016-05-11T09:30:00+00:00
     * start_date : 2016-05-07
     * end_date : 2016-06-07
     */


        private int patient_id;
        private int id;
        private String device_id;
        private int medicine_id;
        private String number;
        private boolean taken;
        private String setting_time;
        private String eat_medicine_time;
        private String name;
        private String unit;
        private Object quantity;
        private String created_at;
        private String updated_at;
        private String actualize_time;
        private String start_date;
        private String end_date;

        public int getPatient_id() {
            return patient_id;
        }

        public void setPatient_id(int patient_id) {
            this.patient_id = patient_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public int getMedicine_id() {
            return medicine_id;
        }

        public void setMedicine_id(int medicine_id) {
            this.medicine_id = medicine_id;
        }

        public Object getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public boolean isTaken() {
            return taken;
        }

        public void setTaken(boolean taken) {
            this.taken = taken;
        }

        public String getSetting_time() {
            return setting_time;
        }

        public void setSetting_time(String setting_time) {
            this.setting_time = setting_time;
        }

        public String getEat_medicine_time() {
            return eat_medicine_time;
        }

        public void setEat_medicine_time(String eat_medicine_time) {
            this.eat_medicine_time = eat_medicine_time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Object getQuantity() {
            return quantity;
        }

        public void setQuantity(Object quantity) {
            this.quantity = quantity;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getActualize_time() {
            return actualize_time;
        }

        public void setActualize_time(String actualize_time) {
            this.actualize_time = actualize_time;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }
}