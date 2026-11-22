# MyTube — YouTube-style Android app (Kotlin)

A YouTube-style Android app built with Kotlin and Jetpack libraries.

## Features
- Home feed with video thumbnails, titles, channel, views and duration badges (RecyclerView)
- Player screen with an embedded YouTube IFrame player (WebView)
- Clean Material 3 UI, safe WebView lifecycle handling

## Tech
- Kotlin, Android SDK 35 (minSdk 24)
- ViewBinding, RecyclerView, Material Components
- Coil for image loading

## Build
Open the project in Android Studio (Ladybug or newer) and let Gradle sync, or run:

```bash
./gradlew :app:assembleDebug
```

## Project structure
```
app/src/main/java/com/shakil/youtubeapp/
  MainActivity.kt     home feed
  PlayerActivity.kt   embedded YouTube player
  VideoAdapter.kt     feed list adapter
  Video.kt            video model
  VideoFeed.kt        sample video data
```
