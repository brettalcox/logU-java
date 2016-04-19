package com.loguapp.logu_java;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.github.dkharrat.nexusdialog.FormActivity;
import com.github.dkharrat.nexusdialog.controllers.DatePickerController;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.github.dkharrat.nexusdialog.controllers.SelectionController;

import java.io.DataOutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by brettalcox on 4/18/16.
 */
public class UpdateLiftActivity extends FormActivity {
    @Override
    protected void initForm() {
        setTitle("Update Lift");
        setContentView(R.layout.form_activity);
        Bundle extras = getIntent().getExtras();

        List<String> lifts = Arrays.asList("Squat", "Pause Squat", "Front Squat", "Box Squat", "Hack Squat", "Zerker Squat", "Pin Squat", "Good Mornings", "Bench", "Close Grip Bench", "Incline Bench", "Decline Bench", "Pause Bench", "Floor Press", "Pin Press", "Deadlift", "Deficit Deadlift", "Pause Deadlift", "Snatch Grip Deadlift", "Straight Leg Deadlift", "Romanian Deadlift", "Overhead Press", "Sots Press", "Pullups", "Dips", "Push Ups", "Crunches", "Sit-ups", "General Ab Work", "Bent Over Rows", "T-Bar Rows", "Kroc Rows", "Upright Rows", "Cable Upright Rows", "Cable Seated Rows", "Straight Bar Bicep Curls", "EZ Bar Bicep Curls", "Barbell Bicep Curls", "Hammer Curls", "Cable Curls", "Chest Fly", "Cable Standing Fly", "Lat Pulldown", "Shoulder Fly", "Lateral Raise", "Shoulder Shrug", "Tricep Extension", "Tricep Pushdown", "Dumbbell Bench", "Dumbbell Incline Press", "Dumbbell Press", "Skullcrushers", "21's", "Leg Press", "Leg Extension", "Leg Curl", "Standing Calf Raise", "Seated Calf Raise", "Snatch", "Clean and Jerk", "Power Clean", "Power Snatch", "Hang Clean", "Hang Snatch", "Snatch Pulls", "Clean Pulls");

        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");

        final FormSectionController section1 = new FormSectionController(this, "Update Lift");

        final Prefs preferences = new Prefs();

        section1.addElement(new DatePickerController(this, "date", "Date", true, dateFormat));
        section1.addElement(new SelectionController(this, "lift", "Lift", true, "Select Lift", lifts, true));
        section1.addElement(new EditTextController(this, "sets", "Sets", "", true, InputType.TYPE_CLASS_NUMBER));
        section1.addElement(new EditTextController(this, "reps", "Reps", "", true, InputType.TYPE_CLASS_NUMBER));
        section1.addElement(new EditTextController(this, "weight", "Weight", "", true, InputType.TYPE_CLASS_NUMBER));
        section1.addElement(new EditTextController(this, "intensity", "Intensity", "", true, InputType.TYPE_CLASS_NUMBER));
        section1.addElement(new EditTextController(this, "notes", "Notes"));

        //getModel().setValue("date", extras.getString("Date"));
        getModel().setValue("lift", extras.getString("Lift"));
        getModel().setValue("weight", extras.getString("Weight"));

        ButtonFormElement buttonElem = new ButtonFormElement(this, "updateElem", "");
        buttonElem.getUpdateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat initialDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                SimpleDateFormat logDateFormat = new SimpleDateFormat("M/d/yyyy");
                try {

                    if (getModel().getValue("date") == null || getModel().getValue("lift") == null ||
                            getModel().getValue("sets") == null || getModel().getValue("reps") == null ||
                            getModel().getValue("weight") == null) {
                        new AlertDialog.Builder(UpdateLiftActivity.this)
                                .setTitle("Update Lift Failed!")
                                .setMessage("Fill out all of the required fields.")
                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Date date = initialDateFormat.parse(getModel().getValue("date").toString());
                        System.out.println(logDateFormat.format(date));

                        if (getModel().getValue("intensity") == null) {
                            getModel().setValue("intensity", "0");
                        }

                        if (getModel().getValue("notes") == null) {
                            getModel().setValue("notes", " ");
                        }

                        String username = preferences.getUsername(UpdateLiftActivity.this);

                        String queryParam = "name=" + username + "&date=" + logDateFormat.format(date).toString() + "&lift=" +
                                getModel().getValue("lift").toString() + "&sets=" + getModel().getValue("sets").toString() +
                                "&reps=" + getModel().getValue("reps").toString() + "&weight=" + getModel().getValue("weight").toString() +
                                "&intensity=" + getModel().getValue("intensity").toString() + "&notes=" + getModel().getValue("notes").toString();
                        System.out.println(queryParam);
                        new UpdateLiftData().execute(queryParam);

                        Intent intent = new Intent(UpdateLiftActivity.this, Dashboard.class);
                        intent.putExtra("shouldUpdateDash", true);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        section1.addElement(buttonElem);
        getFormController().addSection(section1);
    }

    public class UpdateLiftData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            String urlParam = params[0];

            try {
                String url = "https://loguapp.com/swift2.php";
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
            return null;
        }
    }
}
