package com.example.mdns

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo

/**
 * Created by Iswarya on 17/07/20
 */
class MdnsAdvertiser(advertiser: NewtorkAdvertiser) :
    NsdManager.RegistrationListener {

    private var observer: NewtorkAdvertiser? = null

    interface NewtorkAdvertiser {
        fun onDeviceRegistration(message: String)
    }

    init {
        this.observer = advertiser
    }

    override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        observer!!.onDeviceRegistration("Failed to register")

    }

    override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        observer!!.onDeviceRegistration("Failed to unregister")

    }

    override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
        observer!!.onDeviceRegistration("Service registered")
    }

    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
        observer!!.onDeviceRegistration("Service unregistered")

    }
}
