plugins {
    alias(libs.plugins.garnishKmp)
    alias(libs.plugins.garnishPublishing)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
        androidMain.dependencies {
            implementation(libs.play.review.ktx)
        }
    }
}
