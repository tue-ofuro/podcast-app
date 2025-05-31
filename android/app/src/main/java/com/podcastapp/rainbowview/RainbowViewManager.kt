package com.podcastapp.rainbowview

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class RainbowViewManager : SimpleViewManager<RainbowView>() {

    companion object {
        const val REACT_CLASS = "RainbowView"
        const val COMMAND_START = 1
        const val COMMAND_STOP = 2
    }

    override fun getName(): String {
        return REACT_CLASS
    }

    override fun createViewInstance(reactContext: ThemedReactContext): RainbowView {
        return RainbowView(reactContext)
    }

    @ReactProp(name = "updateMillis", defaultInt = 100)
    fun setUpdateMillis(view: RainbowView, updateMillis: Int) {
        view.updateMillis = updateMillis
    }

    override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
        return MapBuilder.builder<String, Any>()
            .put("onColorChanged", MapBuilder.of("registrationName", "onColorChanged"))
            .build()
    }

    override fun getCommandsMap(): Map<String, Int>? {
        return MapBuilder.of(
            "start", COMMAND_START,
            "stop", COMMAND_STOP
        )
    }

    override fun receiveCommand(root: RainbowView, commandId: String?, args: ReadableArray?) {
        super.receiveCommand(root, commandId, args)
        val commandIdInt = commandId?.toIntOrNull() ?: return
        
        when (commandIdInt) {
            COMMAND_START -> root.startChangeColor()
            COMMAND_STOP -> root.stopChangeColor()
        }
    }

    override fun onDropViewInstance(view: RainbowView) {
        super.onDropViewInstance(view)
        view.stopChangeColor()
    }
}
