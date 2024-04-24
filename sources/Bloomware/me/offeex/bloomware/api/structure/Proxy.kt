package me.offeex.bloomware.api.structure

data class Proxy(var ipPort: String, var username: String, var password: String, var type: ProxyType) {
    val port: Int
        get() = ipPort.split(":")[1].toInt()
    val ip: String
        get() = ipPort.split(":")[0]

    enum class ProxyType {
        SOCKS4, SOCKS5
    }
}