plugins {
    `java-library`
    `maven-publish`
}

apply(plugin = "org.jetbrains.kotlin.jvm")

repositories {
    mavenCentral()
}

val implementation by configurations
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    compileOnly(project(":either"))
    compileOnly("com.squareup.retrofit2:retrofit:2.9.0")
}

val testImplementation by configurations
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.0")
    testImplementation("org.assertj:assertj-core:3.12.2")
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
        create<MavenPublication>("retofit-calladapter") {
            artifactId = "retofit-calladapter"

            from(components["java"])
        }
    }
}
