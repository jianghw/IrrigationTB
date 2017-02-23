package com.cjyun.tb;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.cjyun.tb.supervisor.data.local.SuPatientNewsDBHelper;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);

    }

    public void demo(){


        SuPatientNewsDBHelper helper = new SuPatientNewsDBHelper(getContext());

        helper.getReadableDatabase();

    }

}