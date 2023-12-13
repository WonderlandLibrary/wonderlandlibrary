package com.ohare.client.event.events.player;

import com.ohare.client.event.CancelableEvent;

public class PushEvent extends CancelableEvent {
   private boolean pre;
    public PushEvent(boolean pre) {
        this.pre = pre;
    }
    public boolean isPre() {
        return pre;
    }
}
