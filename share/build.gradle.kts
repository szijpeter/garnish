import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    alias(libs.plugins.garnishKmp)
    alias(libs.plugins.garnishPublishing)
}

// Enable Android resources for FileProvider XML
kotlin {
    val ext = (this as ExtensionAware).extensions
        .getByName("androidLibrary") as KotlinMultiplatformAndroidLibraryExtension
    ext.androidResources.enable = true

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
        }
    }
}
