package ru.smertnix.celestial.event.events;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean state);

}
