plugins {
    `kotlin-dsl`
}

group = "com.garnish.buildlogic.convention"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.dokka.gradlePlugin)
    implementation(libs.bcv.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("garnishKmp") {
            id = "garnish.kmp"
            implementationClass = "GarnishKmpPlugin"
        }
        register("garnishCmp") {
            id = "garnish.cmp"
            implementationClass = "GarnishCmpPlugin"
        }
        register("garnishPublishing") {
            id = "garnish.publishing"
            implementationClass = "GarnishPublishingPlugin"
        }
        register("garnishDetekt") {
            id = "garnish.detekt"
            implementationClass = "GarnishDetektPlugin"
        }
        register("garnishDokka") {
            id = "garnish.dokka"
            implementationClass = "GarnishDokkaPlugin"
        }
    }
}
