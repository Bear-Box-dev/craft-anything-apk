# Android hybrid port

## Decision

Build an Android application with a native Kotlin/Jetpack Compose presentation layer and a Rust
library for portable Craft Anything logic. Connect the two through a small, versioned JNI boundary.
Do not package the existing desktop executable in a WebView and do not claim that the current
WebAssembly viewer is the full application.

This is a staged port, not a promise that every desktop feature can run on Android unchanged.

## Repository audit

The current workspace is a desktop-first Rust application:

- `apps/craft-anything` is the eframe desktop application and enables desktop window-system features.
- `apps/craft-anything-web` is explicitly a browser file viewer. Its dependencies exclude the native
  engine, platform services and AI agent.
- `craft-anything-engine` currently depends on `craft-anything-platform`.
- `craft-anything-platform` uses process management and PTY crates (`process-wrap`,
  `portable-pty`), desktop capture support (`xcap` on Windows/macOS), and Unix process APIs.
- The documented build/release workflows currently target desktop platforms and the browser viewer;
  there is no Android Gradle project or Android APK workflow.

These are material constraints. An Android target cannot be achieved safely by merely adding
`aarch64-linux-android` to the existing desktop build matrix.

## Target architecture

```text
Android app (Kotlin + Jetpack Compose)
  ├── Screens, navigation, permissions, lifecycle and Android file picker
  ├── Android storage adapter (app-scoped workspace; Storage Access Framework)
  ├── Remote coding-agent connection (initially; no bundled desktop CLI assumption)
  └── JNI boundary
        └── Rust Android library
              ├── portable analysis / evidence / format logic
              ├── Android-safe application services
              └── capability interface
                    ├── supported on Android
                    └── unavailable / remote-required
```

The JNI API must be deliberately small and stable. Prefer opaque session handles and JSON request /
response envelopes with explicit error codes over exposing Rust structs directly to Kotlin.
Do not pass raw pointers or Rust-owned buffers across the boundary without an explicit ownership
contract.

## Porting rules

1. Keep portable analysis and evidence logic in Rust; avoid duplicating it in Kotlin.
2. Split platform services behind traits before compiling the engine for Android. Android-specific
   implementations must not depend on desktop process, PTY, screenshot or window APIs.
3. Treat command execution and coding-agent access as explicit capabilities. Android app sandboxing
   and background execution limits mean desktop process assumptions are not valid.
4. Start with app-private project storage. Add user-selected document access through Android's
   Storage Access Framework; do not request broad filesystem access.
5. Never embed provider API keys or a user's Claude/Codex credentials in the APK. Use an explicitly
   configured remote agent or a supported user-controlled local/remote integration.
6. Expose unsupported features honestly in the UI rather than rendering non-functional controls.
7. Keep desktop and web builds unchanged throughout the port.

## Milestones

### M0 — Android foundation

- Add an Android application module and a minimal Compose shell.
- Add a Rust Android bridge crate with a versioned JNI API and a health/capability handshake.
- Build a debug APK in GitHub Actions for arm64-v8a.
- Document local prerequisites and how to retrieve the CI artifact.

Acceptance: the APK installs and launches on an arm64 Android device; the native library loads;
the Kotlin screen can call the Rust bridge and display its version/capabilities.

### M1 — Portable Rust core

- Audit crate-by-crate Android compilation.
- Extract platform-independent analysis, evidence and format operations from desktop services.
- Add Android-safe storage and lifecycle-aware session management.
- Add Rust unit tests and Android instrumentation tests.

Acceptance: a real file can be selected on Android and analyzed by the Rust core without desktop
services or a network connection.

### M2 — Projects and agent workflow

- Implement project creation, progress, approval and result screens in Compose.
- Define a remote/ACP agent transport with explicit authentication and connection status.
- Implement user-approved project file access and command execution policy.
- Add cancellation, process/connection recovery and background/foreground handling.

Acceptance: a small project can be created, built through a configured agent, inspected and reopened
from Android, with approvals and errors surfaced clearly.

### M3 — Feature parity expansion

- Port supported inspection tools one capability at a time.
- Add device-specific performance and memory testing.
- Publish signed release APKs/AABs only after signing and release ownership are configured.

## Out of scope for the first APK

- Bundling Claude Code, Codex or another desktop coding CLI.
- Promising unrestricted shell access or desktop-equivalent process management.
- Claiming that the browser viewer provides the full Craft Anything engine.
- Replacing the existing desktop UI or changing desktop release behavior.

## Definition of done for M0

- `android/` contains a reproducible Gradle project.
- Rust JNI library builds for `arm64-v8a`.
- CI produces an installable debug APK artifact.
- The app displays bridge health and capability information.
- No secrets are committed; no existing desktop workflow is modified.
