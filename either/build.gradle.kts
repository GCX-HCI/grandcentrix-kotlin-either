plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

val implementation by configurations
dependencies {
    kotlin("stdlib-jdk8")
}

val testImplementation by configurations
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.0")
    testImplementation("org.assertj:assertj-core:3.12.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories.add(rootProject.repositories.findByName("GitHubPackages")!!)

    publications {
        create<MavenPublication>("either") {
            artifactId = "either"

            from(components["java"])
        }
    }
}