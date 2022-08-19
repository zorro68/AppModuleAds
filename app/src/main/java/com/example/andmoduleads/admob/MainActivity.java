package com.example.andmoduleads.admob;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.control.admob.AppOpenManager;
import com.ads.control.ads.TLAppAd;
import com.ads.control.ads.TLAdCallback;
import com.ads.control.ads.TLAdConfig;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.ads.wrapper.ApRewardAd;
import com.ads.control.admob.Admob;
import com.ads.control.billing.AppPurchase;
import com.ads.control.funtion.DialogExitListener;
import com.ads.control.dialog.DialogExitApp1;
import com.ads.control.dialog.InAppDialog;
import com.ads.control.funtion.AdCallback;
import com.ads.control.funtion.PurchaseListioner;
import com.example.andmoduleads.R;
import com.example.andmoduleads.applovin.MaxSimpleListActivity;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.nativead.NativeAd;

public class MainActivity extends AppCompatActivity {
    public static final String PRODUCT_ID = "android.test.purchased";
    private static final String TAG = "MAIN_TEST";
    //adjust
    private static final String EVENT_TOKEN_SIMPLE = "g3mfiw";
    private static final String EVENT_TOKEN_REVENUE = "a4fd35";


    private FrameLayout frAds;
    private NativeAd unifiedNativeAd;
    private ApInterstitialAd mInterstitialAd;
    private ApRewardAd rewardAd;

    private boolean isShowDialogExit = false;

    private String idBanner = "";
    private String idNative = "";
    private String idInter = "";

    private int layoutNativeCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frAds = findViewById(R.id.fl_adplaceholder);
        configMediationProvider();
        TLAppAd.getInstance().setCountClickToShowAds(3);
        AppOpenManager.getInstance().setEnableScreenContentCallback(true);
        AppOpenManager.getInstance().setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                Log.e("AppOpenManager", "onAdShowedFullScreenContent: ");

            }
        });

        TLAppAd.getInstance().loadNativeAd(this, idNative, layoutNativeCustom);

        AppPurchase.getInstance().setPurchaseListener(new PurchaseListioner() {
            @Override
            public void onProductPurchased(String productId, String transactionDetails) {
                Log.e("PurchaseListener", "ProductPurchased:" + productId);
                Log.e("PurchaseListener", "transactionDetails:" + transactionDetails);
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void displayErrorMessage(String errorMsg) {
                Log.e("PurchaseListener", "displayErrorMessage:" + errorMsg);
            }

            @Override
            public void onUserCancelBilling() {

            }
        });

        TLAppAd.getInstance().loadBanner(this, idBanner);

        loadAdInterstitial();


        findViewById(R.id.btShowAds).setOnClickListener(v -> {
            if (mInterstitialAd.isReady()) {
                TLAppAd.getInstance().showInterstitialAdByTimes(this, mInterstitialAd, new TLAdCallback() {
                    @Override
                    public void onAdClosed() {
                        Log.i(TAG, "onAdClosed: start content and finish main");
                        startActivity(new Intent(MainActivity.this, ContentActivity.class));
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable ApAdError adError) {
                        super.onAdFailedToShow(adError);
                        Log.i(TAG, "onAdFailedToShow:" + adError.getMessage());
                        startActivity(new Intent(MainActivity.this, ContentActivity.class));
                    }
                }, true);
            } else {
                Toast.makeText(this, "start loading ads", Toast.LENGTH_SHORT).show();
                loadAdInterstitial();
            }
        });

        findViewById(R.id.btForceShowAds).setOnClickListener(v -> {
            if (mInterstitialAd.isReady()) {
                TLAppAd.getInstance().forceShowInterstitial(this, mInterstitialAd, new TLAdCallback() {
                    @Override
                    public void onAdClosed() {
                        Log.i(TAG, "onAdClosed: start content and finish main");
                        startActivity(new Intent(MainActivity.this, MaxSimpleListActivity.class));
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable ApAdError adError) {
                        super.onAdFailedToShow(adError);
                        Log.i(TAG, "onAdFailedToShow:" + adError.getMessage());
                        startActivity(new Intent(MainActivity.this, MaxSimpleListActivity.class));
                    }
                }, false);
            } else {
                loadAdInterstitial();
            }

        });

        findViewById(R.id.btnShowReward).setOnClickListener(v -> {
            if (rewardAd != null && rewardAd.isReady()) {
                TLAppAd.getInstance().forceShowRewardAd(this, rewardAd, new TLAdCallback());
                return;
            }
            rewardAd = TLAppAd.getInstance().getRewardAd(this, getString(R.string.admod_app_reward_id));
        });


        findViewById(R.id.btIap).setOnClickListener(v -> {
            InAppDialog dialog = new InAppDialog(this);
            dialog.setCallback(() -> {
                AppPurchase.getInstance().purchase(this, PRODUCT_ID);
                dialog.dismiss();

            });
            dialog.show();
        });

    }

    private void configMediationProvider() {
        if (TLAppAd.getInstance().getMediationProvider() == TLAdConfig.PROVIDER_ADMOB) {
            idBanner = getString(R.string.admod_banner_id);
            idNative = getString(R.string.admod_native_id);
            idInter = getString(R.string.admod_interstitial_id);
            layoutNativeCustom = com.ads.control.R.layout.custom_native_admod_medium;
        }
//        else {
//            idBanner = getString(R.string.applovin_test_banner);
//            idNative = getString(R.string.applovin_test_native);
//            idInter = getString(R.string.applovin_test_inter);
//            layoutNativeCustom = com.ads.control.R.layout.custom_native_max_medium;
//        }*/
    }

    private void loadAdInterstitial() {

        mInterstitialAd = TLAppAd.getInstance().getInterstitialAds(this, idInter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNativeExit();
    }

    private void loadNativeExit() {
        if (unifiedNativeAd != null)
            return;
        Admob.getInstance().loadNativeAd(this, getString(R.string.admod_native_id), new AdCallback() {
            @Override
            public void onUnifiedNativeAdLoaded(NativeAd unifiedNativeAd) {
                MainActivity.this.unifiedNativeAd = unifiedNativeAd;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (unifiedNativeAd == null)
            return;

        DialogExitApp1 dialogExitApp1 = new DialogExitApp1(this, unifiedNativeAd, 1);
        dialogExitApp1.setDialogExitListener(new DialogExitListener() {
            @Override
            public void onExit(boolean exit) {
                MainActivity.super.onBackPressed();
            }
        });
        dialogExitApp1.setCancelable(false);
        dialogExitApp1.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        AppPurchase.getInstance().handleActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "ProductPurchased:" + data.toString());
        if (AppPurchase.getInstance().isPurchased(this)) {
            findViewById(R.id.btIap).setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}