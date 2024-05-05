package tech.atani.client.listener.handling;

import tech.atani.client.listener.event.Event;
import tech.atani.client.listener.radbus.PubSub;

public class EventHandling {

    // Modified version of radbus from nevalackin on github
    private final PubSub<Event> eventPubSub = PubSub.newInstance(System.err::println);
    private static EventHandling instance;

    public void publishEvent(Event event) {
        eventPubSub.publish(event);
    }

    public void registerListener(Object object) {
        eventPubSub.subscribe(object);
    }

    public void unregisterListener(Object object) {
        eventPubSub.unsubscribe(object);
    }

    public static EventHandling getInstance() {
        return instance;
    }

    public static void setInstance(EventHandling instance) {
        EventHandling.instance = instance;
    }
}
