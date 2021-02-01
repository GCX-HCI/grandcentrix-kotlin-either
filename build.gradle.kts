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
        jcenter {
            name = "Bintray"
            url = uri("https://api.bintray.com/maven/grandcentrix/maven/grandcentrix-kotlin-either/;publish=1")
            credentials {
                username = project.findProperty("bintray.user")?.toString() ?: System.getenv("BINTRAY_USER")
                password = project.findProperty("bintray.token")?.toString() ?: System.getenv("BINTRAY_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("either") {
            groupId = project.group.toString()
            artifactId = "either"
            version = project.version.toString()

            from(components["java"])
        }
    }
}