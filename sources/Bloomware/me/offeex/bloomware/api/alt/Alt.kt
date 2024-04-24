package me.offeex.bloomware.api.alt

open class Alt(var login: String) {
    open inner class Protected(var password: String) : Alt(login) {
        inner class Licensed(var isMicrosoft: Boolean) : Protected(password)
        inner class Server(var ip: String) : Protected(password)
    }
}