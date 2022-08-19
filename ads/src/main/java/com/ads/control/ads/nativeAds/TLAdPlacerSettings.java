package com.ads.control.ads.nativeAds;

public class TLAdPlacerSettings {

    private String adUnitId;
    private int positionFixAd = -1;
    private boolean isRepeatingAd = false;
    private int layoutCustomAd = -1;
    private int layoutAdPlaceHolder = -1;
    private TLAdPlacer.Listener listener;
    public TLAdPlacerSettings(String adUnitId, int layoutCustomAd, int layoutPlaceHolderAd) {
        this.adUnitId = adUnitId;
        this.layoutCustomAd = layoutCustomAd;
        this.layoutAdPlaceHolder = layoutPlaceHolderAd;
    }
    public TLAdPlacerSettings(int layoutCustomAd, int layoutPlaceHolderAd) {
        this.adUnitId = adUnitId;
        this.layoutCustomAd = layoutCustomAd;
        this.layoutAdPlaceHolder = layoutPlaceHolderAd;
    }

    public void setFixedPosition(int positionAd){
        positionFixAd = positionAd;
        isRepeatingAd = false;
    }

    public TLAdPlacer.Listener getListener() {
        return listener;
    }

    public void setListener(TLAdPlacer.Listener listener) {
        this.listener = listener;
    }



    public int getLayoutCustomAd() {
        return layoutCustomAd;
    }

    public int getLayoutAdPlaceHolder() {
        return layoutAdPlaceHolder;
    }

    public void setLayoutAdPlaceHolder(int layoutAdPlaceHolder) {
        this.layoutAdPlaceHolder = layoutAdPlaceHolder;
    }

    public void setLayoutCustomAd(int layoutCustomAd) {
        this.layoutCustomAd = layoutCustomAd;
    }

    public void setRepeatingInterval(int positionAd){
        positionFixAd = positionAd-1;
        isRepeatingAd = true;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public int getPositionFixAd() {
        return positionFixAd;
    }


    public boolean isRepeatingAd() {
        return isRepeatingAd;
    }
}
