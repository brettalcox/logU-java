package com.loguapp.logu_java;

import android.text.InputType;

import com.github.dkharrat.nexusdialog.FormActivity;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;

/**
 * Created by BA042808 on 3/31/2016.
 */
public class SettingsActivity extends FormActivity {

    @Override
    protected void initForm() {
        setContentView(R.layout.form_activity);

        final FormSectionController section1 = new FormSectionController(this, "Settings");

        ToggleFormElement unitElem = new ToggleFormElement(this, "unitElem", "Unit");
        ToggleFormElement genderElem = new ToggleFormElement(this, "genderElem", "Gender");

        section1.addElement(new EditTextController(this, "weight", "Weight", "", true, InputType.TYPE_CLASS_NUMBER));
        unitElem.getAddToggle().setText("Lbs");
        unitElem.getAddToggle().setTextOn("Kgs");
        unitElem.getAddToggle().setTextOff("Lbs");
        section1.addElement(unitElem);

        genderElem.getAddToggle().setText("Male");
        genderElem.getAddToggle().setTextOff("Male");
        genderElem.getAddToggle().setTextOn("Female");
        section1.addElement(genderElem);

        getFormController().addSection(section1);

    }
}
