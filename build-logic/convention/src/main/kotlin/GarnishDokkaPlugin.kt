import com.garnish.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for Dokka API documentation.
 *
 * Applies the Dokka plugin for HTML documentation generation.
 */
class GarnishDokkaPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.findPlugin("dokka").get().get().pluginId)
    }
}
