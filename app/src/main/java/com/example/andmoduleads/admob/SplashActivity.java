package com.example.andmoduleads.admob;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.control.admob.Admob;
import com.ads.control.admob.AppOpenManager;
import com.ads.control.ads.TLAppAd;
import com.ads.control.ads.TLAdCallback;
import com.ads.control.ads.TLAdConfig;
import com.ads.control.ads.TLInitCallback;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.billing.AppPurchase;
import com.ads.control.funtion.AdCallback;
import com.ads.control.funtion.BillingListener;
import com.example.andmoduleads.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private List<String> list = new ArrayList<>();
    private String idAdSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (TLAppAd.getInstance().getMediationProvider() == TLAdConfig.PROVIDER_ADMOB)
            idAdSplash = getString(R.string.admod_interstitial_id);
        else
            idAdSplash = getString(R.string.applovin_test_inter);
        AppPurchase.getInstance().setBillingListener(new BillingListener() {
            @Override
            public void onInitBillingListener(int code) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        loadSplash();
                        loadAdmobAd();
                    }
                });
            }
        }, 5000);

        initBilling();
        AppPurchase.getInstance().setEventConsumePurchaseTest(findViewById(R.id.txtLoading));
    }

    private void initBilling() {
        List<String> listINAPId = new ArrayList<>();
        listINAPId.add(MainActivity.PRODUCT_ID);
        List<String> listSubsId = new ArrayList<>();

        AppPurchase.getInstance().initBilling(getApplication(), listINAPId, listSubsId);
//        AppPurchase.getInstance().addProductId(MainActivity.PRODUCT_ID);

    }

    TLAdCallback adCallback = new TLAdCallback() {
        @Override
        public void onAdFailedToLoad(@Nullable ApAdError adError) {
            super.onAdFailedToLoad(adError);
            startMain();
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Log.d(TAG, "onAdLoaded");
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            Log.d(TAG, "onAdClosed");
            startMain();

        }
    };

    private void loadSplash() {
        Log.d(TAG, "onCreate: show splash ads");
        TLAppAd.getInstance().setInitCallback(new TLInitCallback() {
            @Override
            public void initAdSuccess() {
                TLAppAd.getInstance().loadSplashInterstitialAds(SplashActivity.this, idAdSplash, 30000, 5000, true, adCallback);
            }
        });

    }


    private void loadAdmobAd() {
        AppOpenManager.getInstance().setSplashActivity(SplashActivity.class, getString(R.string.admod_app_open_ad_id), 30000);
        AppOpenManager.getInstance().setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                startMain();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                startMain();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }
        });
        AppOpenManager.getInstance().loadAndShowSplashAds(getString(R.string.admod_app_open_ad_id));
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TLAppAd.getInstance().onCheckShowSplashWhenFail(this, adCallback, 1000);
    }

    @Override
    protected void onDestroy() {
//        AppOpenManager.getInstance().removeFullScreenContentCallback();
        super.onDestroy();
    }
}