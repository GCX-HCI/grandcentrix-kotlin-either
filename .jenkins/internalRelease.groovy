#!/usr/bin/env groovy
import net.grandcentrix.tools.GitHubUtils

@Library('gcx@release/1.9') _

agent('linux') {
    stage('checkout and clean') {
        gitCheckout()
    }

    stage('Assemble the jar') {
        android.gradle "assemble"
    }

    GitHubUtils ghUtils = new GitHubUtils()
    stage('Create GH Release') {
        String tag = "${TAG}"
        String branch = "${BRANCH}"

        // Create the git TAG
        ghUtils.createAndUploadTag(tag)

        // Then we create the release (based on the tag)
        ghUtils.createRelease([
                "tag"         : tag,
                "tagBranch"   : branch,
                "title"       : tag,
                "releaseNotes": tag,
                "asDraft"     : false,
                "asPrerelease": true
        ])
    }

    stage('Publish to artifactory') {
        withCredentials([string(credentialsId: "artifactory-deployment", variable: 'API_KEY')]) {
            android.gradle "artifactoryPublish -PpublishToInternal -PartifactoryUser=jenkins -PartifactoryKey=${API_KEY}"
        }
    }
}