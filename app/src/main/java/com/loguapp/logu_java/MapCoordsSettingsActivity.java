package com.loguapp.logu_java;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by BA042808 on 4/19/2016.
 */
public class MapCoordsSettingsActivity extends Activity implements OnMapReadyCallback {

    private final Prefs preferences = new Prefs();
    private GoogleMap mMap;
    private LatLng gymLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_map_coords);

        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.setMapCoords, mapFragment);
        transaction.commit();
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO: Fix map stuff
        //grab coords from this and make it move smoother (by potentially implementing another method
        // to deal with this.
        mMap = googleMap;

        LatLng markerLocation = new LatLng(preferences.getLat(MapCoordsSettingsActivity.this),
                preferences.getLon(MapCoordsSettingsActivity.this));
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 10));

        final Button sendCoords = (Button) findViewById(R.id.setGymLocation);
        sendCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gymLocation = getMap().getCameraPosition().target;
                System.out.println(gymLocation);

                Intent intent = new Intent(MapCoordsSettingsActivity.this, SettingsActivity.class);
                intent.putExtra("latitude", gymLocation.latitude);
                intent.putExtra("longitude", gymLocation.longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        getMap().setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition cameraPosition) {
                getMap().clear();
                getMap().addMarker(new MarkerOptions().position(cameraPosition.target));
            }
        });
    }

    protected GoogleMap getMap() {
        return mMap;
    }

}
