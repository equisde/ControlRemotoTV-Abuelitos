package com.androiremote.protocol

import android.content.Context
import android.util.Log

class RemoteProtocolManager(private val context: Context) {
    // Aquí iría la lógica pesada de TLS y mTLS para v2.
    // Por ahora simularemos la conexión y el envío de comandos.

    fun startPairing(ip: String, onCodeRequired: () -> Unit) {
        Log.d("Protocol", "Starting pairing with $ip")
        // Simulación: en v2 se inicia un handshake TLS
        onCodeRequired()
    }

    fun finishPairing(code: String, onSuccess: () -> Unit) {
        Log.d("Protocol", "Finishing pairing with code $code")
        // Simulación: se valida el certificado y se guarda
        onSuccess()
    }

    fun sendCommand(command: String) {
        Log.d("Protocol", "Sending command: $command")
        // Simulación: envío de bytes protobuf por el socket TLS
    }
}
