import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

class Config(val args: Array<String>) {
    // All user configurations and arguments are passed here
    @OptIn(ExperimentalCli::class)
    fun parseArgs() {
        val parser = ArgParser("wb")
        // The subcommands
        class List: Subcommand("list", "List the entries(websites) that are in the hosts file") {
            override fun execute() {

            }
        }
    }
}


// wb list
// wb add youtube.com 0.0.0.0
// wb remove google.com