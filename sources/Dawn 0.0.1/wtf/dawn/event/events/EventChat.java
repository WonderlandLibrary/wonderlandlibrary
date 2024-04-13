package wtf.dawn.event.events;

import wtf.dawn.event.Event;

public class EventChat extends Event<EventChat> {

    public static String message;

    public EventChat(String message) {
        this.message = message;
    }

    public static String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
