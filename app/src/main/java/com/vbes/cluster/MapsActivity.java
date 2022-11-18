package com.vbes.cluster;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.vbes.cluster.module.StationInfo;
import com.vbes.cluster.render.MyClusterRender;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<StationInfo> mClusterManager;
    private Random mRandom = new Random(1984);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(51.6723432, 0.148271);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.3f));
        mClusterManager = new ClusterManager<>(this, mMap);
        mClusterManager.setRenderer(new MyClusterRender(this, mMap, mClusterManager));
        mClusterManager.setAnimation(true);//禁用动画
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<StationInfo>() {
            @Override
            public boolean onClusterClick(Cluster<StationInfo> cluster) {
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (ClusterItem item : cluster.getItems()) {
                    builder.include(item.getPosition());
                }
                final LatLngBounds bounds = builder.build();
                try {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        mMap.setOnCameraIdleListener(mClusterManager);
        addItems();
    }

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }

    private LatLng position() {
        return new LatLng(random(51.6723432, 51.38494009999999), random(0.148271, -0.3514683));
    }

    private void addItems() {
        mClusterManager.addItem(new StationInfo(position(), "Walter", false));
        mClusterManager.addItem(new StationInfo(position(), "Gran", false));
        mClusterManager.addItem(new StationInfo(position(), "Ruth", false));
        mClusterManager.addItem(new StationInfo(position(), "Stefan", true));
        mClusterManager.addItem(new StationInfo(position(), "Mechanic", false));
        mClusterManager.addItem(new StationInfo(position(), "Yeats", false));
        mClusterManager.addItem(new StationInfo(position(), "John", false));
        mClusterManager.addItem(new StationInfo(position(), "Trevor the Turtle", false));
        mClusterManager.addItem(new StationInfo(position(), "Teach", false));
        mClusterManager.addItem(new StationInfo(position(), "Walter", false));
        mClusterManager.addItem(new StationInfo(position(), "Gran", false));
        mClusterManager.addItem(new StationInfo(position(), "Ruth", false));
        mClusterManager.addItem(new StationInfo(position(), "Stefan", false));
        mClusterManager.addItem(new StationInfo(position(), "Mechanic", false));
        mClusterManager.addItem(new StationInfo(position(), "Yeats", false));
        mClusterManager.addItem(new StationInfo(position(), "John", false));
        mClusterManager.addItem(new StationInfo(position(), "Trevor the Turtle", false));
        mClusterManager.addItem(new StationInfo(position(), "Teach", true));
        mClusterManager.addItem(new StationInfo(position(), "Walter", false));
        mClusterManager.addItem(new StationInfo(position(), "Gran", false));
        mClusterManager.addItem(new StationInfo(position(), "Ruth", false));
        mClusterManager.addItem(new StationInfo(position(), "Stefan", false));
        mClusterManager.addItem(new StationInfo(position(), "Mechanic", false));
        mClusterManager.addItem(new StationInfo(position(), "Yeats", false));
        mClusterManager.addItem(new StationInfo(position(), "John", false));
        mClusterManager.addItem(new StationInfo(position(), "Trevor the Turtle", false));
        mClusterManager.addItem(new StationInfo(position(), "Teach", false));
        mClusterManager.addItem(new StationInfo(position(), "Walter", false));
        mClusterManager.addItem(new StationInfo(position(), "Gran", false));
        mClusterManager.addItem(new StationInfo(position(), "Ruth", false));
        mClusterManager.addItem(new StationInfo(position(), "Stefan", false));
        mClusterManager.addItem(new StationInfo(position(), "Mechanic", false));
        mClusterManager.addItem(new StationInfo(position(), "Yeats", false));
        mClusterManager.addItem(new StationInfo(position(), "John", false));
        mClusterManager.addItem(new StationInfo(position(), "Trevor the Turtle", false));
        mClusterManager.addItem(new StationInfo(position(), "Teach", false));
    }
}