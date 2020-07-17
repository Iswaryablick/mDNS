package com.example.mdns

import android.annotation.SuppressLint

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo


import com.example.mdns.`object`.ScanObject

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Iswarya on 17/07/20
 */
class ScanResultListener(scanResultListener: ScanResultListener) : NsdManager.ResolveListener {
    private var scanResultListener: ScanResultListener? = null
    private var observable: Observable<ScanObject>? = null

    interface ScanResultListener {
        fun onDeviceFound(data: ScanObject)
    }

    init {
        this.scanResultListener = scanResultListener
    }

    override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
    }

    @SuppressLint("CheckResult")
    override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
        val data = ScanObject()
        data.hostAddress = serviceInfo.host
        data.servicePort = serviceInfo.port
        data.serviceName = serviceInfo.serviceName
        data.serviceType = serviceInfo.serviceType

        observable = Observable.just(data)
        observable!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result -> scanResultListener!!.onDeviceFound(result) }

    }
}
