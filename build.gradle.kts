buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
    }
}

plugins {
    `maven-publish`
}

apply(plugin = "org.jetbrains.kotlin.jvm")

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

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/grandcentrix/grandcentrix-kotlin-either")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "kotlin-either"
            version = project.version.toString()

            from(components["java"])
        }
    }
}