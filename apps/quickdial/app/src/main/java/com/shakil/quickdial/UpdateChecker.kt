package com.shakil.quickdial

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread

object UpdateChecker {

    const val UPDATE_MANIFEST = "https://shaki4fe.github.io/XCa/quickdial-update.json"
    private const val APK_FILE_NAME = "QuickDial-update.apk"

    data class Info(
        val versionCode: Long,
        val versionName: String,
        val apkUrl: String,
        val notes: String
    )

    private fun currentVersionCode(context: Context): Long = try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        if (android.os.Build.VERSION.SDK_INT >= 28) pInfo.longVersionCode
        else @Suppress("DEPRECATION") pInfo.versionCode.toLong()
    } catch (e: Exception) {
        0L
    }

    fun fetch(onResult: (Info?) -> Unit) = thread {
        try {
            val connection = URL(UPDATE_MANIFEST).openConnection().apply {
                connectTimeout = 5000
                readTimeout = 5000
            }
            val json = connection.getInputStream().bufferedReader().use { it.readText() }
            val obj = JSONObject(json)
            onResult(
                Info(
                    versionCode = obj.getLong("versionCode"),
                    versionName = obj.getString("versionName"),
                    apkUrl = obj.getString("apkUrl"),
                    notes = obj.optString("notes", "")
                )
            )
        } catch (e: Exception) {
            onResult(null)
        }
    }

    fun checkForUpdate(activity: Activity, manual: Boolean) {
        fetch { info ->
            activity.runOnUiThread {
                if (info != null && info.versionCode > currentVersionCode(activity)) {
                    showUpdateDialog(activity, info)
                } else if (manual) {
                    Toast.makeText(
                        activity,
                        R.string.update_up_to_date,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showUpdateDialog(activity: Activity, info: Info) {
        val message = activity.getString(
            R.string.update_available_message,
            info.versionName
        ) + if (info.notes.isNotEmpty()) "\n\n${info.notes}" else ""

        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.update_available_title)
            .setMessage(message)
            .setPositiveButton(R.string.update_download) { _, _ ->
                startDownload(activity, info.apkUrl)
            }
            .setNegativeButton(R.string.update_later, null)
            .show()
    }

    private fun startDownload(activity: Activity, apkUrl: String) {
        val manager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(apkUrl)).apply {
            setTitle("QuickDial Pro update")
            setDescription(activity.getString(R.string.update_downloading))
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
            setDestinationInExternalFilesDir(
                activity, Environment.DIRECTORY_DOWNLOADS, APK_FILE_NAME
            )
        }
        val id = manager.enqueue(request)
        Toast.makeText(activity, R.string.update_downloading, Toast.LENGTH_SHORT).show()

        activity.registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(ctx: Context, intent: Intent) {
                    if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == id) {
                        openDownloadedApk(activity)
                    }
                }
            },
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private fun openDownloadedApk(activity: Activity) {
        val file = java.io.File(
            activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            APK_FILE_NAME
        )
        if (!file.exists()) return

        val uri = androidx.core.content.FileProvider.getUriForFile(
            activity, "${activity.packageName}.fileprovider", file
        )
        val install = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            activity.startActivity(install)
        } catch (e: Exception) {
            Toast.makeText(activity, R.string.update_install_failed, Toast.LENGTH_LONG).show()
        }
    }
}
