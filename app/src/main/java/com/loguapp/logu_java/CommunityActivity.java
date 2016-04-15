package com.loguapp.logu_java;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;


/**
 * Created by BA042808 on 3/31/2016.
 */
public abstract class CommunityActivity extends FragmentActivity implements OnMapReadyCallback{

    ListView lview;
    StaticListViewAdapter lviewAdapter;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mapView, mapFragment);
        transaction.commit();
        mapFragment.getMapAsync(this);

        String[] staticList = {"Weekly Poundage Graph", "Weekly Poundage Data", "Targeted Muscle Graph"};
        lview = (ListView) findViewById(R.id.staticCommListView);
        lviewAdapter = new StaticListViewAdapter(CommunityActivity.this, staticList);
        lview.setAdapter(lviewAdapter);
        setListViewHeightBasedOnChildren(lview);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        StaticListViewAdapter listAdapter = (StaticListViewAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        mMap = map;
        setUpClusterer();
    }

    protected GoogleMap getMap() {
        return mMap;
    }

    protected abstract void setUpClusterer();
}
