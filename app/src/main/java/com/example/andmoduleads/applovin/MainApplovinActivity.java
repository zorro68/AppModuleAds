package com.example.andmoduleads.applovin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ads.control.ads.TLAppAd;
import com.ads.control.ads.TLAdCallback;
import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.ads.wrapper.ApNativeAd;
import com.ads.control.ads.wrapper.ApRewardAd;
import com.example.andmoduleads.R;
import com.example.andmoduleads.admob.AdmobSimpleListActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

public class MainApplovinActivity extends AppCompatActivity {

    private FrameLayout frAds;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Button btnLoadReward;
    private ApRewardAd apRewardAd;
    ApInterstitialAd apInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_applovin);
        frAds = findViewById(R.id.fl_adplaceholder);
        shimmerFrameLayout = findViewById(R.id.shimmer_container_native);
        TLAppAd.getInstance().loadBanner(this, getString(R.string.admod_banner_id));
        apInterstitialAd =  TLAppAd.getInstance().getInterstitialAds(this, getString(R.string.admod_interstitial_id), new TLAdCallback(){

            @Override
            public void onInterstitialLoad(@Nullable ApInterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                Log.e("TAG", "AperoAd onInterstitialLoad: " + apInterstitialAd.isReady() );
            }
        });


        //load reward ad
        btnLoadReward = findViewById(R.id.btnLoadReward);
        btnLoadReward.setOnClickListener(view -> {
            startActivity(new Intent(MainApplovinActivity.this, AdmobSimpleListActivity.class));
//            if (apRewardAd != null && apRewardAd.isReady()) {
//                AperoAd.getInstance().forceShowRewardAd(this, apRewardAd, new AperoAdCallback(){
//                    @Override
//                    public void onAdLoaded() {
//                        Toast.makeText(MainApplovinActivity.this, "reward loaded", Toast.LENGTH_SHORT).show();
//                        btnLoadReward.setText("Show Reward");
//                    }
//
//                    @Override
//                    public void onAdClosed() {
//                        startActivity(new Intent(MainApplovinActivity.this, MaxSimpleListActivity.class));
//                    }
//                });
//            } else {
//                apRewardAd = AperoAd.getInstance().getRewardAd(this, getString(R.string.admod_app_reward_id), new AperoAdCallback() {
//                    @Override
//                    public void onAdLoaded() {
//                        Toast.makeText(MainApplovinActivity.this, "reward loaded", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onAdClosed() {
//
//                        startActivity(new Intent(MainApplovinActivity.this, MaxSimpleListActivity.class));
//                    }
//                });
//            }
        });
        //load interstitial
        findViewById(R.id.btnLoadInter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG", "AperoAd onInterstitialLoad: " + apInterstitialAd.isReady() );
                startActivity(new Intent(MainApplovinActivity.this, MaxSimpleListActivity.class));
//                if (apInterstitialAd.isReady()) {
//                    AperoAd.getInstance().forceShowInterstitial(MainApplovinActivity.this, apInterstitialAd, new AperoAdCallback() {
//                        @Override
//                        public void onAdClosed() {
//                            super.onAdClosed();
//                            startActivity(new Intent(MainApplovinActivity.this, ContentActivity.class));
//                        }
//                    }, true);
//                } else {
//                    Toast.makeText(MainApplovinActivity.this, "interstitial not loaded", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainApplovinActivity.this, ContentActivity.class));
//                }
            }
        });

        ShimmerFrameLayout shimmerFrameLayout =  findViewById(R.id.shimmer_container_native) ;
        FrameLayout flParentNative = findViewById(R.id.fl_adplaceholder);
        TLAppAd.getInstance().loadNativeAdResultCallback(this,getString(R.string.applovin_test_native), com.ads.control.R.layout.custom_native_max_small,new TLAdCallback(){
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                TLAppAd.getInstance().populateNativeAdView(MainApplovinActivity.this,nativeAd,flParentNative,shimmerFrameLayout);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        AppLovin.getInstance().loadNativeAd(
//                this,
//                shimmerFrameLayout,
//                frAds,
//                getString(R.string.applovin_test_native),
//                com.ads.control.R.layout.max_native_custom_ad_view
//        );
    }
}