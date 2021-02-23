buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
    }

    allprojects {
        group = "net.grandcentrix.either"
        version = "1.6"
    }
}

repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/grandcentrix/grandcentrix-kotlin-either")
        credentials {
            username = project.findProperty("github.user")?.toString() ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("github.token")?.toString() ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
