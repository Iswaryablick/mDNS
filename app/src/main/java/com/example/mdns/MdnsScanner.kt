package com.example.mdns

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo

/**
 * Created by Iswarya on 17/07/20
 */
class MdnsScanner(scanning: NetworkScanner) : NsdManager.DiscoveryListener {
    private var scanning: NetworkScanner? = null

    init {
        this.scanning = scanning
    }

    interface NetworkScanner {
        fun onSeriveFound(serviceInfo: NsdServiceInfo)

        fun discoveryStatus(message: String)
    }

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
        scanning!!.discoveryStatus("Start Scan failed")

    }

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
        scanning!!.discoveryStatus("Stop Scan failed")
    }

    override fun onDiscoveryStarted(serviceType: String) {
        scanning!!.discoveryStatus("Scan started")


    }

    override fun onDiscoveryStopped(serviceType: String) {
        scanning!!.discoveryStatus("Scan stop")

    }

    override fun onServiceFound(serviceInfo: NsdServiceInfo) {
        scanning!!.onSeriveFound(serviceInfo)
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo) {
        scanning!!.discoveryStatus("Service lost")

    }
}
