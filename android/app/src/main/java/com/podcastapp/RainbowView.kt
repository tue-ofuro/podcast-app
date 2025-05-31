package com.podcastapp

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter

class RainbowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val colors = arrayOf(
        Color.RED, Color.parseColor("#FF7F00"), Color.YELLOW,
        Color.GREEN, Color.BLUE, Color.parseColor("#4B0082"), Color.parseColor("#9400D3")
    )
    
    private var currentColorIndex = 0
    private var handler: Handler? = null
    private var colorChangeRunnable: Runnable? = null
    
    var updateMillis: Int = 1000
        set(value) {
            field = value
            if (isRunning) {
                stopChangeColor()
                startChangeColor()
            }
        }
    
    private var isRunning = false

    init {
        setBackgroundColor(colors[0])
        startChangeColor()
    }

    fun startChangeColor() {
        if (isRunning) return
        isRunning = true
        
        handler = Handler(Looper.getMainLooper())
        colorChangeRunnable = object : Runnable {
            override fun run() {
                currentColorIndex = (currentColorIndex + 1) % colors.size
                val newColor = colors[currentColorIndex]
                setBackgroundColor(newColor)
                sendColorChangedEventToJs(newColor)
                handler?.postDelayed(this, updateMillis.toLong())
            }
        }
        handler?.postDelayed(colorChangeRunnable!!, updateMillis.toLong())
    }

    fun stopChangeColor() {
        isRunning = false
        handler?.removeCallbacks(colorChangeRunnable!!)
        handler = null
        colorChangeRunnable = null
    }

    private fun sendColorChangedEventToJs(color: Int) {
        val arguments = Arguments.createMap().apply {
            putString("color", "0x${color.toUInt().toString(16)}")
        }
        (context as ReactContext)
            .getJSModule(RCTEventEmitter::class.java)
            .receiveEvent(id, "onColorChanged", arguments)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopChangeColor()
    }
}
