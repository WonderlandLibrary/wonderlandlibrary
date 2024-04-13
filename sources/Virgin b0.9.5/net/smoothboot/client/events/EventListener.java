package net.smoothboot.client.events;


public interface EventListener {
        void addListen(Class <? extends Event> event);
        void removeListen(Class <? extends Event> event);

        void fireEvent(Event event);
    }