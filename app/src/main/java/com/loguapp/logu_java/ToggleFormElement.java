package com.loguapp.logu_java;

/**
 * Created by BA042808 on 4/1/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ToggleButton;

import com.github.dkharrat.nexusdialog.controllers.LabeledFieldController;

public class ToggleFormElement extends LabeledFieldController {

    public ToggleFormElement(Context ctx, String name, String label) {
        super(ctx, name, label, false);
    }

    @Override
    protected View createFieldView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return inflater.inflate(R.layout.toggle_form_element, null);
    }

    public ToggleButton getAddToggle() {
        return (ToggleButton)getView().findViewById(R.id.add_toggle);
    }

    public void refresh() {
        // nothing to refresh
    }
}