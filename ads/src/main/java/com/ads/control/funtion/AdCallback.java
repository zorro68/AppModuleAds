package com.ads.control.funtion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardedAd;

public class AdCallback {


    public void onAdClosed() {
    }

    // event AD closed when setOpenActivityAfterShowInterAds = true
    public void onAdClosedByUser() {
    }


    public void onAdFailedToLoad(@Nullable LoadAdError i) {
    }

    public void onAdFailedToShow(@Nullable AdError adError) {
    }

    public void onAdLeftApplication() {
    }


    public void onAdLoaded() {
    }
    public void onAdSplashReady() {
    }

    public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {

    }

    public void onAdClicked() {
    }

    public void onAdImpression() {
    }

    public void onRewardAdLoaded(RewardedAd rewardedAd) {
    }


    public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {

    }
}
