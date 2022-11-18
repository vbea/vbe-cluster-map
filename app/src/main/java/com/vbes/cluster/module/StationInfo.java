package com.vbes.cluster.module;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Vbe on 2021/8/17.
 */
public class StationInfo implements ClusterItem {
    private int id;
    private String siteName;
    private LatLng latlng;
    private boolean available;

    public StationInfo() {

    }

    public StationInfo(LatLng l, String name, boolean a) {
        latlng = l;
        siteName = name;
        available = a;
    }

    public void setLatLng(double latitude, double longitude) {
        this.latlng = new LatLng(latitude, longitude);
    }

    public boolean isAvailable() {
        return available;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return latlng;
    }

    @Nullable
    @Override
    public String getTitle() {
        return siteName;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}
