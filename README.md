# Setgreet Android SDK

[![Maven Central](https://img.shields.io/maven-central/v/com.setgreet/setgreet.svg?label=maven%20central)](#)

Setgreet Android SDK allows you to show Setgreet flows in your Android app.

## Requirements

- Android 6.0 (API level 23) and above

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

- Setgreet App Key: You can find your App Key at [Apps page](https://app.setgreet.com/apps).

Initialize the SDK in your Application class or where you want:

```kotlin
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

Clears user identification data and resets user session state for logout scenarios. A new anonymous ID is generated after reset.

**Example:**

```kotlin
Setgreet.resetUser()
```

### Anonymous ID

The SDK automatically generates an anonymous ID on initialization, which persists across app launches. When `identifyUser` is called, the anonymous identity is merged with the identified user. A new anonymous ID is generated when `resetUser()` is called.

```kotlin
val anonId = Setgreet.anonymousId
```

### Show Flow

- Setgreet Flow ID: The flow ID is a unique identifier for the flow you want to show. You can get the flow ID from the flow's URL at the web app. For example, if the flow URL is `https://app.setgreet.com/flows/1234`, the flow ID is `1234`.

To show the Setgreet flow, call the following method:

```kotlin
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

## Flow Callbacks

Listen to flow lifecycle events to track user interactions and flow completion.

**Available Callbacks:**

- `onFlowStarted`: Called when a flow begins displaying
- `onFlowCompleted`: Called when user completes all screens in the flow
- `onFlowDismissed`: Called when user dismisses the flow before completion
- `onScreenChanged`: Called when user navigates between screens
- `onActionTriggered`: Called when user interacts with buttons
- `onPermissionRequested`: Called when a permission request completes
- `onError`: Called when an error occurs during flow operations

**Example:**

```kotlin
Setgreet.setFlowCallbacks {
    onFlowStarted { event ->
        Log.d("Setgreet", "Flow ${event.flowId} started with ${event.screenCount} screens")
    }

    onFlowCompleted { event ->
        Log.d("Setgreet", "Flow completed: ${event.flowId}, duration: ${event.durationMs}ms")
    }

    onFlowDismissed { event ->
        Log.d("Setgreet", "Flow dismissed: ${event.reason} at screen ${event.screenIndex + 1}")
    }

    onScreenChanged { event ->
        Log.d("Setgreet", "Screen changed: ${event.fromIndex + 1} -> ${event.toIndex + 1}")
    }

    onActionTriggered { event ->
        Log.d("Setgreet", "Action: ${event.actionType}")
        event.actionName?.let { customEvent ->
            Log.d("Setgreet", "Custom event name: $customEvent")
        }
    }

    onPermissionRequested { event ->
        Log.d("Setgreet", "Permission ${event.permissionType}: ${event.result}")
    }

    onError { event ->
        Log.e("Setgreet", "Error: ${event.errorType} - ${event.message}")
    }
}
```

### Event Types

| Event | Description | Key Properties |
| ----- | ----------- | -------------- |
| `FlowStarted` | Flow begins presenting | `flowId`, `screenCount` |
| `FlowCompleted` | User completes the flow | `flowId`, `durationMs` |
| `FlowDismissed` | Flow dismissed before completion | `flowId`, `reason`, `screenIndex` |
| `ScreenChanged` | User navigates between screens | `fromIndex`, `toIndex` |
| `ActionTriggered` | Button action triggered | `actionType`, `actionName` |
| `PermissionRequested` | Permission request completed | `permissionType`, `result` |
| `FlowError` | Error during flow operations | `errorType`, `message` |

### Dismiss Reasons

| Reason | Description |
| ------ | ----------- |
| `USER_CLOSE` | User tapped the close button |
| `USER_SKIP` | User tapped the skip button |
| `BACK_PRESS` | User pressed the back button |
| `SWIPE_DOWN` | User swiped down to dismiss |
| `REPLACED` | Flow was replaced by another flow |
| `PROGRAMMATIC` | Flow was closed programmatically |
| `COMPLETED` | Flow reached its end node |

### Action Types

| Action | Description |
| ------ | ----------- |
| `NEXT` | Navigate to next screen |
| `PREVIOUS` | Navigate to previous screen |
| `SKIP` | Skip the current screen |
| `DISMISS` | Close/dismiss the flow |
| `URL` | Open a URL |
| `REQUEST_NOTIFICATION_PERMISSION` | Request notification permission |
| `REQUEST_LOCATION_PERMISSION` | Request location permission |
| `REQUEST_CAMERA_PERMISSION` | Request camera permission |
| `REQUEST_REVIEW` | Request Play Store review |
| `SHARE` | Open share sheet |
| `OPEN_SETTINGS` | Open app settings |

### Permission Types

| Type | Description |
| ---- | ----------- |
| `notification` | Push notification permission |
| `location` | Location access permission |
| `camera` | Camera access permission |

### Permission Results

| Result | Description |
| ------ | ----------- |
| `granted` | Permission was granted by the user |
| `denied` | Permission was denied by the user |
| `permanently_denied` | Permission was permanently denied |
| `already_granted` | Permission was already granted |
| `not_required` | Permission request was not required |

## Configuration

### SetgreetConfig

```kotlin
SetgreetConfig(
    debugMode = false,          // Enable debug logging
)
```

### Operation Types

- `Operation.CREATE`: Create a new user record
- `Operation.UPDATE`: Update an existing user record
