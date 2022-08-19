
# AndModuleAds

Import Module
~~~
	maven { url 'https://jitpack.io' }
	implementation 'com.github.AperoVN:AperoModuleAds:4.0.0'
~~~	 
# Summary
* [Setup AperoAd](#setup_aperoad)
	* [Setup id ads](#set_up_ads)
	* [Config ads](#config_ads)
	* [Ads Formats](#ads_formats)

* [Billing App](#billing_app)
* [Ads rule](#ads_rule)

# <a id="setup_aperoad"></a>Setup AperoAd
## <a id="set_up_ads"></a>Setup id ads for project
* Config variant test and release in gradle
* test: using id admob test while dev
* release: using exactly id admob,  build release (build file .aab)
~~~    
      productFlavors {
      test {
              manifestPlaceholders = [ ad_app_id:"AD_APP_ID_TEST" ]
              buildConfigField "String", "ads_inter_turn_on", "\"AD_ID_INTERSTIAL_TEST\""
              buildConfigField "String", "ads_inter_turn_off", "\"AD_ID_INTERSTIAL_TEST\""
	      buildConfigField "Boolean", "build_debug", "true"
           }
       release {
               manifestPlaceholders = [ ad_app_id:"AD_APP_ID" ]
               buildConfigField "String", "ads_inter_splash", "\"AD_ID_INTERSTIAL\""
               buildConfigField "String", "ads_inter_turn_on", "\"AD_ID_INTERSTIAL\""
	       buildConfigField "Boolean", "build_debug", "false"
           }
      }
~~~
AndroidManiafest.xml
~~~
  <meta-data
  	android:name="com.google.android.gms.ads.APPLICATION_ID"
  	android:value="@string/admob_app_id" />
~~~
## <a id="config_ads"></a>Config ads
Create class Application
~~~
class App : AdsMultiDexApplication(){
    @Override
    public void onCreate() {
        super.onCreate();
	...
	aperoAdConfig.setMediationProvider(AperoAdConfig.PROVIDER_ADMOB);
        aperoAdConfig.setVariant(BuildConfig.build_debug);
        aperoAdConfig.enableAdjust(ADJUST_TOKEN,EVENT_PURCHASE_ADJUST);
        aperoAdConfig.setIdAdResume(AppOpenManager.AD_UNIT_ID_TEST);
        listTestDevice.add(ID_TEST_DEVICE);
        aperoAdConfig.setListDeviceTest(listTestDevice);
	AperoAd.getInstance().init(this, aperoAdConfig, false);
	}
}
~~~
AndroidManiafest.xml
~~~
<application
android:name=".App"
...
>
~~~

## <a id="ads_formats"></a>Ads formats
### Ad Splash Interstitial
SplashActivity
~~~ 
    AperoAdCallback adCallback = new AperoAdCallback() {
        @Override
        public void onAdFailedToLoad(@Nullable ApAdError i) {
            super.onAdFailedToLoad(i);
            startMain();
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            startMain();

        }
    };
~~~
~~~
        AperoAd.getInstance().setInitCallback(new AperoInitCallback() {
            @Override
            public void initAdSuccess() {
                AperoAd.getInstance().loadSplashInterstitialAds(SplashActivity.this, idAdSplash, TIME_OUT, TIME_DELAY_SHOW_AD, true, adCallback);
            }
        });
~~~
### Interstitial
Load ad interstital before show
~~~
  private fun loadInterCreate() {
	ApInterstitialAd mInterstitialAd = AperoAd.getInstance().getInterstitialAds(this, idInter);
  }
~~~
Show and auto release ad interstitial
~~~
         if (mInterstitialAd.isReady()) {
                AperoAd.getInstance().forceShowInterstitial(this, mInterstitialAd, new AperoAdCallback() {
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
                }, true);
            } else {
                loadAdInterstitial();
            }
~~~
### Ad Banner
include layout banner
activity_main.xml
~~~
  <include
  android:id="@+id/include"
  layout="@layout/layout_banner_control"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:layout_alignParentBottom="true"
  app:layout_constraintBottom_toBottomOf="parent" />
~~~
load ad banner
~~~
  AperoAd.getInstance().loadBanner(this, idBanner);
~~~

### Ad Native
Load ad native before show
~~~
        AperoAd.getInstance().loadNativeAdResultCallback(this,ID_NATIVE_AD, com.ads.control.R.layout.custom_native_max_small,new AperoAdCallback(){
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
               //save or show native 
            }
        });
~~~
show ad native
~~~
	AperoAd.getInstance().populateNativeAdView(MainApplovinActivity.this,nativeAd,flParentNative,shimmerFrameLayout);
~~~
auto load and show native contains loading

activity_main.xml
~~~
  <include layout="@layout/layout_native_control" />
~~~
MainActivity
~~~
  AperoAd.getInstance().loadNativeAd(this, idNative, layoutNativeCustom);
~~~
Load Ad native for recyclerView
~~~~
	// ad native repeating interval
	AperoAdAdapter     adAdapter = AperoAd.getInstance().getNativeRepeatAdapter(this, idNative, layoutCustomNative, com.ads.control.R.layout.layout_native_medium,
                originalAdapter, listener, 4);
	
	// ad native fixed in position
    	AperoAdAdapter   adAdapter = AperoAd.getInstance().getNativeFixedPositionAdapter(this, idNative, layoutCustomNative, com.ads.control.R.layout.layout_native_medium,
                originalAdapter, listener, 4);
	
        recyclerView.setAdapter(adAdapter.getAdapter());
        adAdapter.loadAds();
~~~~
### Ad Reward
Get and show reward
~~~
  ApRewardAd rewardAd = AperoAd.getInstance().getRewardAd(this, idAdReward);

   if (rewardAd != null && rewardAd.isReady()) {
                AperoAd.getInstance().forceShowRewardAd(this, rewardAd, new AperoAdCallback());
            }
});
~~~
### Ad resume
App
~~~ 
  override fun onCreate() {
  	super.onCreate()
  	AppOpenManager.getInstance().enableAppResume()
	aperoAdConfig.setIdAdResume(AppOpenManager.AD_UNIT_ID_TEST);
	...
  }
	

~~~


# <a id="billing_app"></a>Billing app
## Init Billing
Application
~~~
    @Override
    public void onCreate() {
        super.onCreate();
        AppPurchase.getInstance().initBilling(this,listINAPId,listSubsId);
    }
~~~
## Check status billing init
~~~
 if (AppPurchase.getInstance().getInitBillingFinish()){
            loadAdsPlash();
        }else {
            AppPurchase.getInstance().setBillingListener(new BillingListener() {
                @Override
                public void onInitBillingListener(int code) {
                         loadAdsPlash();
                }
            },5000);
        }
~~~
## Check purchase status
    //check purchase with PRODUCT_ID
	 AppPurchase.getInstance().isPurchased(this,PRODUCT_ID);
	 //check purchase all
	 AppPurchase.getInstance().isPurchased(this);
##  purchase
	 AppPurchase.getInstance().purchase(this,PRODUCT_ID);
	 AppPurchase.getInstance().subscribe(this,SUBS_ID);
## Purchase Listener
	         AppPurchase.getInstance().setPurchaseListioner(new PurchaseListioner() {
                 @Override
                 public void onProductPurchased(String productId,String transactionDetails) {

                 }

                 @Override
                 public void displayErrorMessage(String errorMsg) {

                 }
             });

## Consume purchase
	  AppPurchase.getInstance().consumePurchase(PRODUCT_ID);
## Get price
	  AppPurchase.getInstance().getPrice(PRODUCT_ID)
	  AppPurchase.getInstance().getPriceSub(SUBS_ID)
### Show iap dialog
	InAppDialog dialog = new InAppDialog(this);
	dialog.setCallback(() -> {
	     AppPurchase.getInstance().purchase(this,PRODUCT_ID);
	    dialog.dismiss();
	});
	dialog.show();



# <a id="ads_rule"></a>Ads rule
## Always add device test to idTestList with all of your team's device
To ignore invalid ads traffic
https://support.google.com/adsense/answer/16737?hl=en
## Before show full-screen ad (interstitial, app open ad), alway show a short loading dialog
To ignore accident click from user. This feature is existed in library
## Never reload ad on onAdFailedToLoad
To ignore infinite loop
