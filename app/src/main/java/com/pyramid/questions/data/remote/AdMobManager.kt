package com.pyramid.questions.data.remote

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdMobManager private constructor(private val context: Context) {

    companion object {
        private const val TAG = "AdMobManager"

        const val BANNER_AD_ID = "ca-app-pub-5100084329334269/6632683886"
        const val VIDEO_AD_ID = "ca-app-pub-5100084329334269/5211775092"
        const val INTERSTITIAL_AD_ID = "ca-app-pub-5100084329334269~1234567890"
        const val REWARDED_INTERSTITIAL_AD_ID = "ca-app-pub-5100084329334269/1234567891"

        @Volatile
        private var INSTANCE: AdMobManager? = null

        fun getInstance(context: Context): AdMobManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AdMobManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    // Ad instances
    private var rewardedAd: RewardedAd? = null
    private var interstitialAd: InterstitialAd? = null
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null

    // State flows للتتبع
    private val _bannerAdState = MutableStateFlow(AdState.LOADING)
    val bannerAdState: StateFlow<AdState> = _bannerAdState.asStateFlow()

    private val _rewardedAdState = MutableStateFlow(AdState.LOADING)
    val rewardedAdState: StateFlow<AdState> = _rewardedAdState.asStateFlow()

    private val _interstitialAdState = MutableStateFlow(AdState.LOADING)
    val interstitialAdState: StateFlow<AdState> = _interstitialAdState.asStateFlow()

    private val _rewardedInterstitialAdState = MutableStateFlow(AdState.LOADING)
    val rewardedInterstitialAdState: StateFlow<AdState> = _rewardedInterstitialAdState.asStateFlow()

    enum class AdState {
        LOADING,
        LOADED,
        FAILED,
        SHOWING,
        CLOSED,
        REWARDED
    }

    // Enhanced Callbacks
    interface AdLoadListener {
        fun onAdLoaded() {}
        fun onAdFailedToLoad(error: String) {}
        fun onAdShowing() {}
        fun onAdClosed() {}
    }

    interface RewardedAdListener {
        fun onUserEarnedReward(amount: Int, type: String)
        fun onAdClosed() {}
        fun onAdFailedToShow(error: String) {}
        fun onAdShowedFullScreen() {}
    }

    init {
        initializeAdMob()
    }

    private fun initializeAdMob() {
        MobileAds.initialize(context) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            Log.d(TAG, "AdMob initialized with status: $statusMap")

            // تحقق من حالة كل محول إعلانات
            for ((className, status) in statusMap) {
                Log.d(
                    TAG,
                    "Adapter $className: ${status.initializationState}, ${status.description}"
                )
            }
        }
    }

    // Banner Ad Methods with improved error handling
    fun createBannerAd(
        adUnitId: String? = null,
        adSize: AdSize = AdSize.BANNER
    ): AdView {
        val finalAdUnitId = adUnitId ?: BANNER_AD_ID

        return AdView(context).apply {
            setAdSize(adSize)
            this.adUnitId = finalAdUnitId
            Log.d(TAG, "Banner ad created with ID: $finalAdUnitId")
        }
    }

    fun loadBannerAd(
        adView: AdView,
        listener: AdLoadListener? = null
    ) {
        _bannerAdState.value = AdState.LOADING

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d(TAG, "Banner ad loaded successfully")
                _bannerAdState.value = AdState.LOADED
                listener?.onAdLoaded()
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e(TAG, "Banner ad failed to load: Code=${error.code}, Message=${error.message}")
                _bannerAdState.value = AdState.FAILED
                listener?.onAdFailedToLoad("Code: ${error.code}, Message: ${error.message}")
            }

            override fun onAdOpened() {
                Log.d(TAG, "Banner ad opened")
                _bannerAdState.value = AdState.SHOWING
                listener?.onAdShowing()
            }

            override fun onAdClosed() {
                Log.d(TAG, "Banner ad closed")
                _bannerAdState.value = AdState.CLOSED
                listener?.onAdClosed()
            }
        }

        val adRequest = buildAdRequest()
        adView.loadAd(adRequest)
    }

    // Enhanced Rewarded Video Ad Methods
    fun loadRewardedVideoAd(
        adUnitId: String? = null,
        listener: AdLoadListener? = null
    ) {
        val finalAdUnitId = adUnitId ?: VIDEO_AD_ID

        _rewardedAdState.value = AdState.LOADING
        val adRequest = buildAdRequest()

        RewardedAd.load(
            context,
            finalAdUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Rewarded video ad loaded successfully")
                    rewardedAd = ad
                    _rewardedAdState.value = AdState.LOADED
                    listener?.onAdLoaded()

                    // Enhanced full screen callback
                    rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        fun onAdDismissedFullScreen() {
                            Log.d(TAG, "Rewarded video ad dismissed")
                            _rewardedAdState.value = AdState.CLOSED
                            rewardedAd = null
                            listener?.onAdClosed()
                            // Auto-load next ad
                            loadRewardedVideoAd(finalAdUnitId, listener)
                        }

                        fun onAdFailedToShowFullScreen(error: AdError) {
                            Log.e(TAG, "Rewarded video ad failed to show: ${error.message}")
                            _rewardedAdState.value = AdState.FAILED
                            rewardedAd = null
                        }

                        fun onAdShowedFullScreen() {
                            Log.d(TAG, "Rewarded video ad showed full screen")
                            _rewardedAdState.value = AdState.SHOWING
                            listener?.onAdShowing()
                        }
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(
                        TAG,
                        "Rewarded video ad failed to load: Code=${error.code}, Message=${error.message}"
                    )
                    _rewardedAdState.value = AdState.FAILED
                    rewardedAd = null
                    listener?.onAdFailedToLoad("Code: ${error.code}, Message: ${error.message}")
                }
            }
        )
    }

    fun showRewardedVideoAd(
        activity: Activity,
        rewardListener: RewardedAdListener? = null
    ): Boolean {
        return rewardedAd?.let { ad ->
            Log.d(TAG, "Attempting to show rewarded video ad")

            ad.show(activity) { rewardItem ->
                Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                _rewardedAdState.value = AdState.REWARDED
                rewardListener?.onUserEarnedReward(rewardItem.amount, rewardItem.type)
            }

            // Set callback for additional events
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                fun onAdShowedFullScreen() {
                    Log.d(TAG, "Rewarded video ad showed full screen")
                    rewardListener?.onAdShowedFullScreen()
                }

                fun onAdDismissedFullScreen() {
                    Log.d(TAG, "Rewarded video ad dismissed")
                    rewardListener?.onAdClosed()
                }

                fun onAdFailedToShowFullScreen(error: AdError) {
                    Log.e(TAG, "Rewarded video ad failed to show: ${error.message}")
                    rewardListener?.onAdFailedToShow("Failed to show: ${error.message}")
                }
            }

            true
        } ?: run {
            Log.w(TAG, "Rewarded video ad not ready")
            rewardListener?.onAdFailedToShow("Ad not loaded")
            false
        }
    }

    // Rewarded Interstitial Ad Methods (فيديو مكافئ بشاشة كاملة)
    fun loadRewardedInterstitialAd(
        adUnitId: String? = null,
        listener: AdLoadListener? = null
    ) {
        val finalAdUnitId = adUnitId ?: REWARDED_INTERSTITIAL_AD_ID

        _rewardedInterstitialAdState.value = AdState.LOADING
        val adRequest = buildAdRequest()

        RewardedInterstitialAd.load(
            context,
            finalAdUnitId,
            adRequest,
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    Log.d(TAG, "Rewarded interstitial ad loaded successfully")
                    rewardedInterstitialAd = ad
                    _rewardedInterstitialAdState.value = AdState.LOADED
                    listener?.onAdLoaded()

                    rewardedInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        fun onAdDismissedFullScreen() {
                            Log.d(TAG, "Rewarded interstitial ad dismissed")
                            _rewardedInterstitialAdState.value = AdState.CLOSED
                            rewardedInterstitialAd = null
                            listener?.onAdClosed()
                            // Auto-load next ad
                            loadRewardedInterstitialAd(finalAdUnitId, listener)
                        }

                        fun onAdFailedToShowFullScreen(error: AdError) {
                            Log.e(TAG, "Rewarded interstitial ad failed to show: ${error.message}")
                            _rewardedInterstitialAdState.value = AdState.FAILED
                            rewardedInterstitialAd = null
                        }

                        fun onAdShowedFullScreen() {
                            Log.d(TAG, "Rewarded interstitial ad showed full screen")
                            _rewardedInterstitialAdState.value = AdState.SHOWING
                            listener?.onAdShowing()
                        }
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(
                        TAG,
                        "Rewarded interstitial ad failed to load: Code=${error.code}, Message=${error.message}"
                    )
                    _rewardedInterstitialAdState.value = AdState.FAILED
                    rewardedInterstitialAd = null
                    listener?.onAdFailedToLoad("Code: ${error.code}, Message: ${error.message}")
                }
            }
        )
    }

    fun showRewardedInterstitialAd(
        activity: Activity,
        rewardListener: RewardedAdListener? = null
    ): Boolean {
        return rewardedInterstitialAd?.let { ad ->
            Log.d(TAG, "Attempting to show rewarded interstitial ad")

            ad.show(activity) { rewardItem ->
                Log.d(TAG, "User earned reward from interstitial: ${rewardItem.amount} ${rewardItem.type}")
                _rewardedInterstitialAdState.value = AdState.REWARDED
                rewardListener?.onUserEarnedReward(rewardItem.amount, rewardItem.type)
            }
            true
        } ?: run {
            Log.w(TAG, "Rewarded interstitial ad not ready")
            rewardListener?.onAdFailedToShow("Ad not loaded")
            false
        }
    }

    // Interstitial Ad Methods with improvements
    fun loadInterstitialAd(
        adUnitId: String? = null,
        listener: AdLoadListener? = null
    ) {
        val finalAdUnitId = adUnitId ?: INTERSTITIAL_AD_ID

        _interstitialAdState.value = AdState.LOADING
        val adRequest = buildAdRequest()

        InterstitialAd.load(
            context,
            finalAdUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Interstitial ad loaded successfully")
                    interstitialAd = ad
                    _interstitialAdState.value = AdState.LOADED
                    listener?.onAdLoaded()

                    interstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            fun onAdDismissedFullScreen() {
                                Log.d(TAG, "Interstitial ad dismissed")
                                _interstitialAdState.value = AdState.CLOSED
                                interstitialAd = null
                                listener?.onAdClosed()
                                // Auto-load next ad
                                loadInterstitialAd(finalAdUnitId, listener)
                            }

                            fun onAdFailedToShowFullScreen(error: AdError) {
                                Log.e(TAG, "Interstitial ad failed to show: ${error.message}")
                                _interstitialAdState.value = AdState.FAILED
                                interstitialAd = null
                            }

                            fun onAdShowedFullScreen() {
                                Log.d(TAG, "Interstitial ad showed full screen")
                                _interstitialAdState.value = AdState.SHOWING
                                listener?.onAdShowing()
                            }
                        }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(
                        TAG,
                        "Interstitial ad failed to load: Code=${error.code}, Message=${error.message}"
                    )
                    _interstitialAdState.value = AdState.FAILED
                    interstitialAd = null
                    listener?.onAdFailedToLoad("Code: ${error.code}, Message: ${error.message}")
                }
            }
        )
    }

    fun showInterstitialAd(activity: Activity): Boolean {
        return interstitialAd?.let {
            it.show(activity)
            true
        } ?: run {
            Log.w(TAG, "Interstitial ad not ready")
            false
        }
    }

    // Enhanced utility methods
    fun isRewardedVideoAdReady(): Boolean =
        rewardedAd != null && _rewardedAdState.value == AdState.LOADED

    fun isRewardedInterstitialAdReady(): Boolean =
        rewardedInterstitialAd != null && _rewardedInterstitialAdState.value == AdState.LOADED

    fun isInterstitialAdReady(): Boolean =
        interstitialAd != null && _interstitialAdState.value == AdState.LOADED

    fun getAdState(adType: AdType): AdState {
        return when (adType) {
            AdType.BANNER -> _bannerAdState.value
            AdType.REWARDED_VIDEO -> _rewardedAdState.value
            AdType.REWARDED_INTERSTITIAL -> _rewardedInterstitialAdState.value
            AdType.INTERSTITIAL -> _interstitialAdState.value
        }
    }

    enum class AdType {
        BANNER,
        REWARDED_VIDEO,
        REWARDED_INTERSTITIAL,
        INTERSTITIAL
    }

    // Enhanced ad request builder
    private fun buildAdRequest(): AdRequest {
        return AdRequest.Builder()
            .build()
    }

    // Cleanup method
    fun destroyAds() {
        rewardedAd = null
        interstitialAd = null
        rewardedInterstitialAd = null
        _bannerAdState.value = AdState.LOADING
        _rewardedAdState.value = AdState.LOADING
        _interstitialAdState.value = AdState.LOADING
        _rewardedInterstitialAdState.value = AdState.LOADING
        Log.d(TAG, "All ads destroyed and states reset")
    }

    // Preload all ads method
    fun preloadAllAds(listener: AdLoadListener? = null) {
        loadRewardedVideoAd(listener = listener)
        loadRewardedInterstitialAd(listener = listener)
        loadInterstitialAd(listener = listener)
    }

    fun showVideoAd(
        activity: Activity,
        preferInterstitial: Boolean = false,
        rewardListener: RewardedAdListener? = null
    ): Boolean {
        return when {
            preferInterstitial && isRewardedInterstitialAdReady() -> {
                showRewardedInterstitialAd(activity, rewardListener)
            }
            isRewardedVideoAdReady() -> {
                showRewardedVideoAd(activity, rewardListener)
            }
            isRewardedInterstitialAdReady() -> {
                showRewardedInterstitialAd(activity, rewardListener)
            }
            else -> {
                Log.w(TAG, "No video ads available")
                rewardListener?.onAdFailedToShow("No video ads loaded")
                false
            }
        }
    }
}

// Extension functions
fun Context.getAdMobManager(): AdMobManager = AdMobManager.getInstance(this)

// Compose integration with enhanced banner ad composable
@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adSize: AdSize = AdSize.BANNER,
    adUnitId: String? = null,
    onAdLoaded: (() -> Unit)? = null,
    onAdFailedToLoad: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val adMobManager = remember { AdMobManager.getInstance(context) }
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { ctx ->
            adMobManager.createBannerAd(adUnitId, adSize).apply {
                adMobManager.loadBannerAd(
                    this,
                    object : AdMobManager.AdLoadListener {
                        override fun onAdLoaded() {
                            onAdLoaded?.invoke()
                        }
                        override fun onAdFailedToLoad(error: String) {
                            onAdFailedToLoad?.invoke(error)
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun rememberAdMobManager(): AdMobManager {
    val context = LocalContext.current
    return remember { AdMobManager.getInstance(context) }
}