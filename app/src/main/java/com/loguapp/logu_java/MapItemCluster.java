package com.loguapp.logu_java;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by BA042808 on 4/15/2016.
 */
public class MapItemCluster implements ClusterItem {
    private final LatLng mPosition;

    protected MapItemCluster(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public LatLng getPosition() {
        return mPosition;
    }
}
