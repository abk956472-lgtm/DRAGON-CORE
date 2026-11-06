
package com.dragoncore

import android.app.Activity
import android.os.Bundle
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

    private lateinit var results: LinearLayout
    private lateinit var summary: TextView

    private val cyan = Color.rgb(0, 220, 255)
    private val dark = Color.rgb(5, 12, 25)
    private val green = Color.rgb(0, 230, 120)
    private val red = Color.rgb(255, 75, 75)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(24, 35, 24, 20)
            setBackgroundColor(dark)
        }

        val title = TextView(this).apply {
            text = "DRAGON CORE"
            textSize = 29f
            gravity = Gravity.CENTER
            setTextColor(cyan)
        }

        val subtitle = TextView(this).apply {
            text = "NETWORK SCANNER"
            textSize = 16f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
        }

        summary = TextView(this).apply {
            text = "READY TO SCAN"
            textSize = 17f
            gravity = Gravity.CENTER
            setTextColor(green)
            setPadding(0, 25, 0, 20)
        }

        val scanButton = Button(this).apply {
            text = "SCAN NETWORKS"
            setOnClickListener { scanNetworks() }
        }

        val scroll = ScrollView(this)
        results = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        scroll.addView(results)

        root.addView(title)
        root.addView(subtitle)
        root.addView(summary)
        root.addView(scanButton)
        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
        scanNetworks()
    }

    private fun scanNetworks() {
        results.removeAllViews()

        val manager = getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val networks = manager.allNetworks
        var count = 0

        for (network in networks) {
            val caps = manager.getNetworkCapabilities(network)
                ?: continue

            val types = mutableListOf<String>()

            if (caps.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )) types.add("Wi-Fi")

            if (caps.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                )) types.add("Mobile Data")

            if (caps.hasTransport(
                    NetworkCapabilities.TRANSPORT_ETHERNET
                )) types.add("Ethernet")

            if (caps.hasTransport(
                    NetworkCapabilities.TRANSPORT_VPN
                )) types.add("VPN")

            if (caps.hasTransport(
                    NetworkCapabilities.TRANSPORT_BLUETOOTH
                )) types.add("Bluetooth")

            if (types.isEmpty()) continue

            count++

            val internet = caps.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )

            val validated = caps.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_VALIDATED
            )

            val text = TextView(this).apply {
                this.text = buildString {
                    append(types.joinToString(" + "))
                    append("\n")
                    append(
                        if (validated) "INTERNET AVAILABLE"
                        else if (internet) "INTERNET NOT VERIFIED"
                        else "NO INTERNET CAPABILITY"
                    )
                    append("\n--------------------------")
                }

                textSize = 16f
                setTextColor(if (validated) green else red)
                setPadding(12, 20, 12, 20)
            }

            results.addView(text)
        }

        summary.text = "NETWORKS FOUND: $count"

        if (count == 0) {
            val empty = TextView(this).apply {
                text = "NO NETWORKS DETECTED"
                textSize = 17f
                setTextColor(red)
                gravity = Gravity.CENTER
                setPadding(10, 30, 10, 30)
            }
            results.addView(empty)
        }
    }
}
