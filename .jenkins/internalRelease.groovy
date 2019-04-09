#!/usr/bin/env groovy
import net.grandcentrix.tools.GitHubUtils

@Library('gcx@release/1.9') _

agent('kotlin') {
    stage('checkout and clean') {
        gitCheckout()
    }

    stage('Assemble external release APK') {
        android.gradle ":kotlin-either:app:assembleExternalRelease"
    }

    GitHubUtils ghUtils = new GitHubUtils()
    stage('Publish GitHub release') {
        String tag = "${TAG}"
        if (tag.isEmpty()) {
            error "Tag is empty!"
        }
        String branch = "${BRANCH}".isEmpty() ? "master" : "${BRANCH}"
        String title = "${TITLE}".isEmpty() ? tag : "${TITLE}"
        String releaseNotes = "${RELEASE_NOTES}".isEmpty() ? "Not provided" : "${RELEASE_NOTES}"
        String files = "${FILES}"

        // First we create the tag
        ghUtils.createAndUploadTag(tag)

        // Then we create the release (based on the tag)
        ghUtils.createRelease([
                "tag"         : tag,
                "tagBranch"   : branch,
                "title"       : title,
                "releaseNotes": releaseNotes,
                "asDraft"     : false,
                "asPrerelease": true
        ])

        // Finally upload the files
        if (!files.isEmpty()) {
            String releaseId = ghUtils.getReleaseId(tag)
            files.split(",").each {
                String filePath = it.trim()
                // When the path is an directory
                // then we zip all included files
                // Cause its not allowed to use `new File(String)`
                // we ask simply for "contains ." -.-
                if (!filePath.contains(".")) {
                    def indexOfSecondLastSlash = (filePath.substring(0, filePath.length() - 2)).lastIndexOf("/")
                    def zipName = filePath.substring(indexOfSecondLastSlash + 1, filePath.length() - 1)
                    def zipNameWithExt = "${zipName}.zip"
                    zip([
                            "zipFile": zipNameWithExt,
                            "dir"    : filePath,
                            "glob"   : "**/*"
                    ])
                    ghUtils.uploadReleaseAssets(releaseId, zipNameWithExt)
                } else {
                    ghUtils.uploadReleaseAssets(releaseId, filePath)
                }
            }
        }
    }

}