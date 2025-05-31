import React, { forwardRef, useImperativeHandle, useRef } from "react";
import {
  requireNativeComponent,
  UIManager,
  findNodeHandle,
  ViewStyle,
  Platform,
  View,
} from "react-native";

interface NativeRainbowViewProps {
  style?: ViewStyle;
  updateMillis?: number;
  onColorChanged?: (event: { nativeEvent: { color: string } }) => void;
}

export interface RainbowViewProps {
  style?: ViewStyle;
  updateMillis?: number;
  onColorChanged?: (event: { nativeEvent: { color: string } }) => void;
}

export const RAINBOW_CONSTANTS = {
  DEFAULT_UPDATE_MILLIS: 100,
  FAST_UPDATE_MILLIS: 50,
};

// requireNativeComponent for RainbowView
let NativeRainbowView: any = null;
try {
  if (Platform.OS === "android") {
    NativeRainbowView =
      requireNativeComponent<NativeRainbowViewProps>("RainbowView");
  }
} catch (error) {
  console.warn("RainbowView native component not available:", error);
}

export const RainbowView = forwardRef<any, RainbowViewProps>((props, ref) => {
  const viewRef = useRef<View>(null);

  useImperativeHandle(ref, () => ({
    startAnimation: () => {
      if (Platform.OS === "android" && viewRef.current && NativeRainbowView) {
        try {
          UIManager.dispatchViewManagerCommand(
            findNodeHandle(viewRef.current),
            "start",
            []
          );
        } catch (error) {
          console.warn("Failed to start animation:", error);
        }
      }
    },
    stopAnimation: () => {
      if (Platform.OS === "android" && viewRef.current && NativeRainbowView) {
        try {
          UIManager.dispatchViewManagerCommand(
            findNodeHandle(viewRef.current),
            "stop",
            []
          );
        } catch (error) {
          console.warn("Failed to stop animation:", error);
        }
      }
    },
  }));

  if (Platform.OS !== "android" || !NativeRainbowView) {
    // Fallback for non-Android platforms or when native component is not available
    return (
      <View
        style={[{ backgroundColor: "#ff0000" }, props.style]}
        ref={viewRef}
      />
    );
  }

  return <NativeRainbowView {...props} ref={viewRef} />;
});

export const startRainbowAnimation = (ref: React.RefObject<any>) => {
  if (ref.current?.startAnimation) {
    ref.current.startAnimation();
  }
};

export const stopRainbowAnimation = (ref: React.RefObject<any>) => {
  if (ref.current?.stopAnimation) {
    ref.current.stopAnimation();
  }
};
