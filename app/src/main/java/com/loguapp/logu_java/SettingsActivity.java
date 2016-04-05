package com.loguapp.logu_java;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by BA042808 on 3/31/2016.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Singleton singletonTest = Singleton.getInstance();
        System.out.println(singletonTest.getTest());
    }
}
