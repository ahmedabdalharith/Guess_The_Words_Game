package com.pyramid.questions.data.remote

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdMobManager(private val context: Context) {

    companion object {
//        // Test Ad Unit IDs - Replace with your real Ad Unit IDs
//        private const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
//        private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
//        private const val BANNER_AD_UNIT_ID = "ca-app-pub-5100084329334269/5211775092"


        private const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
        private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        private const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

        private const val TAG = "AdMobManager"
    }

    private var rewardedAd: RewardedAd? = null
    private var interstitialAd: InterstitialAd? = null

    init {
        // Initialize AdMob
        MobileAds.initialize(context) {
            Log.d(TAG, "AdMob initialized successfully")
        }
        loadRewardedAd()
        loadInterstitialAd()
    }

    // Load Rewarded Ad (for video watching rewards)
    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, REWARDED_AD_UNIT_ID, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e(TAG, "Rewarded ad failed to load: ${adError.message}")
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d(TAG, "Rewarded ad loaded successfully")
                rewardedAd = ad
            }
        })
    }

    // Show Rewarded Ad
    fun showRewardedAd(activity: Activity, onRewardEarned: (Int) -> Unit) {
        rewardedAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    Log.d(TAG, "Rewarded ad was clicked")
                }

                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Rewarded ad dismissed")
                    rewardedAd = null
                    loadRewardedAd() // Load next ad
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG, "Rewarded ad failed to show: ${adError.message}")
                    rewardedAd = null
                }

                override fun onAdImpression() {
                    Log.d(TAG, "Rewarded ad recorded an impression")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Rewarded ad showed fullscreen content")
                }
            }

            ad.show(activity) { rewardItem ->
                // User earned reward - give them coins
                val rewardAmount = rewardItem.amount
                Log.d(TAG, "User earned reward: $rewardAmount")
                onRewardEarned(25) // Give 25 coins as mentioned in your dialog
            }
        } ?: run {
            Log.w(TAG, "Rewarded ad is not ready yet")
            // Optionally show a message to user that ad is not ready
        }
    }

    // Load Interstitial Ad
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, BANNER_AD_UNIT_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e(TAG, "Interstitial ad failed to load: ${adError.message}")
                interstitialAd = null
            }

            override fun onAdLoaded(ad: InterstitialAd) {
                Log.d(TAG, "Interstitial ad loaded successfully")
                interstitialAd = ad
            }
        })
    }

    // Show Interstitial Ad
    fun showInterstitialAd(activity: Activity, onAdClosed: () -> Unit = {}) {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Interstitial ad dismissed")
                    interstitialAd = null
                    loadInterstitialAd() // Load next ad
                    onAdClosed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG, "Interstitial ad failed to show: ${adError.message}")
                    interstitialAd = null
                    onAdClosed()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Interstitial ad showed fullscreen content")
                }
            }
            ad.show(activity)
        } ?: run {
            Log.w(TAG, "Interstitial ad is not ready yet")
            onAdClosed()
        }
    }

    // Check if rewarded ad is loaded
    fun isRewardedAdLoaded(): Boolean = rewardedAd != null

    // Check if interstitial ad is loaded
    fun isInterstitialAdLoaded(): Boolean = interstitialAd != null
}