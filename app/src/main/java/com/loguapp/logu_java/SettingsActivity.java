package com.loguapp.logu_java;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.audiofx.BassBoost;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.View;

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

    @Override
    protected void initForm() {
        setContentView(R.layout.form_activity);

        final FormSectionController section1 = new FormSectionController(this, "Settings");

        final ToggleFormElement unitElem = new ToggleFormElement(this, "unitElem", "Unit");
        final ToggleFormElement genderElem = new ToggleFormElement(this, "genderElem", "Gender");
        final EditTextController bodyweightElem = new EditTextController(this, "bodyweight", "Bodyweight", "", true, InputType.TYPE_CLASS_NUMBER);
        final ButtonFormElement submitElem = new ButtonFormElement(this, "saveElem", "");

        unitElem.getAddToggle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitElem.getSaveChangesButton().setClickable(true);
                submitElem.getSaveChangesButton().setAlpha((float) 1);
            }
        });

        genderElem.getAddToggle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitElem.getSaveChangesButton().setClickable(true);
                submitElem.getSaveChangesButton().setAlpha((float) 1);
            }
        });

        submitElem.getSaveChangesButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getModel().getValue("bodyweight") == null) {
                    new AlertDialog.Builder(SettingsActivity.this)
                            .setTitle("Saving changes failed!")
                            .setMessage("Please enter a valid bodyweight.")
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
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
                            submitElem.getSaveChangesButton().setAlpha((float) 0.25);
                        } else {

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

        submitElem.getSaveChangesButton().setClickable(false);
        submitElem.getSaveChangesButton().setAlpha((float) 0.25);

        section1.addElement(unitElem);
        section1.addElement(genderElem);
        section1.addElement(bodyweightElem);
        section1.addElement(submitElem);
        getFormController().addSection(section1);
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
