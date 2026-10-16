package com.example.adguardtv

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.VpnService
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var dnsButton: Button
    private lateinit var customDnsInput: EditText
    private lateinit var blockingStatusLayout: LinearLayout
    private lateinit var blockingText: TextView
    private var pulseAnimator: ObjectAnimator? = null

    private val prefChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == DnsManager.KEY_CONNECTED) {
            runOnUiThread {
                updateUIState()
            }
        }
    }

    private val vpnPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            startVpnService()
        } else {
            Toast.makeText(this, "VPN permission is required to enable AdGuard DNS", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dnsButton = findViewById(R.id.enableDnsButton)
        customDnsInput = findViewById(R.id.customDnsInput)
        blockingStatusLayout = findViewById(R.id.blockingStatusLayout)
        blockingText = findViewById(R.id.blockingText)

        // Restore saved custom DNS if available
        val savedDns = DnsManager.getCustomDns(this)
        if (!savedDns.isNullOrBlank()) {
            customDnsInput.setText(savedDns)
        }

        updateUIState()

        dnsButton.setOnClickListener {
            if (DnsManager.isConnected(this)) {
                disconnectDns()
            } else {
                val inputDns = customDnsInput.text.toString().trim()
                if (!DnsManager.isValidDnsIp(inputDns)) {
                    Toast.makeText(
                        this,
                        "Please enter a valid DNS IP address (e.g. 94.140.14.14) or leave blank for default",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                prepareAndStartVpn()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        DnsManager.registerPreferenceListener(this, prefChangeListener)
        updateUIState()
    }

    override fun onStop() {
        super.onStop()
        DnsManager.unregisterPreferenceListener(this, prefChangeListener)
        stopPulseAnimation()
    }

    override fun onDestroy() {
        stopPulseAnimation()
        super.onDestroy()
    }

    private fun updateUIState() {
        val connected = DnsManager.isConnected(this)
        if (connected) {
            dnsButton.text = getString(R.string.disconnect)
            customDnsInput.isEnabled = false
            blockingStatusLayout.visibility = View.VISIBLE
            startPulseAnimation()
        } else {
            dnsButton.text = getString(R.string.enable_dns)
            customDnsInput.isEnabled = true
            blockingStatusLayout.visibility = View.GONE
            stopPulseAnimation()
        }
    }

    private fun startPulseAnimation() {
        if (pulseAnimator?.isRunning == true) return
        stopPulseAnimation()

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
        if (::blockingText.isInitialized) {
            blockingText.scaleX = 1.0f
            blockingText.scaleY = 1.0f
        }
    }

    private fun prepareAndStartVpn() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            vpnPermissionLauncher.launch(intent)
        } else {
            startVpnService()
        }
    }

    private fun startVpnService() {
        val dns = customDnsInput.text.toString().trim()
        DnsManager.setCustomDns(this, dns.ifEmpty { null })

        val intent = Intent(this, AdGuardVpnService::class.java).apply {
            putExtra(AdGuardVpnService.EXTRA_CUSTOM_DNS, dns)
        }
        startService(intent)
    }

    private fun disconnectDns() {
        val intent = Intent(this, AdGuardVpnService::class.java).apply {
            action = AdGuardVpnService.ACTION_DISCONNECT
        }
        startService(intent)
    }
}