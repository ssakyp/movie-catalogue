plugins {
    kotlin("jvm") version "2.4.10"
    application
}

group = "kz.mobile.dev.homework"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(26)
}

application {
    // Entry point: the top-level main() function inside Main.kt
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}