package com.shakil.quickdial

import android.content.Context

object AppPrefs {

    private const val FILE = "quickdial_prefs"

    fun isPremium(context: Context): Boolean =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .getBoolean("premium_active", false)

    fun isAnnouncementEnabled(context: Context): Boolean =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .getBoolean("announcement_enabled", true)

    fun setAnnouncementEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit().putBoolean("announcement_enabled", enabled).apply()
    }

    fun isTrxIdFormatValid(trxId: String): Boolean {
        val trimmed = trxId.trim()
        return trimmed.length in 8..20 && trimmed.matches(Regex("^[A-Za-z0-9]+$"))
    }

    fun activatePremium(context: Context, trxId: String): Boolean {
        if (!isTrxIdFormatValid(trxId)) return false
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit()
            .putBoolean("premium_active", true)
            .putString("premium_trxid", trxId.trim())
            .apply()
        return true
    }
}
