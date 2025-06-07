import { requireNativeComponent, ViewProps } from "react-native";

interface MusicPlayerViewProps extends ViewProps {
  testID?: string;
  componentWidth?: number; // Kept for now, can be removed later
  componentHeight?: number; // Kept for now, can be removed later
  sourceUrl?: string;
}

const NativeMusicPlayerView =
  requireNativeComponent<MusicPlayerViewProps>("MusicPlayerView");

export default function MusicPlayerView(props: MusicPlayerViewProps) {
  console.log(
    `MusicPlayerJS: Passing componentWidth: ${props.componentWidth}, componentHeight: ${props.componentHeight}, sourceUrl: ${props.sourceUrl}`
  );

  return (
    <NativeMusicPlayerView
      {...props} // Passes style, testID, and now sourceUrl
      // Explicitly passed componentWidth/Height are overridden by {...props} if also present there
      // but it's cleaner to rely on props directly.
      // testID="MusicPlayerTestID" // Already in props if passed from parent
      // componentWidth={250} // Example, should come from props if used
      // componentHeight={180} // Example, should come from props if used
    />
  );
}
