package com.example.adguardtv

import android.content.Context
import android.content.SharedPreferences

object DnsManager {
    private const val PREFS_NAME = "dns_prefs"
    const val KEY_CONNECTED = "is_connected"
    const val KEY_CUSTOM_DNS = "custom_dns"

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
            .putString(KEY_CUSTOM_DNS, dns?.trim())
            .apply()
    }

    fun registerPreferenceListener(context: Context, listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterPreferenceListener(context: Context, listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .unregisterOnSharedPreferenceChangeListener(listener)
    }

    /**
     * Validates whether a given DNS IP is a valid numeric IPv4 or IPv6 address.
     * Null or blank strings are considered valid (app will use default AdGuard DNS).
     */
    fun isValidDnsIp(ip: String?): Boolean {
        if (ip.isNullOrBlank()) return true
        val trimmed = ip.trim()
        val parts = trimmed.split(".")
        if (parts.size == 4) {
            return parts.all { part ->
                part.toIntOrNull()?.let { it in 0..255 && (part == "0" || !part.startsWith("0")) } ?: false
            }
        }
        if (trimmed.contains(":") && trimmed.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' || it == ':' }) {
            val colons = trimmed.count { it == ':' }
            if (colons in 2..7) return true
        }
        return false
    }
}