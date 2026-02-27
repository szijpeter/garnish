package com.garnish.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convenience accessor for the version catalog.
 */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Derives an Android namespace from the project path.
 * E.g. project ":share" → "com.garnish.share"
 *      project ":share-compose" → "com.garnish.share.compose"
 */
internal fun Project.defaultNamespace(): String {
    val suffix = path
        .removePrefix(":")
        .replace("-", ".")
        .replace(":", ".")
    return "com.garnish.$suffix"
}

/**
 * Configures the Android library extension inside a KMP extension.
 * AGP 9 exposes `androidLibrary` as a child extension on [KotlinMultiplatformExtension].
 */
internal fun KotlinMultiplatformExtension.configureAndroidLibrary(
    namespace: String,
    compileSdk: Int,
    minSdk: Int,
    enableAndroidResources: Boolean = false,
) {
    val ext = (this as ExtensionAware).extensions
        .getByName("androidLibrary") as KotlinMultiplatformAndroidLibraryExtension

    ext.namespace = namespace
    ext.compileSdk = compileSdk
    ext.minSdk = minSdk
    ext.androidResources.enable = enableAndroidResources
}
