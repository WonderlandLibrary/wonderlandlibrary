package com.ohare.client.event;

/**
 * made by oHare for Client
 *
 * @since 5/25/2019
 **/
public class CancelableEvent extends Event {

    private boolean canceled;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
