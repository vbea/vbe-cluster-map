package com.vbes.cluster.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.vbes.cluster.R;
import com.vbes.cluster.module.StationInfo;

import androidx.annotation.NonNull;

/**
 * Created by Vbe on 2021/8/17.
 */
public class MyClusterRender extends DefaultClusterRenderer<StationInfo> {
    private final ImageView mImageView;
    private final IconGenerator mIconGenerator, mClusterIconAvailable, mClusterIconUnavailable;

    public MyClusterRender(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        mIconGenerator = new IconGenerator(context);
        mClusterIconAvailable = new IconGenerator(context);
        mClusterIconUnavailable = new IconGenerator(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        //marker
        View markerView = inflater.inflate(R.layout.marker_site, null);
        mImageView = markerView.findViewById(R.id.image);
        mIconGenerator.setBackground(null);
        mIconGenerator.setContentView(markerView);
        //cluster marker
        View clusterView = inflater.inflate(R.layout.marker_cluster, null);
        mClusterIconAvailable.setBackground(null);
        mClusterIconAvailable.setContentView(clusterView);

        View clusterViewUn = inflater.inflate(R.layout.marker_cluster_unavailable, null);
        mClusterIconUnavailable.setBackground(null);
        mClusterIconUnavailable.setContentView(clusterViewUn);
        setMinClusterSize(2);
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull StationInfo station, MarkerOptions markerOptions) {
        // Draw a single person - show their profile photo and set the info window to show their name
        markerOptions.icon(getItemIcon(station)).title(station.getTitle());
    }

    @Override
    protected void onBeforeClusterRendered(@NonNull Cluster<StationInfo> cluster, MarkerOptions markerOptions) {
        // Draw multiple people.
        // 注意：此方法在UI线程上运行。不要在这里花费太多时间（如本例中）。
        markerOptions.icon(getClusterIcon(cluster));
    }

    @Override
    protected void onClusterUpdated(@NonNull Cluster<StationInfo> cluster, Marker marker) {
        // 与onBeforeClusterRendered（）相同的实现（用于更新缓存标记）
        marker.setIcon(getClusterIcon(cluster));
    }

    @Override
    protected void onClusterItemUpdated(@NonNull StationInfo station, Marker marker) {
        // Same implementation as onBeforeClusterItemRendered() (to update cached markers)
        marker.setIcon(getItemIcon(station));
        marker.setTitle(station.getTitle());
    }

    private BitmapDescriptor getItemIcon(StationInfo station) {
        mImageView.setImageResource(station.isAvailable() ? R.drawable.ic_marker : R.drawable.ic_marker_un);
        Bitmap icon = mIconGenerator.makeIcon();
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    private BitmapDescriptor getClusterIcon(Cluster<StationInfo> cluster) {
        boolean available = false;
        for (StationInfo p : cluster.getItems()) {
            if (p.isAvailable()) {
                available = true;
                break;
            }
        }
        Bitmap icon = null;
        if (available) {
            icon = mClusterIconAvailable.makeIcon(String.valueOf(cluster.getSize()));
            //mClusterIconAvailable.setTextAppearance(available ? R.style.TextAvailable : R.style.TextUnavailable);
        } else {
            icon = mClusterIconUnavailable.makeIcon(String.valueOf(cluster.getSize()));
        }
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

//    @Override
//    protected boolean shouldRenderAsCluster(Cluster cluster) {
//        // Always render clusters.
//        return cluster.getSize() > 1;
//    }
}
