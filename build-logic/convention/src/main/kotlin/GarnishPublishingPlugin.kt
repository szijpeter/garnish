import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

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

        afterEvaluate {
            extensions.configure<PublishingExtension> {
                publications.withType<MavenPublication>().configureEach {
                    groupId = findProperty("GROUP")?.toString() ?: groupId
                    version = findProperty("VERSION_NAME")?.toString() ?: version
                    artifactId = "garnish-${project.name}"
                }
            }
        }
    }
}
