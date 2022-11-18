package com.vbes.cluster;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.vbes.cluster.module.Region;
import com.vbes.cluster.module.StationInfo;
import com.vbes.cluster.render.MyClusterRender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vbe on 2021/8/17.
 */
public class ClusterMapView extends MapView implements OnMapReadyCallback {

    public GoogleMap map;
    private final ClusterMapManager manager;
    private final ThemedReactContext context;
    private ClusterManager<StationInfo> mClusterManager;
    private final Map<Marker, StationInfo> markerMap = new HashMap<>();

    private boolean clusterAnimation = true;
    private boolean moveOnMarkerPress = true;
    private boolean showUserLocation = false;
    //LocationManager locationManager;
    private Region initRegion;
    private LatLngBounds boundsToMove;

    @SuppressLint("MissingPermission")
    public ClusterMapView(ThemedReactContext reactContext, ReactApplicationContext appContext, ClusterMapManager uiManager, GoogleMapOptions googleMapOptions) {
        super(getNonBuggyContext(reactContext, appContext), googleMapOptions);
        this.manager = uiManager;
        this.context = reactContext;

        super.onCreate(null);
        super.onResume();
        super.getMapAsync(this);
        //locationManager = (LocationManager)appContext.getSystemService(Context.LOCATION_SERVICE);
        //String bestProvider = locationManager.getBestProvider(null,true);

        /*this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!paused) {
                    ClusterMapView.this.cacheView();
                }
            }
        });*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        manager.pushEvent(context, this, "topMapReady", new WritableNativeMap());

        mClusterManager = new ClusterManager<>(context, map);
        mClusterManager.setRenderer(new MyClusterRender(context, map, mClusterManager));
        mClusterManager.setAnimation(clusterAnimation);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<StationInfo>() {
            @Override
            public boolean onClusterClick(Cluster<StationInfo> cluster) {
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (ClusterItem item : cluster.getItems()) {
                    builder.include(item.getPosition());
                }
                final LatLngBounds bounds = builder.build();
                try {
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<StationInfo>() {
            @Override
            public boolean onClusterItemClick(StationInfo stationInfo) {
                WritableMap event;
                event = makeClickEventData(stationInfo.getPosition());
                event.putString("action", "marker-press");
                event.putInt("id", stationInfo.getId());
                manager.pushEvent(context, ClusterMapView.this, "topMarkerPress", event);
                return false;
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                /*WritableMap event;
                StationInfo stationInfo = getMarkerMap(marker);

                event = makeClickEventData(marker.getPosition());
                event.putString("action", "marker-press");
                event.putInt("id", stationInfo.getId());
                manager.pushEvent(context, ClusterMapView.this, "onMarkerPress", event);*/
                // Return false to open the callout info window and center on the marker
                // https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap
                // .OnMarkerClickListener
                if (moveOnMarkerPress) {
                    return false;
                } else {
                    marker.showInfoWindow();
                    return true;
                }
            }
        });
        map.setOnCameraIdleListener(mClusterManager);
        if (initRegion != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(initRegion.getLocation(), initRegion.getZoom()));
        }
        if (showUserLocation) {
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                map.setMyLocationEnabled(true);
            }
        }
    }

    public boolean hasPermission(String p) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void setAnimation(boolean animation) {
        clusterAnimation = animation;
        if (mClusterManager != null) {
            mClusterManager.setAnimation(animation);//是否启用动画
        }
    }

    public void setShowUserLocation(boolean show) {
        showUserLocation = show;
    }

    public void setRegion(ReadableMap region) {
        if (region == null) return;
        initRegion = new Region(region);
        if (map == null) return;

        LatLngBounds bounds = new LatLngBounds(
            new LatLng(initRegion.getLatitude() - initRegion.getLatitudeDelta() / 2, initRegion.getLongitude() - initRegion.getLongitudeDelta() / 2), // southwest
            new LatLng(initRegion.getLatitude() + initRegion.getLatitudeDelta() / 2, initRegion.getLongitude() + initRegion.getLongitudeDelta() / 2)  // northeast
        );
        if (super.getHeight() <= 0 || super.getWidth() <= 0) {
            // in this case, our map has not been laid out yet, so we save the bounds in a local
            // variable, and make a guess of zoomLevel 10. Not to worry, though: as soon as layout
            // occurs, we will move the camera to the saved bounds. Note that if we tried to move
            // to the bounds now, it would trigger an exception.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(initRegion.getLocation(), initRegion.getZoom()));
            boundsToMove = bounds;
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
            boundsToMove = null;
        }
        initRegion = null;
    }

    public void addClusters(List<StationInfo> items) {
        if (mClusterManager != null) {
            mClusterManager.clearItems();
            mClusterManager.addItems(items);
            mClusterManager.cluster();
        }
    }

    private static boolean contextHasBug(Context context) {
        return context == null ||
                context.getResources() == null ||
                context.getResources().getConfiguration() == null;
    }

    private static Context getNonBuggyContext(ThemedReactContext reactContext, ReactApplicationContext appContext) {
        Context superContext = reactContext;
        if (!contextHasBug(appContext.getCurrentActivity())) {
            superContext = appContext.getCurrentActivity();
        } else if (contextHasBug(superContext)) {
            // we have the bug! let's try to find a better context to use
            if (!contextHasBug(reactContext.getCurrentActivity())) {
                superContext = reactContext.getCurrentActivity();
            } else if (!contextHasBug(reactContext.getApplicationContext())) {
                superContext = reactContext.getApplicationContext();
            } else {
                // ¯\_(ツ)_/¯
            }
        }
        return superContext;
    }

    public WritableMap makeClickEventData(LatLng point) {
        WritableMap event = new WritableNativeMap();

        WritableMap coordinate = new WritableNativeMap();
        coordinate.putDouble("latitude", point.latitude);
        coordinate.putDouble("longitude", point.longitude);
        event.putMap("coordinate", coordinate);

        Projection projection = map.getProjection();
        Point screenPoint = projection.toScreenLocation(point);

        WritableMap position = new WritableNativeMap();
        position.putDouble("x", screenPoint.x);
        position.putDouble("y", screenPoint.y);
        event.putMap("position", position);

        return event;
    }

    private StationInfo getMarkerMap(Marker marker) {
        StationInfo stationInfo = markerMap.get(marker);

        if (stationInfo != null) {
            return stationInfo;
        }

        for (Map.Entry<Marker, StationInfo> entryMarker : markerMap.entrySet()) {
            if (entryMarker.getKey().getPosition().equals(marker.getPosition())  && entryMarker.getKey().getTitle().equals(marker.getTitle())) {
                stationInfo = entryMarker.getValue();
                break;
            }
        }

        return stationInfo;
    }

    public void updateExtraData(Object extraData) {
        // if boundsToMove is not null, we now have the MapView's width/height, so we can apply
        // a proper camera move
        if (boundsToMove != null) {
            HashMap<String, Float> data = (HashMap<String, Float>) extraData;
            int width = data.get("width") == null ? 0 : data.get("width").intValue();
            int height = data.get("height") == null ? 0 : data.get("height").intValue();

            //fix for https://github.com/react-native-community/react-native-maps/issues/245,
            //it's not guaranteed the passed-in height and width would be greater than 0.
            if (width <= 0 || height <= 0) {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsToMove, 0));
            } else {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsToMove, width, height, 0));
            }

            boundsToMove = null;
        }
    }
}
