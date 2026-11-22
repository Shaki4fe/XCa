package com.shakil.quickdial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class PhoneStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_PHONE_STATE_CHANGED) return
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state == TelephonyManager.EXTRA_STATE_OFFHOOK &&
            AppPrefs.isPremium(context) &&
            AppPrefs.isAnnouncementEnabled(context)
        ) {
            context.startService(Intent(context, AnnouncementService::class.java))
        }
    }
}
