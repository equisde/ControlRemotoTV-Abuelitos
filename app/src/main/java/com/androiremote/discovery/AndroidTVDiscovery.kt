package com.androiremote.discovery

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log

class AndroidTVDiscovery(context: Context, private val onDeviceFound: (NsdServiceInfo) -> Unit) {
    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private val serviceType = "_androidtvremote2._tcp."

    private val discoveryListener = object : NsdManager.DiscoveryListener {
        override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
            Log.e("Discovery", "Discovery failed: $errorCode")
        }

        override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
            Log.e("Discovery", "Stop Discovery failed: $errorCode")
        }

        override fun onDiscoveryStarted(serviceType: String?) {
            Log.d("Discovery", "Discovery started")
        }

        override fun onDiscoveryStopped(serviceType: String?) {
            Log.d("Discovery", "Discovery stopped")
        }

        override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
            Log.d("Discovery", "Service found: ${serviceInfo?.serviceName}")
            if (serviceInfo?.serviceType == serviceType) {
                nsdManager.resolveService(serviceInfo, object : NsdManager.ResolveListener {
                    override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
                        Log.e("Discovery", "Resolve failed: $errorCode")
                    }

                    override fun onServiceResolved(resolvedServiceInfo: NsdServiceInfo?) {
                        resolvedServiceInfo?.let { onDeviceFound(it) }
                    }
                })
            }
        }

        override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
            Log.d("Discovery", "Service lost: ${serviceInfo?.serviceName}")
        }
    }

    fun start() {
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    fun stop() {
        nsdManager.stopServiceDiscovery(discoveryListener)
    }
}
