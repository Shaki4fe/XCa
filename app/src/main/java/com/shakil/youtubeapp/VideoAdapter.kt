package com.shakil.youtubeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.shakil.youtubeapp.databinding.ItemVideoBinding

class VideoAdapter(
    private val videos: List<Video>,
    private val onClick: (Video) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        with(holder.binding) {
            thumbnail.load(video.thumbnailUrl) {
                crossfade(true)
                placeholder(R.color.thumbnail_placeholder)
            }
            videoTitle.text = video.title
            videoMeta.text = root.context.getString(
                R.string.video_meta_format, video.views, video.posted
            )
            durationBadge.text = video.duration
            root.setOnClickListener { onClick(video) }
        }
    }

    override fun getItemCount(): Int = videos.size
}
