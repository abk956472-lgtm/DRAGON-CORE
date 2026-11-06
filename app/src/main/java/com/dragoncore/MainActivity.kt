
package com.dragoncore

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var statusText: TextView
    private lateinit var logText: TextView
    private lateinit var timerText: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var monitoring = false
    private var secondsLeft = 300

    private val monitorTask = object : Runnable {
        override fun run() {
            if (!monitoring) return

            checkNetwork()
            secondsLeft--

            timerText.text = "TIME LEFT: ${secondsLeft / 60}:${(secondsLeft % 60).toString().padStart(2, '0')}"

            if (secondsLeft <= 0) {
                stopMonitoring()
                addLog("Emergency monitoring finished.")
            } else {
                handler.postDelayed(this, 5000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(24, 40, 24, 24)
            setBackgroundColor(Color.rgb(5, 12, 25))
        }

        val title = TextView(this).apply {
            text = "DRAGON CORE"
            textSize = 30f
            setTextColor(Color.CYAN)
            gravity = Gravity.CENTER
        }

        statusText = TextView(this).apply {
            text = "STATUS: UNKNOWN"
            textSize = 22f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        timerText = TextView(this).apply {
            text = "TIME LEFT: 05:00"
            textSize = 18f
            setTextColor(Color.CYAN)
            gravity = Gravity.CENTER
        }

        logText = TextView(this).apply {
            text = "DRAGON CORE SYSTEM READY\n"
            textSize = 14f
            setTextColor(Color.WHITE)
        }

        val scanButton = makeButton("SCAN NETWORK")
        val startButton = makeButton("EMERGENCY MODE")
        val stopButton = makeButton("STOP SESSION")

        scanButton.setOnClickListener {
            checkNetwork()
        }

        startButton.setOnClickListener {
            startMonitoring()
        }

        stopButton.setOnClickListener {
            stopMonitoring()
        }

        root.addView(title)
        root.addView(statusText)
        root.addView(timerText)
        root.addView(scanButton)
        root.addView(startButton)
        root.addView(stopButton)

        val scroll = ScrollView(this)
        scroll.addView(logText)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
        checkNetwork()
    }

    private fun makeButton(label: String): Button {
        return Button(this).apply {
            text = label
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(0, 90, 150))
        }
    }

    private fun checkNetwork() {
        val manager = getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val network = manager.activeNetwork
        val capabilities = manager.getNetworkCapabilities(network)

        val online = capabilities?.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        ) == true && capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        )

        if (online) {
            statusText.text = "STATUS: ONLINE"
            statusText.setTextColor(Color.GREEN)
            addLog("Network connection available.")
        } else {
            statusText.text = "STATUS: OFFLINE"
            statusText.setTextColor(Color.RED)
            addLog("No validated internet connection.")
        }
    }

    private fun startMonitoring() {
        if (monitoring) return

        monitoring = true
        secondsLeft = 300
        addLog("Emergency monitoring started.")
        handler.post(monitorTask)
    }

    private fun stopMonitoring() {
        monitoring = false
        handler.removeCallbacks(monitorTask)
        timerText.text = "TIME LEFT: STOPPED"
        addLog("Monitoring stopped.")
    }

    private fun addLog(message: String) {
        logText.append("$message\n")
    }

    override fun onDestroy() {
        handler.removeCallbacks(monitorTask)
        super.onDestroy()
    }
}
