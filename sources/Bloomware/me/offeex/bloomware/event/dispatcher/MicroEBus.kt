package me.offeex.bloomware.event.dispatcher

import me.offeex.bloomware.event.Event
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger

@Suppress("UNCHECKED_CAST")
class MicroEBus {
    private val subscriberCache = ConcurrentHashMap<Class<*>, SubscriberController>()
    private val eventLookupCache = ConcurrentHashMap<Class<out Event>, ConcurrentLinkedQueue<Class<*>>>()
    private val registry = ConcurrentHashMap<Class<*>, ConcurrentLinkedQueue<Any>>()
    private val dispatchingStack = AtomicInteger(0)
    private val toUnregister = ConcurrentLinkedQueue<Any>()

    fun post(event: Event) {
        dispatchingStack.incrementAndGet()
        unwrapTillBase(event::class.java, Event::class.java).forEach { eventClass ->
            eventLookupCache[eventClass]?.forEach { subscriberClass ->
                registry[subscriberClass]?.forEach { subscriber ->
                    if (!toUnregister.contains(subscriber)) {
                        subscriberCache[subscriberClass]?.dispatch(subscriber, event, eventClass as Class<out Event>)
                    }
                }
            }
        }
        dispatchingStack.decrementAndGet()
        if (dispatchingStack.get() == 0) {
            toUnregister.forEach {
                registry[it::class.java]?.remove(it)
            }
            toUnregister.clear()
        }
    }

    fun register(subscriber: Any) {
        if (!subscriberCache.containsKey(subscriber::class.java)) {
            val provider = SubscriberController(subscriber::class.java)
            subscriberCache[subscriber::class.java] = provider
            provider.supportedEvents.forEach {
                eventLookupCache.computeIfAbsent(it) { ConcurrentLinkedQueue() }.add(subscriber::class.java)
            }
        }

        if (!toUnregister.remove(subscriber)) {
            if (registry[subscriber::class.java]?.contains(subscriber) == true) {
                throw IllegalStateException("Subscriber $subscriber is already registered")
            }
            registry.computeIfAbsent(subscriber::class.java) { ConcurrentLinkedQueue() }.add(subscriber)
        }
    }

    fun unregister(subscriber: Any) {
        if (dispatchingStack.get() == 0) {
            registry[subscriber::class.java]?.remove(subscriber)
        } else {
            toUnregister.add(subscriber)
        }
    }
}
