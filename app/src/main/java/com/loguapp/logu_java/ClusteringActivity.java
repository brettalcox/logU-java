package com.loguapp.logu_java;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by BA042808 on 4/15/2016.
 */
public class ClusteringActivity extends CommunityActivity {

    ClusterManager<MapItemCluster> mClusterManager;

    protected void setUpClusterer() {
        // Declare a variable for the cluster manager.

        // Position the map.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 2));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MapItemCluster>(this, getMap());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 100; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MapItemCluster offsetItem = new MapItemCluster(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }
}
