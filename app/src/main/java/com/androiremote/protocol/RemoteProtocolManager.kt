package com.androiremote.protocol

import android.content.Context
import android.util.Log
import com.google.android.remote.protocol.RemoteMessageOuterClass
import com.google.protobuf.ByteString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger
import java.net.Socket
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Date
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RemoteProtocolManager(private val context: Context) {

    private var socket: SSLSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    // Generar certificados y llaves (Simulado/Simplificado para el ejemplo)
    // En una app real, esto se guarda en Keystore
    private fun createSSLContext(): SSLContext {
        val keyPair = generateKeyPair()
        val certificate = generateSelfSignedCertificate(keyPair)
        
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)
        keyStore.setKeyEntry("client", keyPair.private, "password".toCharArray(), arrayOf(certificate))

        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, "password".toCharArray())

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(keyManagerFactory.keyManagers, trustAllCerts, SecureRandom())
        return sslContext
    }

    private fun generateKeyPair(): KeyPair {
        val kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(2048)
        return kpg.generateKeyPair()
    }

    private fun generateSelfSignedCertificate(keyPair: KeyPair): X509Certificate {
        val start = Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24)
        val end = Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 10)
        val serial = BigInteger.valueOf(System.currentTimeMillis())
        val owner = X500Name("CN=AndroiRemote")
        
        val builder = JcaX509v3CertificateBuilder(owner, serial, start, end, owner, keyPair.public)
        val signer = JcaContentSignerBuilder("SHA256WithRSAEncryption").build(keyPair.private)
        val certHolder = builder.build(signer)
        
        return JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder)
    }

    fun startPairing(ip: String, onCodeRequired: () -> Unit) {
        scope.launch {
            try {
                Log.d("Protocol", "Connecting to $ip:6466")
                val sslContext = createSSLContext()
                val factory = sslContext.socketFactory
                socket = factory.createSocket(ip, 6466) as SSLSocket // Port usually 6466 or 6467 for v2
                socket?.startHandshake()
                
                outputStream = socket?.outputStream
                inputStream = socket?.inputStream

                // Send Initial Pairing Request (Simplified)
                // In real protocol, you exchange public keys here differently.
                // sending dummy config message to trigger response
                val configMsg = RemoteMessageOuterClass.RemoteMessage.newBuilder()
                    .setRemoteConfigure(
                        RemoteMessageOuterClass.RemoteConfigureMessage.newBuilder()
                            .setCode1(622)
                            .setDeviceInfo(
                                RemoteMessageOuterClass.RemoteDeviceInfo.newBuilder()
                                    .setModel("AndroiRemote")
                                    .setVendor("Termux")
                                    .setAppVersion("1.0.0")
                                    .setPackageName("com.androiremote")
                                    .build()
                            )
                            .build()
                    )
                    .build()
                
                sendMessage(configMsg)
                
                // Listening for response...
                // In a full implementation, we would parse the response and wait for SECRET challenge.
                // Assuming successful connection triggers code requirement for this flow.
                withContext(Dispatchers.Main) {
                    onCodeRequired()
                }

            } catch (e: Exception) {
                Log.e("Protocol", "Error pairing", e)
            }
        }
    }

    fun finishPairing(code: String, onSuccess: () -> Unit) {
        scope.launch {
            try {
                // Enviar el código de emparejamiento usando Protobuf
                val pairingMsg = RemoteMessageOuterClass.RemoteMessage.newBuilder()
                    .setRemoteSetPairingCode(
                        RemoteMessageOuterClass.RemoteSetPairingCodeMessage.newBuilder()
                            .setCode(code)
                            .build()
                    )
                    .build()
                
                sendMessage(pairingMsg)
                
                // Wait for success confirmation...
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                 Log.e("Protocol", "Error sending code", e)
            }
        }
    }

    fun sendCommand(commandKey: String) {
        scope.launch {
            try {
                val keyEvent = when(commandKey) {
                    "UP" -> 19 // KEYCODE_DPAD_UP
                    "DOWN" -> 20
                    "LEFT" -> 21
                    "RIGHT" -> 22
                    "OK" -> 23
                    "BACK" -> 4
                    "HOME" -> 3
                    "POWER" -> 26
                    else -> 0
                }

                if (keyEvent != 0) {
                    // DOWN event
                    val msgDown = RemoteMessageOuterClass.RemoteMessage.newBuilder()
                        .setRemoteKey(
                            RemoteMessageOuterClass.RemoteKeyMessage.newBuilder()
                                .setKeyCode(keyEvent)
                                .setAction(0) // DOWN
                                .build()
                        )
                        .build()
                    sendMessage(msgDown)

                    // UP event
                    val msgUp = RemoteMessageOuterClass.RemoteMessage.newBuilder()
                        .setRemoteKey(
                            RemoteMessageOuterClass.RemoteKeyMessage.newBuilder()
                                .setKeyCode(keyEvent)
                                .setAction(1) // UP
                                .build()
                        )
                        .build()
                    sendMessage(msgUp)
                }
            } catch (e: Exception) {
                Log.e("Protocol", "Error sending command", e)
            }
        }
    }

    private fun sendMessage(msg: RemoteMessageOuterClass.RemoteMessage) {
        val bytes = msg.toByteArray()
        // Simple framing: [Length (1 byte)][Bytes] - Real protocol uses varint framing
        // Protocol Buffers usually handle this if using writeDelimitedTo
        msg.writeDelimitedTo(outputStream)
        outputStream?.flush()
    }
}