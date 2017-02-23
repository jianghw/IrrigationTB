package com.cjyun.tb.patient.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.bean.DeviceBean;
import com.cjyun.tb.patient.bean.DirectorBean;
import com.cjyun.tb.patient.bean.DrugDetailBean;
import com.cjyun.tb.patient.bean.InfoBean;
import com.cjyun.tb.patient.bean.MedicineDatesBean;
import com.cjyun.tb.patient.bean.MedicineNamesBean;
import com.cjyun.tb.patient.bean.MedicineTimeBean;
import com.cjyun.tb.patient.bean.MyDirectorBean;
import com.cjyun.tb.patient.bean.PatientBean;
import com.cjyun.tb.patient.bean.PhotosBean;
import com.cjyun.tb.patient.bean.TokenBean;
import com.cjyun.tb.patient.bean.UpdateBean;
import com.cjyun.tb.patient.bean.UserBean;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.util.SharedUtils;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

/**
 * 本地持久化
 */
public class LocalData {

    private static final String DB_NAME = "tb.db";
    private static final int DB_VERSION = 1;
    private JhwSQLiteHelper mHelper;
    private SQLiteDatabase mDB;

    private LocalData() {
   /*     mHelper = new JhwSQLiteHelper(TbApp.mContext, DB_NAME, null, DB_VERSION);
        mDB = mHelper.getWritableDatabase();*/
    }

    private void closeTbDatabase() {
        try {
            mDB.close();
            mHelper = null;
            mDB = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean deleteTbDatabase() {
        try {
            return TbApp.mContext.deleteDatabase(DB_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T extends DataSupport> boolean beforeInsert(final T bean) {
        Class<? extends DataSupport> clazz = bean.getClass();
        switch (clazz.getSimpleName()) {
            case "PatientBean":
                PatientBean patientBean = (PatientBean) bean;
                InfoBean infoBean = patientBean.getInfo();
                insertSingleBean(infoBean);
                DeviceBean deviceBean = patientBean.getDevice();
                insertSingleBean(deviceBean);
                UserBean userBean = patientBean.getUser();
                insertSingleBean(userBean);
                return true;
            case "DrugDetailBean":
                DrugDetailBean detailBean = (DrugDetailBean) bean;
                List<MedicineNamesBean> names = detailBean.getMedicine_names();
                insertListBean(names);
                List<MedicineDatesBean> dates = detailBean.getMedicine_dates();
                insertListBean(dates);
                MedicineTimeBean time = detailBean.getMedicine_time();
                insertSingleBean(time);
                return true;
            case "MyDirectorBean":
                MyDirectorBean myDirectorBean = (MyDirectorBean) bean;
                List<DirectorBean> directors = myDirectorBean.getDirector();
                insertListBean(directors);
                List<PhotosBean> photos = myDirectorBean.getPhotos();
                insertListBean(photos);
                return true;
            default:
                return insertSingleBean(bean);
        }
    }

    public <T extends DataSupport> boolean insertListBean(final List<T> tList) {
        if (tList.isEmpty()) return false;
//        final SQLiteDatabase db = Connector.getDatabase();
//        db.beginTransaction();
        synchronized (this) {
            try {
                for (T bean : tList) {
                    insertSingleBean(bean);
                }
//                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
//                db.endTransaction();
//                db.close();
            }
        }
        return true;
    }

    /**
     * id原始插入 即插入更新
     *
     * @param bean
     * @param <T>
     * @return
     */
    public <T extends DataSupport> boolean insertSingleBean(T bean) {
        if (bean == null) return false;
        //是否有id字段~~坑爹的后台
        Object id = getIdByBean(bean);

        if (id != null) {
            Object object = DataSupport.find(bean.getClass(), Long.valueOf(String.valueOf(id)));
            if (object == null) {
                return insertRemoteID(bean, id);
            } else {
                DataSupport.delete(bean.getClass(), Long.valueOf(String.valueOf(id)));
                return insertRemoteID(bean, id);
            }
        } else {
            return bean.save();
        }
    }

    /**
     * 拿出网络数据的id字段数字
     *
     * @param bean
     * @param <T>
     * @return id值
     */
    private <T extends DataSupport> Object getIdByBean(T bean) {
        Class<? extends DataSupport> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("_id")) {
                    return field.get(bean);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把插入数据生成的id变为远程数据的id
     *
     * @param bean
     * @param id
     * @param <T>
     * @return
     */
    private <T extends DataSupport> boolean insertRemoteID(T bean, Object id) {
        bean.save();
        Object newID = getIdByBean(bean);
        ContentValues values = new ContentValues();
        values.put("id", String.valueOf(id));
        return DataSupport.update(bean.getClass(), values, Long.valueOf(String.valueOf(newID))) == 1;
    }

    /**
     * 插入一条数据
     *
     * @param bean 对应的表类型bean
     * @return -1为失败操作
     */
    public <T> long insertBean(T bean) {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        ContentValues contentValues = new ContentValues();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (!fieldName.equalsIgnoreCase("id")) {
                putValueToContent(contentValues, field, bean);
            }
        }
        return mDB.insert(clazz.getSimpleName(), null, contentValues);
    }

    private <T> void putValueToContent(ContentValues contentValues, Field field, T bean) {
        Class<? extends ContentValues> clazz = contentValues.getClass();
        try {
            Object[] parameters = new Object[]{field.getName(), field.get(bean)};//field.get(bean) 拿到对象实例bean的域成员的值~~
            Class<?>[] parameterTypes = getParameterTypes(field, field.get(bean), parameters);
            //e.g. Method m=clazz.getDeclaredMethod("get",new Class[]{int.class,String.class})
            Method method = clazz.getDeclaredMethod("put", parameterTypes);
            method.setAccessible(true);
            method.invoke(contentValues, parameters);
        } catch (IllegalAccessException e) {//非法存取
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private Class<?>[] getParameterTypes(Field field, Object obj, Object[] parameters) {
        Class<?>[] parameTypes = new Class<?>[0];
        if (isCharType(field)) {//属性是char类型时
            parameters[1] = String.valueOf(obj);
            parameTypes = new Class[]{String.class, String.class};
        } else {
            if (field.getType().isPrimitive()) {//基本数据类型
                parameTypes = new Class[]{String.class, getObject(field.getType())};
            } else if ("java.util.Date".equals(field.getType().getName())) {

            } else {
                parameTypes = new Class[]{String.class, field.getType()};
            }
        }
        return parameTypes;
    }

    private Class getObject(Class<?> type) {
        if (null != type) {
            String typeName = type.getName();
            if (typeName.equals("int")) {
                return Integer.class;
            } else if (typeName.equals("short")) {
                return Short.class;
            } else if (typeName.equals("long")) {
                return Long.class;
            } else if (typeName.equals("byte")) {
                return Byte.class;
            } else if (typeName.equals("float")) {
                return Float.class;
            } else if (typeName.equals("double")) {
                return Double.class;
            } else if (typeName.equals("boolean")) {
                return Boolean.class;
            } else if (typeName.equals("char")) {
                return Byte.class;
            }
        }
        return null;
    }

    private boolean isCharType(Field field) {
        String fieldType = field.getType().getName();
        return fieldType.equalsIgnoreCase("char") || fieldType.equalsIgnoreCase("Character");
    }

    /*————————————————————————————————查询数据——————————————————————————————————————*/


    public <T extends DataSupport> T querySimpleByBean(Class<T> clazz) {
        switch (clazz.getSimpleName()) {
            case "PatientBean":
                InfoBean infoBean = DataSupport.findLast(InfoBean.class);
                DeviceBean deviceBean = DataSupport.findLast(DeviceBean.class);
                UserBean userBean = DataSupport.findLast(UserBean.class);
                PatientBean patientBean = new PatientBean();
                patientBean.setInfo(infoBean);
                patientBean.setDevice(deviceBean);
                patientBean.setUser(userBean);
                return (T) patientBean;
            case "DrugDetailBean":
                List<MedicineNamesBean> namesBeanList = DataSupport.findAll(MedicineNamesBean.class);
                List<MedicineDatesBean> datesBeanList = DataSupport.findAll(MedicineDatesBean.class);
                MedicineTimeBean timeBean = DataSupport.findLast(MedicineTimeBean.class);
                DrugDetailBean detailBean = new DrugDetailBean();
                detailBean.setMedicine_names(namesBeanList);
                detailBean.setMedicine_dates(datesBeanList);
                detailBean.setMedicine_time(timeBean);
                return (T) detailBean;
            case "MyDirectorBean":
                List<DirectorBean> directorBeanList = DataSupport.findAll(DirectorBean.class);
                List<PhotosBean> photosBeanList = DataSupport.findAll(PhotosBean.class);

                MyDirectorBean bean = new MyDirectorBean();
                bean.setDirector(directorBeanList);
                bean.setPhotos(photosBeanList);
                return (T) bean;
            default:
                return null;
        }
    }

    public <T extends DataSupport> List<T> queryListByBean(Class<T> clazz) {
        return DataSupport.findAll(clazz);
    }

    public <T> List<T> queryById(Class<T> clazz, int id) {
        Cursor cursor = mDB.query(clazz.getSimpleName(), null, "id=" + id, null, null, null, null);
        List<T> list = getBeanEntity(cursor, clazz);
        return list;
    }

    public <T> T queryById(Class<T> clazz) {
        UpdateBean updateBean = new UpdateBean();
        return (T) updateBean;
    }

    private <T> List<T> getBeanEntity(Cursor cursor, Class<T> clazz) {
        if (null != cursor && cursor.moveToFirst()) {
            try {
                T cla = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    String methodName = getColumnMethodName(field.getType());

                    Class<? extends Cursor> cursorClass = cursor.getClass();
                    Method cursorMethod = cursorClass.getMethod(methodName, int.class);
                    cursorMethod.invoke(cursor, cursor.getColumnIndex(field.getName()));

                    if (field.getType() == boolean.class || field.getType() == Boolean.class) {

                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getColumnMethodName(Class<?> fieldType) {
        String typeName;
        if (fieldType.isPrimitive()) {//基础
            typeName = capitalize(fieldType.getName());
        } else {//String--getName()--class java.lang.String
            typeName = fieldType.getSimpleName();
        }
        String methodName = "get" + typeName;
        if ("getBoolean".equals(methodName)) {
            methodName = "getInt";
        } else if ("getChar".equals(methodName) || "getCharacter".equals(methodName)) {
            methodName = "getString";
        } else if ("getDate".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)) {
            methodName = "getInt";
        }
        return methodName;
    }

    /**
     * 首字母大写
     *
     * @param string
     * @return
     */
    private String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }


    public void saveToken(TokenBean tokenBean) {
        SharedUtils.setString(
                "access_token_" + TbApp.instance().getCUR_USER(), tokenBean.getAccess_token());
        SharedUtils.setString(
                "refresh_token_" + TbApp.instance().getCUR_USER(), tokenBean.getRefresh_token());
        SharedUtils.setInteger(
                "expires_in_" + TbApp.instance().getCUR_USER(), tokenBean.getExpires_in());

        //保存当前用户
        SharedUtils.setString(TbGlobal.JString.CUR_USER, TbApp.instance().getCUR_USER());
        TbApp.instance().setCUR_TOKEN(tokenBean.getAccess_token());
    }

    public static LocalData getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final LocalData INSTANCE = new LocalData();
    }
}
