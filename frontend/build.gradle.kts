
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform") version "2.1.10"
    id("org.openapi.generator") version "7.20.0"
    kotlin("plugin.serialization") version "2.1.10"
    id("com.android.library") version "8.12.3"

    // compose multiplatform
    id("org.jetbrains.compose") version "1.7.3"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10" // Matches your Kotlin version
}

/**
 * to run (desktop):
 * ./gradlew clean jvmRun
 *
 * android:
 * ./gradlew clean installDebug     (installs on test device)
 * ./gradlew assembleDebug          (just assembles the APK)
 *
 */

val ktorVersion = "2.3.12" // Or 3.0.0+ for 2026

kotlin {
    androidTarget()
//    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm("desktop")

    // groups targets
    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            // This tells Kotlin where the generated code lives
            kotlin.srcDir(layout.buildDirectory.dir("generated/src/commonMain/kotlin"))

            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3) // For buttons/text
                implementation(compose.components.resources) // For loading card images later
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("org.slf4j:slf4j-simple:2.0.9") // Adds simple logging
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("androidx.activity:activity-compose:1.12.4")
                implementation("androidx.compose.ui:ui:1.10.3")
                implementation("androidx.compose.material3:material3:1.4.0")
                implementation("androidx.compose.foundation:foundation:1.10.3")
                implementation("androidx.compose.runtime:runtime:1.10.3")
                implementation("com.google.android.material:material:1.13.0")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
        all {
            languageSettings {
                optIn("kotlin.io.encoding.ExperimentalEncodingApi")
                optIn("kotlin.ExperimentalStdlibApi")
            }
        }
    }

    jvmToolchain(23)
}

dependencies {
    // Use the Compose BOM to keep versions in sync
    val composeBom = platform("androidx.compose:compose-bom:2025.02.00") // Use a 2025/2026 version
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.compose.material3:material3")

    // Ensure the test runner can find the theme
    debugImplementation("androidx.compose.material3:material3:1.4.0")
}

android {
    compileOptions {
        // Force Java compilation to use JDK 23 (or whatever you have installed)
        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }
    namespace = "com.cardgame"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
    }
}

openApiGenerate {
    generatorName.set("kotlin")
    library.set("multiplatform")
    val specFile = layout.projectDirectory.file("../specs/api-docs.yaml").asFile
    inputSpec.set(specFile.absolutePath.replace("\\", "/"))
    outputDir.set(layout.buildDirectory.dir("generated").get().asFile.absolutePath)

    configOptions.set(mapOf(
        "library" to "multiplatform",
//        "serializationLibrary" to "kotlinx_serialization",    or will get serialization twice
        "enumPropertyNaming" to "UPPERCASE",
        "dateLibrary" to "kotlinx-datetime"
    ))
}

// Automatically run generation before compilation
tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn("openApiGenerate")
}
tasks.matching { task ->
    task.name.startsWith("prepareComposeResourcesTask") ||
            task.name.startsWith("generateResourceAccessorsFor")
}.configureEach {
    dependsOn("openApiGenerate")
}
tasks.matching { task ->
    task.name.startsWith("compileAndroid") ||
            task.name.startsWith("generateAndroid") ||
            task.name.startsWith("parseDebug") ||
            task.name.startsWith("mergeDebug")
}.configureEach {
    dependsOn("openApiGenerate")
}
tasks.withType<com.android.build.gradle.tasks.MergeResources>().configureEach {
    dependsOn("openApiGenerate")
}
afterEvaluate {
    tasks.findByName("extractDeepLinksDebug")?.dependsOn("openApiGenerate")
    tasks.findByName("extractDeepLinksRelease")?.dependsOn("openApiGenerate")
    tasks.findByName("extractDeepLinksForAarDebug")?.dependsOn("openApiGenerate")
    tasks.findByName("extractDeepLinksForAarRelease")?.dependsOn("openApiGenerate")
}

tasks.register<JavaExec>("jvmRun") {
    group = "application"
    mainClass.set("com.cardgame.MainKt")

    // lazy = good here (prevent error)
    val jvmTarget = kotlin.targets.named<org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget>("desktop")
    classpath = objects.fileCollection().from(provider {
        val compilation = jvmTarget.get().compilations.getByName("main")
        compilation.output.allOutputs + compilation.runtimeDependencyFiles
    })

    dependsOn("desktopJar")
}
tasks.withType<JavaCompile> {
    javaCompiler = javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}