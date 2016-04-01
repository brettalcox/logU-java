package com.loguapp.logu_java;

/**
 * Created by BA042808 on 4/1/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.github.dkharrat.nexusdialog.controllers.LabeledFieldController;

public class ButtonFormElement extends LabeledFieldController {

    public ButtonFormElement(Context ctx, String name, String label) {
        super(ctx, name, label, false);
    }

    @Override
    protected View createFieldView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return inflater.inflate(R.layout.button_form_element, null);
    }

    public Button getAddButton() {
        return (Button)getView().findViewById(R.id.add_btn);
    }

    public void refresh() {
        // nothing to refresh
    }
}