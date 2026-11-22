package com.shakil.quickdial

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.speech.tts.TextToSpeech
import java.util.Locale

class AnnouncementService : Service() {

    private var tts: TextToSpeech? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS && tts != null) {
                tts?.language = Locale("bn", "BD")
                tts?.speak(
                    QuickContact.announcementText,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "quickdial_announcement"
                )
                tts?.setOnUtteranceProgressListener(StopListener(this))
            } else {
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    private class StopListener(private val service: AnnouncementService) :
        android.speech.tts.UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {}
        override fun onDone(utteranceId: String?) = service.stopSelf()
        override fun onError(utteranceId: String?) = service.stopSelf()
    }
}
