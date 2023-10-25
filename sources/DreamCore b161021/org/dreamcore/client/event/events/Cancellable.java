package org.dreamcore.client.event.events;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean state);

}
