#!/usr/bin/env groovy
@Library('gcx@release/1.9') _

agent('android') {
    stage('checkout and clean') {
        gitCheckout()
    }

    /**
     * This will run all **pure** Kotlin unit tests.
     */
    stage('Pure unitTests') {
        android.gradle "test"
    }
}