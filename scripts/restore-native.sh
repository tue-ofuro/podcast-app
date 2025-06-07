#!/bin/bash
# 復元スクリプト: prebuild後にカスタムネイティブファイルをandroidプロジェクトにコピー

set -e

# スクリプトディレクトリの取得
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
CUSTOM_NATIVE_DIR="$SCRIPT_DIR/../custom-native/android"
ANDROID_SRC_DIR="$SCRIPT_DIR/../android/app/src/main/java/com/anonymous/podcastapp"
ANDROID_RES_DIR="$SCRIPT_DIR/../android/app/src/main/res"

# 宛先ディレクトリの作成
mkdir -p "$ANDROID_SRC_DIR"
mkdir -p "$ANDROID_RES_DIR/layout"

# Kotlinファイルを復元
cp "$CUSTOM_NATIVE_DIR/MusicPlayerView.kt" "$ANDROID_SRC_DIR/MusicPlayerView.kt"
cp "$CUSTOM_NATIVE_DIR/MusicPlayerViewManager.kt" "$ANDROID_SRC_DIR/MusicPlayerViewManager.kt"
cp "$CUSTOM_NATIVE_DIR/MusicPlayerViewPackage.kt" "$ANDROID_SRC_DIR/MusicPlayerViewPackage.kt"

# XMLレイアウトファイルを復元
cp "$CUSTOM_NATIVE_DIR/music_player_view.xml" "$ANDROID_RES_DIR/layout/music_player_view.xml"

# --- 重要 ---
# MainApplication.ktファイルは慎重に扱う必要があります。
# prebuildが上書きすると、パッケージ登録情報が失われます。
# このスクリプトは現在、MainApplication.ktの変更を処理していません。
# MusicPlayerViewPackageが登録されていることを手動で確認する必要があります。
# より堅牢な解決策として、カスタムExpo設定プラグインまたはファイルのパッチ適用が考えられます。

echo "Custom native files restored."
echo "IMPORTANT: Manually verify MainApplication.kt for MusicPlayerViewPackage registration."
