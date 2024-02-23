plugins {
    kotlin("jvm") version "1.9.21"
}

group = "nyc.spookyrobotics.piserver"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.nanohttpd:nanohttpd:2.3.1")
    implementation("org.nanohttpd:nanohttpd-websocket:2.3.1")
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation(fileTree("libs"))
    implementation("com.google.code.gson:gson:2.8.6")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}