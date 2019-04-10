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
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val implementation by configurations
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.assertj:assertj-core:3.12.1")
}

configure<ArtifactsExtension> {
    artifactId = "either"
}

configure<ArtifactoryPublishExtension> {
    artifactoryUrl = "https://artifactory.gcxi.de"
    artifactoryRepo = if (hasProperty("publishToInternal")) "maven-internal" else "maven-playground"
    publications = arrayOf("maven")
}
