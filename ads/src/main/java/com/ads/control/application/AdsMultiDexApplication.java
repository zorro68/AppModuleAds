package com.ads.control.application;

import androidx.multidex.MultiDexApplication;

import com.ads.control.ads.TLAdConfig;

import java.util.ArrayList;
import java.util.List;

public abstract class AdsMultiDexApplication extends MultiDexApplication {
    public static String TAG = "AdjustTL";
    protected TLAdConfig adConfig;
    protected List<String> listTestDevice ;
    @Override
    public void onCreate() {
        super.onCreate();
        listTestDevice = new ArrayList<String>();
        adConfig = new TLAdConfig();
        adConfig.setApplication(this);

    }


}
