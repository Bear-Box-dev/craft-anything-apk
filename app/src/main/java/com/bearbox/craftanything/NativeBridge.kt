package com.bearbox.craftanything

/**
 * Stable, deliberately small JNI boundary. Keep Android framework types out of the Rust API.
 * The native library is built from android/rust-bridge for arm64-v8a.
 */
class NativeBridge {
    external fun nativeVersion(): String
    external fun nativeCapabilitiesJson(): String

    companion object {
        init {
            System.loadLibrary("craft_anything_android")
        }
    }
}
