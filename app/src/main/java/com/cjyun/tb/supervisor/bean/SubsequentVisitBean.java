package com.cjyun.tb.supervisor.bean;

/**
 * Created by Administrator on 2016/5/3 0003.
 * 复诊时间的Bean
 */
public class SubsequentVisitBean {

    /**
     * patient_id : 3
     * id : 1
     * director_id : 2
     * setting_date : 2013-05-11
     * actually_time : 2013-05-11T08:00:00.000+08:00
     * created_at : 2016-05-07T10:02:09.170+08:00
     * updated_at : 2013-05-11T08:00:00.000+08:00
     */

        private int patient_id;
        private int id;
        private int director_id;
        private String setting_date;  //复诊时间
        private String actually_time;  //实际复诊时间
        private String created_at;
        private String updated_at;

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

        public int getDirector_id() {
            return director_id;
        }

        public void setDirector_id(int director_id) {
            this.director_id = director_id;
        }

        public String getSetting_date() {
            return setting_date;
        }

        public void setSetting_date(String setting_date) {
            this.setting_date = setting_date;
        }

        public String getActually_time() {
            return actually_time;
        }

        public void setActually_time(String actually_time) {
            this.actually_time = actually_time;
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

}