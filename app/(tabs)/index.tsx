import { Image } from "expo-image";
import { Platform, StyleSheet, View, Button } from "react-native";
import { useState, useRef } from "react";

import { HelloWave } from "@/components/HelloWave";
import ParallaxScrollView from "@/components/ParallaxScrollView";
import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import {
  RainbowView,
  startRainbowAnimation,
  stopRainbowAnimation,
  RAINBOW_CONSTANTS,
} from "@/components/RainbowView";

export default function HomeScreen() {
  const [isAnimating, setIsAnimating] = useState(true);
  const [currentColor, setCurrentColor] = useState("#ff0000");
  const [updateInterval, setUpdateInterval] = useState(
    RAINBOW_CONSTANTS.DEFAULT_UPDATE_MILLIS
  );
  const rainbowViewRef = useRef(null);

  const handleStartStop = () => {
    if (isAnimating) {
      stopRainbowAnimation(rainbowViewRef);
      setIsAnimating(false);
    } else {
      startRainbowAnimation(rainbowViewRef);
      setIsAnimating(true);
    }
  };

  const handleColorChanged = (event: any) => {
    const color = event.nativeEvent.color;
    setCurrentColor(color);
    console.log("Color changed to:", color);
  };

  const toggleSpeed = () => {
    const newInterval =
      updateInterval === RAINBOW_CONSTANTS.DEFAULT_UPDATE_MILLIS
        ? RAINBOW_CONSTANTS.FAST_UPDATE_MILLIS
        : RAINBOW_CONSTANTS.DEFAULT_UPDATE_MILLIS;
    setUpdateInterval(newInterval);
  };

  return (
    <ParallaxScrollView
      headerBackgroundColor={{ light: "#A1CEDC", dark: "#1D3D47" }}
      headerImage={
        <Image
          source={require("@/assets/images/partial-react-logo.png")}
          style={styles.reactLogo}
        />
      }
    >
      <ThemedView style={styles.titleContainer}>
        <ThemedText type="title">Welcome!</ThemedText>
        <HelloWave />
      </ThemedView>

      <ThemedView style={styles.stepContainer}>
        <ThemedText type="subtitle">Native Rainbow View Demo</ThemedText>
        <ThemedText>
          This is a native Android component that changes colors automatically!
        </ThemedText>
        <ThemedText>Current Color: {currentColor}</ThemedText>
        <ThemedText>Update Interval: {updateInterval}ms</ThemedText>

        <RainbowView
          ref={rainbowViewRef}
          style={styles.rainbowView}
          updateMillis={updateInterval}
          onColorChanged={handleColorChanged}
        />

        <View style={styles.buttonContainer}>
          <Button
            title={isAnimating ? "Stop Animation" : "Start Animation"}
            onPress={handleStartStop}
          />
          <Button
            title={`Switch to ${
              updateInterval === RAINBOW_CONSTANTS.DEFAULT_UPDATE_MILLIS
                ? "Fast"
                : "Normal"
            }`}
            onPress={toggleSpeed}
          />
        </View>
      </ThemedView>

      <ThemedView style={styles.stepContainer}>
        <ThemedText type="subtitle">Step 1: Try it</ThemedText>
        <ThemedText>
          Edit{" "}
          <ThemedText type="defaultSemiBold">app/(tabs)/index.tsx</ThemedText>{" "}
          to see changes. Press{" "}
          <ThemedText type="defaultSemiBold">
            {Platform.select({
              ios: "cmd + d",
              android: "cmd + m",
              web: "F12",
            })}
          </ThemedText>{" "}
          to open developer tools.
        </ThemedText>
      </ThemedView>
      <ThemedView style={styles.stepContainer}>
        <ThemedText type="subtitle">Step 2: Explore</ThemedText>
        <ThemedText>
          {`Tap the Explore tab to learn more about what's included in this starter app.`}
        </ThemedText>
      </ThemedView>
      <ThemedView style={styles.stepContainer}>
        <ThemedText type="subtitle">Step 3: Get a fresh start</ThemedText>
        <ThemedText>
          {`When you're ready, run `}
          <ThemedText type="defaultSemiBold">
            npm run reset-project
          </ThemedText>{" "}
          to get a fresh <ThemedText type="defaultSemiBold">app</ThemedText>{" "}
          directory. This will move the current{" "}
          <ThemedText type="defaultSemiBold">app</ThemedText> to{" "}
          <ThemedText type="defaultSemiBold">app-example</ThemedText>.
        </ThemedText>
      </ThemedView>
    </ParallaxScrollView>
  );
}

const styles = StyleSheet.create({
  titleContainer: {
    flexDirection: "row",
    alignItems: "center",
    gap: 8,
  },
  stepContainer: {
    gap: 8,
    marginBottom: 8,
  },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: "absolute",
  },
  rainbowView: {
    height: 100,
    marginVertical: 10,
    borderRadius: 8,
  },
  buttonContainer: {
    flexDirection: "row",
    justifyContent: "space-around",
    marginTop: 10,
    gap: 10,
  },
});
