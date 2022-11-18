package com.vbes.cluster;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vbe on 2021/8/17.
 */
public class ClusterMapPackage implements ReactPackage {
    //public ClusterMapModule clusterMapModule;
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        ClusterMapModule clusterMapModule = new ClusterMapModule(reactContext);
        List<NativeModule> modules = new ArrayList<>();
        modules.add(clusterMapModule);
        return modules;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        ClusterMapManager clusterMapManager = new ClusterMapManager(reactContext);
        return Collections.<ViewManager>singletonList(clusterMapManager);
    }
}
