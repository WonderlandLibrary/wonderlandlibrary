package me.offeex.bloomware.client.setting

private typealias Handler = () -> Unit

class SettingUpdateBus {
    private val handlers = mutableListOf<Handler>()

    fun subscribe(handler: Handler) = handlers.add(handler)
    fun unSubscribe(handler: Handler) = handlers.remove(handler)
    fun trigger() = handlers.forEach { it() }
}
