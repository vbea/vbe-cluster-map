package com.vbes.cluster;

import android.view.View;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.gms.maps.GoogleMapOptions;
import com.vbes.cluster.module.StationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * Created by Vbe on 2021/8/17.
 */
public class ClusterMapManager extends ViewGroupManager<ClusterMapView> {

    public static final String REACT_CLASS = "VbeClusterMap";
    private final ReactApplicationContext appContext;

    protected GoogleMapOptions googleMapOptions;

    public ClusterMapManager(ReactApplicationContext context) {
        this.appContext = context;
        this.googleMapOptions = new GoogleMapOptions();
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected ClusterMapView createViewInstance(ThemedReactContext reactContext) {
        return new ClusterMapView(reactContext, appContext, this, googleMapOptions);
    }

    @Override
    public void updateExtraData(ClusterMapView view, Object extraData) {
        view.updateExtraData(extraData);
    }

    public void pushEvent(ThemedReactContext context, View view, String name, WritableMap data) {
        context.getJSModule(RCTEventEmitter.class).receiveEvent(view.getId(), name, data);
    }

    public void sendEvent(ThemedReactContext context, String eventName, @Nullable WritableMap params) {
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @ReactProp(name = "region")
    public void setRegion(ClusterMapView view, ReadableMap region) {
        view.setRegion(region);
    }

    @ReactProp(name = "data")
    public void setData(ClusterMapView view, ReadableArray data) {
        List<StationInfo> list = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                ReadableMap item = data.getMap(i);
                if (item != null) {
                    StationInfo stationInfo = new StationInfo();
                    stationInfo.setId(item.getInt("id"));
                    stationInfo.setAvailable(item.getBoolean("available"));
                    stationInfo.setSiteName(item.getString("name"));
                    stationInfo.setLatLng(item.getDouble("latitude"), item.getDouble("longitude"));
                    list.add(stationInfo);
                }
            }
        }
        view.addClusters(list);
    }

    @ReactProp(name="animation", defaultBoolean = true)
    public void setAnimation(ClusterMapView view, boolean animation) {
        view.setAnimation(animation);
    }

    @ReactProp(name="showUserLocation", defaultBoolean=false)
    public void setShowUserLocation(ClusterMapView view, boolean showUserLocation) {
        view.setShowUserLocation(showUserLocation);
    }

    @Override
    public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put("topMapReady", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMapReady")))
                .put("topMarkerPress", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMarkerPress")))
                .build();
    }
}
