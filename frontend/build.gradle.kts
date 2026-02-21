
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform") version "2.1.10"
    id("org.openapi.generator") version "7.20.0"
    kotlin("plugin.serialization") version "2.1.10"
    id("com.android.library") version "9.0.1"
}

/** to run: ./gradlew clean openApiGenerate jvmRun */
/** to just update api: cd frontend then run: ./gradlew clean openApiGenerate */
/** then to run: ./gradlew jvmRun */

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
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("org.slf4j:slf4j-simple:2.0.9") // Adds simple logging
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
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
}

dependencies {

}

android {
    namespace = "com.cardgame"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
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
//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
//    compilerOptions {
//        freeCompilerArgs.add("-opt-in=kotlin.io.encoding.ExperimentalEncodingApi")
//        freeCompilerArgs.add("-opt-in=kotlin.ExperimentalStdlibApi")
//    }
//}
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