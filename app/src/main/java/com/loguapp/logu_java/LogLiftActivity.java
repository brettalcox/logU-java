package com.loguapp.logu_java;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by BA042808 on 3/31/2016.
 */
public class LogLiftActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loglift);
        init();
    }

    public void init() {
        TextView text = (TextView) findViewById(R.id.textView5);
        System.out.println(text.getText() + "is the text that is currently displayed.");
    }
}
