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

### Identify User

Identifies a user for Setgreet analytics and flow management.

**Parameters:**

- `userId` (String): The unique identifier for the user
- `attributes` (Optional): Additional user attributes
- `operation` (Optional): The operation type for user attributes (CREATE or UPDATE)
- `locale` (Optional): User's locale (e.g., "en-US"). If not provided, uses device's default locale

**Example:**

```kotlin
Setgreet.identifyUser(
    userId = "user123",
    attributes = mapOf(
        "name" to "John Doe",
        "email" to "john@example.com",
        "plan" to "premium"
    ),
    operation = Operation.CREATE,
    locale = "en-US"
)
```

### Reset User

Clears user identification data and resets user session state for logout scenarios.

**Example:**

```kotlin
Setgreet.resetUser()
```

### Show Flow
- Setgreet Flow ID: The flow ID is a unique identifier for the flow you want to show. You can get the flow ID from the flow's URL at the web app. For example, if the flow URL is `https://app.setgreet.com/flows/1234`, the flow ID is `1234`.

To show the Setgreet flow, call the following method:

```Kotlin
Setgreet.showFlow(flowId = "FLOW_ID")
```

### Track Screen

Tracks a screen view for analytics and potential flow triggers.

**Parameters:**

- `screenName` (String): The name of the screen being viewed
- `properties` (Optional): Additional properties associated with the screen view

**Example:**

```kotlin
Setgreet.trackScreen(
    screenName = "product_detail",
    properties = mapOf(
        "product_id" to "prod_123",
        "category" to "electronics",
        "price" to 299.99
    )
)
```

### Track Event

Tracks custom events for analytics and flow triggers.

**Parameters:**

- `eventName` (String): The name of the custom event
- `properties` (Optional): Additional properties associated with the event

**Example:**

```kotlin
// Android
Setgreet.trackEvent(
    eventName = "purchase_completed",
    properties = mapOf(
        "order_id" to "ord_456",
        "total_amount" to 299.99,
        "payment_method" to "credit_card",
        "items_count" to 3
    )
)
```
