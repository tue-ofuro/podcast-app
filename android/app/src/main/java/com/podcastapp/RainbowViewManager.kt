package com.podcastapp

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class RainbowViewManager : SimpleViewManager<RainbowView>() {
    
    override fun getName(): String {
        return "RainbowView"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): RainbowView {
        return RainbowView(reactContext)
    }

    @ReactProp(name = "updateMillis", defaultInt = 1000)
    fun setUpdateMillis(view: RainbowView, updateMillis: Int) {
        view.updateMillis = updateMillis
    }

    override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
        return mutableMapOf(
            "onColorChanged" to mutableMapOf("registrationName" to "onColorChanged")
        )
    }

    override fun getCommandsMap(): Map<String, Int> {
        return mapOf("start" to COMMAND_START, "stop" to COMMAND_STOP)
    }

    override fun receiveCommand(view: RainbowView, commandId: String?, args: ReadableArray?) {
        super.receiveCommand(view, commandId, args)
        
        when (commandId?.toInt()) {
            COMMAND_START -> view.startChangeColor()
            COMMAND_STOP -> view.stopChangeColor()
        }
    }

    override fun onDropViewInstance(view: RainbowView) {
        view.stopChangeColor()
        super.onDropViewInstance(view)
    }

    override fun getExportedViewConstants(): Map<String, Any> {
        return mapOf(
            "DEFAULT_UPDATE_MILLIS" to 1000,
            "FAST_UPDATE_MILLIS" to 500
        )
    }

    companion object {
        private const val COMMAND_START = 1
        private const val COMMAND_STOP = 2
    }
}
