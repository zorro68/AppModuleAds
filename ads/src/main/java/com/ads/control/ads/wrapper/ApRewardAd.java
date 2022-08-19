package com.ads.control.ads.wrapper;

import com.applovin.mediation.ads.MaxRewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAd;

public class ApRewardAd {
    private RewardedAd admobReward;
    private MaxRewardedAd maxReward;

    public ApRewardAd() {
    }

    public void setAdmobReward(RewardedAd admobReward) {
        this.admobReward = admobReward;
    }

    public void setMaxReward(MaxRewardedAd maxReward) {
        this.maxReward = maxReward;
    }

    public ApRewardAd(MaxRewardedAd maxReward) {
        this.maxReward = maxReward;
    }

    public ApRewardAd(RewardedAd admobReward) {
        this.admobReward = admobReward;
    }

    public RewardedAd getAdmobReward() {
        return admobReward;
    }

    public MaxRewardedAd getMaxReward() {
        return maxReward;
    }

    /**
     * Clean reward when shown
     */
    public void clean(){
        maxReward = null;
        admobReward = null;
    }

    public boolean isReady(){
        return admobReward != null || maxReward !=null && maxReward.isReady();
    }
}
