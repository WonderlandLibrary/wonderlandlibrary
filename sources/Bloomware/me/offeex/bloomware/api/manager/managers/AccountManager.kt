package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.api.alt.Alt
import me.offeex.bloomware.api.manager.Manager

object AccountManager : Manager() {
    val accounts = mutableListOf<Alt>()

    infix fun String.withIp(ip: String) =
        accounts.filterIsInstance<Alt.Protected.Server>().find { it.login == this && it.ip == ip }

    infix fun String.with(password: String) = Alt(this).Protected(password)
    infix fun Alt.Protected.with(isMicrosoft: Boolean) = Licensed(isMicrosoft)
    infix fun Alt.Protected.with(ip: String) = Server(ip)
}