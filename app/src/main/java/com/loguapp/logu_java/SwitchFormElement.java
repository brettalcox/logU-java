package com.loguapp.logu_java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;

import com.github.dkharrat.nexusdialog.controllers.LabeledFieldController;

/**
 * Created by brettalcox on 4/23/16.
 */
public class SwitchFormElement extends LabeledFieldController {
    public SwitchFormElement(Context ctx, String name, String label) {
        super(ctx, name, label, false);
    }

    @Override
    protected View createFieldView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return inflater.inflate(R.layout.switch_form_element, null);
    }

    public Switch getAddSwitch() {
        return (Switch)getView().findViewById(R.id.log_gps_switch);
    }

    public void refresh() {
        // nothing to refresh
    }
}
