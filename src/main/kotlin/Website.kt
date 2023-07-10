class Website(val domain: String, val redirectIp: String) {
    init {
        validateIp(redirectIp);
    }

    companion object {
        fun fromString(websiteString: String): Website {
            val exceptionMessage = "Invalid String"

            // first validate the website
            val websiteList = websiteString.trim().split(" ")
            if (websiteList.size < 2) {
                throw Exception(exceptionMessage)
            }
            val domain = websiteList[1]
            val redirectIp = websiteList[0]
            // validate the redirect IP
            validateIp(redirectIp)

            // return the website
            return Website(domain, redirectIp)
        }
    }

    fun isSimilarTo(other: Website): Boolean {
        return other.domain == domain
    }

    override operator fun equals(other: Any?): Boolean {
        if (other is Website) {
            return other.domain == domain && other.redirectIp == redirectIp
        }
        return false
    }

    override fun hashCode(): Int {
        return domain.hashCode()
    }

    override fun toString(): String {
        return "$redirectIp $domain"
    }

}