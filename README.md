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
Open the project in Android Studio (Ladybug or newer) and let Gradle sync — Android Studio
generates the Gradle wrapper on first sync. After that, the CLI build works:

```bash
./gradlew :app:assembleDebug
```

## Author

**Shakil (Shaki4fe)** — GitHub ID: `345276685`

- Profile: [@Shaki4fe](https://github.com/Shaki4fe)
- Badge: ![@Shaki4fe](https://img.shields.io/badge/followers-%40Shaki4fe-181717?logo=github)
- Repos: ![@Shaki4fe](https://img.shields.io/badge/public%20repos-1-181717?logo=github)

## 💰 অর্ডার ও পেমেন্ট

কাস্টম অ্যাপ বা সফটওয়্যার অর্ডার করতে:

1. **পেমেন্ট মেথড:** বিকাশ (bKash) — Personal
2. **বিকাশ নাম্বার:** `01913340417`
3. **অগ্রিম (advance):** **৫,০০০ টাকা** — অর্ডার শুরু হওয়ার আগে আগাম পাঠাতে হবে
4. অগ্রিম পাঠানোর পর **TrxID** সহ এই রিপোতে একটি [Issue](https://github.com/Shaki4fe/XCa/issues) খুলে জানান
5. TrxID যাচাইয়ের পর অর্ডার শুরু হবে, বাকি পেমেন্ট ডেভেলিভারির সময়

> ⚠️ নোট: টাকা পাঠানোর আগে Issue-তে অর্ডারের বিবরণ লিখে যাচাই করে নিন।

## Project structure
```
app/src/main/java/com/shakil/youtubeapp/
  MainActivity.kt     home feed
  PlayerActivity.kt   embedded YouTube player
  VideoAdapter.kt     feed list adapter
  Video.kt            video model
  VideoFeed.kt        sample video data
```
