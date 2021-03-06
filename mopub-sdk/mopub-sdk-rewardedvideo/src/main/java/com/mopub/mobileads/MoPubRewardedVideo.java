package com.mopub.mobileads;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.mopub.common.MoPubReward;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;

import java.util.Map;

/**
 * A custom event for showing MoPub rewarded videos.
 */
public class MoPubRewardedVideo extends MoPubRewardedAd {

    @NonNull private static final String MOPUB_REWARDED_VIDEO_ID = "mopub_rewarded_video_id";

    @NonNull private RewardedVastVideoInterstitial mRewardedVastVideoInterstitial;


    public MoPubRewardedVideo() {
        mRewardedVastVideoInterstitial = new RewardedVastVideoInterstitial();
    }

    @NonNull
    @Override
    protected String getAdNetworkId() {
        return MOPUB_REWARDED_VIDEO_ID;
    }

    @Override
    protected void onInvalidate() {
        mRewardedVastVideoInterstitial.onInvalidate();
        super.onInvalidate();
    }

    @Override
    protected void loadWithSdkInitialized(@NonNull final Activity activity,
                                          @NonNull final Map<String, Object> localExtras,
                                          @NonNull final Map<String, String> serverExtras) throws Exception {
        super.loadWithSdkInitialized(activity, localExtras, serverExtras);

        mRewardedVastVideoInterstitial.loadInterstitial(activity, new MoPubRewardedVideoListener(),
                localExtras, serverExtras);
    }

    @Override
    protected void show() {
        if (isReady()) {
            MoPubLog.d("Showing MoPub rewarded video.");
            mRewardedVastVideoInterstitial.showInterstitial();
        } else {
            MoPubLog.d("Unable to show MoPub rewarded video");
        }
    }

    private class MoPubRewardedVideoListener extends MoPubRewardedAdListener implements RewardedVastVideoInterstitial.RewardedVideoInterstitialListener {

        public MoPubRewardedVideoListener() {
            super(MoPubRewardedVideo.class);
        }


        @Override
        public void onVideoComplete() {
            if (getRewardedAdCurrencyName() == null) {
                MoPubLog.d("No rewarded video was loaded, so no reward is possible");
            } else {
                MoPubRewardedVideoManager.onRewardedVideoCompleted(mCustomEventClass,
                        getAdNetworkId(),
                        MoPubReward.success(getRewardedAdCurrencyName(),
                                getRewardedAdCurrencyAmount()));
            }
        }
    }

    @Deprecated
    @VisibleForTesting
    void setRewardedVastVideoInterstitial(
            @NonNull final RewardedVastVideoInterstitial rewardedVastVideoInterstitial) {
        mRewardedVastVideoInterstitial = rewardedVastVideoInterstitial;
    }
}