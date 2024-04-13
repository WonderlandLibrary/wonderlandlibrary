package net.smoothboot.client.events;

import java.util.HashMap;

public class EventEmitter {
    public EventEmitter() { }

    private final HashMap<Class<? extends Event>, HashMap<EventListener, Boolean>> eventListeners = new HashMap<Class<? extends Event>, HashMap<EventListener, Boolean>>();

    private final HashMap<Class<? extends Event>, Boolean> eventListened = new HashMap<Class<? extends Event>, Boolean>();

    public boolean isListening(Class<? extends Event> event) {
        return this.eventListened.containsKey(event) && this.eventListened.get(event);
    }

    private HashMap<EventListener, Boolean> getListeners(Class<? extends Event> event) {
        return eventListeners.get(event);
    }

    public void addListener(EventListener listener, Class<? extends Event> event) {
        HashMap<EventListener, Boolean> listeners = this.getListeners(event);


        if (listeners == null) {
            this.eventListeners.put(event, new HashMap<EventListener, Boolean>());


            listeners = this.getListeners(event);
        }

        listeners.put(listener, true);
        this.eventListened.put(event, true);
    }

    public void removeListener(EventListener listener, Class<? extends Event> event) {
        HashMap<EventListener, Boolean> listeners = this.getListeners(event);

        listeners.remove(listener);
        if (listeners.size() == 0) {
            this.eventListened.remove(event);
        }
    }

    public boolean triggerEvent(Event event) {
        if (!this.isListening(event.getClass())) return false;

        HashMap<EventListener, Boolean> listeners = this.getListeners(event.getClass());

        for (EventListener listener : listeners.keySet()) {
            listener.fireEvent(event);
        }

        return true;
    }
}