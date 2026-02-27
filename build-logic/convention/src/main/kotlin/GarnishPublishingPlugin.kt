import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

/**
 * Convention plugin for publishing library modules to mavenLocal.
 *
 * Applies maven-publish and configures:
 * - groupId, version from gradle.properties
 * - POM metadata skeleton (ready for Maven Central later)
 * - Publication to mavenLocal
 */
class GarnishPublishingPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("maven-publish")

        afterEvaluate {
            extensions.configure<PublishingExtension> {
                publications.withType<MavenPublication>().configureEach {
                    groupId = findProperty("GROUP")?.toString() ?: "com.garnish"
                    version = findProperty("VERSION_NAME")?.toString() ?: "0.1.0-SNAPSHOT"

                    pom {
                        name.set(project.name)
                        description.set("Garnish ${project.name} — KMP system essential")
                        url.set("https://github.com/garnish-kmp/garnish")

                        licenses {
                            license {
                                name.set("Apache License 2.0")
                                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                            }
                        }
                        scm {
                            url.set("https://github.com/garnish-kmp/garnish")
                            connection.set("scm:git:git://github.com/garnish-kmp/garnish.git")
                            developerConnection.set("scm:git:ssh://git@github.com/garnish-kmp/garnish.git")
                        }
                    }
                }
            }
        }
    }
}
