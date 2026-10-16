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
- `onActionTriggered`: Called when a button's action is triggered, just before it runs, so it arrives even when the action closes the flow, and even when required inputs on the screen then stop the action (see [Action Types](#action-types))
- `onCustomAction`: Called when user taps a button whose action is Custom, with that action's data
- `onTrackEvent`: Called when a button's Track Event action runs, with the event name and properties
- `onPermissionRequested`: Called when a permission request completes
- `onPurchaseCompleted`, `onPurchaseCancelled`, `onPurchaseFailed`, `onPurchasePending`, `onRestoreCompleted`, `onRestoreFailed`: Called with the outcome of a paywall's purchase and restore buttons (see [Purchases and Paywalls](#purchases-and-paywalls))
- `onError`: Called when a flow the SDK set out to show could not be shown (see [Flow Errors](#flow-errors))

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
        Log.d("Setgreet", "Action: ${event.actionType} on screen ${event.screenIndex + 1}")
    }

    onCustomAction { event ->
        Log.d("Setgreet", "Custom action: ${event.data}")
    }

    onPermissionRequested { event ->
        Log.d("Setgreet", "Permission ${event.permissionType}: ${event.result}")
    }

    onError { event ->
        Log.e("Setgreet", "Error: ${event.errorType} - ${event.message}")
    }
}
```

### Flow Listener

`setFlowListener` receives every flow event as one `SetgreetFlowEvent` and works alongside `setFlowCallbacks`:

```kotlin
Setgreet.setFlowListener { event ->
    when (event) {
        is SetgreetFlowEvent.CustomAction -> Log.d("Setgreet", "Custom action: ${event.data}")
        is SetgreetFlowEvent.TrackEvent -> Log.d("Setgreet", "Track event: ${event.eventName}")
        else -> Unit
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
| `ActionTriggered` | Button action triggered (before it runs) | `actionType`, `actionName`, `screenIndex` |
| `CustomAction` | Custom button action tapped | `flowId`, `data`, `screenIndex` |
| `TrackEvent` | Track Event button action ran | `eventName`, `properties` |
| `PermissionRequested` | Permission request completed | `permissionType`, `result` |
| `FlowError` | A flow failed to fetch, parse or display | `flowId`, `errorType`, `message` |

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

`actionType` is an `ActionType`. Its name in lower case is the value every Setgreet SDK reports, for example `REQUEST_NOTIFICATION_PERMISSION` is `request_notification_permission`.

| Action | Description |
| ------ | ----------- |
| `NEXT` | Navigate to next screen |
| `PREVIOUS` | Navigate to previous screen |
| `SKIP` | Skip the flow |
| `DISMISS` | Close the flow |
| `URL` | Open a URL |
| `REQUEST_NOTIFICATION_PERMISSION` | Request notification permission |
| `REQUEST_LOCATION_PERMISSION` | Request location permission |
| `REQUEST_CAMERA_PERMISSION` | Request camera permission |
| `REQUEST_TRACKING_PERMISSION` | Request tracking permission (always `not_required` on Android) |
| `REQUEST_MICROPHONE_PERMISSION` | Request microphone permission |
| `REQUEST_PHOTO_LIBRARY_PERMISSION` | Request photo library permission |
| `REQUEST_REVIEW` | Request a Play Store review |
| `SHARE` | Open the share sheet |
| `OPEN_SETTINGS` | Open app settings |
| `TRACK_EVENT` | Track an event; `actionName` is the event name |
| `CUSTOM` | Custom action (also delivered to `onCustomAction`) |
| `PURCHASE` | Purchase through your purchase provider |
| `RESTORE_PURCHASES` | Restore purchases through your purchase provider |

`actionName` is set only for `TRACK_EVENT`. An action that does nothing is not reported: `NONE` or an unknown action, a `URL` action with no target, or a purchase or restore button without its action. Auto-advance and option selection are not button actions.

### Flow Errors

`onError` fires when a flow the SDK set out to show does not show:

| `errorType` | When |
| ----------- | ---- |
| `NETWORK` | The request failed, or the server answered with an error status |
| `PARSE` | The response could not be parsed, or carried no flow |
| `DISPLAY` | The flow could not be displayed (for example, no context before `initialize`) |
| `UNKNOWN` | Anything else |

It covers `showFlow`, a flow returned by `trackEvent`, `trackScreen` or `identifyUser` that cannot be parsed or displayed, and the display of every flow. A `trackEvent`, `trackScreen` or `identifyUser` request that fails before any response is only logged, because no flow is known to have matched. `flowId` is empty when the SDK never learned it. Purchase and restore failures have their own callbacks.

### Permission Types

| Type | Description |
| ---- | ----------- |
| `notification` | Push notification permission |
| `location` | Location access permission |
| `camera` | Camera access permission |
| `tracking` | App tracking (always `not_required` on Android) |
| `microphone` | Microphone access permission |
| `photoLibrary` | Photo library access permission |

### Permission Results

| Result | Description |
| ------ | ----------- |
| `granted` | Permission was granted by the user |
| `denied` | Permission was denied by the user |
| `permanently_denied` | Permission was permanently denied |
| `already_granted` | Permission was already granted |
| `not_required` | Permission request was not required |

## Permissions Setup

If your flows use permission buttons, declare the matching permissions in your app's `AndroidManifest.xml`, and only those your flows actually ask for. For an undeclared permission Android shows no dialog, and the SDK reports `not_required`.

```xml
<!-- Notification permission (Android 13 and later) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<!-- Location permission -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

<!-- Camera permission -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- Microphone permission -->
<uses-permission android:name="android.permission.RECORD_AUDIO" />

<!-- Photo library permission -->
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
```

Tracking needs no declaration: on Android its button reports `not_required` and the flow moves on. Declaring `READ_MEDIA_IMAGES` can require a Photo and Video Permissions declaration in Google Play Console, so add it only if a flow uses a photo library button.

## Purchases and Paywalls

A paywall screen built in Setgreet sells through the purchase tool your app already uses, such as RevenueCat or Google Play Billing. Setgreet does not wrap that tool and never needs its API key: you implement `SetgreetPurchaseProvider` on top of it, then register it once, after configuring your tool and initializing Setgreet:

```kotlin
Setgreet.setPurchaseProvider(MyPurchaseProvider())

// Or read a specific offering when a paywall names no source:
Setgreet.setPurchaseProvider(MyPurchaseProvider(), SetgreetPurchaseSource.offering("onboarding"))
```

| Member | Purpose |
| ------ | ------- |
| `providerId` | A short name for your tool, such as `revenuecat` |
| `loadProducts(source)` | Returns the products of a source (the current offering, an offering or a placement) |
| `purchase(activity, productRef, source)` | Buys a product: `PURCHASED`, `CANCELLED`, `PENDING` or `FAILED` |
| `restorePurchases()` | Restores purchases: `RESTORED`, `NOTHING_TO_RESTORE` or `FAILED` |
| `accessState()` | What the user already has access to, or `null` when your tool cannot say |
| `trackPaywallImpression(context)` | Forwards a paywall view to your tool |
| `setAttributes(attributes)` | Forwards user attributes to your tool |
| `identify(userId)`, `reset()` | Optional: keep your tool's user in step with Setgreet |

The paywall branches the flow on the outcome, and the purchase callbacks above report it to your app. The full guide, with a RevenueCat provider ready to copy, is at [setgreet.com/docs/sdk/purchases](https://setgreet.com/docs/sdk/purchases).

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

## External Data in Flows

Flows can render data fetched from **your own** API — a product list filtered by answers the user gave earlier in the same flow. You register the endpoint once in the Setgreet dashboard (Data Sources), bind it into a screen, and it renders.

**Nothing to add to your app.** The SDK resolves a data-bound screen as the user enters it. There is no new API to call, no new permission and no new dependency.

What is worth knowing:

- **Setgreet calls your API from its servers, not from the device**, so your endpoint must be reachable over HTTPS from the public internet. Setgreet signs each request and asserts the end user's id; verify the signature on your side.
- **A failure degrades, it does not blank.** If your API is slow or down, the screen renders without the data-bound parts. Only if the flow author explicitly chose "skip screen" is it passed over.
- **Answers reach your API by input name.** An input named `goal` in the editor arrives as `goal`. Attributes you set via `identifyUser` are available to it too.

See [External Data](https://setgreet.com/docs/personalization/external-data) for the endpoint contract and the signature-verification recipe.
