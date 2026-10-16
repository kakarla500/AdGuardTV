# AdGuard TV

<p align="center">
  <img src="logo.jpg" alt="AdGuard TV Logo" width="180" height="180" style="border-radius: 24px;" />
</p>

<p align="center">
  <strong>Fast, lightweight, and system-wide ad-blocking solution engineered specifically for Android TV.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android%20TV-3DDC84.svg?style=flat&logo=android" alt="Android TV" />
  <img src="https://img.shields.io/badge/Min%20SDK-25%20(Android%207.1+)-informational.svg?style=flat" alt="Min SDK" />
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue.svg?style=flat" alt="Target SDK" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF.svg?style=flat&logo=kotlin" alt="Kotlin" />
  <img src="https://img.shields.io/badge/License-Apache%202.0-orange.svg?style=flat" alt="License" />
</p>

---

## 📺 Overview

**AdGuard TV** is an open-source, non-root DNS-level ad-blocking application customized for the **Android TV (Leanback)** ecosystem. It creates a local-only VPN tunnel dedicated to DNS queries, enforcing AdGuard DNS or any custom DNS provider across your entire television—blocking banner ads, video promotions, tracking domains, and analytics on both **Wi-Fi** and **Ethernet** connections.

---

## ⚡ What AdGuard TV is Capable Of

### 🛡️ 1. System-Wide Ad & Tracker Blocking
- **Network-Level Filtering**: Intercepts domain name resolutions before an application can connect to ad-serving content delivery networks (CDNs).
- **Protects All TV Apps**: Works across streaming apps, web browsers, IPTV players, and sideloaded apps.
- **Privacy & Telemetry Shield**: Prevents smart TV analytics, surveillance telemetry, and tracking trackers from phoning home.

### 🌐 2. Dual-Interface Compatibility (Wi-Fi & Ethernet)
- Standard Android TV settings often restrict setting custom DNS on wired Ethernet ports.
- AdGuard TV bridges this gap using Android's `VpnService` API, ensuring that **both Ethernet LAN cables and Wi-Fi connections** receive full ad-blocking protection.

### 🎮 3. 100% Android TV & Remote Friendly (D-Pad Navigation)
- Tailored exclusively for 10-foot television experiences.
- Intuitive navigation designed for TV remotes (Up/Down/Left/Right/OK buttons).
- Fully compliant with **Android TV Leanback Launcher** guidelines, featuring a custom 16:9 home screen banner (`tv_banner.png`) and dedicated launcher icons.

### ⚙️ 4. Custom DNS Provider Flexibility
- **Default High-Security Servers**: Pre-configured with official AdGuard DNS upstream resolvers:
  - `94.140.14.14`
  - `94.140.15.15`
- **User-Defined DNS**: Seamlessly enter and switch to any custom IPv4 or IPv6 DNS resolver (e.g., Cloudflare `1.1.1.1`, Google `8.8.8.8`, NextDNS, Pi-hole, AdGuard Home, or custom local gateways).
- **Built-in IP Format Validation**: Automatic address validation prevents service crashes and informs the user if a malformed IP address is entered.
- **Persistent Preferences**: Custom DNS settings are automatically remembered across reboots and app launches.

### 🚀 5. Zero Latency & Ultra-Low Resource Overhead
- **DNS-Only Routing**: Unlike traditional VPNs that route all heavy video and audio streams through an encrypted tunnel, AdGuard TV **only routes DNS lookup packets (Port 53)**.
- **Zero Bandwidth Throttling**: 4K/8K HDR video playback continues directly over your local ISP connection with zero speed penalty, buffer lag, or battery/CPU drain on low-power TV processors.

### 🔄 6. Robust Lifecycle & State Synchronization
- **Real-Time UI Updates**: Instant UI state reflection when the VPN starts, disconnects, or is revoked.
- **External Revocation Handling**: Automatically monitors system state (`onRevoke`) so if another VPN connects or system settings revoke permissions, the app cleanly releases resources.
- **Safe Animations**: Pulse status animations automatically shut down when off-screen to avoid background CPU load and memory leaks.

---

## 🛠️ How It Works Under the Hood

```
+-------------------------------------------------------------+
|                        Android TV                           |
|                                                             |
|   [TV App / Browser]                                        |
|           |                                                 |
|           | 1. DNS Query (e.g. ad.doubleclick.net)          |
|           v                                                 |
|   +-----------------------+                                 |
|   |  AdGuard TV (tun0)    | <--- Local VPN Tunnel           |
|   +-----------------------+      (DNS queries only)         |
|           |                                                 |
|           | 2. Redirect to AdGuard DNS                      |
|           v    (94.140.14.14 / 94.140.15.15)                |
|   +-----------------------+                                 |
|   |   AdGuard Resolvers   | ---> Blocked domain returns 0.0.0.0|
|   +-----------------------+                                 |
|                                                             |
|   * Normal Video/Stream Data ---> Direct to ISP (Unmodified)|
+-------------------------------------------------------------+
```

1. **Local TUN Interface**: AdGuard TV establishes a virtual network interface without sending traffic off-device.
2. **Upstream Resolution**: Android directs DNS queries through the established interface to AdGuard's ad-filtering resolvers.
3. **Blackhole Filtering**: Queries for known ad/tracker hosts resolve to `0.0.0.0`, preventing advertisements from downloading.

---

## 📥 Installation

The compiled, ready-to-install APK is available directly in this repository:
- **[Download AdGuardTV.apk](AdGuardTV.apk)**

### Method 1: Install via Wireless ADB (Recommended)
1. On your Android TV, navigate to **Settings > Device Preferences > About**.
2. Click **Build** 7 times until Developer Options are unlocked.
3. Go to **Settings > Device Preferences > Developer Options** and enable **Network Debugging / USB Debugging**.
4. Note your TV's IP address from **Settings > Network & Internet**.
5. Connect and install from your computer:
   ```bash
   adb connect <YOUR_TV_IP>:5555
   adb install -r AdGuardTV.apk
   ```

### Method 2: Install via USB Drive
1. Copy `AdGuardTV.apk` to a USB flash drive.
2. Plug the USB drive into your Android TV.
3. Open a file manager app on your TV (such as *File Commander* or *X-plore*).
4. Locate `AdGuardTV.apk` and click **Install**.

---

## 📖 Usage Instructions

1. Open **AdGuard TV** from your television app launcher.
2. *(Optional)* Enter your preferred custom DNS IP address in the text box. Leave it blank to use the default high-performance AdGuard DNS.
3. Click **Enable AdGuard DNS**.
4. When prompted by Android TV, confirm the VPN Connection Request.
5. The pulsing **"Ads are blocking..."** indicator confirms that protection is active.
6. To disable at any time, click **Disconnect**.

---

## 🏗️ Building from Source

### Prerequisites
- Android Studio Ladybug / Meerkat or later
- Android SDK 36
- JDK 17 / 21

### Build Steps
```bash
# Clone the repository
git clone https://github.com/kakarla500/AdGuardTV.git
cd AdGuardTV

# Run local unit tests
./gradlew test

# Assemble the debug APK
./gradlew assembleDebug
```
The compiled APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.

---

## 📄 License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for full details.
