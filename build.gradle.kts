import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev770"
}

group = "com.taeyeon"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class, org.jetbrains.compose.ExperimentalComposeLibrary::class)
dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.1.0")
    implementation("org.jetbrains.compose.material3:material3:1.1.0")
    implementation("org.jetbrains.compose.material3:material3-desktop:1.1.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Clocky"
            packageVersion = "1.0.0"
        }
    }
}
dependencies {
    implementation(kotlin("reflect"))
}