package com.podcastapp.rainbowview

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class RainbowViewManager : SimpleViewManager<RainbowView>() {

    companion object {
        const val COMMAND_START_ANIMATION = "start"
        const val COMMAND_STOP_ANIMATION = "stop"
    }

    override fun getName(): String {
        return "RainbowView" // JavaScript側で参照する名前
    }

    override fun createViewInstance(reactContext: ThemedReactContext): RainbowView {
        return RainbowView(reactContext)
    }

    @ReactProp(name = "updateMillis", defaultInt = 1000)
    fun setUpdateMillis(view: RainbowView, millis: Int) {
        view.updateMillis = millis
    }

    // onColorChangedイベントをJavaScriptに公開
    override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any>? {
        return MapBuilder.builder<String, Any>()
            .put("onColorChanged", MapBuilder.of("registrationName", "onColorChanged"))
            .build()
    }

    // JavaScriptからのコマンドを受け取る
    override fun getCommandsMap(): Map<String, Int>? {
        return MapBuilder.of(
            COMMAND_START_ANIMATION, COMMAND_START_ANIMATION.hashCode(),
            COMMAND_STOP_ANIMATION, COMMAND_STOP_ANIMATION.hashCode()
        )
    }

    override fun receiveCommand(root: RainbowView, commandId: String?, args: ReadableArray?) {
        when (commandId) {
            COMMAND_START_ANIMATION -> root.startChangeColor()
            COMMAND_STOP_ANIMATION -> root.stopChangeColor()
        }
    }

    // ビューが破棄されるときにアニメーションを停止
    override fun onDropViewInstance(view: RainbowView) {
        super.onDropViewInstance(view)
        view.stopChangeColor()
    }
}
