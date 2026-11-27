package com.shakil.youtubeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.shakil.youtubeapp.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val video = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_VIDEO, Video::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_VIDEO)
        }

        requireNotNull(video) { "Missing video extra" }

        setSupportActionBar(binding.playerToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        binding.playerToolbar.setNavigationOnClickListener { finish() }

        binding.playerTitle.text = video.title
        binding.playerMeta.text = getString(
            R.string.player_meta_format, video.views, video.posted, video.channel
        )

        setupPlayer(video.videoId)
    }

    private fun setupPlayer(videoId: String) {
        binding.youtubePlayer.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            mediaPlaybackRequiresUserGesture = false
            cacheMode = WebSettings.LOAD_DEFAULT
        }
        binding.youtubePlayer.webViewClient = WebViewClient()
        binding.youtubePlayer.loadDataWithBaseURL(
            null,
            playerHtml(videoId),
            "text/html",
            "UTF-8",
            null
        )
    }

    private fun playerHtml(videoId: String): String = """
        <!DOCTYPE html>
        <html>
          <body style="margin:0;padding:0;background:#000;">
            <iframe
              width="100%" height="100%"
              src="https://www.youtube.com/embed/$videoId?autoplay=1&playsinline=1&rel=0&modestbranding=1"
              frameborder="0"
              allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
              allowfullscreen>
            </iframe>
          </body>
        </html>
    """.trimIndent()

    override fun onDestroy() {
        if (::binding.isInitialized) {
            binding.youtubePlayer.loadUrl("about:blank")
            binding.youtubePlayer.destroy()
        }
        super.onDestroy()
    }

    companion object {
        private const val EXTRA_VIDEO = "extra_video"

        fun start(context: Context, video: Video) {
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_VIDEO, video)
            }
            context.startActivity(intent)
        }
    }
}
