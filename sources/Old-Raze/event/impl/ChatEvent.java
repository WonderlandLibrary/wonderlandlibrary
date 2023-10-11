package markgg.event.impl;

import markgg.event.Event;

public class ChatEvent extends Event {

    public String message;

    public ChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
