package com.loguapp.logu_java;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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
 * Created by BA042808 on 3/31/2016.
 */
public class LogLiftActivity extends FormActivity {

    @Override protected void initForm() {
        setTitle("Log Lift");
        setContentView(R.layout.form_activity);
        List<String> lifts = Arrays.asList("Squat", "Pause Squat", "Front Squat", "Box Squat", "Hack Squat", "Zerker Squat", "Pin Squat", "Good Mornings", "Bench", "Close Grip Bench", "Incline Bench", "Decline Bench", "Pause Bench", "Floor Press", "Pin Press", "Deadlift", "Deficit Deadlift", "Pause Deadlift", "Snatch Grip Deadlift", "Straight Leg Deadlift", "Romanian Deadlift", "Overhead Press", "Sots Press", "Pullups", "Dips", "Push Ups", "Crunches", "Sit-ups", "General Ab Work", "Bent Over Rows", "T-Bar Rows", "Kroc Rows", "Upright Rows", "Cable Upright Rows", "Cable Seated Rows", "Straight Bar Bicep Curls", "EZ Bar Bicep Curls", "Barbell Bicep Curls", "Hammer Curls", "Cable Curls", "Chest Fly", "Cable Standing Fly", "Lat Pulldown", "Shoulder Fly", "Lateral Raise", "Shoulder Shrug", "Tricep Extension", "Tricep Pushdown", "Dumbbell Bench", "Dumbbell Incline Press", "Dumbbell Press", "Skullcrushers", "21's", "Leg Press", "Leg Extension", "Leg Curl", "Standing Calf Raise", "Seated Calf Raise", "Snatch", "Clean and Jerk", "Power Clean", "Power Snatch", "Hang Clean", "Hang Snatch", "Snatch Pulls", "Clean Pulls");

        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");

        final FormSectionController section1 = new FormSectionController(this, "New Lift");

        final Prefs preferences = new Prefs();

        section1.addElement(new DatePickerController(this, "date", "Date", true, dateFormat));
        section1.addElement(new SelectionController(this, "lift", "Lift", true, "Select Lift", lifts, true));
        section1.addElement(new EditTextController(this, "sets", "Sets", "", true, InputType.TYPE_CLASS_NUMBER));
        section1.addElement(new EditTextController(this, "reps", "Reps", "", true, InputType.TYPE_CLASS_NUMBER));
        section1.addElement(new EditTextController(this, "weight", "Weight", "", true, InputType.TYPE_CLASS_NUMBER));
        section1.addElement(new EditTextController(this, "intensity", "Intensity", "", true, InputType.TYPE_CLASS_NUMBER));
        section1.addElement(new EditTextController(this, "notes", "Notes"));

        ButtonFormElement buttonElem = new ButtonFormElement(this, "buttonElem", "");
        buttonElem.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat initialDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                SimpleDateFormat logDateFormat = new SimpleDateFormat("M/d/yyyy");
                try {

                    if (getModel().getValue("date") == null || getModel().getValue("lift") == null ||
                            (getModel().getValue("sets") == null || getModel().getValue("sets") == "0") ||
                            (getModel().getValue("reps") == null || getModel().getValue("reps") == "0") ||
                            getModel().getValue("weight") == null) {
                        Snackbar snackbar = Snackbar
                                .make(v, "Log Lift Failed! Fill out all required fields!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Date date = initialDateFormat.parse(getModel().getValue("date").toString());
                        System.out.println(logDateFormat.format(date));

                        if (getModel().getValue("intensity") == null) {
                            getModel().setValue("intensity", "0");
                        }

                        if (getModel().getValue("notes") == null) {
                            getModel().setValue("notes", " ");
                        }

                        String username = preferences.getUsername(LogLiftActivity.this);

                        String queryParam = "name=" + username + "&date=" + logDateFormat.format(date).toString() + "&lift=" +
                                getModel().getValue("lift").toString() + "&sets=" + getModel().getValue("sets").toString() +
                                "&reps=" + getModel().getValue("reps").toString() + "&weight=" + getModel().getValue("weight").toString() +
                                "&intensity=" + getModel().getValue("intensity").toString() + "&notes=" + getModel().getValue("notes").toString();
                        System.out.println(queryParam);
                        new LiftData().execute(queryParam);

                        Intent intent = new Intent(LogLiftActivity.this, Dashboard.class);
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

    public class LiftData extends AsyncTask<String, Void, Void> {
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
