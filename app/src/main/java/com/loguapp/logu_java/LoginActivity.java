package com.loguapp.logu_java;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

/**
 * Created by BA042808 on 4/5/2016.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        System.out.println(preferences.getString("test", "DEFAULT"));
        editor.putString("test", "shared prefs works");
        editor.apply();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginButton: {
                System.out.println("Logging in.");
                Intent i = new Intent(getBaseContext(), Dashboard.class);
                startActivity(i);
                break;
            }
            case R.id.signUpButton: {

            }
        }
    }
}
