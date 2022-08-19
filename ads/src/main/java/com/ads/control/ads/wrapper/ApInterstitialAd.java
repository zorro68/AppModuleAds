package com.ads.control.ads.wrapper;

import com.applovin.mediation.ads.MaxInterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class ApInterstitialAd {
    private InterstitialAd interstitialAd;
    private MaxInterstitialAd maxInterstitialAd;

    public ApInterstitialAd() {
    }

    public ApInterstitialAd(MaxInterstitialAd maxInterstitialAd) {
        this.maxInterstitialAd = maxInterstitialAd;
    }

    public ApInterstitialAd(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
    }


    public void setInterstitialAd(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
    }

    public void setMaxInterstitialAd(MaxInterstitialAd maxInterstitialAd) {
        this.maxInterstitialAd = maxInterstitialAd;
    }

    public Boolean isReady(){
        if (maxInterstitialAd!=null && maxInterstitialAd.isReady())
            return true;
        return interstitialAd != null;
    }

    public Boolean isNotReady(){
        return !isReady();
    }

    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public MaxInterstitialAd getMaxInterstitialAd() {
        return maxInterstitialAd;
    }
}
