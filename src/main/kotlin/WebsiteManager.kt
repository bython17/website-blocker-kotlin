import java.io.File

class WebsiteManager(private val hostsFilePath: File, private val websiteIndicator: String = "#!wb") {
    // Manage the websites
    private var hostsMetadata: MutableList<String> = mutableListOf()
    private var websiteList: MutableList<Website> = mutableListOf()

    init {
        // We should check if the file exists
        if (!hostsFilePath.isFile) {
            throw Exception("The file passed doesn't exist.")
        }

        // We should also parse the hostsFile and add websites we find there
        parseHostsFile(hostsFilePath.readText())
    }

    // Step-by-step procedures
    // 0. make sure that the hosts lines are at least 2.
    // 0.5. Add the first line to the metadata
    // 1. Loop through the lines of the hosts file starting from the 2nd element
    // 2. check if the previous line was a website indicator
    // 3. if step 2 is true, then check if the current line is a website
    // 5. if step 3 is true, then add the website to the websiteList
    // 4. then remove the last line from the hostsMetadata(.i.e the indicator)
    // 6. if either step 3 or 4 are not true then add that line to the metaData list

    private fun parseHostsFile(hostsString: String) {
        val hostsLines = hostsString.lines()
        // if the hostsLines are less than 2, then it means that
        // there is no website, but we'll add the metadata
        if (hostsLines.size < 2) {
            hostsMetadata = hostsLines.toMutableList()
            return
        } else {
            // Add the first line to the metadata
            hostsMetadata.add(hostsLines[0])
            // now we'll parse the hostsString
            for (i in 1..<hostsLines.size) {
                val previousLine = hostsLines[i-1]
                val currentLine = hostsLines[i]

                // check if the previous line was an indicator
                if (previousLine.trim() == websiteIndicator) {
                    try {
                        // will throw an exception if the string is not in
                        // the correct format
                        val website = Website.fromString(currentLine)
                        // remove the last line from the hostsMetaData
                        hostsMetadata.removeLast()
                        // Now try to add the website to the website list
                        try {
                            this.addWebsite(website)
                        } catch (_: Exception) {}
                    } catch (_: Exception) {
                        // if there is an error here that means the line isn't
                        // a website, so we need to add it to the metadata
                        hostsMetadata.add(currentLine)
                    }
                } else {
                    // we need to add the current line to the metadata
                    hostsMetadata.add(currentLine)
                }
            }
        }

    }

    fun getCurrentWebsites(): MutableList<Website> {
        return websiteList
    }

    fun addWebsite(website: Website) {
        checkWriteAccess(hostsFilePath)
        // first validate if the website domain is not
        // duplicate since we are going to use that as a key
        if (website in websiteList) {
            throw Exception("Website ${website.domain} already exists!")
        }
        // check if any website is similar with the other(only the domain is identical)
        // if it is then replace it
        var isWebsiteSimilar = false
        websiteList = websiteList.map {
            if (it.isSimilarTo(website)){
                isWebsiteSimilar = true
                website
            } else it
        }.toMutableList()
        if (!isWebsiteSimilar) {
            websiteList.add(website)
        }
    }

    fun removeWebsite(domain: String) {
        checkWriteAccess(hostsFilePath)
//        getting fancy and using to algorithms ;)
//        val newWebsiteList = websiteList.filter {
//            it.domain != domain
//        }.toMutableList()
//        if (newWebsiteList.size == websiteList.size) {
//            // means there was nothing filtered out.
//            // so we do what we do best, complain
//            throw Exception("There is no website named '$domain'")
//        }
//        websiteList = newWebsiteList
        val domainToBeRemoved = websiteList.find {
            it.domain == domain
        }
        if (domainToBeRemoved == null) {
            throw Exception("There is no website named '$domain'")
        } else {
            websiteList.remove(domainToBeRemoved)
        }
    }

    fun saveToHostsFile() {
        checkWriteAccess(hostsFilePath)
        // Write the websites in the websiteList to the Host file
        val hostsFileString: String
        // map the websites and turn them to strings
        val blockedWebsites = websiteList.map {
            "$websiteIndicator\n$it"
        }
        hostsFileString = "${hostsMetadata.joinToString("\n")}\n${blockedWebsites.joinToString("\n")}"
        hostsFilePath.writeText(hostsFileString)
    }
}
