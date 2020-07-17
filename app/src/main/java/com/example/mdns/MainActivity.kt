package com.example.mdns

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mdns.adapters.ScanAdapter
import com.example.mdns.`object`.ScanObject
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener,
    MdnsAdvertiser.NewtorkAdvertiser, MdnsScanner.NetworkScanner,
    ScanResultListener.ScanResultListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnPublish -> {
                isPublishedClicked = true
                isScanClicked = false
                registerService()
            }
            R.id.btnScan -> if (btnScan!!.text.toString().equals("SCAN", ignoreCase = true)) {
                isPublishedClicked = false
                isScanClicked = true
                scan()
            }
        }
    }
    private var mNsdManager: NsdManager? = null
    private var isPublished: Boolean = false
    private var isScanning: Boolean = false
    private var isPublishedClicked: Boolean = false
    private var isScanClicked: Boolean = false
    private var scanAdapter: ScanAdapter? = null
    private var Mdnsscanner = MdnsScanner(this)
    private var Mdnsadvertiser = MdnsAdvertiser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init() {

        mNsdManager = AppUtils.initializeNSDManger(this)

        var progressLayout = progressLayout
        var btnScan = btnScan
        var btnPublish = btnPublish
        var listRecyclerview = listRecyclerview

        btnScan!!.setOnClickListener(this)
        btnPublish!!.setOnClickListener(this)

        listRecyclerview.layoutManager = LinearLayoutManager(this)
        listRecyclerview.setHasFixedSize(true)
        scanAdapter = ScanAdapter(this)
        listRecyclerview.adapter = scanAdapter
    }

    override fun onPause() {
        if (mNsdManager != null) {
            unRegisterService()
            stopScan() }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (mNsdManager != null) {
            if (isPublishedClicked) {
                progressLayout.visibility=View.VISIBLE
                registerService()
            }
            if (isScanClicked) {
                progressLayout.visibility=View.VISIBLE
                scan()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun scan() {
        if (!isScanning) {
            progressLayout.visibility=View.GONE
            isScanning = true
            scanAdapter!!.refreshAdapter()
            mNsdManager!!.discoverServices(
                AppConstant.SERVICE_TYPE,
                NsdManager.PROTOCOL_DNS_SD, Mdnsscanner
            )
        }
    }
    fun stopScan() {
        if (isScanning) {
            isScanning = false
            mNsdManager!!.stopServiceDiscovery(Mdnsscanner)
        }
    }
    // Register service
    fun registerService() {
        val serviceInfo = NsdServiceInfo()
        serviceInfo.serviceName = AppConstant.SERVICE_NAME
        serviceInfo.serviceType = AppConstant.SERVICE_TYPE
        serviceInfo.port = AppConstant.SERVICE_PORT
        if (!isPublished) {
            progressLayout.visibility=View.GONE
            isPublished = true
            mNsdManager!!.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD,
                Mdnsadvertiser
            )
        }
    }
    //Unregister service
    fun unRegisterService() {
        if (isPublished) {
            isPublished = false
            mNsdManager!!.unregisterService(Mdnsadvertiser)
        }
    }

    override fun onDeviceRegistration(message: String) {
        AppUtils.showToast(this, message)
    }


    override fun onSeriveFound(serviceInfo: NsdServiceInfo) {
        mNsdManager!!.resolveService(serviceInfo, ScanResultListener(this))
    }

    override fun discoveryStatus(message: String) {
        AppUtils.showToast(this, message)

    }

    override fun onDeviceFound(data: ScanObject) {
        runOnUiThread { scanAdapter!!.updateList(data) }
    }
}
