package com.ads.control.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.applovin.mediation.MaxAd;
import com.google.android.gms.ads.AdValue;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsUtil {
    private static final String TAG = "FirebaseAnalyticsUtil";

    public static void logPaidAdImpression(Context context, AdValue adValue, String adUnitId, String mediationAdapterClassName) {
        Log.d(TAG, String.format(
                "Paid event of value %d microcents in currency %s of precision %s%n occurred for ad unit %s from ad network %s.",
                adValue.getValueMicros(),
                adValue.getCurrencyCode(),
                adValue.getPrecisionType(),
                adUnitId,
                mediationAdapterClassName));

        Bundle params = new Bundle(); // Log ad value in micros.
        params.putLong("valuemicros", adValue.getValueMicros());
        // These values below won’t be used in ROAS recipe.
        // But log for purposes of debugging and future reference.
        params.putString("currency", adValue.getCurrencyCode());
        params.putInt("precision", adValue.getPrecisionType());
        params.putString("adunitid", adUnitId);
        params.putString("network", mediationAdapterClassName);

        FirebaseAnalytics.getInstance(context).logEvent("paid_ad_impression", params);
    }

    public static void logPaidAdImpression(Context context, MaxAd adValue ) {
        Log.d(TAG, "Paid event of value :" + adValue.getRevenue() +
                        "  microcents in currency  for ad unit :"+adValue.getAdUnitId()+ " from ad network  " +  adValue.getNetworkName());

        Bundle params = new Bundle(); // Log ad value in micros.
        params.putDouble("valuemicros", adValue.getRevenue());
        // These values below won’t be used in ROAS recipe.
        // But log for purposes of debugging and future reference.
        params.putString("adunitid",  adValue.getAdUnitId());
        params.putString("network", adValue.getNetworkName());

        FirebaseAnalytics.getInstance(context).logEvent("paid_ad_impression", params);
    }

    public static void logClickAdsEvent(Context context, String adUnitId) {
        Log.d(TAG, String.format(
                "User click ad for ad unit %s.",
                adUnitId));
        Bundle bundle = new Bundle();
        bundle.putString("ad_unit_id", adUnitId);
        FirebaseAnalytics.getInstance(context).logEvent("event_user_click_ads", bundle);
    }
}
