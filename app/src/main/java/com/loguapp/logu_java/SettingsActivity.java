package com.loguapp.logu_java;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;

import com.github.dkharrat.nexusdialog.FormActivity;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;

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
                        preferences.setUnit(SettingsActivity.this, 1);
                    } else {
                        preferences.setUnit(SettingsActivity.this, 0);
                    }

                    preferences.setBodyweight(SettingsActivity.this, Integer.parseInt(bodyweightElem.getEditText().getText().toString()));
                    System.out.println(preferences.getBodyweight(SettingsActivity.this));

                    submitElem.getSaveChangesButton().setClickable(false);
                    submitElem.getSaveChangesButton().setAlpha((float) 0.25);
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
}
