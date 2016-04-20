package com.loguapp.logu_java;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by BA042808 on 4/20/2016.
 */
public class GraphListViewAdapter extends BaseAdapter {

    Activity context;
    String lift[];

    public GraphListViewAdapter(Activity context, String lift[]) {
        this.context = context;
        this.lift = lift;
    }

    @Override
    public int getCount() {
        return lift.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.lift_row_layout, null);
            holder = new ViewHolder();
            holder.txtViewLift = (TextView) convertView.findViewById(R.id.LiftRow);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewLift.setText(lift[position]);

        return convertView;
    }

    private class ViewHolder {
        TextView txtViewLift;
    }
}
