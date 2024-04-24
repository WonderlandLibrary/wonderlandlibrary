package me.offeex.bloomware.event.dispatcher

import me.offeex.bloomware.event.Event
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

@Suppress("UNCHECKED_CAST")
class SubscriberController(rootClass: Class<*>) {
    private val listeners = ConcurrentHashMap<Class<out Event>, ConcurrentLinkedQueue<Method>>()
    val supportedEvents: Set<Class<out Event>>
        get() = listeners.keys

    /* Collect listeners from root class and all its supeclasses in hierarchy */
    init {
        val listeners = collectListeners(rootClass)
        for (listener in listeners) {
            if (listener.parameterCount != 1) {
                throw IllegalStateException("Subscriber method should have only 1 parameter (${listener.name} in ${listener.declaringClass.name})")
            }
            /* Check if parameter is subclass of Event */
            val parameterType = listener.parameterTypes[0]
            if (!Event::class.java.isAssignableFrom(parameterType)) {
                throw IllegalStateException("Subscriber method parameter should be subclass of Event (${listener.name} in ${listener.declaringClass.name})")
            }

            listener.trySetAccessible()

            /* Creates queue for event type if it doesn't exist, then adds listener to it */
            val queue = this.listeners.computeIfAbsent(parameterType as Class<out Event>) { ConcurrentLinkedQueue() }
            queue.add(listener)
        }
    }

    fun dispatch(subscriber: Any, event: Event, type: Class<out Event>) {
        listeners[type]?.forEach { it.invoke(subscriber, event) }
    }
}
