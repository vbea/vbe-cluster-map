package com.vbes.cluster;

import android.annotation.SuppressLint;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

/**
 * Created by Vbe on 2021/8/17.
 */
public class ClusterMapModule extends ReactContextBaseJavaModule {
    public static final String NAME="VbeClusterModule";
    @SuppressLint("StaticFieldLeak")
    private static ReactApplicationContext reactContext;


    public ClusterMapModule(ReactApplicationContext context) {
        super(reactContext);
        reactContext = context;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
