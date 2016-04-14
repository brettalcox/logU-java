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
public class StaticListViewAdapter extends BaseAdapter
{
    Activity context;
    String list[];

    public StaticListViewAdapter(Activity context, String[] list) {
        super();
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return list.length;
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
        TextView static_row;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.static_table_row, null);
            holder = new ViewHolder();
            holder.static_row = (TextView) convertView.findViewById(R.id.static_row);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.static_row.setText(list[position]);

        return convertView;
    }

}
