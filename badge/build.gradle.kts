plugins {
    alias(libs.plugins.garnishKmp)
    alias(libs.plugins.garnishPublishing)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
        }
    }
}
