/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventChatSend
extends EventCancellable {
    private static String message;
    private boolean canceled;

    public static String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        EventChatSend.message = message;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean state) {
        this.canceled = state;
    }
}

