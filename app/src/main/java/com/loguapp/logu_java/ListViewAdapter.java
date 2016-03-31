package com.loguapp.logu_java;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by brettalcox on 3/30/16.
 */
public class ListViewAdapter extends BaseAdapter
{
    Activity context;
    String date[];
    String lift[];
    String set_rep[];
    String weight[];

    public ListViewAdapter(Activity context, String[] date, String[] lift, String[] set_rep, String[] weight) {
        super();
        this.context = context;
        this.date = date;
        this.lift = lift;
        this.set_rep = set_rep;
        this.weight = weight;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return date.length;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView txtViewDate;
        TextView txtViewLift;
        TextView txtViewSetRep;
        TextView txtViewWeight;

    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.rowlayout, null);
            holder = new ViewHolder();
            holder.txtViewDate = (TextView) convertView.findViewById(R.id.textView1);
            holder.txtViewLift = (TextView) convertView.findViewById(R.id.textView2);
            holder.txtViewSetRep = (TextView) convertView.findViewById(R.id.textView3);
            holder.txtViewWeight = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewDate.setText(date[position]);
        holder.txtViewLift.setText(lift[position]);
        holder.txtViewSetRep.setText(set_rep[position]);
        holder.txtViewWeight.setText(weight[position]);

        return convertView;
    }

}
