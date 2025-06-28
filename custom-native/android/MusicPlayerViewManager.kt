package com.anonymous.podcastapp

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class MusicPlayerViewManager(
    private val reactContext: ReactApplicationContext
) : SimpleViewManager<FrameLayout>() {
    private val TAG = "MusicPlayerDebug"
    private val REACT_CLASS = "MusicPlayerView"

    override fun getName() = "MusicPlayerView"

    override fun createViewInstance(reactContext: ThemedReactContext): FrameLayout {
        Log.d(TAG, "MusicPlayerViewManager: createViewInstance called (SimpleViewManager)")
        val frameLayout = object : FrameLayout(reactContext) {
            override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
                val widthMode = MeasureSpec.getMode(widthMeasureSpec)
                val widthSize = MeasureSpec.getSize(widthMeasureSpec)
                val heightMode = MeasureSpec.getMode(heightMeasureSpec)
                val heightSize = MeasureSpec.getSize(heightMeasureSpec)

                Log.d(TAG, "FrameLayout.onMeasure (SimpleVM): specs: " +
                        "width[mode=${MeasureSpec.toString(widthMode)}, size=$widthSize], " +
                        "height[mode=${MeasureSpec.toString(heightMode)}, size=$heightSize]")
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                Log.d(TAG, "FrameLayout.onMeasure (SimpleVM): after super.onMeasure: " +
                        "measuredWidth=$measuredWidth, measuredHeight=$measuredHeight")
            }

            override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
                Log.d(TAG, "FrameLayout.onLayout (SimpleVM): changed=$changed, " +
                        "l=$left, t=$top, r=$right, b=$bottom. " +
                        "Current w=$width, h=$height")
                super.onLayout(changed, left, top, right, bottom)
            }

            override fun requestLayout() {
                Log.d(TAG, "FrameLayout.requestLayout (SimpleVM) called. Current w=$width, h=$height")
                super.requestLayout()
            }
        }
        frameLayout.setBackgroundColor(Color.BLUE) // Re-enable blue background
        Log.d(TAG, "MusicPlayerViewManager: FrameLayout instance created with blue background (SimpleViewManager).")

        // Create and add the actual MusicPlayerView (magenta) as a child
        val musicPlayerView = MusicPlayerView(reactContext) // Use ThemedReactContext
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        frameLayout.addView(musicPlayerView, layoutParams)
        Log.d(TAG, "MusicPlayerViewManager: Added MusicPlayerView (magenta) to FrameLayout with MATCH_PARENT.")

        return frameLayout
    }

    @ReactProp(name = "testID")
    fun setTestID(view: FrameLayout, testID: String?) {
        Log.d(TAG, "MusicPlayerViewManager (SimpleVM): setTestID called with: $testID for view ${view.hashCode()}")
    }

    @ReactProp(name = "sourceUrl")
    fun setSourceUrl(view: FrameLayout, url: String?) {
        Log.d(TAG, "MusicPlayerViewManager: setSourceUrl called with: $url")
        // Ensure that the MusicPlayerView instance is correctly targeted
        val musicPlayerView = view.getChildAt(0) as? MusicPlayerView
        musicPlayerView?.setSourceUrl(url)
    }

    override fun onDropViewInstance(view: FrameLayout) {
        Log.d(TAG, "MusicPlayerViewManager: onDropViewInstance called for view: $view")
        val musicPlayerView = view.getChildAt(0) as? MusicPlayerView
        musicPlayerView?.onCleanup() // Call cleanup on the specific instance
        super.onDropViewInstance(view)
    }

    // @ReactProp(name = "componentWidth") ... // Keep commented out
    // @ReactProp(name = "componentHeight") ... // Keep commented out

    // addView is not needed for SimpleViewManager as it doesn't manage React children
    // override fun addView(parent: FrameLayout, child: View, index: Int) { ... }

    // setupLayout and manuallyLayoutChildren remain commented out
    // private fun setupLayout(view: FrameLayout) { ... }
    // private fun manuallyLayoutChildren(view: FrameLayout) { ... }
}
