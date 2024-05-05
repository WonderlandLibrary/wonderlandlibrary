package vip.radium.event.impl.player;

import vip.radium.event.CancellableEvent;

public final class SendMessageEvent extends CancellableEvent {

    private String message;

    public SendMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
