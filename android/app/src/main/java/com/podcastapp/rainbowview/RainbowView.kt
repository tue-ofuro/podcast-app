package com.podcastapp.rainbowview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper // Looper をインポート
import android.view.View
import com.facebook.react.bridge.Arguments
import com.facebook.react.uimanager.ThemedReactContext

class RainbowView(context: ThemedReactContext) : View(context) {
    var updateMillis: Int = 1000
    private var currentColorIndex = 0
    private val colors = listOf(
        Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA
    )
    private val paint = Paint()
    private val handler = Handler(Looper.getMainLooper()) // Looper.getMainLooper() を使用
    private var isRunning = false

    private val colorChangeRunnable = object : Runnable {
        override fun run() {
            currentColorIndex = (currentColorIndex + 1) % colors.size
            invalidate()
            sendColorChangedEvent()
            if (isRunning) {
                handler.postDelayed(this, updateMillis.toLong())
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = colors[currentColorIndex]
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    fun startChangeColor() {
        if (!isRunning) {
            isRunning = true
            handler.post(colorChangeRunnable)
        }
    }

    fun stopChangeColor() {
        isRunning = false
        handler.removeCallbacks(colorChangeRunnable)
    }

    private fun sendColorChangedEvent() {
        val event = Arguments.createMap()
        event.putString("color", String.format("#%06X", 0xFFFFFF and colors[currentColorIndex]))
        (context as? ThemedReactContext)?.getJSModule(com.facebook.react.uimanager.events.RCTEventEmitter::class.java)
            ?.receiveEvent(this.id, "onColorChanged", event) // this.id を使用 (より明示的)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopChangeColor()
    }
}
