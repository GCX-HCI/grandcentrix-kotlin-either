#!/usr/bin/env groovy
import net.grandcentrix.tools.GitHubUtils

@Library('gcx@release/1.9') _

agent('kotlin') {
    stage('checkout and clean') {
        gitCheckout()
    }

    stage('Publish to artifactory') {
        withCredentials([string(credentialsId: "artifactory-deployment", variable: 'API_KEY')]) {
            android.gradle "artifactoryPublish -PpublishToInternal -PartifactoryUser=jenkins -PartifactoryKey=${API_KEY}"
        }
    }
}