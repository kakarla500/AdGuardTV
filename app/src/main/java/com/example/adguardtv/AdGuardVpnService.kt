package com.example.adguardtv

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor

class AdGuardVpnService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_DISCONNECT) {
            stopVpn()
            return START_NOT_STICKY
        }
        
        val customDns = intent?.getStringExtra(EXTRA_CUSTOM_DNS)
        startVpn(customDns)
        return START_STICKY
    }

    private fun startVpn(customDns: String?) {
        if (vpnInterface != null) return

        try {
            val builder = Builder()
                .setSession("AdGuard for TV")
                .addAddress("10.0.0.2", 32)
            
            if (!customDns.isNullOrBlank()) {
                builder.addDnsServer(customDns)
            } else {
                builder.addDnsServer("94.140.14.14")
                builder.addDnsServer("94.140.15.15")
            }
            
            vpnInterface = builder.establish()
            DnsManager.setConnected(this, true)
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf()
        }
    }

    private fun stopVpn() {
        try {
            vpnInterface?.close()
            vpnInterface = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        DnsManager.setConnected(this, false)
        stopSelf()
    }

    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }

    companion object {
        const val ACTION_DISCONNECT = "com.example.adguardtv.STOP"
        const val EXTRA_CUSTOM_DNS = "extra_custom_dns"
    }
}