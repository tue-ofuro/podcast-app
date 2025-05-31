package com.podcastapp // アプリのルートパッケージ名に合わせてください

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.podcastapp.rainbowview.RainbowViewManager // 作成したRainbowViewManagerをインポート

class MyAppPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return emptyList()
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return listOf(
            RainbowViewManager() // RainbowViewManagerをリストに追加
        )
    }
}
