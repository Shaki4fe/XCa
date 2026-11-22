package com.shakil.youtubeapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val title: String,
    val channel: String,
    val views: String,
    val posted: String,
    val duration: String,
    val videoId: String
) : Parcelable {
    val thumbnailUrl: String get() = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
}
