import guru.stefma.androidartifacts.ArtifactsExtension
import guru.stefma.artifactorypublish.ArtifactoryPublishExtension

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
        classpath("guru.stefma.artifactorypublish:artifactorypublish:1.1.0")
    }
}

apply(plugin = "java-library")
apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "guru.stefma.artifactorypublish")

group = "net.grandcentrix.either"
version = "1.0"

repositories {
    mavenCentral()
}

val implementation by configurations
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

val testImplementation by configurations
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.0")
    testImplementation("org.assertj:assertj-core:3.12.2")
}

configure<ArtifactsExtension> {
    artifactId = "either"
}

configure<ArtifactoryPublishExtension> {
    artifactoryUrl = "https://artifactory.gcxi.de"
    artifactoryRepo = if (hasProperty("publishToInternal")) "maven-internal" else "maven-playground"
    publications = arrayOf("maven")
}
