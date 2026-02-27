# review

Cross-platform in-app review for Kotlin Multiplatform.

## API

```kotlin
enum class ReviewResult { Requested, NotAvailable, Error }

interface InAppReview {
    suspend fun requestReview(): ReviewResult
}
```

## Usage

```kotlin
// Android
val review = InAppReview(activity)

// iOS
val review = InAppReview()

val result = review.requestReview()
when (result) {
    ReviewResult.Requested -> println("Review request accepted")
    ReviewResult.NotAvailable -> println("Not available on this device")
    ReviewResult.Error -> println("Something went wrong")
}
```

## Platform Details

| Platform | Implementation |
|---|---|
| Android | Google Play In-App Review API (`review-ktx 2.0.2`) |
| iOS | `SKStoreReviewController.requestReview()` |

> **Note**: Both platforms may silently suppress the dialog if the user has already reviewed or the app has requested too frequently.
