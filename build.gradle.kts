// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.dagger.hilt) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinx.kover) apply false
    id("io.gitlab.arturbosch.detekt") version libs.versions.detektVersion.get()
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

detekt {
    toolVersion = libs.versions.detektVersion.get()

    source = files(
        "app/src/main/java",
        "compose/src/main/java",
        "extension/src/main/java",
        "note/src/main/java"
    )
    parallel = false
    config = files("detekt-config.yml")
    buildUponDefaultConfig = false
    disableDefaultRuleSets = false

    debug = false
    ignoreFailures = false

    ignoredBuildTypes = listOf(libs.versions.release.get())
    ignoredFlavors = listOf(libs.versions.prod.get())
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_17.toString()
    reports {
        xml {
            outputLocation.set(file("build/reports/detekt/detekt.xml"))
        }
        html {
            outputLocation.set(file("build/reports/detekt/detekt.html"))
        }
    }
}
