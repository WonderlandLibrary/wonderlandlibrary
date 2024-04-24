package me.offeex.bloomware.client.setting

private typealias ValueHandler<T> = (oldValue: T, newValue: T) -> Unit

class SettingValueUpdateBus<T> {
    private val handlers: MutableList<ValueHandler<T>> = mutableListOf()

    fun subscribe(handler: ValueHandler<T>) = handlers.add(handler)
    fun unSubscribe(handler: ValueHandler<T>) = handlers.remove(handler)
    fun trigger(oldValue: T, newValue: T) = handlers.forEach { it(oldValue, newValue) }
}
