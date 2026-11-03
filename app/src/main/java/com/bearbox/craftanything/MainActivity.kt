package com.bearbox.craftanything

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Minimal Compose foundation screen.
 *
 * This reports native bridge health instead of exposing desktop features that have not
 * been ported to Android yet.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bridgeStatus: String
        val version: String
        val capabilities: String

        try {
            val bridge = NativeBridge()
            version = bridge.nativeVersion()
            capabilities = bridge.nativeCapabilitiesJson()
            bridgeStatus = "Rust bridge connected"
        } catch (error: UnsatisfiedLinkError) {
            version = "Unavailable"
            capabilities = "[]"
            bridgeStatus = "Rust bridge unavailable: ${error.message}"
        }

        setContent {
            MaterialTheme {
                FoundationScreen(
                    bridgeStatus = bridgeStatus,
                    version = version,
                    capabilities = capabilities
                )
            }
        }
    }
}

@Composable
private fun FoundationScreen(
    bridgeStatus: String,
    version: String,
    capabilities: String
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0E0E11),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("CRAFT ANYTHING", color = Color.LightGray, fontSize = 13.sp)
            Text(
                "Android foundation",
                modifier = Modifier.padding(top = 12.dp, bottom = 20.dp),
                fontSize = 28.sp
            )
            Text(bridgeStatus, color = Color(0xFF7DD3AA))
            Text(
                "Native bridge: $version",
                modifier = Modifier.padding(top = 12.dp),
                color = Color.LightGray
            )
            Text(
                "Capabilities: $capabilities",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.LightGray,
                fontSize = 12.sp
            )
            Text(
                "This is the first Android milestone. Desktop project-building and AI agent " +
                    "features are not available in this foundation build yet.",
                modifier = Modifier.padding(top = 24.dp),
                color = Color.LightGray
            )
        }
    }
}
