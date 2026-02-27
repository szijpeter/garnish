import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for publishing Garnish modules.
 *
 * Uses com.vanniktech.maven.publish for:
 * - Maven Central (Central Portal) publication
 * - in-memory signing
 * - generated source/javadoc jars
 * - POM metadata from gradle.properties (POM_*)
 *
 * Publishing credentials/signing material are injected via environment variables
 * mapped to Gradle properties (ORG_GRADLE_PROJECT_*).
 */
class GarnishPublishingPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.vanniktech.maven.publish")

        extensions.configure<MavenPublishBaseExtension> {
            coordinates(
                artifactId = "garnish-${project.name}",
            )
        }
    }
}
