import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.4.21"
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("org.asciidoctor.convert") version "1.5.8"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("namecheckers.java-library-conventions")
}

group = "eu.ncouret"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_15

repositories {
    mavenCentral()
    jcenter()
}

val snippetsDir = file("build/generated-snippets")
val resilience4jVersion = "1.6.1"
extra["snippetsDir"] = snippetsDir
extra["springCloudVersion"] = "Hoxton.SR8"
extra["testcontainersVersion"] = "1.14.3"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.data:spring-data-relational")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.github.resilience4j:resilience4j-spring-boot2:${resilience4jVersion}")
    implementation("io.github.resilience4j:resilience4j-circuitbreaker:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-ratelimiter:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-retry:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-bulkhead:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-cache:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-timelimiter:$resilience4jVersion")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "14"
    }
}