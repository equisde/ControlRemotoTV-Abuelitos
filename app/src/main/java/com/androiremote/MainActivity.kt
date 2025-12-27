package com.androiremote

import android.content.Context
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androiremote.discovery.AndroidTVDiscovery
import com.androiremote.protocol.RemoteProtocolManager
import com.androiremote.ui.theme.AndroiRemoteTheme

enum class AppState {
    WIZARD_DISCOVERY,
    WIZARD_PAIRING,
    REMOTE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroiRemoteTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("remote_prefs", Context.MODE_PRIVATE) }
    val savedIp = prefs.getString("saved_ip", null)
    
    var currentState by remember { mutableStateOf(if (savedIp == null) AppState.WIZARD_DISCOVERY else AppState.REMOTE) }
    var discoveredDevices by remember { mutableStateOf(setOf<NsdServiceInfo>()) }
    var selectedDevice by remember { mutableStateOf<NsdServiceInfo?>(null) }
    var pairingCode by remember { mutableStateOf("") }

    val discovery = remember {
        AndroidTVDiscovery(context) { device ->
            discoveredDevices = discoveredDevices + device
        }
    }

    val protocolManager = remember { RemoteProtocolManager(context) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        when (currentState) {
            AppState.WIZARD_DISCOVERY -> {
                DiscoveryView(
                    devices = discoveredDevices.toList(),
                    onStartDiscovery = { discovery.start() },
                    onDeviceSelected = { device ->
                        discovery.stop()
                        selectedDevice = device
                        protocolManager.startPairing(device.host.hostAddress ?: "") {
                            currentState = AppState.WIZARD_PAIRING
                        }
                    }
                )
            }
            AppState.WIZARD_PAIRING -> {
                PairingView(
                    onCodeEntered = { code ->
                        protocolManager.finishPairing(code) {
                            prefs.edit().putString("saved_ip", selectedDevice?.host?.hostAddress).apply()
                            currentState = AppState.REMOTE
                        }
                    }
                )
            }
            AppState.REMOTE -> {
                RemoteView(
                    onCommand = { cmd -> protocolManager.sendCommand(cmd) },
                    onReset = {
                        prefs.edit().clear().apply()
                        currentState = AppState.WIZARD_DISCOVERY
                    }
                )
            }
        }
    }
}

@Composable
fun DiscoveryView(devices: List<NsdServiceInfo>, onStartDiscovery: () -> Unit, onDeviceSelected: (NsdServiceInfo) -> Unit) {
    LaunchedEffect(Unit) { onStartDiscovery() }
    
    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.setup_title), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.setup_desc), style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        
        if (devices.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp), strokeWidth = 6.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.searching), style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn {
                items(devices) { device ->
                    Button(
                        onClick = { onDeviceSelected(device) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        contentPadding = PaddingValues(24.dp)
                    ) {
                        Text(device.serviceName, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun PairingView(onCodeEntered: (String) -> Unit) {
    var code by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.pairing_code), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        
        TextField(
            value = code,
            onValueChange = { if (it.length <= 6) code = it },
            textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { if (code.length >= 4) onCodeEntered(code) },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(24.dp)
        ) {
            Text(stringResource(R.string.connect), fontSize = 24.sp)
        }
    }
}

@Composable
fun RemoteView(onCommand: (String) -> Unit, onReset: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            RemoteButton(stringResource(R.string.btn_power), Color.Red, Modifier.weight(1f)) { onCommand("POWER") }
            Spacer(modifier = Modifier.width(8.dp))
            RemoteButton("RECONFIGURAR", Color.Gray, Modifier.weight(1f)) { onReset() }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // D-PAD Gigante para abuelitos
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            RemoteButton(stringResource(R.string.btn_up), MaterialTheme.colorScheme.primary, Modifier.size(120.dp)) { onCommand("UP") }
            Row {
                RemoteButton(stringResource(R.string.btn_left), MaterialTheme.colorScheme.primary, Modifier.size(120.dp)) { onCommand("LEFT") }
                Spacer(modifier = Modifier.width(8.dp))
                RemoteButton(stringResource(R.string.btn_ok), Color(0xFF4CAF50), Modifier.size(120.dp)) { onCommand("OK") }
                Spacer(modifier = Modifier.width(8.dp))
                RemoteButton(stringResource(R.string.btn_right), MaterialTheme.colorScheme.primary, Modifier.size(120.dp)) { onCommand("RIGHT") }
            }
            RemoteButton(stringResource(R.string.btn_down), MaterialTheme.colorScheme.primary, Modifier.size(120.dp)) { onCommand("DOWN") }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Row(modifier = Modifier.fillMaxWidth()) {
            RemoteButton(stringResource(R.string.btn_back), Color(0xFFFF9800), Modifier.weight(1f)) { onCommand("BACK") }
            Spacer(modifier = Modifier.width(16.dp))
            RemoteButton(stringResource(R.string.btn_home), Color(0xFF2196F3), Modifier.weight(1f)) { onCommand("HOME") }
        }
    }
}

@Composable
fun RemoteButton(text: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
    }
}
