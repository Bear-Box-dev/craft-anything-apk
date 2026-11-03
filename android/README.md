# Android port

The Android application is being developed as a hybrid port: Kotlin/Jetpack Compose for the Android
experience and a Rust library for portable Craft Anything functionality, connected through JNI.

The existing desktop application and browser file viewer are not Android APKs. The Android port is
staged because the current engine still depends on desktop-oriented process, PTY and capture
services. See [the Android hybrid port plan](../docs/android-hybrid-port.md) for the architecture,
known constraints and acceptance criteria.

The first milestone is an installable arm64 debug APK that validates the Android shell and Rust
bridge. It is not yet feature parity with the desktop application.
