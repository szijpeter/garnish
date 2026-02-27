import dev.garnish.buildlogic.configureAndroidLibrary
import dev.garnish.buildlogic.defaultNamespace
import dev.garnish.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for KMP + Compose Multiplatform library modules.
 *
 * Extends [GarnishKmpPlugin] (garnish.kmp) with:
 * - Compose Multiplatform plugin
 * - Compose Compiler plugin
 * - Compose runtime/foundation/ui dependencies in commonMain
 * - iOS framework binary configuration
 * - Android resources enabled
 */
class GarnishCmpPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        // Apply base KMP convention first
        pluginManager.apply("garnish.kmp")

        // Then Compose plugins
        pluginManager.apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
        pluginManager.apply(libs.findPlugin("composeCompiler").get().get().pluginId)

        extensions.configure<KotlinMultiplatformExtension> {
            // Enable Android resources for Compose modules
            configureAndroidLibrary(
                namespace = defaultNamespace(),
                compileSdk = libs.findVersion("androidCompileSdk").get().requiredVersion.toInt(),
                minSdk = libs.findVersion("androidMinSdk").get().requiredVersion.toInt(),
                enableAndroidResources = true,
            )

            // iOS framework binaries
            listOf(
                iosArm64(),
                iosSimulatorArm64()
            ).forEach { iosTarget ->
                iosTarget.binaries.framework {
                    baseName = project.name
                        .split("-")
                        .joinToString("") { it.replaceFirstChar(Char::uppercase) }
                    isStatic = true
                }
            }

            // Compose dependencies
            sourceSets.apply {
                commonMain.dependencies {
                    implementation(libs.findLibrary("compose-runtime").get())
                    implementation(libs.findLibrary("compose-foundation").get())
                    implementation(libs.findLibrary("compose-ui").get())
                    implementation(libs.findLibrary("compose-uiToolingPreview").get())
                }
            }
        }
    }
}
