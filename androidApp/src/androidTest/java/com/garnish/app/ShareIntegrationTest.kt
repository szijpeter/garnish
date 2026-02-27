package com.garnish.app

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.garnish.share.ShareKit
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
}
