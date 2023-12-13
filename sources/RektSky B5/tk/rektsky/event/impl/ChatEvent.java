/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import tk.rektsky.event.Event;

public class ChatEvent
extends Event {
    private String message;
    private boolean canceled = false;
    private boolean canceledLogging = false;

    public ChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isCanceledLogging() {
        return this.canceledLogging;
    }

    public void setCanceledLogging(boolean canceledLogging) {
        this.canceledLogging = canceledLogging;
    }
}

