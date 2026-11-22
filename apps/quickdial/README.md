# QuickDial — one-tap call dialer (Kotlin)

একটা ছোট কিন্তু কাজের Android ডায়ালার অ্যাপ — Kotlin-এ বানানো।

## Features
- **রেডি কান্টাক্ট:** শাকিলের নাম্বার `01913340417` সেভ করা — এক ট্যাপে কল ডায়ালার খুলবে
- **সম্পূর্ণ ডায়াল-প্যাড:** `0-9`, `*`, `#` — যেকোনো নাম্বার ডায়াল করা যায়
- **Backspace:** ট্যাপ করলে এক অক্ষর মুছবে, লং-প্রেস করলে পুরো নাম্বার মুছবে
- **সেফ কলিং:** `ACTION_DIAL` ব্যবহার করে — কোনো পারমিশন লাগে না, ব্যবহারকারী নিজে কল বাটন চাপে
- Material 3 UI, ViewBinding, minSdk 24 / SDK 35

## Build
`apps/quickdial/` ফোল্ডারটা Android Studio-তে ওপেন করো (Gradle sync-এ wrapper তৈরি হবে), তারপর:

```bash
./gradlew :app:assembleDebug
```

## Structure
```
apps/quickdial/app/src/main/java/com/shakil/quickdial/
  MainActivity.kt  dial pad + call logic
  QuickContact.kt  saved contact config
```
