# Setgreet Android SDK
[![Maven Central](https://img.shields.io/maven-central/v/com.setgreet/setgreet.svg?label=maven%20central)](#)

Setgreet Android SDK allows you to show Setgreet flows in your Android app.

## Requirements
- Android 5.0 (API level 21) and above

## Installation
Add mavenCentral() to your project's build.gradle.kts file:
```gradle.kts
allprojects {
    repositories {
        mavenCentral()
    }
}
```

Add the following to your app's build.gradle.kts file:
```gradle.kts
dependencies {
    implementation("com.setgreet:setgreet:LATEST_VERSION")
}
```

## Usage

### Initialization
- Setgreet App Key: You can get the app key while creating a new app in the Setgreet flow editor.

Initialize the SDK in your Application class:

```Kotlin
class MyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()

        Setgreet.initialize(
            context = this,
            appKey = "APP_KEY",
            config = SetgreetConfig(
                debugMode = false
            )
        )
    }
}
```

### Show Flow
- Setgreet Flow ID: The flow ID is a unique identifier for the flow you want to show. You can get the flow ID from the flow's URL at the web app. For example, if the flow URL is `https://app.setgreet.com/flows/1234`, the flow ID is `1234`.

To show the Setgreet flow, call the following method:

```Kotlin
Setgreet.showFlow(flowId = "FLOW_ID")
```
