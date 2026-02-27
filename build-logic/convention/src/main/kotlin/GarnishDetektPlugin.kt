import dev.garnish.buildlogic.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

/**
 * Convention plugin for Detekt static analysis.
 *
 * Applies Detekt with shared config, adds the formatting ruleset,
 * and wires `detekt` into the `check` task.
 */
class GarnishDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("io.gitlab.arturbosch.detekt")

        extensions.configure<DetektExtension> {
            buildUponDefaultConfig = true
            allRules = false
            parallel = true
            // Use config file if it exists
            val configFile = rootProject.file("config/detekt.yml")
            if (configFile.exists()) {
                config.setFrom(configFile)
            }
        }

        dependencies {
            "detektPlugins"(libs.findLibrary("detekt-formatting").get())
        }

        tasks.withType<Detekt>().configureEach {
            jvmTarget = "17"
            reports {
                html.required.set(true)
                xml.required.set(false)
                txt.required.set(false)
                sarif.required.set(false)
            }
        }
    }
}
