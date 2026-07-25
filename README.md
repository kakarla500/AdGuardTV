## Disclaimer

This project is provided for educational and research purposes only.

The purpose of this application is to demonstrate how Android's Private DNS feature can be configured programmatically and to help developers learn Android application development.

The author does not encourage or support the use of this software for any unlawful activity or in violation of the terms of service of any network, service provider, or platform.

Users are solely responsible for ensuring that their use of this software complies with all applicable laws, regulations, and service agreements. The author assumes no liability for any misuse or damages resulting from the use of this project.

# AdGuard for TV

A lightweight, open-source ad-blocking solution specifically designed for Android TV. This app enforces AdGuard DNS (or any custom DNS) system-wide on both Wi-Fi and Ethernet connections without requiring root access.

## How It Works
Android restricts programmatic DNS changes on standard Ethernet/Wi-Fi interfaces for security. To bypass this, **AdGuard for TV** utilizes the Android **VpnService API**:

1. **Local VPN Tunnel**: The app creates a local-only VPN interface on the device.
2. **DNS Redirection**: Instead of routing all data traffic to a remote server, it only intercepts DNS queries (port 53).
3. **Upstream Resolution**: These queries are forwarded to AdGuard's DNS servers (`94.140.14.14`, `94.140.15.15`) or a user-specified custom DNS.
4. **Ad Blocking**: Since AdGuard DNS filters requests for known ad-serving domains at the source, ads are blocked before they can even be downloaded by your TV apps.

## Features
- **One-Click Connect**: Simple D-Pad friendly toggle to enable/disable protection.
- **Custom DNS Support**: Flexibility to use your own DNS provider (e.g., Cloudflare, Google, or private servers).
- **Universal Compatibility**: Works seamlessly on both **WiFi** and **Ethernet** connections.
- **Visual Status**: A clean animation and status indicator to show when protection is active.

## Installation

### Method 1: Install via APK
1. Download the latest `AdGuardTV_v2.apk`.
2. Enable **Unknown Sources** in your Android TV Settings (**Settings > Device Preferences > Security & Restrictions**).
3. Install the APK using ADB from your computer:
   ```bash
   adb install AdGuardTV_v2.apk
   ```

### Method 2: Build from Source
1. Clone the repository:
   ```bash
   git clone https://github.com/kakarla500/AdGuardTV.git
   ```
2. Open the project in **Android Studio**.
3. Build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```

## Usage
1. Launch **AdGuard for TV** from your TV's app launcher.
2. (Optional) Enter a custom DNS address if you don't want to use the default AdGuard servers.
3. Click **Enable AdGuard DNS**.
4. **Important**: When prompted, accept the Android VPN Connection Request. This is required for the app to redirect DNS traffic internally.
5. Once connected, you will see the "Ads are blocking..." status animation.

## Technical Details
- **Language**: 100% Kotlin
- **UI Toolkit**: Android XML (TV Leanback compatible)
- **Minimum SDK**: API 25 (Android 7.1)
- **Permissions**: `INTERNET`, `ACCESS_NETWORK_STATE`, `BIND_VPN_SERVICE`

## Development Process
This project was developed using a modern **AI-assisted engineering workflow**. By collaborating with AI tools within the Android Studio environment, the development lifecycle was significantly accelerated:
- **Architectural Direction**: The high-level design (VPN tunneling for DNS) was directed by the lead developer.
- **Rapid Prototyping**: AI was used to scaffold the `VpnService` boilerplate and UI layouts.
- **Quality Assurance**: Unit tests were generated and verified through collaborative debugging.

This project stands as a testament to how human creativity and AI efficiency can work together to build functional, secure, and useful software in record time.

---
*Developed by [kakarla500](https://github.com/kakarla500)*
