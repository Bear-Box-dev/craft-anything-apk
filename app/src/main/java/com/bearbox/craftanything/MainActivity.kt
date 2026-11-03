package com.bearbox.craftanything

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Minimal Android foundation screen.
 *
 * This intentionally reports the native bridge state instead of exposing desktop features
 * that are not yet ported. The full project workflow will be added in later milestones.
 */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bridgeStatus: String
        val version: String
        val capabilities: String

        try {
            val bridge = NativeBridge()
            version = bridge.nativeVersion()
            capabilities = bridge.nativeCapabilitiesJson()
            bridgeStatus = "Rust engine bridge connected"
        } catch (error: UnsatisfiedLinkError) {
            version = "Unavailable"
            capabilities = "[]"
            bridgeStatus = "Rust bridge could not be loaded: ${error.message}"
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(28.dp(), 28.dp(), 28.dp(), 28.dp())
            setBackgroundColor(Color.rgb(14, 14, 17))
        }

        root.addView(label("CRAFT ANYTHING", 13f, Color.LTGRAY))
        root.addView(label("Android foundation", 28f, Color.WHITE))
        root.addView(label(bridgeStatus, 16f, Color.rgb(125, 211, 170)))
        root.addView(label("Native bridge: $version", 14f, Color.LTGRAY))
        root.addView(label("Capabilities: $capabilities", 13f, Color.LTGRAY))
        root.addView(
            label(
                "This is the first Android milestone. Desktop project-building and agent features " +
                    "are not available in this foundation build yet.",
                14f,
                Color.LTGRAY
            )
        )
        setContentView(root)
    }

    private fun label(value: String, size: Float, color: Int) =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            setPadding(0, 0, 0, 18.dp())
        }

    private fun Int.dp(): Int = (this * resources.displayMetrics.density).toInt()
}
