# Expo アプリへようこそ 👋

これは [`create-expo-app`](https://www.npmjs.com/package/create-expo-app) で作成された [Expo](https://expo.dev) プロジェクトです。

## 始め方

1.  ネイティブの Android/iOS プロジェクトファイルを生成（まだない場合や再生成が必要な場合）：

    ```bash
    npx expo prebuild
    ```

2.  カスタムネイティブモジュールを復元：

    ```bash
    sh scripts/restore-native.sh
    ```

3.  Android でアプリをビルドして実行：
    ```bash
    npx expo run:android
    ```

出力には、以下でアプリを開くオプションが表示されます：

- [development build](https://docs.expo.dev/develop/development-builds/introduction/)
- [Android エミュレーター](https://docs.expo.dev/workflow/android-studio-emulator/)
- [iOS シミュレーター](https://docs.expo.dev/workflow/ios-simulator/)
- [Expo Go](https://expo.dev/go)（Expo での アプリ開発を試すための限定的なサンドボックス）

**app** ディレクトリ内のファイルを編集することで開発を始められます。このプロジェクトは[ファイルベースルーティング](https://docs.expo.dev/router/introduction)を使用しています。

## 新しいプロジェクトを始める

準備ができたら、以下を実行してください：

```bash
npm run reset-project
```

このコマンドは、スターターコードを **app-example** ディレクトリに移動し、開発を始められる空の **app** ディレクトリを作成します。
