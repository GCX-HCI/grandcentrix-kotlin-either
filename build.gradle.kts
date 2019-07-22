import guru.stefma.androidartifacts.ArtifactsExtension
import guru.stefma.artifactorypublish.ArtifactoryPublishExtension

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
        classpath("guru.stefma.artifactorypublish:artifactorypublish:1.2.0")
    }
}

apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "guru.stefma.artifactorypublish")

group = "net.grandcentrix.either"
version = "1.3"

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

tasks.withType<Test> {
    useJUnitPlatform()
}

configure<ArtifactsExtension> {
    artifactId = "either"
}

configure<ArtifactoryPublishExtension> {
    artifactoryUrl = when {
	hasProperty("publishToPublic") -> "https://repo.gcxi.de"
	else 			       -> "https://artifactory.gcxi.de"
    }
    artifactoryRepo = when {
	hasProperty("publishToInternal") -> "maven-internal" 
	hasProperty("publishToPublic") 	 -> "maven"
	else 				 -> "maven-playground"
    }
    publications = arrayOf("maven")
}
