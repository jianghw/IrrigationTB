package com.tryking.trykingcitychoose;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseActivity;
import com.cjyun.tb.patient.custom.OsiEditText;
import com.cjyun.tb.patient.util.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressActivity extends BaseActivity {

    @Bind(R.id.ll_actionBar_back)
    LinearLayout mLlActionBarBack;
    @Bind(R.id.tv_title_left)
    TextView mTvTitleLeft;
    @Bind(R.id.include_actionBar)
    RelativeLayout mIncludeActionBar;
    @Bind(R.id.title_province)
    TextView mTitleProvince;
    @Bind(R.id.title_city)
    TextView mTitleCity;
    @Bind(R.id.title_district)
    TextView mTitleDistrict;
    @Bind(R.id.title_street)
    TextView mTitleStreet;
    @Bind(R.id.address_list)
    ListView mAddressList;
    @Bind(R.id.oEdit_1)
    OsiEditText mOEdit1;
    @Bind(R.id.oEdit_2)
    OsiEditText mOEdit2;

    @Bind(R.id.btn_no)
    Button mBtnNo;
    @Bind(R.id.btn_ok)
    Button mBtnOk;
    @Bind(R.id.lr_details_address)
    LinearLayout mLrDetailsAddress;
    private int currentLevel = 1;
    private CityDBManager manager;
    private SQLiteDatabase database;
    private List<CityItemBean> cityList;
    private AddressCityListAdapter adapter;
    private CityItemBean provinceItem;
    private CityItemBean cityItem;
    private CityItemBean districtItem;
    private CityItemBean streetItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);

        cityList = new ArrayList<>();
        initCityData();


        mAddressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (currentLevel) {
                    case 1:
                        provinceItem = cityList.get(position);
                        mTitleProvince.setText(provinceItem.getName());
                        mTitleProvince.setVisibility(View.VISIBLE);

                        initList2(provinceItem.getCode());
                        currentLevel++;
                        break;
                    case 2:
                        cityItem = cityList.get(position);
                        mTitleCity.setText(cityItem.getName());
                        mTitleCity.setVisibility(View.VISIBLE);

                        initList3(cityItem.getCode());
                        currentLevel++;
                        break;
                    case 3:
                        districtItem = cityList.get(position);
                        mTitleDistrict.setText(districtItem.getName());
                        mTitleDistrict.setVisibility(View.VISIBLE);

                        initList4(districtItem.getCode());
                        currentLevel++;
                        break;
                    case 4:
                        streetItem = cityList.get(position);
                        mTitleStreet.setText(streetItem.getName());
                        mTitleStreet.setVisibility(View.VISIBLE);

                        printAddress();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initCityData() {
        manager = new CityDBManager(getApplicationContext());
        manager.openDatabase();
        database = manager.getDatabase();
        cityList.clear();
        try {
            String sql = "select code,name from t_prov_city_area_street where level=1";
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                CityItemBean cityItemBean = new CityItemBean();
                cityItemBean.setName(name);
                cityItemBean.setCode(code);
                cityList.add(cityItemBean);
            }
            adapter = new AddressCityListAdapter(this, cityList);
            mAddressList.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        database.close();
        manager.closeDatabase();
        adapter.notifyDataSetChanged();
        if (cityList.size() == 0) {
            printAddress();
        }
    }

    private void printAddress() {
        SsoMemberAddress address = new SsoMemberAddress();
        address.setProvince(provinceItem.getName());
        address.setCity(cityItem == null ? "" : cityItem.getName());
        address.setArea(districtItem == null ? "" : districtItem.getName());
        address.setAddressName(streetItem == null ? "" : streetItem.getName());

        mAddressList.setVisibility(View.GONE);
        mLrDetailsAddress.setVisibility(View.VISIBLE);
        mOEdit1.setTitle("地址");
        mOEdit2.setTitle("详情");
        mOEdit1.setEtContent(address.getAddressContent());

       /* Message msg = new Message();
        msg.what = 1;
        msg.obj = address;
        mHandler.sendMessage(msg);*/
    }

    private void initList2(String pCode) {
        manager = new CityDBManager(getApplicationContext());
        manager.openDatabase();
        database = manager.getDatabase();
        cityList.clear();
        try {
            String sql = "select code,name from t_prov_city_area_street where level=2 and parentId='" + pCode + "'";
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                CityItemBean cityItemBean = new CityItemBean();
                cityItemBean.setName(name);
                cityItemBean.setCode(code);
                cityList.add(cityItemBean);
            }
            adapter = new AddressCityListAdapter(this, cityList);
            mAddressList.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        database.close();
        manager.closeDatabase();
        adapter.notifyDataSetChanged();
        if (cityList.size() == 0) {
            printAddress();
        }
    }

    private void initList3(String pCode) {
        manager = new CityDBManager(getApplicationContext());
        manager.openDatabase();
        database = manager.getDatabase();
        cityList.clear();

        try {
            String sql = "select code,name from t_prov_city_area_street where level=3 and parentId='" + pCode + "'";
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                CityItemBean cityItemBean = new CityItemBean();
                cityItemBean.setName(name);
                cityItemBean.setCode(code);
                cityList.add(cityItemBean);
            }
            adapter = new AddressCityListAdapter(this, cityList);
            mAddressList.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        database.close();
        manager.closeDatabase();
        adapter.notifyDataSetChanged();
        if (cityList.size() == 0) {
            printAddress();
        }
    }

    private void initList4(String pCode) {
        manager = new CityDBManager(getApplicationContext());
        manager.openDatabase();
        database = manager.getDatabase();
        cityList.clear();

        try {
            String sql = "select code,name from t_prov_city_area_street where level=4 and parentId='" + pCode + "'";
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                CityItemBean cityItemBean = new CityItemBean();
                cityItemBean.setName(name);
                cityItemBean.setCode(code);
                cityList.add(cityItemBean);
            }
            adapter = new AddressCityListAdapter(this, cityList);
            mAddressList.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        database.close();
        manager.closeDatabase();
        adapter.notifyDataSetChanged();
        if (cityList.size() == 0) {
            printAddress();
        }
    }

    @OnClick(R.id.btn_no)
    public void onClickForNo() {
        finish();
    }

    @OnClick(R.id.btn_ok)
    public void onClickForOk() {
        String text1 = mOEdit1.getEditText();
        String text2 = mOEdit2.getEditText();
        if (TextUtils.isEmpty(text1) || TextUtils.isEmpty(text2)) {
            ToastUtils.showMessage("请确保填写地址详情后保存");
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", text1 + text2);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    @OnClick(R.id.ll_actionBar_back)
    public void onClickBack() {
        finish();
    }

    private final MyHandler mHandler = new MyHandler(this);

    static class MyHandler extends Handler {
        WeakReference<AddressActivity> weakReference;

        public MyHandler(AddressActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AddressActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        SsoMemberAddress address = new SsoMemberAddress();
                        address = (SsoMemberAddress) msg.obj;
                        //                        detailAddress.setText(address.getAddressContent());
                        ToastUtils.showMessage(address.getAddressContent());
                        break;
                    default:
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cityList != null && !cityList.isEmpty()) cityList.clear();
        ButterKnife.unbind(this);
    }
}
