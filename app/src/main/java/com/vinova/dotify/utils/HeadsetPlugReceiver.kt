package com.vinova.dotify.utils

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.app.NotificationCompat.getExtras
import android.os.Bundle
import android.util.Log


class HeadsetPlugReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent("android.intent.action.MAIN")
        if (intent.action.equals(Intent.ACTION_HEADSET_PLUG)) {
            when (intent.getIntExtra("state", -1)) {
                0 -> i.putExtra("state", "false")
                1 ->  i.putExtra("state", "true")
                else ->  i.putExtra("state", "unknow")
            }
        }
        context.sendBroadcast(i)

    }
}