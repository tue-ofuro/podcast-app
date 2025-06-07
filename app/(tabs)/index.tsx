import MusicPlayerView from "@/components/MusicPlayerView";
import { Image } from "expo-image";
import { ScrollView, StyleSheet, Text, View } from "react-native";

export default function HomeScreen() {
  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.contentContainer}
    >
      <View style={styles.headerContainer}>
        <Image
          source={require("@/assets/images/partial-react-logo.png")}
          style={styles.reactLogo}
        />
      </View>

      <View style={styles.titleContainer}>
        <Text style={styles.title}>Music Player</Text>
      </View>
      <MusicPlayerView
        style={{
          width: 250,
          height: 180,
          marginBottom: 32,
        }}
        sourceUrl="https://anchor.fm/s/2b3dd74c/podcast/play/34449268/https%3A%2F%2Fd3ctxlq1ktw2nl.cloudfront.net%2Fstaging%2F2021-4-29%2F00b5e94b-02b8-b094-c1f8-0291b6f4708b.mp3"
      />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  contentContainer: {
    paddingHorizontal: 16,
    paddingBottom: 20,
  },
  headerContainer: {
    height: 200,
    position: "relative",
    marginHorizontal: -16,
    marginBottom: 16,
  },
  titleContainer: {
    marginBottom: 24,
    alignItems: "center",
  },
  title: {
    fontSize: 32,
    fontWeight: "bold",
    lineHeight: 32,
  },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: "absolute",
  },
});
