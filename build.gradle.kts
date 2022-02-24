import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    application
}

group = "federico.mete"
version = "1.0"

val spekVersion = "2.0.17"
val mockkVersion = "1.12.2"
var logbackVersion = "1.2.10"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    testImplementation(kotlin("test-junit5"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")

    //implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:5.6.0")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
    //testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect:5.6.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    implementation("io.ktor:ktor-server-netty:1.5.2")
    implementation("io.ktor:ktor-html-builder:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
    implementation("com.google.code.gson:gson:2.8.5")

    implementation("io.mockk:mockk:$mockkVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")


}


tasks.test {
    useJUnitPlatform {
        includeEngines("spek2")
    }}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClassName = "com.showsrecommendations.application.ServerKt"
}