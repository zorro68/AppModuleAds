package com.ads.control.ads.wrapper;

import android.view.View;

import com.ads.control.ads.nativeAds.StatusNative;
import com.google.android.gms.ads.nativead.NativeAd;

public class ApNativeAd {
    private int layoutCustomNative;
    private View nativeView;
    private NativeAd admobNativeAd;
    private StatusNative status ;


    public ApNativeAd(StatusNative status) {
        this.status = status;
    }



    public ApNativeAd(int layoutCustomNative, View nativeView) {
        this.layoutCustomNative = layoutCustomNative;
        this.nativeView = nativeView;
    }

    public ApNativeAd(int layoutCustomNative, NativeAd admobNativeAd) {
        this.layoutCustomNative = layoutCustomNative;
        this.admobNativeAd = admobNativeAd;
    }

    public NativeAd getAdmobNativeAd() {
        return admobNativeAd;
    }

    public void setAdmobNativeAd(NativeAd admobNativeAd) {
        this.admobNativeAd = admobNativeAd;
    }

    public ApNativeAd() {
    }

    public StatusNative getStatus() {
        return status;
    }

    public void setStatus(StatusNative status) {
        this.status = status;
    }

    public int getLayoutCustomNative() {
        return layoutCustomNative;
    }

    public void setLayoutCustomNative(int layoutCustomNative) {
        this.layoutCustomNative = layoutCustomNative;
    }

    public View getNativeView() {
        return nativeView;
    }

    public void setNativeView(View nativeView) {
        this.nativeView = nativeView;
    }


}
