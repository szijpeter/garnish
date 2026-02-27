package dev.garnish.app

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import androidx.core.content.IntentCompat
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.garnish.share.ShareKit
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShareIntegrationTest {

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun shareTextLaunchesChooserIntent() {
        intending(hasAction(Intent.ACTION_CHOOSER))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ShareKit(context).shareText("Hello from instrumented test")

        intended(hasAction(Intent.ACTION_CHOOSER))
    }

    @Test
    fun shareFileLaunchesChooserIntent() {
        intending(hasAction(Intent.ACTION_CHOOSER))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ShareKit(context).shareFile(
            fileBytes = "col1,col2\nA,B".encodeToByteArray(),
            fileName = "sample.csv",
            mimeType = "text/csv",
        )

        intended(hasAction(Intent.ACTION_CHOOSER))
    }

    @Test
    fun shareImageGrantsReadUriPermissionToChooserAndTargetIntent() {
        intending(hasAction(Intent.ACTION_CHOOSER))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ShareKit(context).shareImage(
            imageBytes = byteArrayOf(1, 2, 3, 4),
            mimeType = "image/png",
        )

        intended(chooserHasReadableSharedStream())
    }

    private fun chooserHasReadableSharedStream(): Matcher<Intent> = object : TypeSafeMatcher<Intent>() {
        override fun describeTo(description: Description) {
            description.appendText(
                "chooser intent with stream URI + ClipData and FLAG_GRANT_READ_URI_PERMISSION " +
                    "on both chooser and target share intent",
            )
        }

        override fun matchesSafely(item: Intent): Boolean {
            if (item.action != Intent.ACTION_CHOOSER) return false

            val chooserReadFlag = item.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION != 0
            val target = item.parcelableExtra(Intent.EXTRA_INTENT, Intent::class.java) ?: return false
            val targetReadFlag = target.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION != 0
            val streamUri = target.parcelableExtra(Intent.EXTRA_STREAM, Uri::class.java) ?: return false
            val targetClipUri = target.clipData?.getItemAt(0)?.uri
            val chooserClipUri = item.clipData?.getItemAt(0)?.uri

            return chooserReadFlag &&
                targetReadFlag &&
                targetClipUri == streamUri &&
                chooserClipUri == streamUri
        }
    }

    private fun <T : Parcelable> Intent.parcelableExtra(key: String, clazz: Class<T>): T? =
        IntentCompat.getParcelableExtra(this, key, clazz)
}
