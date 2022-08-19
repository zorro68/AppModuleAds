package com.ads.control.ads.nativeAds;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ads.control.R;
import com.ads.control.admob.Admob;
import com.ads.control.ads.wrapper.ApAdValue;
import com.ads.control.ads.wrapper.ApNativeAd;
import com.ads.control.funtion.AdCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TLAdPlacer {
    String TAG = "TLAdPlacer";
    private HashMap<Integer, ApNativeAd> listAd = new HashMap<>();
    private List<Integer> listPositionAd = new ArrayList<>();
    private TLAdPlacerSettings settings;
    private RecyclerView.Adapter adapterOriginal;
    private Activity activity;
    private int countLoadAd = 0;

    public TLAdPlacer(TLAdPlacerSettings settings, RecyclerView.Adapter adapterOriginal, Activity activity) {
        this.settings = settings;
        this.adapterOriginal = adapterOriginal;
        this.activity = activity;
        configData();

    }

    public void configData() {
        if (settings.isRepeatingAd()) {
            //calculator position add ad native to list
            int posAddAd = 0;
            int countNewAdapter = adapterOriginal.getItemCount();
            while (posAddAd <= countNewAdapter - settings.getPositionFixAd()) {
                posAddAd += settings.getPositionFixAd();
                listAd.put(posAddAd, new ApNativeAd(StatusNative.AD_INIT));
                Log.i(TAG, "add native to list pos: " + posAddAd);
                listPositionAd.add(posAddAd);
                posAddAd++;
                countNewAdapter++;
            }
        } else {
            listPositionAd.add(settings.getPositionFixAd());
            listAd.put(settings.getPositionFixAd(), new ApNativeAd(StatusNative.AD_INIT));
        }
    }

    public void renderAd(int pos, RecyclerView.ViewHolder holder) {

        if (listAd.get(pos).getAdmobNativeAd() == null && listAd.get(pos).getStatus() != StatusNative.AD_LOADING) {
            holder.itemView.post(() -> {
                ApNativeAd nativeAd = new ApNativeAd(StatusNative.AD_LOADING);
                listAd.put(pos, nativeAd);
                Admob.getInstance().loadNativeAd(activity, settings.getAdUnitId(), new AdCallback() {
                    @Override
                    public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                        super.onUnifiedNativeAdLoaded(unifiedNativeAd);
                        Log.i(TAG, "native ad in recycle loaded position: " + pos);
                        unifiedNativeAd.setOnPaidEventListener(new OnPaidEventListener() {
                            @Override
                            public void onPaidEvent(@NonNull AdValue adValue) {
                                TLAdPlacer.this.onAdRevenuePaid(new ApAdValue(adValue));
                            }
                        });

                        TLAdPlacer.this.onAdLoaded(pos);
                        NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(activity)
                                .inflate(settings.getLayoutCustomAd(), null);
                        FrameLayout adPlaceHolder = holder.itemView.findViewById(R.id.fl_adplaceholder);
                        ShimmerFrameLayout containerShimmer = holder.itemView.findViewById(R.id.shimmer_container_native);

                        nativeAd.setAdmobNativeAd(unifiedNativeAd);
                        nativeAd.setStatus(StatusNative.AD_LOADED);
                        listAd.put(pos, nativeAd);

                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);
                        adPlaceHolder.setVisibility(View.VISIBLE);
                        Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, nativeAdView);
                        adPlaceHolder.removeAllViews();
                        adPlaceHolder.addView(nativeAdView);
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        TLAdPlacer.this.onAdClicked();
                    }

                });
            });
        } else {
//            if (nativeAd.getStatus() == StatusNative.AD_LOADED) {
//                //ad native loaded not attach view
//                Log.i(TAG, "ad native render position: " + pos);
//                containerShimmer.stopShimmer();
//                containerShimmer.setVisibility(View.GONE);
//                adPlaceHolder.setVisibility(View.VISIBLE);
//                Admob.getInstance().populateUnifiedNativeAdView(nativeAd.getAdmobNativeAd(), nativeAdView);
//                adPlaceHolder.removeAllViews();
//                adPlaceHolder.addView(nativeAdView);
//
//                nativeAd.setStatus(StatusNative.AD_RENDER_SUCCESS);
//                listAd.put(pos, nativeAd);
//            }
        }
    }

    public void loadAds() {
        countLoadAd = 0;
        Admob.getInstance().loadNativeAds(activity, settings.getAdUnitId(), new AdCallback() {
            @Override
            public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                super.onUnifiedNativeAdLoaded(unifiedNativeAd);
                ApNativeAd nativeAd = new ApNativeAd(settings.getLayoutCustomAd(), unifiedNativeAd);
                nativeAd.setStatus(StatusNative.AD_LOADED);
                listAd.put(listPositionAd.get(countLoadAd), nativeAd);
                Log.i(TAG, "native ad in recycle loaded: " + countLoadAd);
                countLoadAd++;
            }
        }, Math.min(listAd.size(), settings.getPositionFixAd()));
    }

    public boolean isAdPosition(int pos) {
        ApNativeAd nativeAd = listAd.get(pos);
        return nativeAd != null;
    }

    public int getOriginalPosition(int posAdAdapter) {
        int countAd = 0;
        for (int i = 0; i < posAdAdapter; i++) {
            if (listAd.get(i) != null)
                countAd++;
        }
        return posAdAdapter - countAd;
    }

    public int getAdjustedCount() {
        int countMinAd;
        if (settings.isRepeatingAd()) {
            countMinAd = adapterOriginal.getItemCount() / settings.getPositionFixAd();
        } else {
            countMinAd = 1;
        }

        return adapterOriginal.getItemCount() + Math.min(countMinAd, listAd.size());
    }


    public void onAdLoaded(int position) {
        Log.i(TAG, "Ad native loaded in pos: " + position);
        if (settings.getListener() != null)
            settings.getListener().onAdLoaded(position);
    }

    public void onAdRemoved(int position) {
        Log.i(TAG, "Ad native removed in pos: " + position);
        if (settings.getListener() != null)
            settings.getListener().onAdRemoved(position);
    }

    public void onAdClicked() {
        Log.i(TAG, "Ad native clicked ");
        if (settings.getListener() != null)
            settings.getListener().onAdClicked();
    }

    public void onAdRevenuePaid(ApAdValue adValue) {
        Log.i(TAG, "Ad native revenue paid ");
        if (settings.getListener() != null)
            settings.getListener().onAdRevenuePaid(adValue);
    }


    public interface Listener {
        void onAdLoaded(int position);

        void onAdRemoved(int position);

        void onAdClicked();

        void onAdRevenuePaid(ApAdValue adValue);
    }
}
