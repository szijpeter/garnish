plugins {
    alias(libs.plugins.garnishCmp)
    alias(libs.plugins.garnishPublishing)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.screen)
        }
    }
}
