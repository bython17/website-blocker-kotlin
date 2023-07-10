import java.io.File
import java.util.*

enum class OS {
    WINDOWS, LINUX, MAC
}

fun validateIp(ipAddress: String) {
    // validate the redirectIp to be an IP
    val octets = ipAddress.split(".")
    val exceptionMessage = "Invalid redirect IP Address: $ipAddress"
    // check if 4 octets exist
    if (octets.size != 4) {
        throw Exception(exceptionMessage)
    }
    // check if each individual element has an
    // upper bound of 255 and lower bound 0
    for (octet in octets) {
        try {
            val intOctet = octet.toUInt()
            // no need to check if intOctet is > 255, since it's a UInt
            if (intOctet > 255u) {
                throw NumberFormatException()
            }
        } catch (_: NumberFormatException){
            throw Exception(exceptionMessage)
        }
    }
}

fun getOS(): OS? {
    val os = System.getProperty("os.name").lowercase(Locale.getDefault())
    return when {
        os.contains("win") -> {
            OS.WINDOWS
        }
        os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
            OS.LINUX
        }
        os.contains("mac") -> {
            OS.MAC
        }
        else -> null
    }
}

fun checkWriteAccess(file: File) {
    if (!file.canWrite()) {
        throw Exception("PermissionDenied: Can't write into the hosts file.")
    }
}

fun getHostsFilePath(): String {
    val os = getOS() ?: throw Exception("Unsupported OS")
    return when(os) {
        OS.WINDOWS -> "c:\\Windows\\System32\\Drivers\\etc\\hosts"
        OS.MAC -> "/private/etc/hosts"
        OS.LINUX -> "/etc/hosts"
    }
}