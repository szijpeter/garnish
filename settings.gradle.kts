rootProject.name = "Garnish"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

includeBuild("build-logic")
include(":composeApp")
include(":androidApp")
include(":torch")
include(":torch-compose")
include(":share")
include(":share-compose")
include(":haptic")
include(":haptic-compose")
include(":screen")
include(":screen-compose")
include(":badge")
include(":clipboard")
include(":clipboard-compose")
include(":review")