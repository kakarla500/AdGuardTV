# Walkthrough - AdGuard for TV Pro (v2)

I have successfully implemented the second major iteration of the application, now rebranded as **AdGuard for TV**.

## New Features

### 🎨 Rebranding & UI
- **App Name**: Renamed to "AdGuard for TV".
- **Logo Placeholder**: Added an `ImageView` in the main layout ready for the custom logo.
- **Custom DNS**: Added an `EditText` field allowing users to input their own DNS server (e.g., `1.1.1.1`).
- **Blocking Animation**: Added a "Ads are blocking..." indicator with a smooth pulsing animation that triggers when the service is active.

### ⚙️ Enhanced Logic
- **Custom DNS Routing**: The `VpnService` now dynamically switches between AdGuard's default DNS and the user's custom entry.
- **Persistent Settings**: Custom DNS values are saved using `SharedPreferences`.

### ✅ Quality Assurance
- **Comprehensive Unit Tests**: Updated `DnsManagerTest` to cover the new custom DNS storage scenarios.
- **Verification**: All **4 unit tests passed** successfully.

## Delivery
- **APK Saved**: The version 2 APK is available on your desktop as **`AdGuardTV_v2.apk`**.

> [!TIP]
> To use the custom logo you provided, simply save it as `logo.png` in the `app/src/main/res/drawable/` folder and rebuild. The app is already configured to prioritize branding in its manifest and layout.
