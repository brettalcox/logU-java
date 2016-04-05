package com.loguapp.logu_java;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by BA042808 on 3/31/2016.
 */
public class CommunityActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        Singleton singletonTest = Singleton.getInstance();
        singletonTest.setTest("this is a test");
        System.out.println(singletonTest.getTest());
    }
}
