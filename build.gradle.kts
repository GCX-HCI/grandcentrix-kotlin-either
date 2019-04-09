buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.3.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
        classpath("guru.stefma.artifactorypublish:artifactorypublish:1.1.0")
    }
}

plugins {
    id("guru.stefma.artifactorypublish")
    id("org.jetbrains.kotlin.jvm") version "1.3.11"
}

group = "net.grandcentrix.either"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

androidArtifact {
    artifactId = "either"
}

artifactoryPublish {
    artifactoryUrl = "https://artifactory.gcxi.de"
    artifactoryRepo = if (hasProperty("publishToInternal")) "maven-internal" else "maven-playground"
    publications = arrayOf("maven")
}
