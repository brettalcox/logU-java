package com.loguapp.logu_java;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.InputType;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.dkharrat.nexusdialog.FormActivity;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;

import java.io.DataOutputStream;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by BA042808 on 3/31/2016.
 */
public class SettingsActivity extends FormActivity {

    private final Prefs preferences = new Prefs();
    static final int GYM_LOCATION_RESULT = 2;

    final ButtonFormElement mapButton = new ButtonFormElement(this, "mapElem", "Gym Location");

    @Override
    protected void initForm() {
        setContentView(R.layout.form_activity);

        final FormSectionController section1 = new FormSectionController(this, "Settings");
        final FormSectionController section2 = new FormSectionController(this, "Privacy Settings");

        final ToggleFormElement unitElem = new ToggleFormElement(this, "unitElem", "Unit");
        final ToggleFormElement genderElem = new ToggleFormElement(this, "genderElem", "Gender");
        final EditTextController bodyweightElem = new EditTextController(this, "bodyweight", "Bodyweight", "", true, InputType.TYPE_CLASS_NUMBER);
        final ButtonFormElement submitElem = new ButtonFormElement(this, "saveElem", "");
        final SwitchFormElement logGPSelem = new SwitchFormElement(this, "logGPSelem", "Log GPS on Community Map");

        unitElem.getAddToggle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitElem.getSaveChangesButton().setClickable(true);
                submitElem.getSaveChangesButton().setAlpha(1F);
            }
        });

        genderElem.getAddToggle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitElem.getSaveChangesButton().setClickable(true);
                submitElem.getSaveChangesButton().setAlpha(1F);
            }
        });

        submitElem.getSaveChangesButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (getModel().getValue("bodyweight") == null) {
                    Snackbar snackbar = Snackbar
                            .make(v, "Save Changes Failed! Enter a valid weight.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    if (unitElem.getAddToggle().getText() == "Lbs") {
                        preferences.setUnit(SettingsActivity.this, 1);
                    } else {
                        preferences.setUnit(SettingsActivity.this, 0);
                    }

                    if (genderElem.getAddToggle().getText() == "Male") {
                        preferences.setGender(SettingsActivity.this, "M");
                    } else {
                        preferences.setGender(SettingsActivity.this, "F");
                    }

                    preferences.setBodyweight(SettingsActivity.this, Integer.parseInt(bodyweightElem.getEditText().getText().toString()));
                    System.out.println(preferences.getBodyweight(SettingsActivity.this));

                    try {
                        if (new UserSettings().execute("username=" + preferences.getUsername(SettingsActivity.this) + "&unit=" + preferences.getUnit(SettingsActivity.this)
                                + "&gender=" + preferences.getGender(SettingsActivity.this) + "&bodyweight=" + preferences.getBodyweight(SettingsActivity.this)).get()) {
                            submitElem.getSaveChangesButton().setClickable(false);
                            submitElem.getSaveChangesButton().setAlpha(0.25F);

                            Snackbar snackbar = Snackbar
                                    .make(v, "Saved Changes.", Snackbar.LENGTH_LONG);
                            View view = snackbar.getView();
                            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTextColor(Color.WHITE);
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(v, "Saving Changes Failed! Do you have a network connection?", Snackbar.LENGTH_LONG);
                            View view = snackbar.getView();
                            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTextColor(Color.WHITE);
                            snackbar.show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        unitElem.getAddToggle().setText("Lbs");
        unitElem.getAddToggle().setTextOn("Kgs");
        unitElem.getAddToggle().setTextOff("Lbs");

        genderElem.getAddToggle().setText("Male");
        genderElem.getAddToggle().setTextOff("Male");
        genderElem.getAddToggle().setTextOn("Female");

        if (preferences.getUnit(SettingsActivity.this) == 0) {
            unitElem.getAddToggle().setChecked(true);
        }

        if (preferences.getGender(SettingsActivity.this) == "F") {
            genderElem.getAddToggle().setChecked(true);
        }

        if (preferences.getBodyweight(SettingsActivity.this) != 9000) {
            getModel().setValue("bodyweight", preferences.getBodyweight(SettingsActivity.this));
        }

        submitElem.getSaveChangesButton().setClickable(false);
        submitElem.getSaveChangesButton().setAlpha(0.25F);

        if (!(preferences.getLat(SettingsActivity.this) != 6969) && !(preferences.getLon(SettingsActivity.this) != 6969)) {
            mapButton.getAddButton().setText("Set Gym Location");
        } else {
            mapButton.getAddButton().setText(preferences.getLat(SettingsActivity.this) + ", "
                    + preferences.getLon(SettingsActivity.this));
        }
        mapButton.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MapCoordsSettingsActivity.class);
                startActivityForResult(i, GYM_LOCATION_RESULT);
            }
        });

        if (!(preferences.getLat(SettingsActivity.this) == 6969) && !(preferences.getLon(SettingsActivity.this) == 6969)) {
            logGPSelem.getAddSwitch().setClickable(true);
            logGPSelem.getAddSwitch().setAlpha(1F);
        } else {
            logGPSelem.getAddSwitch().setClickable(false);
            logGPSelem.getAddSwitch().setAlpha(0.25F);
        }

        logGPSelem.getAddSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (logGPSelem.getAddSwitch().isChecked()) {
                    preferences.setLogGPS(SettingsActivity.this, true);

                    Snackbar snackbar = Snackbar
                            .make(buttonView, "Log Gym Location Enabled", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snackbar.show();
                } else {
                    preferences.setLogGPS(SettingsActivity.this, false);

                    Snackbar snackbar = Snackbar
                            .make(buttonView, "Log Gym Location Disabled", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snackbar.show();
                }
            }
        });

        section1.addElement(unitElem);
        section1.addElement(genderElem);
        section1.addElement(bodyweightElem);
        section1.addElement(submitElem);
        section2.addElement(mapButton);
        section2.addElement(logGPSelem);
        getFormController().addSection(section1);
        getFormController().addSection(section2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GYM_LOCATION_RESULT) {
            if (resultCode == RESULT_OK) {
                preferences.setLat(SettingsActivity.this, data.getDoubleExtra("latitude", 0));
                preferences.setLon(SettingsActivity.this, data.getDoubleExtra("longitude", 0));

                mapButton.getAddButton().setText(preferences.getLat(SettingsActivity.this) + ", " + preferences.getLon(SettingsActivity.this));

                Snackbar snackbar = Snackbar
                        .make(getCurrentFocus(), "Gym Location Saved.", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar
                        .make(getCurrentFocus(), "Gym Location Not Saved.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    public class UserSettings extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            String urlParam = params[0];

            try {
                String url = "https://loguapp.com/swift10.php";
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("accept", "application/json");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = urlParam;

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
