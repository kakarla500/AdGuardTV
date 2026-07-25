package com.example.adguardtv

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var dnsButton: Button
    private lateinit var customDnsInput: EditText
    private lateinit var blockingStatusLayout: LinearLayout
    private var pulseAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dnsButton = findViewById(R.id.enableDnsButton)
        customDnsInput = findViewById(R.id.customDnsInput)
        blockingStatusLayout = findViewById(R.id.blockingStatusLayout)

        updateUIState()

        dnsButton.setOnClickListener {
            if (DnsManager.isConnected(this)) {
                disconnectDns()
            } else {
                prepareAndStartVpn()
            }
        }
    }

    private fun updateUIState() {
        val connected = DnsManager.isConnected(this)
        if (connected) {
            dnsButton.text = "Disconnect"
            customDnsInput.isEnabled = false
            blockingStatusLayout.visibility = View.VISIBLE
            startPulseAnimation()
        } else {
            dnsButton.text = "Enable AdGuard DNS"
            customDnsInput.isEnabled = true
            blockingStatusLayout.visibility = View.GONE
            stopPulseAnimation()
        }
    }

    private fun startPulseAnimation() {
        val blockingText = findViewById<View>(R.id.blockingText)
        pulseAnimator = ObjectAnimator.ofPropertyValuesHolder(
            blockingText,
            PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.1f),
            PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.1f)
        ).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }

    private fun stopPulseAnimation() {
        pulseAnimator?.cancel()
        pulseAnimator = null
    }

    private fun prepareAndStartVpn() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, VPN_REQUEST_CODE)
        } else {
            onActivityResult(VPN_REQUEST_CODE, Activity.RESULT_OK, null)
        }
    }

    private fun disconnectDns() {
        val intent = Intent(this, AdGuardVpnService::class.java)
        intent.action = AdGuardVpnService.ACTION_DISCONNECT
        startService(intent)
        DnsManager.setConnected(this, false)
        updateUIState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VPN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val dns = customDnsInput.text.toString()
            DnsManager.setCustomDns(this, dns)
            
            val intent = Intent(this, AdGuardVpnService::class.java).apply {
                putExtra(AdGuardVpnService.EXTRA_CUSTOM_DNS, dns)
            }
            startService(intent)
            updateUIState()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUIState()
    }

    companion object {
        private const val VPN_REQUEST_CODE = 1001
    }
}