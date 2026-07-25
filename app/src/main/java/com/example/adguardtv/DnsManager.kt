package com.example.adguardtv

import android.content.Context

object DnsManager {
    private const val PREFS_NAME = "dns_prefs"
    private const val KEY_CONNECTED = "is_connected"
    private const val KEY_CUSTOM_DNS = "custom_dns"

    fun isConnected(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_CONNECTED, false)
    }

    fun setConnected(context: Context, connected: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_CONNECTED, connected)
            .apply()
    }

    fun getCustomDns(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_CUSTOM_DNS, null)
    }

    fun setCustomDns(context: Context, dns: String?) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CUSTOM_DNS, dns)
            .apply()
    }
}