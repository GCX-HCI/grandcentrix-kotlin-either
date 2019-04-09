#!/usr/bin/env groovy
@Library('gcx@release/1.9') _

agent('kotlin') {
    stage('checkout and clean') {
        gitCheckout()
    }

    stage('Assemble app devDebug and externalRelease') {
        android.gradle "assembleDevDebug assembleExternalRelease"
    }

    /**
     * This will run all **pure** Kotlin unit tests.
     */
    stage('Pure unitTests') {
        android.gradle ":begaconnectsdk:begaid:test :begaconnectsdk:siren:test :begaconnectsdk:begaqrparser:test"
    }

    /**
     * This will run lint on the flavor/buildconfig:
     * * debug
     * * devDebug
     */
    stage('lint') {
        android.gradle "lintDebug lintDevDebug"
        androidLint unstableTotalAll: '0', failedTotalAll: '0'
    }

    stage('Publish to artifactory') {
        withCredentials([string(credentialsId: "artifactory-deployment", variable: 'API_KEY')]) {
            android.gradle "artifactoryPublish -PpublishToInternal -PartifactoryUser=jenkins -PartifactoryKey=${API_KEY}"
        }
    }
}