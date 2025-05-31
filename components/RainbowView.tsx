import React from "react";
import {
  requireNativeComponent,
  UIManager,
  findNodeHandle,
  ViewProps,
  NativeSyntheticEvent,
  View,
  Platform,
} from "react-native";

interface ColorChangedData {
  color: string;
}

interface RainbowViewProps extends ViewProps {
  updateMillis?: number;
  onColorChanged?: (e: NativeSyntheticEvent<ColorChangedData>) => void;
}

export const RAINBOW_CONSTANTS = {
  DEFAULT_UPDATE_MILLIS:
    (UIManager as any).RainbowView?.Constants?.DEFAULT_UPDATE_MILLIS || 1000,
  FAST_UPDATE_MILLIS:
    (UIManager as any).RainbowView?.Constants?.FAST_UPDATE_MILLIS || 500,
};

let NativeRainbowView: any;

// Only require native component on Android
if (Platform.OS === "android") {
  try {
    NativeRainbowView = requireNativeComponent<RainbowViewProps>("RainbowView");
  } catch (error) {
    console.warn("RainbowView native component not found, using fallback");
    NativeRainbowView = View;
  }
} else {
  NativeRainbowView = View;
}

export const RainbowView = React.forwardRef<any, RainbowViewProps>(
  (props, ref) => {
    if (Platform.OS === "android" && NativeRainbowView !== View) {
      return <NativeRainbowView {...props} ref={ref} />;
    }

    // Fallback for non-Android or when native component is not available
    return (
      <View
        {...props}
        ref={ref}
        style={[
          props.style,
          { backgroundColor: "#ff0000" }, // Static color as fallback
        ]}
      />
    );
  }
);

export const startRainbowAnimation = (viewRef: React.RefObject<any>) => {
  const viewId = findNodeHandle(viewRef.current);
  if (viewId) {
    UIManager.dispatchViewManagerCommand(
      viewId,
      "startRainbow", // COMMAND_START
      []
    );
  }
};

export const stopRainbowAnimation = (viewRef: React.RefObject<any>) => {
  const viewId = findNodeHandle(viewRef.current);
  if (viewId) {
    UIManager.dispatchViewManagerCommand(
      viewId,
      "stopRainbow", // COMMAND_STOP
      []
    );
  }
};
