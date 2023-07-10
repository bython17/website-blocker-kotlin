import kotlinx.cli.*
import java.io.File
import kotlin.system.exitProcess

@ExperimentalCli
fun run(args: Array<String>, websiteManger: WebsiteManager) {
    val argParser = ArgParser("wb")
    // The redirect IP
    val redirectIp by argParser.option(ArgType.String, "redirect-ip", "r", description="The IP address" +
            " the blocked will be redirected to(Tip: Spin up ur own server and put the IP here)").default("0.0.0.0")

    class List: Subcommand("list", "List hosts file entries(websites) that are added using this program") {
        override fun execute() {
            val printableString = websiteManger.getCurrentWebsites().joinToString("\n") {
                "${it.domain} -> ${it.redirectIp}"
            }
            if (printableString.trim() == "") {
                    throw Exception("No websites found in the hosts file")
            } else {
                println(printableString)
            }
        }
    }
    class Add: Subcommand("add", "Add a hosts file entry(website) to the hosts file.") {
        val domain by argument(ArgType.String, "domain", description="Domain name of the site (NB: " +
                "Don't use 'www.' before the domain since that's a subdomain")

        override fun execute() {
            val website = Website(domain, redirectIp)
            websiteManger.addWebsite(website)
            println("Successfully added $domain[$redirectIp]")
        }
    }
    class Remove: Subcommand("remove", "Remove a hosts file entry(website) from the hosts file.") {
        val domain by argument(ArgType.String, "domain", description="Domain name of the site (NB: " +
                "Don't use 'www.' before the domain since that's a subdomain")

        override fun execute() {
            websiteManger.removeWebsite(domain)
            println("Successfully removed $domain")
        }
    }
    argParser.subcommands(List(), Add(), Remove())
    argParser.parse(args)
}

@OptIn(ExperimentalCli::class)
fun main(args: Array<String>) {
    try {
        val hostsFile = File(getHostsFilePath())
        val websiteManager = WebsiteManager(hostsFile)

        run(args, websiteManager)

        // save your configs to the hosts file
        websiteManager.saveToHostsFile()
    } catch (err: Exception) {
        System.err.println(err.message)
        exitProcess(1)
    }
}
