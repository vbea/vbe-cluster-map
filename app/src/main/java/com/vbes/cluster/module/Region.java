package com.vbes.cluster.module;

import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Vbe on 2021/8/17.
 */
public class Region {
  private Double latitude;
  private Double longitude;
  private Double longitudeDelta;
  private Double latitudeDelta;
  private LatLng location;
  private Float zoom;

  public Region(ReadableMap map) {
    latitude = map.getDouble("latitude");
    longitude = map.getDouble("longitude");
    latitudeDelta = map.getDouble("latitudeDelta");
    longitudeDelta = map.getDouble("longitudeDelta");
    if (map.hasKey("zoom")) {
      Double z = map.getDouble("zoom");
      zoom = Float.valueOf(z.toString());
    }
    location = new LatLng(latitude, longitude);
  }

  public LatLng getLocation() {
    return location;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public Double getLongitudeDelta() {
    return longitudeDelta;
  }

  public Double getLatitudeDelta() {
    return latitudeDelta;
  }

  public Float getZoom() {
    if (zoom != null)
      return zoom;
    else
      return 10f;
  }
}