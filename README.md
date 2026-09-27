# PASA - Private Android Security Agent

**Phase 1: Foundation Setup**

## ⚠️ Current Status: Phase 1 Only

This repository contains **Phase 1** of PASA - a foundation setup implementation.

### What Phase 1 Includes:
- ✅ Setup wizard with Telegram bot configuration
- ✅ Telegram Bot API connection testing
- ✅ Device information display
- ✅ Configuration status dashboard
- ✅ Android Keystore encrypted storage
- ✅ Material 3 UI with Jetpack Compose

### What Phase 1 Does NOT Include:
- ❌ Telegram long polling
- ❌ Remote command execution
- ❌ Foreground service
- ❌ Security monitoring
- ❌ Device control features
- ❌ Camera/microphone/location access

**Phase 2 implementation is not started.**

---

## 🏗️ Architecture

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: Clean Architecture (data, domain, features, ui)
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)
- **Build System**: Gradle 8.2 + AGP 8.2.0

### Project Structure

```
app/src/main/java/com/ashna/pasa/
├── data/
│   ├── local/          # Secure storage, preferences, device info
│   └── telegram/       # Telegram API client (test connection only)
├── domain/
│   ├── model/          # Data models
│   └── command/        # Future command handling (placeholder)
├── features/
│   ├── setup/          # Setup wizard
│   ├── dashboard/      # Main dashboard
│   └── security/       # Security status view
├── ui/
│   ├── theme/          # Material 3 theme
│   ├── components/     # Reusable UI components
│   └── navigation/     # Navigation graph
└── util/               # Constants and extensions
```

---

## 🚀 Build Instructions

### Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or later
- **JDK 17** or later
- **Gradle 8.2** (use wrapper)

### Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Gazi2029/PASA-Phase1.git
   cd PASA-Phase1
   ```

2. **Generate Gradle wrapper** (if missing):
   ```bash
   gradle wrapper --gradle-version 8.2
   ```

3. **Open in Android Studio**:
   - File → Open → Select `PASA-Phase1` folder
   - Wait for Gradle sync

4. **Generate launcher icons**:
   - Right-click `res` → New → Image Asset
   - Configure adaptive launcher icons
   - Generate all densities

5. **Build the project**:
   ```bash
   ./gradlew assembleDebug
   ```

6. **Install on device/emulator**:
   ```bash
   ./gradlew installDebug
   ```

---

## 📱 Usage

### First Launch - Setup Wizard

1. **Welcome Screen**
   - Review Phase 1 limitations

2. **Device Information**
   - Auto-detected device name and ID

3. **Telegram Configuration**
   - Enter your Telegram bot token
   - Enter your Telegram chat ID
   - ⚠️ **Security Warning**: Entering a chat ID does not verify ownership

4. **Test Connection**
   - Test Telegram Bot API connection
   - Verify bot credentials

5. **Setup Complete**
   - Proceed to dashboard

### Dashboard

- View device status
- Check Telegram configuration
- Monitor battery and network
- Access security status

### Security Status

- Review configuration checks
- Phase 1: Configuration verification only
- Phase 2 features marked as "Not Implemented"

---

## 🔒 Security Notes

### Phase 1 Security Features:
- ✅ Bot token stored in Android Keystore (AES-256-GCM encryption)
- ✅ Secure network communication (HTTPS only)
- ✅ No cleartext secrets in logs
- ✅ ProGuard/R8 obfuscation enabled in release builds

### Phase 1 Security Limitations:
- ⚠️ Chat ID verification not implemented
- ⚠️ No command authentication
- ⚠️ No secure channel verification

**Do not use in production until Phase 2 security features are implemented.**

---

## 📦 Dependencies

- **AndroidX Core**: 1.12.0
- **Jetpack Compose**: BOM 2024.01.00
- **Material 3**: Included in Compose BOM
- **Navigation Compose**: 2.7.6
- **Security Crypto**: 1.1.0-alpha06 (Android Keystore)
- **OkHttp**: 4.12.0 (Telegram API)
- **Coroutines**: 1.7.3

---

## 🛠️ Development

### Code Style
- Follow Kotlin official style guide
- Use meaningful variable names
- Document public APIs

### Testing
Phase 1 does not include unit tests or instrumentation tests.

### Build Variants
- **Debug**: Development build with debugging enabled
- **Release**: Optimized build with ProGuard/R8 enabled

---

## 📋 Known Issues

1. **Gradle wrapper not included**: Must generate manually
2. **Launcher icons missing**: Must generate with Image Asset tool
3. **Chat ID verification missing**: Phase 2 feature
4. **No command handling**: Phase 2 feature

---

## 🔮 Future Work (Phase 2)

- Telegram long polling service
- Secure command authentication
- Remote device control commands
- Security monitoring features
- Foreground service implementation
- Proper chat ID verification

---

## 📄 License

This is a personal project. License TBD.

---

## 👤 Author

Created as part of PASA (Private Android Security Agent) development.

**Current Phase**: 1 (Setup Foundation)  
**Version**: 1.0.0-phase1  
**Build Status**: ⚠️ Not verified in CI/CD

---

## ⚠️ Disclaimer

This app is intended for **personal use only** to secure your own Android device.

- Respect Android privacy and security mechanisms
- Do not bypass Android security features
- Do not use for unauthorized device access
- Comply with all applicable laws and regulations

**Phase 1 is a foundation only. Production use requires Phase 2 completion.**
