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
        version = "1.4"
    }
}
