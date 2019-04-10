#!/usr/bin/env groovy
@Library('gcx@release/1.9') _

agent('linux') {
    stage('checkout and clean') {
        gitCheckout()
    }

    /**
     * This will run all **pure** Kotlin unit tests.
     */
    stage('Pure unitTests') {
        sh "./gradlew test"
    }
}