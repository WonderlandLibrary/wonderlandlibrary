package club.async.event.impl;

import club.async.event.Event;
import net.minecraft.util.ChatComponentText;

public class EventChat extends Event {

    private final String message;

    public EventChat(String message) {
        this.message = message;
    }

    public final String getMessage() {
        return message;
    }

}
