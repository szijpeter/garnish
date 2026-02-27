import com.garnish.buildlogic.configureAndroidLibrary
import com.garnish.buildlogic.defaultNamespace
import com.garnish.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for KMP library modules (no Compose).
 *
 * Applies:
 * - kotlin("multiplatform")
 * - com.android.kotlin.multiplatform.library
 *
 * Configures:
 * - Android library defaults (namespace, compileSdk, minSdk)
 * - iOS targets (iosArm64, iosSimulatorArm64)
 * - Explicit API mode
 * - commonTest dependency on kotlin-test
 */
class GarnishKmpPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
        pluginManager.apply(libs.findPlugin("androidKmpLibrary").get().get().pluginId)
        pluginManager.apply(libs.findPlugin("binaryCompatibilityValidator").get().get().pluginId)
        pluginManager.apply("garnish.detekt")
        pluginManager.apply("garnish.dokka")

        // Configure BCV for KMP
        extensions.configure<kotlinx.validation.ApiValidationExtension> {
            @Suppress("OPT_IN_USAGE")
            klib.enabled = true
        }

        // No explicit configuration needed for default 0.17.0 KMP support
        // but it might need a trigger.

        extensions.configure<KotlinMultiplatformExtension> {
            // Android KMP Library configuration via AGP 9 extension
            configureAndroidLibrary(
                namespace = defaultNamespace(),
                compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt(),
                minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt(),
                enableAndroidResources = false,
            )

            // Explicit API mode for library modules
            explicitApi()

            // iOS targets
            iosArm64()
            iosSimulatorArm64()

            // Common test dependencies
            sourceSets.apply {
                commonTest.dependencies {
                    implementation(libs.findLibrary("kotlin-test").get())
                }
            }
        }
    }
}
